/*******************************************************************************
 * Copyright (c) 2014 tobbaumann. All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: tobbaumann - initial API and implementation
 ******************************************************************************/
/**
 *
 */
package org.tobbaumann.jobs.builder;

/**
 * @author tobbaumann
 *
 */
public class Jobs {

  private Jobs() {}

  /**
   * Returns a job builder. You may want to use following methods:
   * <ul>
   * <li>title(if not set a default title is used)</li>
   * <li>isSystemJob or isUserJob(default)</li>
   * <li>runnable or runnableWithProgress (one of the method has to be used)</li>
   * <li><b>build</b> to get the Job instance</li>
   * <li>or <b>buildAndShedule</b> that builds, shedules and returns the job.</li>
   * </ul>
   *
   * @return a new builder instance
   */
  public static JobBuilder builder() {
    return new JobBuilder();
  }

  /**
   * Returns a job builder using the title and the runnable.
   *
   * @param title the title of the job
   * @param runnable the runnable to run in the job
   * @return a new builder instance
   */
  public static JobBuilder builder(String title, Runnable runnable) {
    return builder().title(title).runnable(runnable);
  }
}
