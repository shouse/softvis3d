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
package de.rinderle.softvis3d.dao.scm;

import org.apache.commons.lang.StringUtils;

/**
 * Created by stefanrinderle on 06.11.15.
 */
public class ScmCommitCountCalculationService extends ScmCalculationService {

  /**
   * TODO: This is not the commit count but the line count!!!
   * FIX!
   */
  @Override
  public int getNodeValue(final String scmCommitterString, final String scmTimeString) {
    if (StringUtils.isBlank(scmCommitterString)) {
      return 0;
    } else {
      final String[] resultCommitter = splitPlainScmInfo(scmCommitterString);
      return resultCommitter.length;
    }
  }

}
