/*******************************************************************************
 * Copyright (c) 2014 tobbaumann. All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: tobbaumann - initial API and implementation
 ******************************************************************************/
package org.tobbaumann.jobs.builder;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import com.google.common.base.Throwables;

/**
 * Lets a <tt>Runnable</tt> act as a <tt>IRunnableWithProgress</tt> with unknown amount of work.
 *
 * @author tobbaumann
 *
 */
public class RunnableAdapter implements IRunnableWithProgress {

  private final String title;
  private final Runnable runnable;

  /**
   * Constructs a new instance.
   *
   * @param title the title to display in the progress monitor
   * @param runnable the runnable to run
   */
  public RunnableAdapter(String title, Runnable runnable) {
    this.title = checkNotNull(title);
    this.runnable = checkNotNull(runnable);
  }

  @Override
  public void run(final IProgressMonitor monitor) throws InvocationTargetException,
      InterruptedException {
    try {
      monitorBeginTask(monitor);
      runnable.run();
    } catch (Exception e) {
      handleError(e);
    } finally {
      monitorDone(monitor);
    }
  }

  private void monitorBeginTask(final IProgressMonitor monitor) {
    if (monitor != null) {
      monitor.beginTask(title, IProgressMonitor.UNKNOWN);
    }
  }

  private void handleError(Exception e) throws InterruptedException, InvocationTargetException {
    Throwables.propagateIfPossible(e);
    Throwables.propagateIfInstanceOf(e, InterruptedException.class);
    throw new InvocationTargetException(e);
  }

  private void monitorDone(final IProgressMonitor monitor) {
    if (monitor != null) {
      monitor.done();
    }
  }

  public String getTitle() {
    return title;
  }

  public Runnable getAdaptedRunnable() {
    return this.runnable;
  }
}
