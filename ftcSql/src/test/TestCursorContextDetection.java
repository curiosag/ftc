package test;

import org.junit.Test;

import manipulations.CursorContext;
import manipulations.QueryPatching;

import static org.junit.Assert.*;

import org.antlr.v4.runtime.ParserRuleContext;
import org.cg.ftc.shared.interfaces.SqlCompletionType;

public class TestCursorContextDetection {
	private boolean debug = false;

	@SuppressWarnings("unused")
	private boolean withinContext(CursorContext context, ParserRuleContext... candidates) {
		if (debug) {
			System.out.println("--- finding any of ---");
			for (int i = 0; i < candidates.length; i++)
				System.out.println(candidates[i].getClass().getCanonicalName());

			System.out.println("--- in stack ---");
			for (ParserRuleContext e : context.getContextStack())
				System.out.println(e.getClass().getName());

			System.out.println("--- pinned at ---");
			System.out.println(context.getParserRuleContext().getClass().getSimpleName());

		}
		return manipulations.Util.withinContext(context, candidates);
	}

	public void _TestSqlContext(String query, int cursorPos, SqlCompletionType typeExpected) {
		System.out.println(String.format("Testing '%s' expecting %s", query, typeExpected.name()));
		QueryPatching p = test.Util.getPatcher(query, cursorPos);

		assertTrue(p.cursorContext.completionOptionsContain(typeExpected));
	}

	@Test
	public void TestSqlContext() {
		_TestSqlContext(null, 0, SqlCompletionType.ftSql);
		_TestSqlContext("", 0, SqlCompletionType.ftSql);
		_TestSqlContext(" ", 0, SqlCompletionType.ftSql);
		_TestSqlContext("humti", 0, SqlCompletionType.unknown);

	}

	@Test
	public void TestResultColumnContext() {
		// that's covered in TestCursorContextResultColumn, including Aggregates
	}

	@Test
	public void TestGroupByOrderBy() {
		String query = "select a from x ";
		_TestSqlContext(query, query.indexOf("x") + 2, SqlCompletionType.groupBy);
		_TestSqlContext(query, query.indexOf("x") + 2, SqlCompletionType.orderBy);
	}

	@Test(expected = java.lang.AssertionError.class)
	public void TestFoulGroupBy1() {
		String query = "select a  ";
		_TestSqlContext(query, query.indexOf("x") + 2, SqlCompletionType.groupBy);
	}
	
	@Test(expected = java.lang.AssertionError.class)
	public void TestFoulGroupBy2() {
		String query = "select a from x group ";
		_TestSqlContext(query, query.indexOf("x") + 2, SqlCompletionType.groupBy);
	}

	@Test(expected = java.lang.AssertionError.class)
	public void TestFoulOrderBy() {
		String query = "select a from x order ";
		_TestSqlContext(query, query.indexOf("x") + 2, SqlCompletionType.orderBy);
	}

}
