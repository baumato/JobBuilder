/*******************************************************************************
 * Copyright (c) 2013 Tobias Baumann.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tobias Baumann - initial API and implementation
 ******************************************************************************/
package org.tobbaumann.jobs.builder;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.progress.IProgressConstants;

import com.google.common.base.Strings;

/**
 * Builder to create and schedule jobs. For details on jobs, see
 * http://www.eclipse.org/articles/Article-Concurrency/jobs-api.html
 *
 * @author tobbaumann
 *
 */
public class JobBuilder {

	private static final String DEFAULT_TITLE = "Operation in progress...";

	public enum JobKind {
		DEFAULT, USER, SYSTEM;
	}

	private String title = DEFAULT_TITLE;
	private IRunnableWithProgress progressRunnable = null;
	private JobKind kind = JobKind.DEFAULT;
	private Integer priority = null;
	private ImageDescriptor image = null;
	private String jobCompletionTitle = null;
	private UserFeedbackRunnable userFeedback = null;

	/** package private constructor */
	JobBuilder() {
	}

	/**
	 * Sets the title of the job, that is e.g. shown in the progress view.
	 *
	 * @param title
	 *            the job title, not null
	 * @return this
	 */
	public JobBuilder title(String title) {
		this.title = title;
		return this;
	}

	/**
	 * This given image is used to associate an <code>ImageDescriptor</code>
	 * with the Job. If the Job is shown in the UI, this descriptor is used to
	 * create an icon that represents the Job. (e.g. in the progress view).
	 *
	 * @param image
	 * @return this
	 */
	public JobBuilder image(ImageDescriptor image) {
		this.image = image;
		return this;
	}

	/**
	 * Defines that the job is a system job. System jobs, by default, do not
	 * appear in the Progress view (unless the view is in verbose mode) and do
	 * not animate the status line. If neither isSystemJob nor isUserJob is
	 * called, then the job is a default job. A default job will show UI
	 * affordance.
	 *
	 * @return this
	 */
	public JobBuilder isSystemJob() {
		kind = JobKind.SYSTEM;
		return this;
	}

	/**
	 * Defines that the job to create is a user job. User jobs and default jobs
	 * will show UI affordances when running. In addition, a user job will show
	 * a progress dialog to the user with the option to be run in the
	 * background. If neither isSystemJob nor isUserJob is called, then the job
	 * is a default job.
	 *
	 * @return this
	 */
	public JobBuilder isUserJob() {
		kind = JobKind.USER;
		return this;
	}

	/**
	 * Takes a runnable to be processed in the job. If an exception occurs the
	 * exception is wrapped and the job returns an error {@code IStatus}. If the
	 * job has a UI representation the progress is unknown.
	 *
	 * @param runnable
	 *            the runnable
	 * @return this
	 */
	public JobBuilder runnable(Runnable runnable) {
		this.progressRunnable = new RunnableAdapter(title, runnable);
		return this;
	}

	/**
	 * Takes a runnable to be processed in the job. If an exception occurs the
	 * exception is wrapped and the job returns an error {@code IStatus}. If the
	 * job has a UI representation the progress is unknown. The ThrowingRunnable
	 * compared to the usual Runnable does not force you to wrap exceptions in
	 * runtime exceptions.
	 *
	 * @param runnable
	 *            the runnable
	 * @return this
	 */
	public JobBuilder runnable(ThrowingRunnable runnable) {
		this.progressRunnable = new ThrowingRunnableAdapter(title, runnable);
		return this;
	}

	/**
	 * Takes a runnable with progress to monitor the job.
	 *
	 * @param runnable
	 * @return this
	 */
	public JobBuilder runnable(IRunnableWithProgress runnable) {
		this.progressRunnable = runnable;
		return this;
	}

	/**
	 * <p>
	 * If the user does not choose to run the job in the background, then they
	 * will know when the job has completed because the progress dialog will
	 * close. However, if they choose to run the job in the background (by using
	 * the dialog button or the preference), they will not know when the job has
	 * completed.
	 * <p>
	 * If this method is used and the progress dialog is not modal, it causes
	 * the job to remain in the progress view. A hyperlink with the given title
	 * is created and when the user clicks on it, the given
	 * <tt>UserFeedbackRunnable<tt> gets executed to show the results of the finished job. This allows to
	 * not interrupt the user because the job results are not not displayed immediately.
	 *
	 * @param jobCompletionTitle
	 *            may be null or empty to use the default text
	 * @param userFeedback
	 *            the runnable to run
	 * @return
	 */
	public JobBuilder userFeedbackForFinishedJob(String jobCompletionTitle,
			UserFeedbackRunnable userFeedback) {
		this.jobCompletionTitle = jobCompletionTitle;
		this.userFeedback = checkNotNull(userFeedback, "The given user feedback runnable is null");
		return this;
	}

	/**
	 * Sets Job.SHORT as priority which gives the job a higher priority than the
	 * default Job.LONG.
	 *
	 * @return this.
	 */
	public JobBuilder highPriority() {
		priority = Job.SHORT;
		return this;
	}

	/**
	 * Builds the job with behaviour set by this builder.
	 *
	 * @return the built job
	 */
	public Job build() {
		return buildInternalJob();
	}

	private InternalJob buildInternalJob() {
		checkState(Strings.emptyToNull(title) != null, "The job title is empty or null.");
		checkState(progressRunnable != null, "The job's runnable is not set.");
		if (Strings.isNullOrEmpty(jobCompletionTitle)) {
			if (title.equals(DEFAULT_TITLE)) {
				jobCompletionTitle = "Finished operation";
			} else {
				jobCompletionTitle = "Finished '" + title + "'.";
			}
		}
		InternalJob job = new InternalJob(this);
		return job;
	}

	/**
	 * Builds the job and schedules it. This is useful if you don't want to add
	 * job listener before scheduling.
	 *
	 * @return the job
	 */
	public Job buildAndShedule() {
		InternalJob job = buildInternalJob();
		job.schedule();
		return job;
	}

	/**
	 * Builds the job and schedules it with delay. See
	 * {@link Job#schedule(long)} for more information about scheduling.
	 *
	 * @param delay
	 *            a time delay in timeUnit before the job should run
	 * @param timeUnit
	 *            the timeUnit for the given delay
	 * @return the job
	 */
	public Job buildAndScheduleWithDelay(long delay, TimeUnit timeUnit) {
		InternalJob job = buildInternalJob();
		job.schedule(TimeUnit.MILLISECONDS.convert(delay, timeUnit));
		return job;
	}

	/**
	 *
	 * @author baumato
	 *
	 */
	private static final class InternalJob extends Job {

		private final IRunnableWithProgress progressRunnable;
		private final ImageDescriptor image;
		private final String jobCompletionTitle;
		private final UserFeedbackRunnable userFeedback;

		private IStatus jobResult;

		public InternalJob(JobBuilder builder) {
			super(builder.title);
			this.progressRunnable = builder.progressRunnable;
			setSystem(builder.kind == JobKind.SYSTEM);
			setUser(builder.kind == JobKind.USER);
			if (builder.priority != null) {
				setPriority(builder.priority);
			}
			this.image = builder.image;
			this.userFeedback = builder.userFeedback;
			this.jobCompletionTitle = builder.jobCompletionTitle;
		}

		@Override
		public boolean belongsTo(Object family) {
			return getName().equals(family);
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
}
