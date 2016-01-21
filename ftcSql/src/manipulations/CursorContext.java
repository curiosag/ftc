package manipulations;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.cg.common.check.Check;
import org.cg.common.structures.OrderedIntTuple;
import org.cg.common.structures.Tuple;
import org.cg.common.util.CollectionUtil;
import org.cg.common.util.Op;
import org.cg.ftc.parser.Ctx;
import org.cg.ftc.shared.interfaces.SqlCompletionType;
import org.cg.ftc.shared.interfaces.SyntaxElement;
import org.cg.ftc.shared.uglySmallThings.Const;

import com.google.common.base.Optional;

public class CursorContext {
	private final boolean debug = Const.debugCursorContext;
	private final CursorContextListener cursorContextListener;

	public final int cursorIndex;
	public final List<SqlCompletionType> completionOptions = new ArrayList<SqlCompletionType>();
	public Optional<String> name;
	public Optional<OrderedIntTuple> boundaries;
	public Optional<String> otherName;
	public Optional<OrderedIntTuple> otherBoundaries;
	public Optional<OrderedIntTuple> totalNameBoundaries; // all of "X.y"
	
	public Optional<String> underlyingTableName = Optional.absent();

	private final String[] tokensLeftColumnExpr = new String[] { "WHERE", "AND", "OR" };
	private static final String[] tokensRightColumExpr = new String[] { "AND", "OR", "ORDER", "GROUP", "OFFSET", "LIMIT", ";" };
	private final String[] emptyTokenSet = new String[] {};
	private final String[] tokensEndOfStmt = new String[] { ";" };

	public CursorContext(CursorContextListener c) {
		Check.notNull(c);
		cursorContextListener = c;
		this.cursorIndex = c.cursorIndex;
		if (c.tableList.size() == 1)
			underlyingTableName = CollectionUtil.last(c.tableList).TableName(); 

		if (c.nameAtCursor.isPresent())
			this.totalNameBoundaries = c.nameAtCursor.get().getTotalBoundaries();

		if (!c.nameAtCursor.isPresent())
			initNonModelElement(c);
		else if (c.nameAtCursor.get() instanceof NameRecognitionTable)
			initTable(c);
		else if (c.nameAtCursor.get() instanceof NameRecognitionColumn)
			initColumn(c);
		else
			throw new RuntimeException("invalid case: " + c.nameAtCursor.get().getClass().getName());
	}

	private void initColumn(CursorContextListener c) {

		NameRecognitionColumn atCursor = (NameRecognitionColumn) c.nameAtCursor.get();
		name = atCursor.ColumnName();
		boundaries = atCursor.BoundariesColumnName();
		otherName = resolveTableName(atCursor.TableName(), c.tableList);
		otherBoundaries = atCursor.BoundariesTableName();

		// don't try to change this. a valid result column value may have been
		// retrieved
		// despite an absent result column context.
		addCompletionOption(SqlCompletionType.column);
		// On the other hand a column context may exist despite an absent result
		// column value
		// and further snippets could be valid completions
		addCompletionOptions(probeSqlElementType(c));
	}

	private void initTable(CursorContextListener c) {

		addCompletionOption(SqlCompletionType.table);
		NameRecognitionTable atCursor = (NameRecognitionTable) c.nameAtCursor.get();

		name = resolveTableName(atCursor.TableName(), c.tableList);
		boundaries = atCursor.BoundariesTableName();
		otherName = Optional.absent();
		otherBoundaries = Optional.absent();
	}

	private void initNonModelElement(CursorContextListener c) {
		name = Optional.absent();
		boundaries = Optional.absent();
		otherName = Optional.absent();
		otherBoundaries = Optional.absent();
		addCompletionOptions(probeSqlElementType(c));
	}

	public SqlCompletionType getModelElementType() {
		if (completionOptionsContain(SqlCompletionType.column))
			return SqlCompletionType.column;
		else if (completionOptionsContain(SqlCompletionType.table))
			return SqlCompletionType.table;
		else
			return SqlCompletionType.unknown;
		// TODO Op.in accepts a list where SqlElementType should be??
		// if (Op.in(completionOptions, SqlElementType.column))
		// return SqlElementType.column;
		// else if (Op.in(completionOptions, SqlElementType.table))
		// return SqlElementType.table;
		// else
		// return SqlElementType.unknown;
	}

	public boolean completionOptionsContain(SqlCompletionType t) {
		return completionOptions.indexOf(t) >= 0;
	}

	private void addCompletionOptions(List<SqlCompletionType> l) {
		for (SqlCompletionType t : l)
			addCompletionOption(t);
	}

	private void addCompletionOption(SqlCompletionType t) {
		if (!completionOptionsContain(t))
			completionOptions.add(t);
	}

	private boolean thisIs(ParserRuleContext c) {
		return this.getParserRuleContext().isPresent() && Ctx.is(this.getParserRuleContext().get(), c);
	}

	private boolean inScope(ParserRuleContext c) {
		return Ctx.findInScope(c, this.getContextStack()).isPresent();
	}

	private List<SqlCompletionType> probeSqlElementType(CursorContextListener c) {
		List<SqlCompletionType> result = new LinkedList<SqlCompletionType>();

		if (probeSqlStatement(result))
			return result;

		if (!(probeColumnConditionExpr(c, result) || probeResultColumn(result)))
			probeGroupByOrderBy(c, result);

		return result;
	}

	private boolean findSyntaxElement(String value) {
		return SyntaxElement.findCaseInsensitive(cursorContextListener.syntaxElements, value);
	}

