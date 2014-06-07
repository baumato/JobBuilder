/*******************************************************************************
 * Copyright (c) 2014 Tobias Baumann.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Tobias Baumann - initial API and implementation
 ******************************************************************************/
package de.baumato.jobs.builder;

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.core.runtime.jobs.ISchedulingRule;

import com.google.common.base.Objects;

/**
 * Jobs scheduled with this rule will not run concurrently but sequentially when the same lock
 * object has been given.
 */
class NotConcurrentlyRule implements ISchedulingRule {

  private final Object lock;

  public NotConcurrentlyRule(Object lock) {
    this.lock = checkNotNull(lock);
  }

  @Override
  public boolean contains(ISchedulingRule rule) {
    return rule == this;
  }

  @Override
  public boolean isConflicting(ISchedulingRule rule) {
    if (rule instanceof NotConcurrentlyRule) {
      NotConcurrentlyRule that = (NotConcurrentlyRule) rule;
      return lock.equals(that.lock);
    }
    return false;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this).add("lock", lock).toString();
  }
}
