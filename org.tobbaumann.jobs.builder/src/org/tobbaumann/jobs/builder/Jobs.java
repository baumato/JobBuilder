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
/**
 *
 */
package org.tobbaumann.jobs.builder;

/**
 * @author tobbaumann
 *
 */
public class Jobs {

	private Jobs() {
	}

	/**
	 * Returns a job builder. You may want to use following methods:
	 * <ul>
	 * <li>title(if not set a default title is used)</li>
	 * <li>isSystemJob or isUserJob(default)</li>
	 * <li>runnable or runnableWithProgress (one of the method has to be used)</li>
	 * <li><b>build</b> to get the Job instance</li>
	 * <li>or <b>buildAndShedule</b> that builds, schedules and returns the job.</li>
	 * </ul>
	 *
	 * @return the builder instance
	 * @throws IllegalStateException thrown by build or buildAndShedule if neither runnable nor
	 *             runnableWithProgress has been set or if given title is null or empty.
	 */
	public static JobBuilder builder() {
		return new JobBuilder();
	}
}
