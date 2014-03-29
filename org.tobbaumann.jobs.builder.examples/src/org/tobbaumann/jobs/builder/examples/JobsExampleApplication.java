/*******************************************************************************
 * Copyright (c) 2014 tobbaumann. All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: tobbaumann - initial API and implementation
 ******************************************************************************/
package org.tobbaumann.jobs.builder.examples;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

public class JobsExampleApplication implements IApplication {

  @Override
  public Object start(IApplicationContext context) {
    Display display = PlatformUI.createDisplay();
    try {
      int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
      if (returnCode == PlatformUI.RETURN_RESTART) {
        return IApplication.EXIT_RESTART;
      }
      return IApplication.EXIT_OK;
    } finally {
      display.dispose();
    }
  }

  @Override
  public void stop() {
    if (!PlatformUI.isWorkbenchRunning()) {
      return;
    }
    final IWorkbench workbench = PlatformUI.getWorkbench();
    final Display display = workbench.getDisplay();
    display.syncExec(new Runnable() {
      @Override
      public void run() {
        if (!display.isDisposed()) {
          workbench.close();
        }
      }
    });
  }
}
