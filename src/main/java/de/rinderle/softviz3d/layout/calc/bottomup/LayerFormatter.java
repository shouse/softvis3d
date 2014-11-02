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
package de.rinderle.softviz3d.layout.calc.bottomup;

import att.grappa.Graph;
import de.rinderle.softviz3d.layout.calc.LayoutViewType;
import de.rinderle.softviz3d.sonar.MinMaxValueDao;

public interface LayerFormatter {
  void format(Graph graph, Integer depth, LayoutViewType viewType);

  double calcBuildingHeight(Double value, MinMaxValueDao minMaxMetricHeight);

  double calcSideLength(Double value, MinMaxValueDao minMaxMetricFootprint);

  double calcEdgeRadius(int counter, MinMaxValueDao minMaxEdgeCounter);
}
