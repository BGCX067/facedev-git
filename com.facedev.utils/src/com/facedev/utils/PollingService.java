package com.facedev.utils;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import static java.lang.Math.min;

/**
 * Implements polling logic which assumes that some resource is requested for updates every specific interval.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public final class PollingService {
	public static interface PollingTask extends Callable<Boolean> {
		/**
		 * Performs some piece of polling functionality.
		 * @return <code>true</code> if this task is still active and <code>false</code> if it should be removed form queue.
		 * Returning <code>null</code> means the same as returning <code>true</code>.
		 * @throws Throwable
		 */
		Boolean call() throws Exception;
	}
	
	private static final PollingService instance = new PollingService();
	
	private final Scheduler scheduler = new Scheduler();
	private volatile List<ScheduledTask> tasks;
	
	private PollingService() {}
	
	public static PollingService getInstance() {
		return instance;
	}
	
	/**
	 * Schedules specific task to execute with given delay (in milliseconds).
	 * It is not guaranteed that delay will hold. In case when there are a lot of long-running tasks
	 * delay will be enlarged. Call to the underline task will be synchronized over PollingService instance.
	 * @param task
	 * @param delay (milliseconds)
	 */
	public synchronized void schedule(PollingTask task, long delay) {
		if (task == null) {
			throw new NullPointerException();
		}
		if (tasks == null) {
			tasks = new CopyOnWriteArrayList<ScheduledTask>();
			scheduler.start();
		}
		tasks.add(new ScheduledTask(task, delay));
		notifyAll();
	}
	
	public synchronized void remove(PollingTask task) {
		if (task == null) {
			throw new NullPointerException();
		}
		for (ScheduledTask item : tasks) {
			if (item.task.equals(task)) {
				tasks.remove(item);
			}
		}
	}

	private class ScheduledTask {
		private final PollingTask task;
		private final long delay;
		
		// keep this confined to Scheduler thread.
		private long lastRun;
		
		ScheduledTask(PollingTask task, long delay) {
			this.task = task;
			this.delay = delay;
		}
	}
	
	private class Scheduler extends Thread {
		
		Scheduler() {
			setDaemon(true);
		}

		@Override
		public void run() {
			try {
				while (Thread.currentThread().isAlive()) {
					runTasks();
				}
			} catch (InterruptedException ex) {
				// ignore & return
			}
		}

		private void runTasks() throws InterruptedException {
			synchronized (PollingService.this) {
				final long now = System.currentTimeMillis();
				long interval = Long.MAX_VALUE;
				for (ScheduledTask task : tasks) {
					interval = min(interval, runOrCalculateWaitTime(now, task));
				}
				PollingService.this.wait(interval);
			}
		}

		private long runOrCalculateWaitTime(final long now, ScheduledTask task) throws InterruptedException {
			long wait4next = task.lastRun + task.delay - now;
			if (wait4next > 0) {
				return wait4next;
			}
			task.lastRun = now;
			try {
				Boolean result = task.task.call();
				if (result != null && !result) {
					tasks.remove(task);
				}
			} catch (InterruptedException ex) {
				throw ex;
			} catch (Throwable th) {
				th.printStackTrace();
				tasks.remove(task);
			}
			return task.delay;
		}
	}
}
