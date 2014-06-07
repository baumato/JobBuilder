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
package de.baumato.jobs.builder;

import org.eclipse.core.runtime.IStatus;

/**
 * UserFeedbackRunnable
 *
 * @see #performUserFeedback(boolean)
 */
public interface UserFeedbackRunnable {

  /**
   * Gives feedback to the user. Runs in the UI thread.
   *
   * @see JobBuilder#immediateUserFeedback(String, UserFeedbackRunnable)
   * @see JobBuilder#userFeedback(String, UserFeedbackRunnable)
   *
   * @param jobResult the result of the job
   * @param immediateFeedback <code>true</code> if the feedback is given directly after job
   *        completion, false otherwise (e.g. user decided to run the job in the background and
   *        reviews the job result in the progress view later on.)
   */
  public void performUserFeedback(IStatus jobResult, boolean immediateFeedback);

}