	private void probeGroupByOrderBy(CursorContextListener c, List<SqlCompletionType> result) {
		if (endOfStatement(c) && findSyntaxElement("FROM")) {
			boolean group = findSyntaxElement("GROUP");
			boolean order = findSyntaxElement("ORDER");
			boolean where = findSyntaxElement("WHERE");

			if (!group && !order && ! where) 
				result.add(SqlCompletionType.keywordWhere);
			
			if (!group && !order) {
				result.add(SqlCompletionType.groupBy);
				result.add(SqlCompletionType.orderBy);
			}
			if (group && !order)
				result.add(SqlCompletionType.orderBy);
		}
	}

	private boolean endOfStatement(CursorContextListener c) {
		return checkContext(c, cursorIndex, emptyTokenSet, tokensEndOfStmt);
	}

	private boolean probeColumnConditionExpr(CursorContextListener c, List<SqlCompletionType> result) {
		int resultSizeBefore = result.size();
		Optional<ParserRuleContext> prc = Ctx.findInScope(Ctx.Expr, c.parserRuleStack);

		if (prc.isPresent()) {
			if (anyColumnValue()) {
				Check.isTrue(totalNameBoundaries.isPresent());
				OrderedIntTuple b = totalNameBoundaries.get();

				if (checkContext(c, b.lo() - 1, tokensLeftColumnExpr, emptyTokenSet)
						&& checkContext(c, b.hi() + 1, emptyTokenSet, tokensRightColumExpr))
					result.add(SqlCompletionType.columnConditionExprAfterColumn);

			} else
				result.add(SqlCompletionType.columnConditionExpr);
		}
		return result.size() > resultSizeBefore;
	}

	private boolean checkContext(CursorContextListener l, int checkFromIndex, String[] validTokensLeft,
			String[] validTokensRight) {

		Tuple<List<SyntaxElement>> c = l.partitionSyntaxElements(checkFromIndex);
		List<SyntaxElement> ctxLeft = c.e1;
		List<SyntaxElement> ctxRight = c.e2;

		if (debug) {
			test.Util.debugTokens(String.format("Tokens left of %d:", checkFromIndex), checkFromIndex, ctxLeft);
			test.Util.debugTokens(String.format("Tokens right of %d:", checkFromIndex), checkFromIndex, ctxRight);
		}

		return leftContextOk(validTokensLeft, ctxLeft) && rightContextOk(validTokensRight, ctxRight);
	}

	private boolean rightContextOk(String[] validTokensRight, List<SyntaxElement> ctxRight) {
		return validTokensRight.length == 0 || ctxRight.isEmpty()
				|| Op.inCaseInsensitive(ctxRight.get(0).value, validTokensRight);
	}

	private boolean leftContextOk(String[] validTokensLeft, List<SyntaxElement> ctxLeft) {
		return validTokensLeft.length == 0 || ctxLeft.isEmpty()
				|| Op.inCaseInsensitive(ctxLeft.get(ctxLeft.size() - 1).value, validTokensLeft);
	}

	private void add(List<SqlCompletionType> result, SqlCompletionType... elements) {
		for (int i = 0; i < elements.length; i++)
			result.add(elements[i]);
	}

	private boolean anyColumnValue() {
		return name.isPresent() || otherName.isPresent() || dotRead();
	}

	private boolean dotRead() {
		for (Token token : cursorContextListener.allErrorTokensRead)
			if (token.getText().equals("."))
				return true;
		return false;
	}

	private boolean probeResultColumn(List<SqlCompletionType> result) {
		int originalSize = result.size();

		boolean isColumn = completionOptionsContain(SqlCompletionType.column);
		if (!isColumn && thisIs(Ctx.ResultColumn)) {
			add(result, SqlCompletionType.column);
			isColumn = true;
		}

		probeAggregate(result, isColumn);

		return isColumn || result.size() > originalSize;
	}

	private void probeAggregate(List<SqlCompletionType> result, boolean isColumn) {
		if (isColumn && !anyColumnValue() && !inScope(Ctx.Aggregate) && !inScope(Ctx.Expr))
			add(result, SqlCompletionType.aggregate);
	}

	private boolean probeSqlStatement(List<SqlCompletionType> result) {
		if (!thisIs(Ctx.FusionTablesSql))
			return false;

		if (getSyntaxElements().isEmpty()) {
			result.add(SqlCompletionType.ftSql);
			return true;
		} else
			return invalidQuery(result);
	}

	private boolean invalidQuery(List<SqlCompletionType> result) {
		// can't react to invalid query
		result.add(SqlCompletionType.unknown);
		return false;
	}

	public static CursorContext instance(CursorContextListener context) {
		Check.notNull(context);

		return new CursorContext(context);
	}

	private Optional<String> resolveTableName(Optional<String> tableNameRecognized,
			List<NameRecognitionTable> tableList) {

		if (!tableNameRecognized.isPresent())
			return Optional.absent();

		for (NameRecognitionTable r : tableList)
			if (r.TableAlias().isPresent() && tableNameRecognized.equals(r.TableAlias()))
				return r.TableName();

		return tableNameRecognized;
	}

	public Optional<ParserRuleContext> getParserRuleContext() {
		return cursorContextListener.getParserRuleContext();
	}

	public Stack<ParserRuleContext> getContextStack() {
		return cursorContextListener.parserRuleStack;
	}

	public List<NameRecognitionTable> getTableList() {
		return cursorContextListener.tableList;
	}

	public List<SyntaxElement> getSyntaxElements() {
		return cursorContextListener.syntaxElements;
	}

	@SuppressWarnings("unused")
	private boolean checkContext(CursorContextListener l, ParserRuleContext c, String[] validTokensLeft,
			String[] validTokensRight) {
		Optional<ParserRuleContext> prc = Ctx.findInScope(c, l.parserRuleStack);

		if (prc.isPresent())
			return checkContext(l, prc.get().stop.getStopIndex(), validTokensLeft, validTokensRight);

		return false;
	}

}
