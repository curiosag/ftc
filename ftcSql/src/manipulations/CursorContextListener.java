package manipulations;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.IntervalSet;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.cg.common.structures.OrderedIntTuple;
import org.cg.common.util.Op;
import org.cg.common.util.StringUtil;
import org.cg.ftc.parser.FusionTablesSqlParser;
import org.cg.ftc.shared.uglySmallThings.Const;

import com.google.common.base.Optional;

public class CursorContextListener extends SyntaxElementListener implements OnError {

	private final static boolean debug = Const.debugCursorContextListener;

	final int cursorIndex;

	public String[] expectedSymbols = new String[0];
	public Token offendingSymbol = null;
	private Token lastTerminalRead = null;
	private LinkedList<Token> errorTokensRead = new LinkedList<Token>();
	public final LinkedList<Token> allErrorTokensRead = new LinkedList<Token>();

	private Optional<NameRecognition> currentNameRecognition = Optional.absent();

	private ParserRuleContext parserRuleContext = null;
	public final Stack<ParserRuleContext> parserRuleStack = new Stack<ParserRuleContext>();
	public final List<NameRecognition> allNames = new ArrayList<NameRecognition>();

	/**
	 * 
	 * Optional<NameRecognition> nameAtCursor
	 *
	 * absent: if the cursorIdex doesen't reflect a table or column context
	 * otherwise: either an Optional of TableNameRecognition or
	 * ColumnNameRecognition
	 * 
	 * In both cases name properties may be incomplete or entirely absent,
	 * indicating, that it actually is a column or table context, but no names
	 * have been typed yet
	 * 
	 * If it is a ColumnNameRecognition, the associated table name, if any,
	 * reflects the actual table name as defined in the query with aliases
	 * resolved
	 * 
	 * Examples: cursorIndex = 2 "SELECT a from A" -> absent (cursorIndex
	 * missed)
	 * 
	 * assume that cursorIndex is in the column range "SELECT A.a from X as A;"
	 * -> TableName = "X", ColumnName = "a" "SELECT a from A;" -> TableName =
	 * "A", ColumnName = "a" "SELECT a " -> TableName = absent, ColumnName = "a"
	 * "SELECT A.a " -> TableName = absent, ColumnName = "a"
	 * 
	 * assume that cursorIndex is in the table name range "SELECT a  from A;" ->
	 * TableName = "A", TableAlias = absent "SELECT   from A as X;" -> TableName
	 * = "A", TableAlias = "X"
	 */
	public Optional<NameRecognition> nameAtCursor = Optional.absent();

	public List<NameRecognitionTable> tableList = new ArrayList<NameRecognitionTable>();

	private final FusionTablesSqlParser parser;

	public CursorContextListener(int cursorIndex, FusionTablesSqlParser parser, BufferedTokenStream tokens) {
		super(parser, tokens);
		this.cursorIndex = cursorIndex;
		this.parser = parser;

		if (debug)
			System.out.println(String.format("\ngetting context at position %d", cursorIndex));
	}

	@Override
	public void notifyOnError(Token offendingToken, Token missingToken, IntervalSet tokensExpected) {
		super.notifyOnError(offendingToken, missingToken, tokensExpected);
		if (offendingToken != null)
			offendingSymbol = offendingToken;
		if (tokensExpected.size() > 0)
			expectedSymbols = getTokenNames(tokensExpected);

		debugOnError();
	}

	private void debugOnError() {
		if (debug) {
			if (offendingSymbol != null)
				System.out.println("Offended by: " + offendingSymbol.getText());

			System.out.println("expected: " + StringUtil.ToCsv(expectedSymbols, ","));

		}
	}

	private String[] getTokenNames(IntervalSet s) {
		return criminalExtraction(s);
	}

	@SuppressWarnings("static-access")
	private String[] criminalExtraction(IntervalSet s) {
		String cont = s.toString(parser.VOCABULARY);
		if (debug)
			System.out.println(cont);
		return cont.split(",");
	}

