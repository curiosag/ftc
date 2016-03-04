package manipulations;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import javax.swing.table.DefaultTableModel;
import org.cg.common.check.Check;
import org.cg.common.core.Logging;
import org.cg.common.http.HttpStatus;
import org.cg.common.interfaces.Progress;
import org.cg.common.threading.OnAllProcessed;
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
	private final OnAllProcessed<List<QueryResult>> onAllProcessed;
	private TaskHandler<Void> compositeQueryExecutions = null;
	private List<QueryResult> queryResults = new LinkedList<QueryResult>();

	private boolean cancelled = false;
	private boolean started = false;
	private int maxThreads = 10;

	public CompositeQueryExecutor(List<String> queries, Connector connector, Progress p,
			OnAllProcessed<List<QueryResult>> onAllProcessed, Logging logger) {
		this.progress = new SynchronizedProgressDecorator(p);
		this.logger = logger;
		progress.init(queries.size());

		this.queries = queries;
		this.connector = connector;
		this.onAllProcessed = onAllProcessed;
	}

	public CompositeQueryExecutor executeAsync() {
		Check.isFalse(cancelled);
		Check.isFalse(started);
		started = true;
		compositeQueryExecutions = new Parallel.ForEach<String, Void>(queries).withFixedThreads(maxThreads)
				.apply(new Parallel.F<String, Void>() {
					public Void apply(String s) {
						incExecutions(s, connector.fetch(s));
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
		return queryResults.size();
	}

	private synchronized void incExecutions(String queryExecuted, QueryResult queryResult) {
		if (queryResult.status != HttpStatus.SC_OK)
			logger.Info(String.format("status %s msg %s running composite query %s", queryExecuted,
					queryResult.status.name(), queryResult.message.or("")));

		queryResults.add(queryResult);
		progress.announce(numberExecuted());
		if (numberExecuted() == queries.size())
			onAllProcessed.announce(queryResults);
	}

	public static QueryResult joinOkResults(List<QueryResult> results) {
		Vector<String> columns = createPopulated("rowid");
		Vector<Vector<String>> rows = new Vector<Vector<String>>();

		for (QueryResult r : results)
			if (r.status == HttpStatus.SC_OK && r.data.get().getRowCount() > 0 && r.data.get().getColumnCount() > 0)
				rows.add(createPopulated(r.data.get().getValueAt(0, 0).toString()));

		return new QueryResult(HttpStatus.SC_OK, new DefaultTableModel(rows, columns), "");
	}

	private static Vector<String> createPopulated(String value) {
		Vector<String> result = new Vector<String>();
		result.add(value);
		return result;
	}

}
