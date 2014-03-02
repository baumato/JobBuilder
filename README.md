JobBuilder
==========

Following the builder pattern this API allows to create and schedule Eclipse jobs in an easy manner.

## A few examples

### Build and schedule a system job with a Runnable
```java
Jobs.builder()
	.title("Very important task in progress")
	.isSystemJob()
	.runnable(new Runnable() {
		public void run() {
			doImportantTask();
		}
	}).buildAndSchedule();
```

### Build and schedule a user job with a IRunnableWithProgress
```java
Job myUserJob = Jobs.builder()
	.title("Very important task triggered by user").isUserJob()
	.runnable(new IRunnableWithProgress() {
		@Override
		public void run(IProgressMonitor monitor) throws InvocationTargetException,
				InterruptedException {
			doImportantTask(monitor);
		}
	}).build();
// add listener to job if you like
// myUserJob.addJobChangeListener(...)
myUserJob.schedule();
```

### Build and schedule job with user feedback

If the user does not choose to run the job in the background, then they will know
when the job has completed because the progress dialog will close.
However, if they choose to run the job in the background
(by using the dialog button or the preference), they will not know when
the job is finished.

If the userFeedbackForFinishedJob is used and the progress dialog is not modal,
it causes the job to remain in the progress view. A hyperlink with the given title
is created and when the user clicks on it, the given UserFeedbackRunnable gets
executed to show the results of the finished job. This allows to not interrupt the
user because the job results are not not displayed immediately.

```java
Jobs.builder()
	.title("Long running task in progress")
	.runnable(new IRunnableWithProgress() {
		@Override
		public void run(IProgressMonitor monitor) throws InvocationTargetException,
				InterruptedException {
			doImportantTask(monitor);
		}
	}).userFeedbackForFinishedJob("Long running progress completed", new UserFeedbackRunnable() {
		@Override
		public void performUserFeedback(IStatus jobResult) {
			if (jobResult.getSeverity() == IStatus.ERROR) {
				showAppropriateError(jobResult);
			} else {
				informAboutJobResultAndUpdateUi();
			}
		}
	}).buildAndShedule();
```