	/**
	 * there are repeated calls for startRecognition / stopRecognition in
	 * successive nested rules r0->r1-r2, say. It covers the cases
	 * 
	 * - from a correct input where all rules work as expected until r2 picking
	 * its content from a terminal node - to errors in the input causing any of
	 * r2, r1 to fail, picking some to all of its content from error nodes. -
	 * the empty case, where no information from the desired context could be
	 * picked, except that it was the context - if r0 fails, nothing can be said
	 * at all
	 * 
	 * the cases:
	 * 
	 * Result_columnContext ... empty and error cases where no column names
	 * follow
	 * 
	 * Ordering_termContext ... same for ordering term
	 * 
	 * ExprContext ... and for expressions, though this remains ambiguous e.g.
	 * how do you want to tell on a merely syntactic level an incomplete numeric
	 * literal("1.") from a pathologically incomplete
	 * Qualified_column_nameContext where the table name is "1"?
	 * 
	 * Qualified_column_in_expressionnameContext ... introduced for the
	 * st_intersects branch in expressions and then used everywhere, to
	 * eliminate at least one differentiation
	 * 
	 * Result_columnContext -> Aggregate_expContext ->
	 * Qualified_column_nameContext ... aggregate comes with extra tokens "AVG("
	 * or "SUM(" that need to be skipped - or treated in class
	 * "NameRecognitionState" which would be more complicated
	 * 
	 * Qualified_column_nameContext ... stand alone version of the previous case
	 * 
	 * 
	 */

	@Override
	public void enterTable_name_with_alias(FusionTablesSqlParser.Table_name_with_aliasContext ctx) {
		super.enterTable_name_with_alias(ctx);
		startNameRecognition(new NameRecognitionTable(), ctx);
	}

	@Override
	public void exitTable_name_with_alias(FusionTablesSqlParser.Table_name_with_aliasContext ctx) {
		super.exitTable_name_with_alias(ctx);
		stopNameRecognition(ctx);
	}

	@Override
	public void enterResult_column(FusionTablesSqlParser.Result_columnContext ctx) {
		super.enterResult_column(ctx);
		startNameRecognition(new NameRecognitionColumn(), ctx);
	}

	@Override
	public void exitResult_column(FusionTablesSqlParser.Result_columnContext ctx) {
		super.exitResult_column(ctx);
		stopNameRecognition(ctx);
	}

	@Override
	public void enterOrdering_term(FusionTablesSqlParser.Ordering_termContext ctx) {
		super.enterOrdering_term(ctx);
		startNameRecognition(new NameRecognitionColumn(), ctx);
	}

	@Override
	public void exitOrdering_term(FusionTablesSqlParser.Ordering_termContext ctx) {
		super.exitOrdering_term(ctx);
		stopNameRecognition(ctx);
	}

	@Override
	public void enterExpr(FusionTablesSqlParser.ExprContext ctx) {
		super.enterExpr(ctx);
		startNameRecognition(new NameRecognitionColumn(), ctx);
	}

	@Override
	public void exitExpr(FusionTablesSqlParser.ExprContext ctx) {
		super.exitExpr(ctx);
		stopNameRecognition(ctx);
		evaluateExprContextMatchOnExitRule(ctx);
	}

	@Override
	public void enterQualified_column_name(FusionTablesSqlParser.Qualified_column_nameContext ctx) {
		super.enterQualified_column_name(ctx);
		startNameRecognition(new NameRecognitionColumn(), ctx);
	}

	@Override
	public void exitQualified_column_name(FusionTablesSqlParser.Qualified_column_nameContext ctx) {
		super.exitQualified_column_name(ctx);
		stopNameRecognition(ctx);
	}

	@Override
	public void enterColumn_name_beginning_expr(FusionTablesSqlParser.Column_name_beginning_exprContext ctx) {
		super.enterColumn_name_beginning_expr(ctx);
		startNameRecognition(new NameRecognitionColumn(), ctx);
	}

	@Override
	public void exitColumn_name_beginning_expr(FusionTablesSqlParser.Column_name_beginning_exprContext ctx) {
		super.exitColumn_name_beginning_expr(ctx);
		stopNameRecognition(ctx);
	}

	@Override
	public void enterColumn_name_in_dml(FusionTablesSqlParser.Column_name_in_dmlContext ctx) {
		super.enterColumn_name_in_dml(ctx);
		startNameRecognition(new NameRecognitionColumn(), ctx);
	}

	@Override
	public void exitColumn_name_in_dml(FusionTablesSqlParser.Column_name_in_dmlContext ctx) {
		super.exitColumn_name_in_dml(ctx);
		stopNameRecognition(ctx);
	}

