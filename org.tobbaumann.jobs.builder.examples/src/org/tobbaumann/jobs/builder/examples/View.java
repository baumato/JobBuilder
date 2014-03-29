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
package org.tobbaumann.jobs.builder.examples;

import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.part.ViewPart;
import org.tobbaumann.jobs.builder.Jobs;
import org.tobbaumann.jobs.builder.UserFeedbackRunnable;

public class View extends ViewPart {
  public static final String ID = "org.tobbaumann.jobs.builder.examples.view";

  private static final int WIDTH = 350;
  private static final int HEIGHT = 110;

  private Composite parent;
  private Shell shell;

  @Override
  public void createPartControl(Composite parent) {
    this.parent = parent;
    this.shell = parent.getShell();
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
    imageJob();
    systemJob();
    compactSystemJobWithTitle();
    compactSystemJobWithDefaultTitle();
    scheduledJob();
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
        + ".userFeedbackForFinishedJob(title + \":Done\", new UserFeedbackRunnable() {\n"
        + "  public void performUserFeedback(IStatus jobResult) {\n"
        + "    MessageDialog.openInformation(shell, title, \"Done\");\n" + "  }\n"
        + "}).buildAndSchedule();", WIDTH + 250, HEIGHT + 50);
    btnButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        Jobs.builder().title(title).isUserJob().runnable(new TestRunnable())
            .userFeedbackForFinishedJob(title + ":Done", new UserFeedbackRunnable() {
              @Override
              public void performUserFeedback(IStatus jobResult) {
                MessageDialog.openInformation(shell, title, "Done");
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

  private void scheduledJob() {
    Button btnButton = new Button(parent, SWT.PUSH);
    final String title = "scheduled job";
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
    return Activator.getImageDescriptor("icons/sample.png");
  }

  @Override
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
