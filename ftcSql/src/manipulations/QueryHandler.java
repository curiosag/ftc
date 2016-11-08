package manipulations;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.Token;
import org.cg.common.check.Check;
import org.cg.common.core.Logging;
import org.cg.common.http.HttpStatus;
import org.cg.common.interfaces.Continuation;
import org.cg.common.interfaces.Progress;
import org.cg.common.structures.StackLight;
import org.cg.common.util.Op;
import org.cg.common.util.StringUtil;
import org.cg.ftc.parser.FusionTablesSqlLexer;
import org.cg.ftc.shared.interfaces.Connector;
import org.cg.ftc.shared.interfaces.SyntaxElement;
import org.cg.ftc.shared.interfaces.SyntaxElementType;
import org.cg.ftc.shared.structures.ClientSettings;
import org.cg.ftc.shared.structures.ColumnInfo;
import org.cg.ftc.shared.structures.ConnectionStatus;
import org.cg.ftc.shared.structures.QueryAtHand;
import org.cg.ftc.shared.structures.QueryResult;
import org.cg.ftc.shared.structures.TableInfo;
import org.cg.ftc.shared.uglySmallThings.Const;

import com.google.common.base.Optional;

import manipulations.QueryPatching;
import manipulations.results.RefactoredSql;
import manipulations.results.ResolvedTableNames;
import manipulations.results.TableInfoResolver;
import manipulations.results.TableReference;

public class QueryHandler extends Observable {
	private final boolean reload = true;
	private final Logging logger;
	private final Connector connector;
	private final boolean preview = true;
	private final boolean execute = !preview;
	private final List<TableInfo> tableInfo = new LinkedList<TableInfo>();
	private final Map<String, TableInfo> tableNameToTableInfo = new HashMap<String, TableInfo>();
	private final Map<String, TableInfo> tableIdToTableInfo = new HashMap<String, TableInfo>();
	private final Map<String, TableInfo> exteranlTableIdToTableInfo = new HashMap<String, TableInfo>();
	private final List<String> inexistingExternalTableIds = new ArrayList<String>();

	private TableNameToIdMapper tableNameToIdMapper = new TableNameToIdMapper();

	private final ClientSettings settings;
	private final TableInfoResolver tableInfoResolver = new TableInfoResolver() {

		@Override
		public Optional<TableInfo> getTableInfo(String nameOrId) {
			loadTableCaches(false);
			return getTableInfoAsOptional(nameOrId);
		}

		@Override
		public List<TableInfo> listTables() {
			loadTableCaches(false);
			return tableInfo;
		}
	};
	private CompositeQueryExecutor compositeQueryExecutor;

	public QueryHandler(Logging logger, Connector connector, ClientSettings settings) {
		Check.notNull(logger);
		Check.notNull(connector);
		this.logger = logger;
		this.connector = connector;
		this.settings = settings;
	}

	public ConnectionStatus reset(Dictionary<String, String> connectionInfo) {
		ConnectionStatus result = connector.reset(connectionInfo);
		reloadTableList();
		return result;
	};

	private void log(String msg) {
		logger.Info(msg);
	}

	private QueryManipulator createManipulator(String query) {
		return new QueryManipulator(tableInfoResolver, tableNameToIdMapper, logger, query);
	}

	public void reloadTableList() {
		loadTableCaches(reload);
	}

	private Optional<TableReference> resolveTableReferenceInQuery(NameRecognitionTable tableRecognized) {
		if (!tableRecognized.TableName().isPresent())
			return Optional.absent();

		Optional<TableInfo> maybeInfo = getTableInfoAsOptional(tableRecognized.TableName().get());
		if (!maybeInfo.isPresent())
			return Optional.absent();

		TableInfo info = maybeInfo.get();
		return Optional
				.of(new TableReference(info.name, tableRecognized.TableAlias(), info.id, getColumnNames(info.columns)));
	}

