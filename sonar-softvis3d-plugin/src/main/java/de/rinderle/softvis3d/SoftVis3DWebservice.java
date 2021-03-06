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
package de.rinderle.softvis3d;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.rinderle.softvis3d.webservice.VisualizationWebserviceHandler;
import org.sonar.api.server.ws.WebService;

public class SoftVis3DWebservice implements WebService {

  private final VisualizationWebserviceHandler visualizationHandler;

  public SoftVis3DWebservice() {
    final Injector softVis3DInjector = Guice.createInjector();

    this.visualizationHandler = softVis3DInjector.getInstance(VisualizationWebserviceHandler.class);
  }

  @Override
  public void define(final Context context) {
    final WebService.NewController controller = context.createController("api/softVis3D");
    controller.setDescription("SoftVis3D webservice");

    // create the URL /api/softVis3D/getVisualization
    final NewAction action = controller
        .createAction("getVisualization")
        .setDescription("Get getVisualization structure")
        .setHandler(this.visualizationHandler);

    action.createParam("projectKey");
    action.createParam("metrics");

    controller.setSince("5.5");

    // important to apply changes
    controller.done();
  }
}
