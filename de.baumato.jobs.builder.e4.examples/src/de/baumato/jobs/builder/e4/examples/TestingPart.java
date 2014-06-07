package de.baumato.jobs.builder.e4.examples;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import de.baumato.jobs.builder.Jobs;
import de.baumato.jobs.builder.UserFeedbackRunnable;

public class TestingPart {

  private static final int WIDTH = 350;
  private static final int HEIGHT = 110;

  @Inject
  private Display display;

  @Inject
  private Shell shell;

  private Composite parent;

  @PostConstruct
  public void createComposite(Composite parent) {
    this.parent = parent;
    parent.setLayout(new FillLayout(SWT.VERTICAL));
    addJobButtons();
  }

  private void addJobButtons() {
    shortestDefinedJob();
    defaultJob();
    highPriorityJob();
    lowestPriorityJob();
    notConcurrentlyJob();
    userJob();
    userJobWithListener();
    userJobWithFeedback();
    defaultJobWithFeedback();
    defaultJobWithImmediateFeedback();
    imageJob();
    systemJob();
    compactSystemJobWithTitle();
    compactSystemJobWithDefaultTitle();
    scheduledWithDelayJob();
  }

  private void shortestDefinedJob() {
    Button btnButton = new Button(parent, SWT.PUSH);
    final String title = "shortest defined job";
    btnButton.setText(title);
    HtmlToolTip.applyWithPre(btnButton,
        "Jobs.builder(title, new TestRunnable())\n  .buildAndSchedule();", WIDTH, HEIGHT);
    btnButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        Jobs.builder(title, new TestRunnable()).buildAndSchedule();
      }
    });
  }

  private void defaultJob() {
    Button btnButton = new Button(parent, SWT.PUSH);
    final String title = "default job";
    btnButton.setText(title);
    HtmlToolTip
        .applyWithPre(
            btnButton,
            "Jobs.builder()\n  .title(title)\n  .runnable(new TestRunnable(6))\n  .buildAndSchedule();",
            WIDTH, HEIGHT);
    btnButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        Jobs.builder().title(title).runnable(new TestRunnable(6)).buildAndSchedule();
      }
    });
  }

  private void highPriorityJob() {
    Button btnButton = new Button(parent, SWT.PUSH);
    final String title = "hight priority job";
    btnButton.setText(title);
    HtmlToolTip
        .applyWithPre(
            btnButton,
            "Jobs.builder()\n  .title(title)\n  .highPriority()\n  .runnable(new TestRunnable())\n  .buildAndSchedule();",
            WIDTH, HEIGHT);
    btnButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        Jobs.builder().title(title).highPriority().runnable(new TestRunnable()).buildAndSchedule();
      }
    });
  }

  private void lowestPriorityJob() {
    Button btnButton = new Button(parent, SWT.PUSH);
    final String title = "lowest priority job";
    btnButton.setText(title);
    HtmlToolTip
        .applyWithPre(
            btnButton,
            "Jobs.builder()\n  .title(title)\n  .lowestPriority()\n  .runnable(new TestRunnable(8))\n  .buildAndSchedule();",
            WIDTH, HEIGHT);
    btnButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        Jobs.builder().title(title).lowestPriority().runnable(new TestRunnable(8))
            .buildAndSchedule();
      }
    });
  }

  private void notConcurrentlyJob() {
    Button btnButton = new Button(parent, SWT.PUSH);
    final String title = "not concurrently job";
    btnButton.setText(title);
    HtmlToolTip
        .applyWithPre(
            btnButton,
            "Jobs.builder()\n  .title(title)\n  .runsNotConcurrently()\n  .runnable(new TestRunnable())\n  .buildAndSchedule();",
            WIDTH, HEIGHT);
    btnButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        Jobs.builder().title(title).runsNotConcurrently().runnable(new TestRunnable())
            .buildAndSchedule();
      }
    });
  }

  private void userJob() {
    Button btnButton = new Button(parent, SWT.PUSH);
    final String title = "user job";
    btnButton.setText(title);
    HtmlToolTip
        .applyWithPre(
            btnButton,
            "Jobs.builder()\n  .title(title)\n  .isUserJob()\n  .runnable(new TestRunnable())\n  .buildAndSchedule();",
            WIDTH, HEIGHT);
    btnButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        Jobs.builder().title(title).isUserJob().runnable(new TestRunnable()).buildAndSchedule();
      }
    });
  }

  private void userJobWithListener() {
    Button btnButton = new Button(parent, SWT.PUSH);
    final String title = "user job with listener";
    btnButton.setText(title);
    String toolTipText =
        "Jobs.builder()\n" + "  .title(title)\n" + "  .isUserJob()\n"
            + "  .runnable(new TestRunnable())\n"
            + "  .addJobChangeListener(new JobChangeAdapter() {\n"
            + "    public void done(IJobChangeEvent event) {\n" + "      Jobs.builder()\n"
            + "        .title(title + \" is done. This is a follow up job.\")\n"
            + "        .runnable(new TestRunnable())\n" + "        .buildAndSchedule();\n"
            + "    }\n" + "  }).buildAndSchedule();";
    HtmlToolTip.applyWithPre(btnButton, toolTipText, 600, 200);
    btnButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        Jobs.builder().title(title).isUserJob().runnable(new TestRunnable())
            .addJobChangeListener(new JobChangeAdapter() {
              @Override
              public void done(IJobChangeEvent event) {
                Jobs.builder().title(title + " is done. This is a follow up job.")
                    .runnable(new TestRunnable()).buildAndSchedule();
              }
            }).buildAndSchedule();
      }
    });
  }

  private void userJobWithFeedback() {
    Button btnButton = new Button(parent, SWT.PUSH);
    final String title = "user job with feedback";
    btnButton.setText(title);
    HtmlToolTip.applyWithPre(btnButton, "Jobs.builder()\n" + ".title(title)\n" + ".isUserJob()\n"
        + ".runnable(new TestRunnable())\n"
        + ".userFeedback(title + \":Done\", new UserFeedbackRunnable() {\n"
        + "  public void performUserFeedback(IStatus jobResult) {\n"
        + "    MessageDialog.openInformation(shell, title, \"Done\");\n" + "  }\n"
        + "}).buildAndSchedule();", WIDTH + 250, HEIGHT + 50);
    btnButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        Jobs.builder().title(title).isUserJob().runnable(new TestRunnable())
            .userFeedback(title + ":Done", new UserFeedbackRunnable() {
              @Override
              public void performUserFeedback(IStatus jobResult, boolean immediateFeedback) {
                System.out.println(Thread.currentThread());
                MessageDialog.openInformation(shell, title, "Done");
              }
            }).buildAndSchedule();
      }
    });
  }

  private void defaultJobWithFeedback() {
    Button btnButton = new Button(parent, SWT.PUSH);
    final String title = "default job with feedback";
    btnButton.setText(title);
    HtmlToolTip.applyWithPre(btnButton, "Jobs.builder()\n" + ".title(title)\n"
        + ".runnable(new TestRunnable())\n"
        + ".userFeedback(title + \":Done\", new UserFeedbackRunnable() {\n"
        + "  public void performUserFeedback(IStatus jobResult) {\n"
        + "    MessageDialog.openInformation(shell, title, \"Done\");\n" + "  }\n"
        + "}).buildAndSchedule();", WIDTH + 250, HEIGHT + 50);
    btnButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        Jobs.builder().title(title).runnable(new TestRunnable())
            .userFeedback(title + ":Done", new UserFeedbackRunnable() {
              @Override
              public void performUserFeedback(IStatus jobResult, boolean immediateFeedback) {
                System.out.println(Thread.currentThread());
                MessageDialog.openInformation(shell, title, "Done");
              }
            }).buildAndSchedule();
      }
    });
  }

  private void defaultJobWithImmediateFeedback() {
    Button btnButton = new Button(parent, SWT.PUSH);
    final String title = "default job with immediate feedback";
    btnButton.setText(title);
    HtmlToolTip.applyWithPre(btnButton, "Jobs.builder()\n" + ".title(title)\n"
        + ".runnable(new TestRunnable())\n"
        + ".givesImmediateUserFeedback(new UserFeedbackRunnable() {\n"
        + "  public void performUserFeedback(IStatus jobResult) {\n"
        + "    MessageDialog.openInformation(shell, title, \"Done\");\n" + "  }\n"
        + "}).buildAndSchedule();", WIDTH + 250, HEIGHT + 50);
    btnButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        Jobs.builder().title(title).runnable(new TestRunnable())
            .immediateUserFeedback(new UserFeedbackRunnable() {
              @Override
              public void performUserFeedback(IStatus jobResult, boolean immediateFeedback) {
                MessageDialog.openInformation(shell, jobResult.getMessage(), "Done");
              }
            }).buildAndSchedule();
      }
    });
  }


  private void imageJob() {
    Button btnButton = new Button(parent, SWT.PUSH);
    final String title = "job with image";
    btnButton.setText(title);
    HtmlToolTip.applyWithPre(btnButton, "Jobs.builder()\n" + ".title(title)\n" + ".isUserJob()\n"
        + ".image(getImage())\n" + ".runnable(new TestRunnable())\n" + ".buildAndSchedule();",
        WIDTH, HEIGHT);
    btnButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        Jobs.builder().title(title).isUserJob().image(getImage()).runnable(new TestRunnable())
            .buildAndSchedule();
      }
    });
  }

  private void systemJob() {
    Button btnButton = new Button(parent, SWT.PUSH);
    final String title = "system job";
    btnButton.setText(title);
    HtmlToolTip.applyWithPre(btnButton, "Jobs.builder()\n" + "  .title(title)\n"
        + "  .isSystemJob()\n" + "  .runnable(new TestRunnable())\n" + "  .buildAndSchedule();",
        WIDTH, HEIGHT);
    btnButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        Jobs.builder().title(title).isSystemJob().runnable(new TestRunnable()).buildAndSchedule();
      }
    });
  }

  private void compactSystemJobWithTitle() {
    Button btnButton = new Button(parent, SWT.PUSH);
    final String title = "compact system job with title";
    btnButton.setText(title);
    HtmlToolTip.applyWithPre(btnButton, "Jobs.builder(title, new TestRunnable())\n"
        + "  .isSystemJob()\n" + "  .buildAndSchedule();", WIDTH, HEIGHT);
    btnButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        Jobs.builder(title, new TestRunnable()).isSystemJob().buildAndSchedule();
      }
    });
  }

  private void compactSystemJobWithDefaultTitle() {
    Button btnButton = new Button(parent, SWT.PUSH);
    final String title = "compact system job using default title";
    btnButton.setText(title);
    HtmlToolTip.applyWithPre(btnButton, "Jobs.builder()\n" + "  .runnable(new TestRunnable())\n"
        + "  .isSystemJob()\n" + "  .buildAndSchedule();", WIDTH, HEIGHT);
    btnButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        Jobs.builder().runnable(new TestRunnable()).isSystemJob().buildAndSchedule();
      }
    });
  }

  private void scheduledWithDelayJob() {
    Button btnButton = new Button(parent, SWT.PUSH);
    final String title = "schedule job with delay";
    btnButton.setText(title);
    HtmlToolTip.applyWithPre(btnButton, "Jobs.builder()\n" + "  .title(title)\n"
        + "  .isSystemJob()\n" + "  .runnable(new TestRunnable())\n"
        + "  .buildAndScheduleWithDelay(3, TimeUnit.SECONDS);", WIDTH, HEIGHT);
    btnButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        Jobs.builder().title("scheduled job").runnable(new TestRunnable())
            .buildAndScheduleWithDelay(3, TimeUnit.SECONDS);
      }
    });
  }

  private ImageDescriptor getImage() {
    try {
      URL url = new URL("platform:/plugin/de.baumato.jobs.builder.e4.examples/icons/sample.png");
      ImageDescriptor imgDescr = ImageDescriptor.createFromURL(url);
      final Image img = imgDescr.createImage(display);
      shell.addListener(SWT.Dispose, new Listener() {
        @Override
        public void handleEvent(Event event) {
          img.dispose();
        }
      });
      return imgDescr;
    } catch (Exception e) {
      return ImageDescriptor.getMissingImageDescriptor();
    }
  }

  @Focus
  public void setFocus() {
    if (parent != null && !parent.isDisposed()) {
      parent.setFocus();
    }
  }

  private final class TestRunnable implements Runnable {
    private final int jobDurationInSeconds;

    TestRunnable() {
      this(3);
    }

    TestRunnable(int jobDurationInSeconds) {
      this.jobDurationInSeconds = jobDurationInSeconds;
    }

    @Override
    public void run() {
      for (int i = 0; i < jobDurationInSeconds; i++) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
