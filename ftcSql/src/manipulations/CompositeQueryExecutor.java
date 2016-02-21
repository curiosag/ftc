package manipulations;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.cg.common.check.Check;
import org.cg.common.interfaces.Progress;
import org.cg.common.threading.OnAllProcessed;
import org.cg.common.threading.Parallel;
import org.cg.common.threading.SynchronizedProgressDecorator;
import org.cg.ftc.shared.interfaces.Connector;
import org.cg.common.threading.Parallel.TaskHandler;

public class CompositeQueryExecutor {

	private final Progress progress;
	private final List<String> queries;
	private final Connector connector;
	private final OnAllProcessed onAllProcessed;
	private int numberCompositesExecuted = 0;
	private TaskHandler<Void> compositeQueryExecutions = null;

	private boolean cancelled = false;
	private boolean started = false;
	private int maxThreads = 10;

	public CompositeQueryExecutor(List<String> queries, Connector connector, Progress p,
			OnAllProcessed onAllProcessed) {
		this.progress = new SynchronizedProgressDecorator(p);
		progress.init(queries.size());

		this.queries = queries;
		this.connector = connector;
		this.onAllProcessed = onAllProcessed;
	}

	public CompositeQueryExecutor executeAsync() {
		Check.isFalse(cancelled);
		Check.isFalse(started);
		started = true;
		compositeQueryExecutions = new Parallel.ForEach<String, Void>(queries)
				.withFixedThreads(maxThreads)
				.apply(new Parallel.F<String, Void>() {
					public Void apply(String s) {
						connector.fetch(s);
						incExecutions();
						return null;
					}
				});
		return this;
	}

	public synchronized void cancel() {
		if (!cancelled)
			try {
				compositeQueryExecutions.shutDownNow();
				compositeQueryExecutions.awaitTermination(0, TimeUnit.NANOSECONDS);
			} catch (InterruptedException e) {
			}
		cancelled = true;
	}

	public int numberExecuted() {
		return numberCompositesExecuted;
	}

	private synchronized void incExecutions() {
		numberCompositesExecuted++;
		progress.announce(numberCompositesExecuted);
		if (numberCompositesExecuted == queries.size())
			onAllProcessed.announce();
	}

}
