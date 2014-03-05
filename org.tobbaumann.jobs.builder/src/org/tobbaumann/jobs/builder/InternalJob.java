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

import static com.google.common.base.Objects.firstNonNull;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.progress.IProgressConstants;
import org.tobbaumann.jobs.builder.JobBuilder.JobKind;

/**
 *
 * @author tobbaumann
 *
 */
final class InternalJob extends Job {

  private final Object family;
  private final IRunnableWithProgress progressRunnable;
  private final ImageDescriptor image;
  private final String jobCompletionTitle;
  private final UserFeedbackRunnable userFeedback;

  private IStatus jobResult;

  public InternalJob(JobBuilder builder) {
    super(builder.title);
    this.family = firstNonNull(builder.family, builder.title);
    this.progressRunnable = builder.progressRunnable;
    setSystem(builder.kind == JobKind.SYSTEM);
    setUser(builder.kind == JobKind.USER);
    if (builder.priority != null) {
      setPriority(builder.priority);
    }
    this.image = builder.image;
    this.userFeedback = builder.userFeedback;
    this.jobCompletionTitle = builder.jobCompletionTitle;
    if (builder.listener != null) {
      addJobChangeListener(builder.listener);
    }
    if (builder.schedulingRule != null) {
      setRule(builder.schedulingRule);
    }
  }

  @Override
  public boolean belongsTo(Object family) {
    return this.family.equals(family);
  }

  @Override
  protected IStatus run(IProgressMonitor monitor) {
    jobResult = null;
    try {
      applyImageIfAvailable();
      updateErrorHandlingBehaviour();
      progressRunnable.run(monitor);
      jobResult = createStatus();
    } catch (InterruptedException e) {
      handleInterruption(e);
    } catch (Exception e) {
      handleError(e);
    } finally {
      updateErrorHandlingBehaviour();
      applyUserFeedback();
    }
    return jobResult;
  }

  private void handleInterruption(InterruptedException e) {
    jobResult = new Status(IStatus.CANCEL, "com.hlcl.sje", "Job has been canceled.", e);
  }

  private void handleError(Exception e) {
    Throwable t = e;
    if (e instanceof InvocationTargetException) {
      t = ((InvocationTargetException) e).getTargetException();
    }
    jobResult = new Status(IStatus.ERROR, "com.hlcl.sje", "Job finished with errors.", t);
  }

  private void applyImageIfAvailable() {
    if (image != null) {
      setProperty(IProgressConstants.ICON_PROPERTY, image);
    }
  }

  private void updateErrorHandlingBehaviour() {
    setProperty(IProgressConstants.NO_IMMEDIATE_ERROR_PROMPT_PROPERTY,
        Boolean.valueOf(userFeedback != null && !isModal()));
  }

  private void applyUserFeedback() {
    if (userFeedback != null && !isModal()) {
      Action userFeedbackAction = new Action() {
        @Override
        public void run() {
          userFeedback.performUserFeedback(jobResult);
        }
      };
      setProperty(IProgressConstants.KEEP_PROPERTY, Boolean.TRUE);
      setProperty(IProgressConstants.ACTION_PROPERTY, userFeedbackAction);
    }
  }

  /**
   * Checks if the job is currently in modal mode.
   */
  private boolean isModal() {
    Boolean isModal = (Boolean) getProperty(IProgressConstants.PROPERTY_IN_DIALOG);
    return isModal == null ? false : isModal.booleanValue();
  }

  private IStatus createStatus() {
    return new Status(IStatus.OK, "com.hlcl.sje", IStatus.OK, jobCompletionTitle, null);
  }
}
