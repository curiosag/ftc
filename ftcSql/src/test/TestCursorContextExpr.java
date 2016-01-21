package test;

import static org.junit.Assert.*;

import org.cg.common.util.Op;
import org.cg.ftc.shared.interfaces.SqlCompletionType;
import org.junit.Test;

import manipulations.CursorContext;

public class TestCursorContextExpr {

	private CursorContext debug(CursorContext c)
	{
		String ops = "";
		for ( SqlCompletionType o : c.completionOptions) 
			ops = ops + o.name() + " ";
		System.out.println("completion options: " + ops);
		
		return c;
	}
	
	@Test
	public void test() {
		String q = "Select a from s where x ";
		int index = q.indexOf("x");
		
		CursorContext c = debug(test.Util.getCursorContext(q, index));
		assertTrue(Op.eq(c.completionOptions, SqlCompletionType.column, SqlCompletionType.columnConditionExprAfterColumn));
		assertTrue(c.getModelElementType() == SqlCompletionType.column);
		
		
	}

}