	private Optional<TableInfo> getTableInfoAsOptional(String tableName) {
		return Optional.fromNullable(resolveTableInfo(tableName));
	}

	private TableInfo resolveTableInfo(String tableName) {
		loadTableCaches(false);

		String tableRef = StringUtil.stripQuotes(tableName);
		TableInfo result = tableNameToTableInfo.get(tableRef);
		if (result == null)
			result = tableIdToTableInfo.get(tableRef);
		if (result == null)
			result = resolveExternalTable(tableName).orNull();

		return result;
	}

	private Optional<TableInfo> resolveExternalTable(String tableId) {
		if (!validTableId(tableId))
			return Optional.absent();

		Optional<TableInfo> result = probeCachedExternalTables(tableId);
		if (result.isPresent())
			return result;

		if (probeInexistingExternaTables(tableId))
			return Optional.absent();

		return probeExternalTable(tableId);
	}

	private boolean validTableId(String tableId) {
		return (tableId.length() == Const.LEN_TABLEID) && tableId.indexOf(" ") < 0;
	}

	private boolean probeInexistingExternaTables(String tableId) {
		return inexistingExternalTableIds.indexOf(tableId) >= 0;
	}

	private Optional<TableInfo> probeCachedExternalTables(String tableId) {
		return Optional.fromNullable(exteranlTableIdToTableInfo.get(tableId));
	}

	private Optional<TableInfo> probeExternalTable(String tableId) {
		Optional<TableInfo> result;

		logger.Info("probing for external table " + tableId);
		QueryResult queryResult = hdlQuery(StatementType.DESCRIBE, String.format("describe %s;", tableId), execute);

		if (queryResult.message.isPresent())
			logger.Info(queryResult.message.get());

		if (!queryResult.data.isPresent()) {
			inexistingExternalTableIds.add(tableId);
			result = Optional.absent();
		} else {
			TableInfo tableInfo = createTableInfo(tableId, queryResult.data.get());
			exteranlTableIdToTableInfo.put(tableId, tableInfo);
			result = Optional.of(tableInfo);
		}
		return result;
	}

	private final static int idxName = 1;
	private final static int idxType = 2;
	private final static int countResultColumns = 3;

	private TableInfo createTableInfo(String tableId, TableModel tableModel) {
		if (tableModel.getRowCount() == 0)
			return null;
		Check.isTrue(tableModel.getColumnCount() == countResultColumns);

		List<ColumnInfo> columns = new LinkedList<ColumnInfo>();
		for (int i = 0; i < tableModel.getRowCount(); i++)
			columns.add(new ColumnInfo(getStringValueAt(tableModel, i, idxName),
					getStringValueAt(tableModel, i, idxType), ""));

		return new TableInfo(tableId, tableId, "", columns);
	}

	private String getStringValueAt(TableModel m, int rowIndex, int columnIndex) {
		return (String) m.getValueAt(rowIndex, columnIndex);
	}

	private synchronized List<TableInfo> loadTableCaches(boolean reload) {
		if (tableInfo.isEmpty() || reload) {
			tableInfo.clear();
			tableInfo.addAll(connector.getTableInfo());
			populateTableMaps(tableInfo);
		}
		return tableInfo;
	}

	private void populateTableMaps(List<TableInfo> tableInfo) {
		tableNameToTableInfo.clear();
		tableIdToTableInfo.clear();
		for (TableInfo t : tableInfo) {
			tableNameToTableInfo.put(t.name, t);
			tableIdToTableInfo.put(t.id, t);
		}
		tableNameToIdMapper = new TableNameToIdMapper(tableInfo);
	}

	public List<TableInfo> getTableList() {
		return loadTableCaches(!reload);
	}

