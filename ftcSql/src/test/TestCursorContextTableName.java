package test;

import static org.junit.Assert.*;

import org.cg.common.util.Op;
import org.cg.ftc.shared.interfaces.SqlCompletionType;
import org.junit.Test;

import manipulations.CursorContext;

public class TestCursorContextTableName {

	private final SqlCompletionType table = SqlCompletionType.table;
	
	private CursorContext getCursorContext(String query, int cursorPosition) {
		return test.Util.getCursorContext(query, cursorPosition);
	};

	private int shift(int i) {
		return i + 0;
	}

	@Test
	public void testUpdateStmt() {
		String q = "update x set y = 1;";
		CursorContext c = getCursorContext(q, q.indexOf("x"));
		checkSqlElement(c);
	}
	
	@Test
	public void testInsertStmt() {
		String q = "insert into a (col1, col2) values (1, 2);";
		CursorContext c = getCursorContext(q, q.indexOf("x"));
		checkSqlElement(c);
	}
	
	//
	
	@Test(expected = RuntimeException.class)
	public void testInvalidCursorPosition1() {
		String q = "Select x from  s";
		getCursorContext(q, q.length() + 1);
	}

	@Test(expected = RuntimeException.class)
	public void testInvalidCursorPosition2() {
		getCursorContext("Select x from  s", -1);
	}

	@Test
	public void testCursorContextTableName() {
		CursorContext c = getCursorContext("Select x from  s", shift(11));
		assertEquals(SqlCompletionType.unknown, c.getModelElementType());

		c = getCursorContext("Select x from  s", shift(15));
		checkSqlElement(c);
		
		String query = "Select  from ";
		c = getCursorContext(query, shift(11));

		assertEquals(c.getModelElementType(), SqlCompletionType.unknown);
		assertFalse(c.name.isPresent());

		query = "Select  from x";
		c = getCursorContext(query, shift(query.indexOf("x")));
		checkSqlElement(c);
		
		assertTrue(c.name.isPresent());
		assertEquals("x", c.name.get());

		query = "Select c from x as y";
		c = getCursorContext(query, shift(query.indexOf("x")));

		checkSqlElement(c);
		assertTrue(c.name.isPresent());
		assertEquals("x", c.name.get());

		query = "Select a.x from A left outer join X as E on ";
		c = getCursorContext(query, shift(query.indexOf("X")));

		checkSqlElement(c);
		assertTrue(c.name.isPresent());
		assertEquals("X", c.name.get());
	}

	private void checkSqlElement(CursorContext c) {
		assertEquals(table, c.getModelElementType());
		assertTrue(Op.eq(c.completionOptions, table));
	}
}
