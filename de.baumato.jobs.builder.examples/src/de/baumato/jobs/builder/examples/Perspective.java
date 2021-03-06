/*******************************************************************************
 * Copyright (c) 2014 Tobias Baumann.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Tobias Baumann - initial API and implementation
 ******************************************************************************/
package de.baumato.jobs.builder.examples;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

  @Override
  public void createInitialLayout(IPageLayout layout) {
    layout.setEditorAreaVisible(false);
    layout.setFixed(true);
  }

}