	public TableModel getTableInfo() {

		Vector<String> columns = new Vector<String>();
		columns.add("table id");
		columns.add("name");

		Vector<Vector<String>> rows = new Vector<Vector<String>>();

		List<String> names = new ArrayList<String>();

		for (TableInfo i : getTableList()) {
			if (names.contains(i.name))
				log("Duplicate table name: '" + i.name + "' name to ID substitution may fail.");

			names.add(i.name);

			Vector<String> row = new Vector<String>();
			row.add(i.id);
			row.add(i.name);
			rows.add(row);
		}

		return new DefaultTableModel(rows, columns);
	}

	public TableModel getColumnInfo(TableInfo info) {
		Check.notNull(info);

		Vector<String> columns = new Vector<String>();
		columns.add("column name");
		columns.add("type");

		Vector<Vector<String>> rows = new Vector<Vector<String>>();

		for (ColumnInfo i : info.columns) {
			Vector<String> row = new Vector<String>();
			row.add(i.name);
			row.add(i.type);
			rows.add(row);
		}

		return new DefaultTableModel(rows, columns);
	}

	private QueryResult hdlAlterTable(QueryManipulator ftr, boolean preview) {
		ResolvedTableNames id = ftr.getAlterTableIdentifiers();

		String msg;
		if (id.problemsEncountered.isPresent())
			msg = id.problemsEncountered.get();
		else if (preview)
			msg = String.format("Api call rename to %s, table id %s", id.nameTo, id.idFrom.or(""));
		else {
			msg = connector.renameTable(id.idFrom.get(), id.nameTo);
			onStructureChanged();
		}
		return packQueryResult(msg);
	}

	private QueryResult hdlQuery(StatementType statementType, String query, boolean preview) {
		RefactoredSql r = createManipulator(addSemicolon(query)).refactorQuery();

		String prepared = r.refactored;
		if (statementType == StatementType.SELECT)
			prepared = addLimit(prepared);

		QueryResult result;

		if (r.problemsEncountered.isPresent())
			return packQueryResult(r.problemsEncountered.get());

		else if (preview)
			result = packQueryResult(prepared);
		else {
			result = connector.fetch(prepared);
			if (Op.in(statementType, StatementType.CREATE_VIEW, StatementType.DROP, StatementType.ALTER))
				onStructureChanged();
		}
		return result;
	}

	private String addSemicolon(String query) {
		if (query.trim().endsWith(";"))
			return query;
		else
			return query + ";";
	}

	private String addLimit(String refactored) {
		String q = refactored.toUpperCase();
		if (q.indexOf("LIMIT") >= 0)
			return refactored;

		refactored = refactored.replace(";", "");
		if (q.indexOf("OFFSET") < 0)
			refactored = refactored + "\nOFFSET 0";

		refactored = refactored + String.format("\nLIMIT %d;", settings.defaultQueryLimit);

		return refactored;
	}

	private QueryResult hdlDropTable(QueryManipulator ftr, boolean preview) {
		ResolvedTableNames id = ftr.getTableNameToDrop();
		String msg;
		if (id.problemsEncountered.isPresent())
			msg = id.problemsEncountered.get();
		else if (preview)
			msg = "Api call delete, table id: " + id.idFrom.get();
		else
			try {
				connector.deleteTable(id.idFrom.get());
				onStructureChanged();
				msg = "dropped '" + id.idFrom.get() + "'";
			} catch (IOException e) {
				msg = e.getMessage();
			}
		return packQueryResult(msg);
	}

	public QueryResult getQueryResult(String query, Progress progress, Continuation<QueryResult> onExecutionFinished) {
		Check.notNull(progress);
		logger.Info(String.format("running: '%s'", query));

		try {
			return innerGetQueryResult(query, progress, onExecutionFinished);
		} catch (Exception e) {
			return packQueryResult(e.getMessage());
		}
	}

