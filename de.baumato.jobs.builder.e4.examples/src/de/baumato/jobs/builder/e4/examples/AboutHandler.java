package de.baumato.jobs.builder.e4.examples;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class AboutHandler {
  @Execute
  public void execute(Shell shell) {
    MessageDialog.openInformation(shell, "About", "Testing de.baumato.jobs.builder bundle.");
  }
}
