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
package de.rinderle.softvis3d.domain;

import java.util.Objects;

public class SnapshotStorageKey {

  private static final String UNDERSCORE = "_";
  private final String key;

  public SnapshotStorageKey(final VisualizationRequest requestDTO) {
    this.key = requestDTO.getRootSnapshotKey() + UNDERSCORE + getKeyFromMetrics(requestDTO.getMetrics()) + UNDERSCORE;
  }

  private String getKeyFromMetrics(String[] metrics) {
    final StringBuilder result = new StringBuilder();
    for (final String metric : metrics) {
        result.append(metric);
    }
    return result.toString();
  }

  @Override
  public String toString() {
    return key;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final SnapshotStorageKey that = (SnapshotStorageKey) o;
    return Objects.equals(key, that.key);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key);
  }
}
