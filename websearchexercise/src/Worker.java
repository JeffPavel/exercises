package websearchexercise;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Worker<T> extends Thread {
	private AtomicBoolean execute;
	private ConcurrentLinkedQueue<Callable<T>> tasks;
	private List<T> results;

	public Worker(String name, AtomicBoolean execute, ConcurrentLinkedQueue<Callable<T>> tasks, List<T> results) {
		super(name);
		this.execute = execute;
		this.tasks = tasks;
		this.results = results;
	}

	@Override
	public void run() {
		try {
			// Continue to execute when the execute flag is true, or when there are tasks
			while (execute.get() || !tasks.isEmpty()) {
				Callable<T> callable;
				// Poll a runnable from the queue and execute it
				while ((callable = tasks.poll()) != null) {
					WebsiteSearcher.log(String.format("%s is processing %s", this.getName(), callable.toString()));
					T result = callable.call();
					results.add(result);
				}
				execute.set(false);
			}
		} catch (Exception e) {
			WebsiteSearcher.log("Error occurred in thread: " + this.getName(), true);
			e.printStackTrace();
		}
	}

	public boolean isActive() {
		return execute.get();
	}
}