	private QueryResult innerGetQueryResult(String query, Progress progress,
			Continuation<QueryResult> onExecutionFinished) {
		QueryManipulator ftr = createManipulator(query);
		switch (ftr.statementType) {
		case ALTER:
			return hdlAlterTable(ftr, execute);

		case SELECT:
			return hdlQuery(ftr.statementType, query, execute);

		case INSERT:
			return hdlQuery(ftr.statementType, query, execute);

		case UPDATE:
			return hdlRowIdCompositeQuery(ftr.statementType, query, progress, onExecutionFinished);

		case DELETE:
			return hdlRowIdCompositeQuery(ftr.statementType, query, progress, onExecutionFinished);

		case CREATE_VIEW:
			return hdlQuery(ftr.statementType, query, execute);

		case DROP:
			return hdlDropTable(ftr, execute);

		case DESCRIBE:
			return hdlQuery(ftr.statementType, query, execute);

		case SHOW:
			return hdlQuery(ftr.statementType, query, execute);

		case CTAS:
			return hdlCtas(ftr.statementType, query, execute);

		default:
			return packQueryResult("Statement not covered: " + query);
		}
	}

	private synchronized QueryResult hdlRowIdCompositeQuery(StatementType statementType, String query,
			final Progress progress, final Continuation<QueryResult> onExecutionFinished) {

		Check.isTrue(Op.in(statementType, StatementType.DELETE, StatementType.UPDATE));
		RefactoredSql r = createManipulator(addSemicolon(query)).refactorQuery();

		Optional<QueryResult> p = shouldPreemt(statementType, r, compositeQueryExecutor);
		if (p.isPresent())
			return p.get();

		String selectRowidQuery = getSelectRowIdQuery(r);
		final QueryResult rowids = connector.fetch(selectRowidQuery);

		if (!rowids.data.isPresent() || rowids.data.get().getRowCount() == 0)
			return new QueryResult(HttpStatus.SC_NO_CONTENT, null, "No rows affected: " + selectRowidQuery);

		final List<String> compositeQueries = createCompositeQueries(rowids.data.get(), getQueryTemplate(r));
		compositeQueryExecutor = new CompositeQueryExecutor(compositeQueries, connector, progress,
				new Continuation<QueryResult>() {

					@Override
					public void invoke(QueryResult result) {
						try {
							String feedback = String.format("%s of %d composite queries processed", getCount(result),
									compositeQueries.size());
							onExecutionFinished.invoke(new QueryResult(result.status, result.data.get(),
									result.message.or("") + feedback));
							compositeQueryExecutor = null;
						} catch (Exception e) {
							compositeQueryExecutor = null;
							onExecutionFinished.invoke(new QueryResult(HttpStatus.SC_METHOD_FAILURE, null,
									"Catastrophic unexpected exception: \n" + fmtStackTrace(e)));
						}
					}
				}, logger).executeAsync();

		return new QueryResult(HttpStatus.SC_CONTINUE, null,
				String.format("Queued %s %d times", statementType.name(), compositeQueries.size()));
	}

	private String fmtStackTrace(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}

	private String getCount(QueryResult result) {
		Check.isTrue(result.data.isPresent() && result.data.get().getColumnCount() == 1
				&& result.data.get().getRowCount() == 1);
		return result.data.get().getValueAt(0, 0).toString();
	}

	private List<String> createCompositeQueries(TableModel data, String queryTemplate) {
		final List<String> result = new LinkedList<String>();
		int i = 0;
		while (i < data.getRowCount()) {
			result.add(String.format(queryTemplate, data.getValueAt(i, 0).toString()));
			i++;
		}
		return result;
	}

	private Optional<QueryResult> shouldPreemt(StatementType statementType, RefactoredSql r,
			CompositeQueryExecutor compositeQueryExecutor2) {
		if (compositeQueryExecutor != null)
			return Optional.of(packQueryResult("New query not processed. Composite queries being executed"));

		if (r.problemsEncountered.isPresent())
			return Optional.of(packQueryResult(r.problemsEncountered.get()));

		if (r.tableIds.size() != 1)
			return Optional
					.of(packQueryResult(String.format("%d tableids found for requested query", r.tableIds.size())));

		if (r.keywordWhereStartIndex < 0)
			return Optional.of(hdlQuery(statementType, r.refactored, execute));

		return Optional.absent();
	}

