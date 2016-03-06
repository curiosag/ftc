package test;

import static org.junit.Assert.*;

import org.cg.common.core.SystemLogger;
import org.cg.ftc.shared.structures.ClientSettings;
import org.cg.ftc.shared.structures.QueryAtHand;
import org.junit.Test;

import com.google.common.base.Optional;

import manipulations.QueryHandler;

public class TestQueryHandler {

	@Test
	public void test() {
		QueryHandler q = new QueryHandler(new SystemLogger(), new MockConnector(), ClientSettings.instance(this.getClass()));
		
		String text = "Select * from table1;  Select a,b from table2;";

		Optional<QueryAtHand> split = q.getQueryAtCaretPosition(text, 200);
		assertFalse(split.isPresent());
		
		split = q.getQueryAtCaretPosition(text, 0);
		assertTrue(split.isPresent());
		assertEquals(0, split.get().caretPositon);
		assertEquals("Select * from table1;", split.get().query);

		split = q.getQueryAtCaretPosition(text, 20); // before ";" in query1
		assertTrue(split.isPresent());
		assertEquals(20, split.get().caretPositon);
		
		// blanks before statements make no valid statement context
		split = q.getQueryAtCaretPosition(text, 21); // after ";" in query1. Note, that's a blank!
		assertFalse(split.isPresent());
		split = q.getQueryAtCaretPosition(text, 22);
		assertFalse(split.isPresent());
		
		split = q.getQueryAtCaretPosition(text, 23); // before "S" in SELECT in query2
		assertTrue(split.isPresent());
		assertEquals(0, split.get().caretPositon);
		
		split = q.getQueryAtCaretPosition(text, 28); // before the "t" in " select a,b"
		assertTrue(split.isPresent());
		assertEquals(5, split.get().caretPositon);
		assertEquals("Select a,b from table2;", split.get().query);
	}

}
