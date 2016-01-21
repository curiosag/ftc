package test;

import org.antlr.v4.runtime.ParserRuleContext;
import org.cg.common.check.Check;
import org.cg.common.util.Op;
import org.cg.ftc.shared.interfaces.SqlCompletionType;
import org.cg.ftc.shared.structures.AbstractCompletion;
import org.cg.ftc.shared.structures.ModelElementCompletion;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

import com.google.common.base.Optional;

import manipulations.CursorContext;
import manipulations.CursorContextListener;
import manipulations.QueryManipulator;
import manipulations.QueryPatching;

public class TestCursorContextColumn {

	private final boolean debug = true;
	private final SqlCompletionType column = SqlCompletionType.column;
	private final SqlCompletionType aggregate = SqlCompletionType.aggregate;
	private final SqlCompletionType unknown = SqlCompletionType.unknown;
	
	private int shift(int i) {
		return i + 0;
	}

	// @Test
	public void debugContextListener() {
		String query = "Select x. from AA as b;";
		QueryManipulator m = test.Util.getManipulator(query);
		int indexBeforeCursor = query.indexOf('x');
		// indexBeforeCursor = 6;

		CursorContextListener c = m.getCursorContextListener(indexBeforeCursor);
		c.debug(query, indexBeforeCursor);
	}

	@Test
	public void testCursorOutsideColumnContext() {
		CursorContext c = test.Util.getCursorContext("Select a from  s", shift(11));

		assertEquals(unknown, c.getModelElementType());
		assertFalse(c.name.isPresent());
	}

	@Test(expected = java.lang.AssertionError.class)
	public void testResultFoulColumnEmpty() {
		// Result_columnContext pops up, but startIndex is, where "where" is
		CursorContext c = test.Util.getCursorContext("Select  where", shift(7));
		assertEquals(column, c.getModelElementType());
	}

	@Test
	public void testResultColumnEmpty() {
		CursorContext c;

		c = test.Util.getCursorContext("Select ", shift(4));
		assertTrue(c.getModelElementType() == unknown);

		c = test.Util.getCursorContext("Select ", shift(7));
		assertTrue(c.getModelElementType() == column);
		assertTrue(Op.eq(c.completionOptions, column, aggregate));

		c = test.Util.getCursorContext("Select a,  ", shift(10));
		assertTrue(c.getModelElementType() == column);
		assertTrue(Op.eq(c.completionOptions, column, aggregate));

	}

	@Test
	public void testColumnJoker() {
		CursorContext c;

		c = test.Util.getCursorContext("Select *", shift(7));
		assertEquals("*", c.name.get());
		checkSingleSqlCompletion(c, column);
		
		c = test.Util.getCursorContext("Select a.*  from o;", shift(7));

		assertEquals("*", c.name.get());
		assertEquals("a", getTableName(c).get());
		checkSingleSqlCompletion(c, column);
	}

	private void checkSingleSqlCompletion(CursorContext c, SqlCompletionType t) {
		assertTrue(c.getModelElementType() == t);
		assertTrue(Op.eq(c.completionOptions, t));
	}

	@Test
	public void testContextResultColumn() {
		testPermutations("select %s", column);
		testPermutations("select %s from a;", column);
	}

	@Test
	public void testTableAliasResolution() {
		CursorContext c;

		String query = "Select a.x from u left outer join b as a on u.id = a.id";
		c = test.Util.getCursorContext(query, shift(query.indexOf("a")));

		assertTrue(c.name.isPresent());
		assertTrue(getTableName(c).isPresent());
		assertEquals("x", c.name.get());
		assertEquals("b", getTableName(c).get());
		checkSingleSqlCompletion(c, column);

	}

	private boolean debugEq(List<SqlCompletionType> comparand, SqlCompletionType... sqlCompletionTypes)
	{
		String found = "";
		for (SqlCompletionType t : comparand) 
			found = found + t.name() + ", ";
		
		String expected = "";
		for (SqlCompletionType t : sqlCompletionTypes) 
			expected = expected + t.name() + ", ";
		
		System.out.println(String.format("Completions expected: %s  completions found: %s", expected, found));
		return Op.eq(comparand, sqlCompletionTypes);
	}
	
	@Test
	public void testContextExpressionEmpty() {
		String query = "Select a from b where ";
		CursorContext c = test.Util.getCursorContext(query, shift(query.length()));
		Check.isTrue(debugEq(c.completionOptions, column, SqlCompletionType.columnConditionExpr));
	}

	@Test
	public void testContextExpression() {
		testPermutations("Select a from b where %s;", column, SqlCompletionType.columnConditionExprAfterColumn);
		testPermutations("Select a from b where A=1 and %s=2", column);
		testPermutations("Select a from b where st_intersects(%s, circle(latlang(1,1), 1))", column);
		testPermutations("Select a from b where st_intersects(%s, circ", column);
		testPermutations("Select a from b where st_intersects(%s", column);
	}

	@Test
	public void testContextGroupingClause() {
		testPermutations("Select a from b group by %s;", column);
		testPermutations("Select a from b group by %s", column);
	}

