/*
 * Copyright (c) 2012-2017 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package org.eclipse.che.selenium.core.factory;

import org.eclipse.che.api.factory.shared.dto.FactoryDto;
import org.eclipse.che.selenium.core.SeleniumWebDriver;
import org.eclipse.che.selenium.core.client.TestFactoryServiceClient;
import org.eclipse.che.selenium.core.client.TestWorkspaceServiceClient;
import org.eclipse.che.selenium.core.entrance.Entrance;
import org.eclipse.che.selenium.core.provider.TestDashboardUrlProvider;
import org.eclipse.che.selenium.core.user.TestUser;
import org.openqa.selenium.WebDriver;

/** @author Anatolii Bazko */
public class TestFactory {
  private final TestUser owner;
  private final FactoryDto factoryDto;
  private final TestDashboardUrlProvider dashboardUrl;
  private final TestFactoryServiceClient testFactoryServiceClient;
  private final TestWorkspaceServiceClient workspaceServiceClient;
  private final Entrance entrance;
  private final String factoryUrl;

  public TestFactory(
      String factoryUrl,
      TestUser owner,
      FactoryDto factoryDto,
      TestDashboardUrlProvider dashboardUrl,
      TestFactoryServiceClient factoryServiceClient,
      TestWorkspaceServiceClient workspaceServiceClient,
      Entrance entrance) {
    this.factoryDto = factoryDto;
    this.owner = owner;
    this.factoryUrl = factoryUrl;
    this.dashboardUrl = dashboardUrl;
    this.testFactoryServiceClient = factoryServiceClient;
    this.workspaceServiceClient = workspaceServiceClient;
    this.entrance = entrance;
  }

  /** Adds authentication token into the browser and opens factory url. */
  public void authenticateAndOpen(SeleniumWebDriver driver) throws Exception {
    driver.get(dashboardUrl.get().toString());
    entrance.login(owner);
    driver.get(factoryUrl);
  }

  /** Opens factory url. */
  public void open(WebDriver driver) throws Exception {
    driver.get(factoryUrl);
  }

  public void delete() throws Exception {
    workspaceServiceClient.deleteFactoryWorkspaces(
        factoryDto.getWorkspace().getName(), owner.getName());
    deleteFactory();
  }

  private void deleteFactory() throws Exception {
    if (isNamedFactory()) {
      testFactoryServiceClient.deleteFactory(factoryDto.getName());
    }
  }

  private boolean isNamedFactory() {
    return factoryDto.getName() != null;
  }
}
