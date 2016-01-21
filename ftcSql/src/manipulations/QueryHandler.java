package manipulations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
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
	private boolean debug = Const.debugQueryHandler;

	private boolean reload = true;
	private final Logging logger;
	private final Connector connector;
	private final boolean preview = true;
	private final boolean execute = !preview;
	private final List<TableInfo> tableInfo = new LinkedList<TableInfo>();
	private final Map<String, TableInfo> tableNameToTableInfo = new HashMap<String, TableInfo>();
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

	private void reloadTableList() {
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
		TableInfo result = tableNameToTableInfo.get(StringUtil.stripQuotes(tableName));

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
		for (TableInfo t : tableInfo)
			tableNameToTableInfo.put(t.name, t);
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
		RefactoredSql r = createManipulator(query).refactorQuery();

		String prepared = r.refactored;
		if (!Op.in(statementType, StatementType.DESCRIBE, StatementType.SHOW))
			prepared = addLimit(prepared);

		if (r.problemsEncountered.isPresent())
			return packQueryResult(r.problemsEncountered.get());

		else if (preview)
			return packQueryResult(prepared);
		else
			return connector.fetch(prepared);
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

	public QueryResult getQueryResult(String query) {
		logger.Info(String.format("running: '%s'", query));

		try {
			return innerGetQueryResult(query);
		} catch (Exception e) {
			return packQueryResult(e.getMessage());
		}
	}

	private QueryResult innerGetQueryResult(String query) {
		QueryManipulator ftr = createManipulator(query);
		switch (ftr.statementType) {
		case ALTER:
			return hdlAlterTable(ftr, execute);

		case SELECT:
			return hdlQuery(ftr.statementType, query, execute);

		// case INSERT:
		// return hdlQuery(query, ftr, preview);
		//
		// case UPDATE:
		// return hdlQuery(query, ftr, preview);
		//
		// case DELETE:
		// return hdlQuery(query, ftr, preview);

		case CREATE_VIEW:
			return hdlQuery(ftr.statementType, query, execute);

		case DROP:
			return hdlDropTable(ftr, execute);

		case DESCRIBE:
			return hdlQuery(ftr.statementType, query, execute);

		case SHOW:
			return hdlQuery(ftr.statementType, query, execute);

		default:
			return packQueryResult("Statement not covered: " + query);
		}
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

		List<SyntaxElement> result = getSyntaxElements(createManipulator(query).getCursorContextListener(0));
		lastQuery = query;
		lastHighlighting = result;

		return result;
	}

	private List<SyntaxElement> getSyntaxElements(CursorContextListener l) {
		Optional<TableReference> tableReference;
		if (l.tableList.size() >= 1)
			tableReference = resolveTableReferenceInQuery(l.tableList.get(l.tableList.size() - 1));
		else
			tableReference = Optional.absent();

		Semantics semantics = new Semantics(tableReference, l.allNames);
		semantics.setSemanticAttributes(l.syntaxElements);

		List<SyntaxElement> complete = addNonSyntaxTokens(l.syntaxElements, l.tokens);

		if (debug)
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
		new Thread(new Runnable() {
			public void run() {
				reloadTableList();
				notifyObservers();
			}
		}).start();
	}

}
