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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import com.google.common.base.Throwables;

/**
 * Lets a <tt>ThrowingRunnable</tt> act as a <tt>IRunnableWithProgress</tt>.
 *
 * @author tobbaumann
 *
 */
public class ThrowingRunnableAdapter implements IRunnableWithProgress {

	private final String title;
	private final ThrowingRunnable runnable;

	/**
	 * @param title
	 *            the title to be display in the progress monitor
	 * @param runnable
	 *            the runnable to run
	 */
	public ThrowingRunnableAdapter(String title, ThrowingRunnable runnable) {
		this.title = checkNotNull(title);
		this.runnable = checkNotNull(runnable);
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		try {
			if (monitor != null) {
				monitor.beginTask(title, IProgressMonitor.UNKNOWN);
			}
			runnable.run();
			if (monitor != null) {
				monitor.done();
			}
		} catch (Exception e) {
			Throwables.propagateIfPossible(e);
			Throwables.propagateIfInstanceOf(e, InterruptedException.class);
			throw new InvocationTargetException(e);
		}
	}
}
