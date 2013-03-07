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

/**
 * The ThrowingRunnable in comparison to the <code>Runnable</code> may throw
 * exceptions so the client is not forced to wrap them in runtime exceptions.
 * 
 * @author tobbaumann
 * 
 */
public interface ThrowingRunnable {

	/**
	 * Runs the ThrowingRunnable. May throw Exceptions.
	 * 
	 * @throws Exception
	 */
	public void run() throws Exception;

}
