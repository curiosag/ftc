package manipulations;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import javax.swing.table.DefaultTableModel;
import org.cg.common.check.Check;
import org.cg.common.core.Logging;
import org.cg.common.http.HttpStatus;
import org.cg.common.interfaces.Continuation;
import org.cg.common.interfaces.Progress;
import org.cg.common.threading.Parallel;
import org.cg.common.threading.SynchronizedProgressDecorator;
import org.cg.ftc.shared.interfaces.Connector;
import org.cg.ftc.shared.structures.QueryResult;
import org.cg.common.threading.Parallel.TaskHandler;

public class CompositeQueryExecutor {

	private final Logging logger;
	private final Progress progress;
	private final List<String> queries;
	private final Connector connector;
	private final Continuation<QueryResult> onCompositesDone;
	private TaskHandler<Void> compositeQueryExecutions = null;
	private List<QueryResult> queryResults = new LinkedList<QueryResult>();

	private boolean cancelled = false;
	private boolean started = false;
	private int maxThreads = 2;

	public CompositeQueryExecutor(List<String> queries, Connector connector, Progress p,
			Continuation<QueryResult> onAllProcessed, Logging logger) {
		this.progress = new SynchronizedProgressDecorator(p);
		this.logger = logger;
		progress.init(queries.size());

		this.queries = queries;
		this.connector = connector;
		this.onCompositesDone = onAllProcessed;
	}

	public CompositeQueryExecutor executeAsync() {
		Check.isFalse(cancelled);
		Check.isFalse(started);
		started = true;
		compositeQueryExecutions = new Parallel.ForEach<String, Void>(queries).withFixedThreads(maxThreads)
				.apply(new Parallel.F<String, Void>() {
					public Void apply(String s) {
						// async queries tend to create sc_service_unavailable
						QueryResult result = new QueryResult(HttpStatus.SC_SERVICE_UNAVAILABLE, null, null);
						while (! cancelled && result.status == HttpStatus.SC_SERVICE_UNAVAILABLE)
							result = connector.fetch(s);
						incExecutions(s, result);
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

	private void incExecutions(String queryExecuted, QueryResult queryResult) {
		if (queryResult.status != HttpStatus.SC_OK)
			logger.Info(String.format("Not o.k.: status %s msg %s running composite query %s",
					queryResult.status.name(), queryResult.message.or(""), queryExecuted));

		queryResults.add(queryResult);
		progress.announce(queryResults.size());

		if (queryResults.size() == queries.size()) 
			onCompositesDone.invoke(joinOkResults(queryResults));
	}

	private QueryResult joinOkResults(List<QueryResult> results) {
		// in fact each result only has the count of affected rows, which is always 1, but not the rowid
		// for an unconditional delete statement the result would be "all rows" instead of a number btw
		int count = 0;
		for (QueryResult r : results) 
			if (r.status == HttpStatus.SC_OK)
				count ++;

		Vector<Vector<String>> rows = new Vector<Vector<String>>();
		rows.add(createPopulated(Integer.toString(count)));
		
		return new QueryResult(HttpStatus.SC_OK, new DefaultTableModel(rows, createPopulated("affected_rows")), "");
	}

	private Vector<String> createPopulated(String value) {
		Vector<String> result = new Vector<String>();
		result.add(value);
		return result;
	}

}