	@Override
	public void enterTable_name_in_ddl(FusionTablesSqlParser.Table_name_in_ddlContext ctx) {
		super.enterTable_name_in_ddl(ctx);
		startNameRecognition(new NameRecognitionTable(), ctx);
	}

	@Override
	public void exitTable_name_in_ddl(FusionTablesSqlParser.Table_name_in_ddlContext ctx) {
		super.exitTable_name_in_ddl(ctx);
		stopNameRecognition(ctx);
	}

	@Override
	public void enterTable_name_in_dml(FusionTablesSqlParser.Table_name_in_dmlContext ctx) {
		super.enterTable_name_in_dml(ctx);
		startNameRecognition(new NameRecognitionTable(), ctx);
	}

	@Override
	public void exitTable_name_in_dml(FusionTablesSqlParser.Table_name_in_dmlContext ctx) {
		super.exitTable_name_in_dml(ctx);
		stopNameRecognition(ctx);
	}

	@Override
	public void visitTerminal(TerminalNode node) {
		super.visitTerminal(node);
		recognize(node.getSymbol(), getStop(node));

		lastTerminalRead = node.getSymbol();
		if (errorTokensRead.size() > 0)
			errorTokensRead.clear();

		debugTerminal(node);
	}

	@Override
	public void visitErrorNode(ErrorNode node) {
		super.visitErrorNode(node);
		if (!isGenericError(node.getText()))
			recognize(node.getSymbol(), getStop(node));

		addErrorToken(node);
		debugErrorNode(node);
	}

	private void addErrorToken(ErrorNode node) {
		errorTokensRead.add(node.getSymbol());
		allErrorTokensRead.add(node.getSymbol());
	}

	@Override
	public void enterEveryRule(ParserRuleContext ctx) {
		super.enterEveryRule(ctx);
		onEnterRule(ctx);
	}

	@Override
	public void exitEveryRule(ParserRuleContext ctx) {
		super.exitEveryRule(ctx);
		onExitRule(ctx);
	}

	private OrderedIntTuple getLastErrorBoundaries(int stretchBy) {
		return OrderedIntTuple.create(errorTokensRead.getLast().getStartIndex(),
				errorTokensRead.getLast().getStopIndex() + stretchBy);
	}

	private void evaluateExprContextMatchOnExitRule(ParserRuleContext ctx) {
		// smthg like "WHERE a " will give a terminal "WHERE" plus one error
		// node "a" the cursor is 1 after a in this case, token
		// boundary ends with a, therefore stretchBy
		int stretchBy = 2;
		if (lastTerminalRead != null
				&& StringUtil.equalsAny(lastTerminalRead.getText().toUpperCase(), "WHERE", "AND", "OR")
				&& errorTokensRead.size() == 1 && cursorWithinBoundaries(getLastErrorBoundaries(stretchBy))) {
			parserRuleContext = ctx;
			if (debug)
				System.out.println("-> matched in hindsight");
		}

	}

	private void onEnterRule(ParserRuleContext ctx) {
		debugContext(ctx, "enter");

		if (cursorWithinBoundaries(ctx)) {
			pushContext(ctx);
			if (debug)
				System.out.println("set context at cursor to : " + ctx.getClass().getName());
			parserRuleContext = ctx;
		}
	}

	private void onExitRule(ParserRuleContext ctx) {
		debugContext(ctx, "exit");
		popContextWhenNoMatch();
	}

	private void startNameRecognition(NameRecognition current, ParserRuleContext ctx) {
		currentNameRecognition = Optional.of(current);

		OrderedIntTuple range = getRuleRange(ctx);

		if (cursorWithinBoundaries(range))
			nameAtCursor = currentNameRecognition;

		if (current.getClass() == NameRecognitionTable.class)
			tableList.add((NameRecognitionTable) current);

		debugStartRecognition(range);

	}

	private OrderedIntTuple getRuleRange(ParserRuleContext ctx) {
		return OrderedIntTuple.create(getStart(ctx), getStop(ctx));
	}

	private void stopNameRecognition(ParserRuleContext ctx) {
		if (currentNameRecognition.isPresent()) {
			enlistName(currentNameRecognition.get());
			currentNameRecognition = Optional.absent();
		}
	}

	private void enlistName(NameRecognition curr) {
		if (curr.getName1().isPresent() || curr.getName2().isPresent())
			allNames.add(curr);
	}

