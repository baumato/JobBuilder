/*******************************************************************************
 * Copyright (c) 2014 tobbaumann. All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: tobbaumann - initial API and implementation
 ******************************************************************************/
package org.tobbaumann.jobs.builder;

import org.eclipse.core.runtime.IStatus;

/**
 * UserFeedbackRunnable
 *
 * @see #performUserFeedback(boolean)
 *
 * @author tobbaumann
 *
 */
public interface UserFeedbackRunnable {

  /**
   * <p>
   * If the user does not choose to run the job in the background, then they will know when the job
   * has completed because the progress dialog will close. However, if they choose to run the job in
   * the background (by using the dialog button or the preference), they will not know when the job
   * has completed.
   * <p>
   * If a <tt>UserFeedbackRunnable</tt> is used, it causes the job to remain in the progress view. A
   * hyperlink with the given title is created and when the user clicks on it, the given
   * UserFeedbackRunnable gets executed to show the results of the finished job. This allows to not
   * interrupt the user because the job results are not not displayed immediately. The argument
   * <tt>jobResult</tt> allows to perform a different behaviour in error cases:
   * <tt>jobResult.getSeverity() == IStatus.ERROR</tt>
   *
   * @param jobResult
   */
  public void performUserFeedback(IStatus jobResult);

}