	private String getQueryTemplate(RefactoredSql r) {
		String dmlQuery = r.refactored.substring(0, r.keywordWhereStartIndex);
		String queryTemplate = dmlQuery + " WHERE ROWID = '%s'";
		return queryTemplate;
	}

	private String getSelectRowIdQuery(RefactoredSql r) {
		String tableId = r.tableIds.get(0);
		String queryCondition = r.refactored.substring(r.keywordWhereStartIndex);
		String rowidQuery = String.format("SELECT ROWID FROM %s %s", tableId, queryCondition);
		return rowidQuery;
	}

	private final static int idxNameFrom = 7;
	private final static int idxNameTo = 2;
	private final static int numTokens = 8;

	private QueryResult hdlCtas(StatementType statementType, String query, boolean preview) {
		String norm = StringUtil.coalesce(query.trim().replace("\n", " ").replace(";", ""), " ");
		String[] parts = norm.split(" ");

		if (parts.length != numTokens)
			return packQueryResult("Invalid statement: " + query);

		String nameFrom = parts[idxNameFrom].trim();
		TableInfo from = resolveTableInfo(nameFrom);

		if (from == null)
			return packQueryResult("Can't resolve table " + nameFrom);

		if (!preview) {
			QueryResult result = connector.copyTable(from.id, parts[idxNameTo].trim());
			onStructureChanged();
			return result;
		} else
			return packQueryResult(query.replace(nameFrom, from.id));

	}

	private static QueryResult packQueryResult(String msg) {
		return new QueryResult(HttpStatus.SC_BAD_REQUEST, null, msg);
	}

	public String previewExecutedSql(String query) {
		QueryManipulator ftr = createManipulator(query);

		switch (ftr.statementType) {

		case ALTER:
			return hdlAlterTable(ftr, preview).message.or("");

		case SELECT:
			return hdlQuery(ftr.statementType, query, preview).message.or("");

		case INSERT:
			return hdlQuery(ftr.statementType, query, preview).message.or("");

		case UPDATE:
			return hdlQuery(ftr.statementType, query, preview).message.or("");

		case DELETE:
			return hdlQuery(ftr.statementType, query, preview).message.or("");

		case CREATE_VIEW:
			return hdlQuery(ftr.statementType, query, preview).message.or("");

		case DROP:
			return hdlDropTable(ftr, preview).message.or("");

		case DESCRIBE:
			return hdlQuery(ftr.statementType, query, preview).message.or("");

		case SHOW:
			return hdlQuery(ftr.statementType, query, preview).message.or("");

		case CTAS:
			return hdlCtas(ftr.statementType, query, preview).message.or("");

		default:
			return "Statement not covered: " + query;
		}

	}

	public QueryPatching getPatcher(String query, int cursorPos) {
		return createManipulator(query).getPatcher(cursorPos);
	}

	private String lastQuery = null;
	List<SyntaxElement> lastHighlighting = null;

	public List<SyntaxElement> getHighlighting(String query) {
		if (lastHighlighting != null && StringUtil.nullableEqual(query, lastQuery))
			return lastHighlighting;

		List<SyntaxElement> result = getSyntaxElements(query);
		lastQuery = query;
		lastHighlighting = result;

		return result;
	}

	private List<SyntaxElement> getSyntaxElements(String query) {
		CursorContextListener l = createManipulator(query).getCursorContextListener(0);
		Semantics semantics = new Semantics();
		for (NameRecognitionTable r : l.getTableList()) {
			Optional<TableReference> ref = resolveTableReferenceInQuery(r);
			if (ref.isPresent())
				semantics.addReference(ref.get());
		}

		for (Split s : createManipulator(query).splitStatements().splits)
			semantics.setSemanticAttributes(s, l.syntaxElements, l.getTableList());

		List<SyntaxElement> complete = addNonSyntaxTokens(l.syntaxElements, l.tokens);

		debug(complete);

		return complete;
	}