	private int getStop(ParserRuleContext ctx) {
		if (ctx.stop == null)
			return -1;
		else
			return ctx.stop.getStopIndex();
	}

	private int getStart(ParserRuleContext ctx) {
		if (ctx.start == null)
			return -1;
		else
			return ctx.start.getStartIndex();
	}

	private void debugStartRecognition(OrderedIntTuple range) {
		if (debug) {
			String within = cursorWithinBoundaries(range) ? " match" : " no match";
			System.out.println(String.format("Recognition boundaries lo:%d hi:%d cursor at: %d -> %s", range.lo(),
					range.hi(), cursorIndex, within));
		}
	}

	private boolean cursorWithinBoundaries(ParserRuleContext c) {
		return cursorWithinBoundaries(getRuleRange(c));
	}

	private boolean cursorWithinBoundaries(OrderedIntTuple o) {
		return Op.between(o.lo(), cursorIndex, o.hi());
	}

	private void recognize(Token token, int stopIndex) {
		if (currentNameRecognition.isPresent()) {
			currentNameRecognition.get().digest(token);
			debugRecognize(token);
		}
	}

	private void debugRecognize(Token token) {
		if (debug)
			System.out.println("recognizing " + token.getText());

	}

	private int getStop(TerminalNode node) {
		return node.getSymbol().getStopIndex();
	}

	private int getStop(ErrorNode node) {
		return node.getSymbol().getStopIndex();
	}

	private void debugTerminal(TerminalNode node) {
		debugToken("Terminal: " + qt(node.getText()), getBoundaries(node));
	}

	private void debugErrorNode(ErrorNode node) {
		debugToken("Error: " + node.getText(), getBoundaries(node));
	}

	private String qt(String s) {
		return ">>" + s + "<<";
	}

	private void debugToken(String what, OrderedIntTuple o) {
		if (debug) {
			String swapped = o.swap ? " (indices swapped)" : "";
			String withinBoundaries = cursorWithinBoundaries(o) ? "+" : "-";
			System.out
					.println(String.format("%s %s from %d to %d %s", withinBoundaries, what, o.lo(), o.hi(), swapped));
		}
	}

	private void debugContext(ParserRuleContext ctx, String inOrOut) {
		debugToken(inOrOut + " rule: " + ctx.getClass().getSimpleName(), getBoundaries(ctx));
	}

	private OrderedIntTuple getBoundaries(ParserRuleContext ctx) {
		return getRuleRange(ctx);
	}

	private OrderedIntTuple getBoundaries(TerminalNode n) {
		return OrderedIntTuple.create(n.getSymbol().getStartIndex(), n.getSymbol().getStopIndex());
	}

	private String markPos(int pos) {
		String result = " "; // for 1st quote around query
		for (int i = 0; i < pos; i++)
			result = result + ' ';
		result = result + "^|";
		return result;
	}

	private void debugColumn(NameRecognitionColumn t) {
		if (debug)
			System.out.println("Column: " + t.ColumnName().or("") + " of table: " + t.TableName().or(""));
	}

	private void debugTable(NameRecognitionTable t) {
		if (debug)
			System.out.println("Table: " + t.TableName().or("") + " alias: " + t.TableAlias().or(""));
	}

	private void debug(NameRecognition r) {
		if (debug) {
			if (r instanceof NameRecognitionColumn)
				debugColumn((NameRecognitionColumn) r);
			if (r instanceof NameRecognitionTable)
				debugTable((NameRecognitionTable) r);
			System.out.println("recognition state: " + r.state.name());
		}
	}

	private void debug() {
		if (debug) {
			if (nameAtCursor.isPresent())
				debug(nameAtCursor.get());
			for (NameRecognitionTable t : tableList)
				debugTable(t);
		}
	}

	public void debug(String query, int cursorPos) {
		if (debug) {
			debug();
			System.out.println(String.format("'%s' cursor after pos: %d", query, cursorPos));
			System.out.println(markPos(cursorPos));
		}
	}

	private void pushContext(ParserRuleContext c) {
		parserRuleStack.push(c);
	}

	private void popContextWhenNoMatch() {
		if (parserRuleContext == null && !parserRuleStack.isEmpty())
			parserRuleStack.pop();
	}

	public Optional<ParserRuleContext> getParserRuleContext() {
		return Optional.fromNullable(parserRuleContext);
	}

}
