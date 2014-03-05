/*******************************************************************************
 * Copyright (c) 2014 tobbaumann.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     tobbaumann - initial API and implementation
 ******************************************************************************/
package org.tobbaumann.jobs.builder;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import com.google.common.base.Throwables;

/**
 * Lets a <tt>Runnable</tt> act as a <tt>IRunnableWithProgress</tt>.
 *
 * @author tobbaumann
 *
 */
public class RunnableAdapter implements IRunnableWithProgress {

  private final String title;
  private final Runnable runnable;

  /**
   * @param title the title to display in the progress monitor
   * @param runnable the runnable to run
   */
  public RunnableAdapter(String title, Runnable runnable) {
    this.title = checkNotNull(title);
    this.runnable = checkNotNull(runnable);
  }

  public String getTitle() {
    return title;
  }

  public Runnable getAdaptedRunnable() {
    return this.runnable;
  }

  @Override
  public void run(final IProgressMonitor monitor) throws InvocationTargetException,
      InterruptedException {
    try {
      if (monitor != null) {
        monitor.beginTask(title, IProgressMonitor.UNKNOWN);
      }
      runnable.run();
    } catch (Exception e) {
      Throwables.propagateIfPossible(e);
      Throwables.propagateIfInstanceOf(e, InterruptedException.class);
      throw new InvocationTargetException(e);
    } finally {
      if (monitor != null) {
        monitor.done();
      }
    }
  }
}