	@Test
	public void testContextOrderingClause() {
		testPermutations("Select a from b order by %s;", column);
		testPermutations("Select a from b order by %s", column);
	}

	@Test
	public void testContextAggregateClause() {
		testPermutations("Select b, sum(%s) from b as A group by A.x order by a.y;", column);
		testPermutations("Select b, sum(%s", column);
	}

	@Test(expected = java.lang.AssertionError.class)
	public void testContextFoulAggregateClause() {
		testPermutations("Select b, sum %s)", column);
	}

	/**
	 * 
	 * @param queryTemplate
	 *            smthg like "select %s from x where y=0" or
	 *            "select a from x where %s=0"
	 * 
	 *            %s will be replaced by all of these combinations, which are
	 *            expected when checking the results.
	 * 
	 *            Table Column %s T v "T.v" T null "T." null v ".v" null null ""
	 * 
	 */

	private final boolean emptyContext = true;

	public void testPermutations(String queryTemplate, SqlCompletionType... elementTypesExpected) {
		testPermutatation(queryTemplate, "T", "v", false, elementTypesExpected);
		testPermutatation(queryTemplate, "T", "", false, elementTypesExpected); // pass "" rather than
															// null
		// to indicate non-existence
		testPermutatation(queryTemplate, "", "v", false, elementTypesExpected);
		testPermutatation(queryTemplate, "", "", false, elementTypesExpected);
		testPermutatation(queryTemplate, "", "", emptyContext, elementTypesExpected);

		testPatching(queryTemplate);

	}

	private void testPermutatation(String queryTemplate, String tableE, String columnE, boolean emptyContext,
			SqlCompletionType... elementTypesExpected) {

		int pos = queryTemplate.indexOf("%s");

		String retColVal = tableE + "." + columnE;
		String query = queryTemplate.replace("%s", retColVal);

		QueryManipulator m = test.Util.getManipulator(query);

		for (int i = pos; i < pos + retColVal.length(); i++) {
			CursorContext c = m.getCursorContext(pos);
			checkPermutation(i, c, tableE, columnE, query, emptyContext(emptyContext), elementTypesExpected);
		}
	}

	public void testPatching(String queryTemplate) {
		testPatching(queryTemplate, "v");
		testPatching(queryTemplate, "");
	}

	private void testPatching(String queryTemplate, String value) {
		System.out.println("template: " + queryTemplate);

		int pos = queryTemplate.indexOf("%s");

		String query = queryTemplate.replace("%s", value);
		QueryPatching c = test.Util.getPatcher(query, pos);

		AbstractCompletion parent = new ModelElementCompletion(SqlCompletionType.table, "XXX", null);
		AbstractCompletion col = new ModelElementCompletion(SqlCompletionType.column, "XXX", parent);
		String patched = c.patch(col);

		System.out.println("patched: " + patched);
	}

	private void checkPermutation(int cursorPos, CursorContext context, String tableE, String columnE, String query,
			boolean emptyContext, SqlCompletionType... elementTypesExpected) {
		debugPermutation(cursorPos, context, tableE, columnE, query, emptyContext, elementTypesExpected);

		
		assertTrue(Op.eq(context.completionOptions, elementTypesExpected));
		checkString(context.name, columnE);
		checkString(getTableName(context), tableE);
	}

	private Optional<String> getTableName(CursorContext context) {
		return context.otherName;
	}

	private void debugPermutation(int cursorPos, CursorContext context, String tableE, String columnE, String query,
			boolean emptyContext, SqlCompletionType... elementTypesExpected) {

		if (!debug)
			return;

		String fmtString;

		System.out.print("---------------------------------");
		System.out.print("Element types expected: ");
		for (SqlCompletionType t : elementTypesExpected)
			System.out.print(t.name() + ", ");
		System.out.print("\n");

		System.out.println("Context stack: ");
		for ( ParserRuleContext t : context.getContextStack())
			System.out.println(t.getClass().getSimpleName());
		System.out.print("\n");
		
		if (emptyContext)
			System.out.println("Checking for empty context, query '" + query + "'");
		else {
			fmtString = "Checking for table='%s', column='%s' cursor pos %d in query '%s'";
			System.out.println(String.format(fmtString, tableE, columnE, cursorPos, query));

			System.out.print("completion options: ");
			for (SqlCompletionType e : context.completionOptions)
				System.out.print(e.name() + ", ");
			System.out.print("\n");
			
			test.Util.debugTokens("Tokens", cursorPos, context);
		}

		String col = context.name.or("absent");
		String tab = getTableName(context).or("absent");

		fmtString = "recognized: table='%s', column='%s'";
		System.out.println(String.format(fmtString, tab, col));
	}


	private boolean emptyContext(boolean... emptyContext) {
		return emptyContext.length > 0 && emptyContext[0];
	}

	private void checkString(Optional<String> s, String valueExpected) {
		if (valueExpected == "")
			assertFalse(s.isPresent());
		else {
			assertTrue(s.isPresent());
			assertEquals(valueExpected, s.get());
		}
	}

}
