package manipulations;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.xpath.XPath;
import org.cg.common.check.Check;
import org.cg.common.core.Logging;
import org.cg.common.util.StringUtil;
import org.cg.ftc.parser.FusionTablesSqlParser;
import org.cg.ftc.shared.interfaces.SyntaxElement;
import org.cg.ftc.shared.uglySmallThings.Const;

import com.google.common.base.Optional;

import manipulations.Util.Stuff;
import manipulations.results.ParseResult;
import manipulations.results.RefactoredSql;
import manipulations.results.ResolvedTableNames;
import manipulations.results.Splits;
import manipulations.results.TableInfoResolver;

public class QueryManipulator {

	private static Map<String, StatementType> statementTypes = new HashMap<String, StatementType>();

	static {
		statementTypes.put("ALTER", StatementType.ALTER);
		statementTypes.put("CREATE", StatementType.CREATE_VIEW);
		statementTypes.put("DELETE", StatementType.DELETE);
		statementTypes.put("INSERT", StatementType.INSERT);
		statementTypes.put("SELECT", StatementType.SELECT);
		statementTypes.put("UPDATE", StatementType.UPDATE);
		statementTypes.put("DROP", StatementType.DROP);
		statementTypes.put("DESCRIBE", StatementType.DESCRIBE);
		statementTypes.put("SHOW", StatementType.SHOW);
	}

	private class DiggedAliases extends ParseResult {
		Map<String, String> aliases;

		public DiggedAliases(Map<String, String> aliases, String problemsEncountered) {
			super(problemsEncountered);

			this.aliases = aliases;
		}
	}

	private final TableInfoResolver tableInfoResolver;
	private final TableNameToIdMapper tableNameToIdMapper;
	private final ParseTreeWalker walker = new ParseTreeWalker();
	private final String query;
	public final StatementType statementType;

	public QueryManipulator(TableInfoResolver tableInfoResolver, TableNameToIdMapper tableNameToIdMapper, Logging log,
			String query) {
		Check.notNull(tableInfoResolver);
		Check.notNull(tableNameToIdMapper);
		Check.notNull(log);
		query = StringUtil.nonNull(query);

		this.tableInfoResolver = tableInfoResolver;
		this.tableNameToIdMapper = tableNameToIdMapper;
		this.query = query;

		statementType = getStatementType(query);
	}

	private static StatementType getStatementType(String query) {
		// avoids the xpath listener complications and is more efficient
		query = query.trim().toUpperCase();
		for (Entry<String, StatementType> e : statementTypes.entrySet())
			if (query.startsWith(e.getKey()))
				return e.getValue();

		return StatementType.UNKNOWN;
	}

	// Xpath will use an error listener internally that prints stuff to the
	// console
	@SuppressWarnings("unused")
	private StatementType getStatementType(FusionTablesSqlParser parser) {
		String xpath = "//" + Const.rulename_sql_stmt;

		Collection<ParseTree> s = XPath.findAll(parser.fusionTablesSql(), xpath, parser);
		if (s.size() == 0)
			return StatementType.UNKNOWN;
		else
			return Util.getSqlStatementType(s.iterator().next(), parser);
	}

	private FusionTablesSqlParser getParser() {
		return Util.getParser(query).parser;
	}

	public RefactoredSql refactorQuery() {
		Stuff stuff = Util.getParser(query);
		VerboseErrorListener errorListener = Util.addVerboseErrorListener(stuff.parser);

		DiggedAliases tableAliasToName = getTableAliases();

		NameToIDSubstitution substi = new NameToIDSubstitution(stuff.parser, stuff.tokenStream, tableNameToIdMapper,
				tableAliasToName.aliases);

		walker.walk(substi, stuff.parser.fusionTablesSql());

		return new RefactoredSql(query, substi.tuted(),
				StringUtil.concat(tableAliasToName.problemsEncountered.orNull(), errorListener.getErrors()));
	}

	private String getNextTerminal(Iterator<ParseTree> iter, FusionTablesSqlParser parser) {
		if (iter.hasNext())
			return Util.getTerminalValue(iter.next(), parser);
		else
			return null;
	}

	private ResolvedTableNames resolveTableNames(String nameFrom, String nameTo, String problemsTillNow) {
		String problem = null;

		Optional<String> tableId = resolveTableId(nameFrom);
		if (!tableId.isPresent())
			problem = "Could not resolve id for table name '" + nameFrom + "'\r\n";

		return new ResolvedTableNames(nameFrom, tableId, nameTo, StringUtil.concat(problem, problemsTillNow));

	}

	private Optional<String> resolveTableId(String nameFrom) {
		return tableNameToIdMapper.resolveTableId(nameFrom);
	}

