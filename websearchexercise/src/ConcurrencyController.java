package websearchexercise;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConcurrencyController {

	private ConcurrentLinkedQueue<Callable<MatchResult>> tasks;
    private List<MatchResult> results; 
    private List<Worker<MatchResult>> workers;
	
	public ConcurrencyController() {
		this(20);
	}

	public ConcurrencyController(int numThreads) {
		tasks = new ConcurrentLinkedQueue<Callable<MatchResult>>();
		workers = new ArrayList<Worker<MatchResult>>();
		results = Collections.synchronizedList(new ArrayList<MatchResult>(tasks.size()));
		for (int i = 0; i < numThreads; i++) {
			workers.add(new Worker<MatchResult>("Worker "+i, new AtomicBoolean(), tasks, results));
		}
	}
	
	public void invokeTasks(Collection<Callable<MatchResult>> tasks) {
		this.tasks.addAll(tasks);
		for (Worker<MatchResult> worker: workers) {
			worker.start();
		}
	}
	
	//blocks until all workers are done
	public List<MatchResult> getResults() {
		while (true) {
			boolean hasActiveThreads = false;
			for (Worker<MatchResult> w: workers) {
				if (w.isAlive()) {
					hasActiveThreads = true;
					break;
				}
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				
			}
			if (!hasActiveThreads) {
				return results;		
			}
		}		
	}
}