	private List<SyntaxElement> addNonSyntaxTokens(List<SyntaxElement> syntaxElements, BufferedTokenStream tokens) {

		StackLight<SyntaxElement> regularElements = new StackLight<SyntaxElement>(syntaxElements);

		List<SyntaxElement> result = new LinkedList<SyntaxElement>();

		for (Token t : tokens.getTokens()) {
			if (!regularElements.empty() && regularElements.peek().tokenIndex == t.getTokenIndex())
				result.add(regularElements.pop());
			else {
				SyntaxElementType type = getIrregularType(t);
				if (type != SyntaxElementType.unknown)
					result.add(SyntaxElement.create(t, type));
			}
		}

		return result;
	}

	private SyntaxElementType getIrregularType(Token t) {
		SyntaxElementType type = SyntaxElementType.unknown;

		if (t.getChannel() == FusionTablesSqlLexer.WHITESPACE) {
			if (t.getText().equals("\n"))
				type = SyntaxElementType.newline;
			else
				type = SyntaxElementType.whitespace;
		} else if (t.getChannel() == FusionTablesSqlLexer.HIDDEN)
			type = SyntaxElementType.comment;

		return type;
	}

	private void debug(List<SyntaxElement> syntaxElements) {
		if (!Const.debugQueryHandler)
			return;
		System.out.println("--- syntax elements ---");
		for (SyntaxElement s : syntaxElements)
			System.out.println(String.format("%s %s %d-%d %s", s.value.replace("\n", "NL"), s.type.name(), s.from, s.to,
					s.hasSemanticError() ? "<bad>" : "ok"));
	}

	private List<String> getColumnNames(List<ColumnInfo> columns) {
		Check.notNull(columns);
		List<String> result = new ArrayList<String>();

		for (ColumnInfo c : columns)
			result.add(c.name);

		return result;
	}

	private void onStructureChanged() {
		reloadTableList();
		notifyObservers();
	}

	public synchronized void cancelRequest() {
		if (compositeQueryExecutor != null) {
			compositeQueryExecutor.cancel();
			compositeQueryExecutor = null;
		}

	}

	/**
	 * restores trailing whitespace of single queries and sets the new end
	 * index. Whitespace gets get cut off during parsing if statements are not
	 * terminated with ";".
	 * 
	 * @param splits
	 *            parsed splits
	 * @param source
	 * @return
	 */
	private List<Split> restoreTrailingWhitespace(List<Split> splits, String source) {
		List<Split> result = new ArrayList<Split>();
		for (Split split : splits)
			if (split.text.endsWith(";"))
				result.add(split);
			else
				result.add(restoreTrailingWhitespace(split, source));
		return result;
	}

	private Split restoreTrailingWhitespace(Split split, String source) {
		int i = split.stop;
		while (i + 1 < source.length()
				&& (source.charAt(i + 1) == ' ' || source.charAt(i + 1) == '\r' || source.charAt(i + 1) == '\n'))
			i++;

		return new Split(source.substring(split.start, i + 1), split.start, i);
	}

	public Optional<QueryAtHand> getQueryAtCaretPosition(String text, int pos, boolean returnSingleQueryAnyway) {
		List<Split> splits = restoreTrailingWhitespace(getQueries(text), text);

		if (returnSingleQueryAnyway && splits.size() == 1)
			return Optional.of(new QueryAtHand(text, pos));
		else
			for (Split s : splits)
				if (Op.between(s.start, pos, s.stop))
					return Optional.of(new QueryAtHand(s.text, pos - s.start));

		return Optional.absent();
	}

	public List<Split> getQueries(String text) {
		return createManipulator(text).splitStatements().splits;
	}
}