	public ResolvedTableNames getAlterTableIdentifiers() {
		Check.isTrue(statementType == StatementType.ALTER);

		FusionTablesSqlParser parser = getParser();
		VerboseErrorListener errorListener = Util.addVerboseErrorListener(parser);

		String xpath = "//alter_table_stmt//string_literal";
		Iterator<ParseTree> names = XPath.findAll(parser.fusionTablesSql(), xpath, parser).iterator();

		String nameFrom = null;
		String nameTo = null;

		nameFrom = getNextTerminal(names, parser);
		nameTo = getNextTerminal(names, parser);

		return resolveTableNames(nameFrom, nameTo, errorListener.getErrors());
	}

	public DiggedAliases getTableAliases() {
		Map<String, String> result = new HashMap<String, String>();
		FusionTablesSqlParser parser = getParser();
		VerboseErrorListener errorListener = Util.addVerboseErrorListener(parser);

		String xpath = "//table_name_with_alias";
		Iterator<ParseTree> trees = XPath.findAll(parser.fusionTablesSql(), xpath, parser).iterator();
		while (trees.hasNext()) {
			ParseTree parseTree = trees.next();
			addAlias(result, parseTree);
		}
		return new DiggedAliases(result, errorListener.getErrors());
	}

	private void addAlias(Map<String, String> result, ParseTree t) {
		TerminalNode tName = Util.digTerminal(t);
		TerminalNode tAlias = null;

		if (tName != null && t.getChildCount() > 1) {
			tAlias = Util.digTerminal(t.getChild(t.getChildCount() - 1));
			if (tAlias != null)
				result.put(tName.getText(), tAlias.getText());
		}
	}

	public ResolvedTableNames getTableNameToDrop() {
		Check.isTrue(statementType == StatementType.DROP);

		FusionTablesSqlParser parser = getParser();
		VerboseErrorListener errorListener = Util.addVerboseErrorListener(parser);

		String xpath = "//string_literal";

		Iterator<ParseTree> name = XPath.findAll(parser.fusionTablesSql(), xpath, parser).iterator();
		String tableName = getNextTerminal(name, parser);

		return resolveTableNames(tableName, null, errorListener.getErrors());
	}

	public Splits splitStatements() {
		Stuff stuff = Util.getParser(query);
		VerboseErrorListener errorListener = Util.addVerboseErrorListener(stuff.parser);

		StatementSplitter splitter = new StatementSplitter(stuff.tokenStream);

		walker.walk(splitter, stuff.parser.fusionTablesSql());
		return new Splits(splitter.splits, errorListener.getErrors());
	}

	public CursorContextListener getCursorContextListener(int cursorPosition) {
		Stuff stuff = Util.getParser(query);

		CursorContextListener cursorContextListener = new CursorContextListener(cursorPosition, stuff.parser,
				stuff.tokenStream);
		stuff.parser.setErrorHandler(new RecognitionErrorStrategy(cursorContextListener));
		stuff.lexer.removeErrorListeners();
		stuff.parser.removeErrorListeners();

		walker.walk(cursorContextListener, stuff.parser.fusionTablesSql());

		return cursorContextListener;
	}

	private boolean symBoundary(char what) {
		char[] boundarySyms = new char[] { ' ', '(' };
		for (char c : boundarySyms)
			if (what == c)
				return true;
		return false;
	}

	private int moveToLastButOneBlank(String query, int cursorPos) {
		if (!cursorInValidRange(query, cursorPos) || cursorPos < 1 || query.length() < 2)
			return cursorPos;

		if (query.charAt(cursorPos) == ' ' && query.charAt(cursorPos - 1) == ' ')
			while (cursorPos >= 2 && query.charAt(cursorPos - 2) == ' ')
				cursorPos--;

		return cursorPos;
	}

	private boolean cursorInValidRange(String query, int cursorPos) {
		return cursorPos > 0 && cursorPos < query.length();
	}

	private int placeIntoValidTokenRange(String query, int cursorPos) {
		if (StringUtil.emptyOrNull(query))
			return cursorPos;

		cursorPos = moveToLastButOneBlank(query, cursorPos);

		if (cursorInValidRange(query, cursorPos) && !symBoundary(query.charAt(cursorPos - 1)))
			cursorPos--;

		return cursorPos;
	}

	public CursorContext getCursorContext(int cursorPosition) {
		Check.isTrue(cursorPosition >= 0
				&& cursorPosition <= query.length()); /* it is <=, not < */

		CursorContext result = CursorContext
				.instance(getCursorContextListener(placeIntoValidTokenRange(query, cursorPosition)));
		return result;
	}

	public QueryPatching getPatcher(int cursorPosition) {

		return new QueryPatching(tableInfoResolver, getCursorContext(cursorPosition), cursorPosition, query);
	}

	public List<SyntaxElement> getSyntaxElements() {
		Stuff stuff = Util.getParser(query);

		CursorContextListener l = new CursorContextListener(0, stuff.parser, stuff.tokenStream);
		stuff.parser.removeErrorListeners();
		walker.walk(l, stuff.parser.fusionTablesSql());

		return l.syntaxElements;
	}

}
