/**
 * SoftVis3D Sonar plugin
 * Copyright (C) 2016 Stefan Rinderle and Yvo Niedrich
 * stefan@rinderle.info / yvo.niedrich@gmail.com
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
package de.rinderle.softvis3d.cache;

import com.google.inject.Singleton;
import de.rinderle.softvis3d.base.domain.tree.RootTreeNode;
import de.rinderle.softvis3d.domain.SnapshotStorageKey;

@Singleton
public class SnapshotCacheService {

  private final Cache<SnapshotStorageKey, RootTreeNode> storage;

  SnapshotCacheService() {
    storage = new Cache<>();
  }

  public void printCacheContents() {
    storage.logKeys();
  }

  public boolean containsKey(final SnapshotStorageKey key) {
    return storage.containsKey(key);
  }

  public void save(final SnapshotStorageKey key, final RootTreeNode result) {
    storage.put(key, result);
  }

  public RootTreeNode getSnapshotTreeResult(final SnapshotStorageKey key) {
    return storage.get(key);
  }

}
