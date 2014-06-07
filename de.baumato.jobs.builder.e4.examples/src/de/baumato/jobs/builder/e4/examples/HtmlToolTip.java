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
package de.baumato.jobs.builder.e4.examples;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;

public class HtmlToolTip extends ToolTip {

  private final String toolTipText;
  private final int width;
  private final int height;

  static void apply(Control control, String toolTipText, int width, int height) {
    new HtmlToolTip(control, toolTipText, width, height);
  }

  static void applyWithPre(Control control, String toolTipText, int width, int height) {
    apply(control, "<pre font=\"Courier New\">" + toolTipText + "</pre>", width, height);
  }

  private HtmlToolTip(Control control, String toolTipText, int width, int height) {
    super(control);
    this.toolTipText = toolTipText;
    this.width = width;
    this.height = height;
  }

  @Override
  protected Composite createToolTipContentArea(Event event, Composite parent) {
    Composite comp = new Composite(parent, SWT.NONE);
    GridLayoutFactory.fillDefaults().spacing(0, 0).applyTo(comp);
    Browser browser = new Browser(comp, SWT.NO_SCROLL);
    browser.setText(toolTipText);
    Point size = createSize(browser);
    browser.setLayoutData(new GridData(size.x, size.y));
    return comp;
  }

  private Point createSize(Browser browser) {
    if (width == SWT.DEFAULT && height == SWT.DEFAULT) {
      GC gc = new GC(browser);
      gc.setFont(browser.getFont());
      Point pt = gc.stringExtent(toolTipText);
      gc.dispose();
      return new Point(Math.max(100, pt.x), Math.max(30, pt.y));
    }
    return new Point(width, height);
  }

}
