/*
 * SoftViz3d Sonar plugin
 * Copyright (C) 2013 Stefan Rinderle
 * stefan@rinderle.info
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package de.rinderle.softviz3d.tree;

import com.google.inject.Inject;
import de.rinderle.softviz3d.layout.calc.LayoutViewType;
import de.rinderle.softviz3d.sonar.SonarDao;
import de.rinderle.softviz3d.sonar.SonarSnapshotDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResourceTreeServiceImpl implements ResourceTreeService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ResourceTreeServiceImpl.class);

  /**
   * Instead of annotating this class as singleton within the dependency injection, the
   * calculated trees have to be static. Because there are two different DI containers.
   * One for the Page extension of sonar and one for the webservice extention.
   * This will prevent the calculation of the same tree structure once for
   * the page and once for the webservice.
   */
  private static Map<String, TreeNode> alreadyLoadedTrees = new ConcurrentHashMap<String, TreeNode>();

  // TODO: different generated id sequence in ResourceTreeServiceImpl and PathWalker.
  private int generatedIdSequence = Integer.MAX_VALUE - 1000000;

  @Inject
  private SonarDao sonarDao;
  @Inject
  private OptimizeTreeStructure optimizeTreeStructure;

  @Override
  public TreeNode getOrCreateTreeStructure(final LayoutViewType type, final int rootSnapshotId, final int heightMetric, final int footprintMetric) {
    LOGGER.info("loadedPathWalkers size " + this.alreadyLoadedTrees.size());
    for (Map.Entry<String, TreeNode> entry : this.alreadyLoadedTrees.entrySet()) {
      LOGGER.info(entry.getKey() + entry.getValue().getName());
    }
    LOGGER.info("---");

    if (!this.alreadyLoadedTrees.containsKey(this.getAlreadyLoadedMapKey(type, rootSnapshotId))) {
      final TreeNode tree = createTreeStructure(rootSnapshotId, heightMetric, footprintMetric);

      this.alreadyLoadedTrees.put(this.getAlreadyLoadedMapKey(type, rootSnapshotId), tree);
    }

    return this.alreadyLoadedTrees.get(this.getAlreadyLoadedMapKey(type, rootSnapshotId));
  }

  private TreeNode createTreeStructure(int rootSnapshotId, int heightMetric, int footprintMetric) {
    LOGGER.info("Created tree structure for id " + rootSnapshotId);
    final PathWalker pathWalker = new PathWalker(rootSnapshotId);

    final List<SonarSnapshotDTO> flatChildren = getFlatChildrenWithMetricsFromDatabase(rootSnapshotId, heightMetric, footprintMetric);

    for (final SonarSnapshotDTO flatChild : flatChildren) {
      pathWalker.addPath(flatChild);
    }

    TreeNode resultTree = pathWalker.getTree();

    optimizeTreeStructure.removeUnecessaryNodes(resultTree);

    return resultTree;
  }

  private List<SonarSnapshotDTO> getFlatChildrenWithMetricsFromDatabase(int rootSnapshotId, int heightMetric, int footprintMetric) {
    final List<SonarSnapshotDTO> result = new ArrayList<SonarSnapshotDTO>();

    final List<Object[]> resultHeightMetric = this.sonarDao.getAllProjectElementsWithMetric(rootSnapshotId, heightMetric);
    final List<Object[]> resultFootprintMetric = this.sonarDao.getAllProjectElementsWithMetric(rootSnapshotId, footprintMetric);

    // join result lists
    for (int i = 0; i < resultHeightMetric.size(); i++) {
      final int id = (Integer) resultHeightMetric.get(i)[0];
      final String path = (String) resultHeightMetric.get(i)[1];
      BigDecimal heightMetricValue = (BigDecimal) resultHeightMetric.get(i)[2];
      BigDecimal footprintMetricValue = (BigDecimal) resultFootprintMetric.get(i)[2];

      // check for null values
      if (heightMetricValue == null) {
        heightMetricValue = BigDecimal.ZERO;
      }

      if (footprintMetricValue == null) {
        footprintMetricValue = BigDecimal.ZERO;
      }

      final SonarSnapshotDTO element = new SonarSnapshotDTO(id, path, heightMetricValue.doubleValue(), footprintMetricValue.doubleValue());

      result.add(element);
    }

    return result;

  }

  private String getAlreadyLoadedMapKey(final LayoutViewType type, final int rootSnapshotId) {
    return rootSnapshotId + "_" + type.name();
  }

  private int getNextSequence() {
    return generatedIdSequence++;
  }

  @Override
  public List<TreeNode> getChildrenNodeIds(final LayoutViewType type, final Integer rootSnapshotId, final Integer id) {
    final TreeNode treeNode = this.findNode(type, rootSnapshotId, id);

    return this.getChildrenNodes(treeNode.getChildren());
  }

  @Override
  public List<TreeNode> getChildrenLeafIds(final LayoutViewType type, final Integer rootSnapshotId, final Integer id) {
    final TreeNode treeNode = this.findNode(type, rootSnapshotId, id);

    return this.getChildrenLeaves(treeNode.getChildren());
  }

  @Override
  public TreeNode findNode(final LayoutViewType type, final Integer rootSnapshotId, final Integer id) {
    final TreeNode rootNode = this.alreadyLoadedTrees.get(this.getAlreadyLoadedMapKey(type, rootSnapshotId));

    return this.recursiveSearch(id, rootNode);
  }

  @Override
  public TreeNode addInterfaceLeafNode(final LayoutViewType type, final Integer rootSnapshotId, final String intLeafLabel, final Integer parentId) {
    final TreeNode rootNode = this.alreadyLoadedTrees.get(this.getAlreadyLoadedMapKey(type, rootSnapshotId));

    // search for parent node
    final TreeNode parent = this.recursiveSearch(parentId, rootNode);

    final Integer id = getNextSequence();
    final TreeNode interfaceLeafTreeNode = new TreeNode(id, parent, parent.getDepth() + 1, TreeNodeType.DEPENDENCY_GENERATED, "elevatorNode_" + id, 0, 0);
    parent.getChildren().put(intLeafLabel, interfaceLeafTreeNode);

    return interfaceLeafTreeNode;
  }

  @Override
  public TreeNode findInterfaceLeafNode(final LayoutViewType type, final Integer rootSnapshotId, final String intLeafLabel) {
    final TreeNode rootNode = this.alreadyLoadedTrees.get(this.getAlreadyLoadedMapKey(type, rootSnapshotId));
    return this.recursiveSearch(intLeafLabel, rootNode);
  }

  private TreeNode recursiveSearch(final String name, final TreeNode treeNode) {
    final Map<String, TreeNode> children = treeNode.getChildren();
    TreeNode temp;

    if (children.containsKey(name)) {
      return children.get(name);
    } else if (!children.isEmpty()) {
      for (final TreeNode child : children.values()) {
        temp = this.recursiveSearch(name, child);
        if (temp != null) {
          return temp;
        }
      }
    }

    return null;
  }

  private TreeNode recursiveSearch(final Integer id, final TreeNode treeNode) {
    if (treeNode.getId().equals(id)) {
      /**
       * check if there is a child treeNode with the same id.
       * This is to parse long paths and get the last treeNode
       * of the chain with the same id.
       */
      for (final TreeNode child : treeNode.getChildren().values()) {
        if (child.getId().equals(id)) {
          return this.recursiveSearch(id, child);
        }
      }

      return treeNode;
    }

    final Map<String, TreeNode> children = treeNode.getChildren();
    TreeNode temp;
    if (!children.isEmpty()) {
      for (final TreeNode child : children.values()) {
        temp = this.recursiveSearch(id, child);
        if (temp != null) {
          return temp;
        }
      }
    }

    return null;
  }

  private List<TreeNode> getChildrenNodes(final Map<String, TreeNode> children) {
    final List<TreeNode> result = new ArrayList<TreeNode>();

    for (final TreeNode child : children.values()) {
      if (!child.getChildren().isEmpty()) {
        result.add(child);
      }
    }

    return result;
  }

  private List<TreeNode> getChildrenLeaves(final Map<String, TreeNode> children) {
    final List<TreeNode> result = new ArrayList<TreeNode>();

    for (final TreeNode child : children.values()) {
      if (child.getChildren().isEmpty()) {
        result.add(child);
      }
    }

    return result;
  }

}
