package org.cg.common.structures;

import static org.junit.Assert.*;

import org.cg.common.structures.OrderedIntTuple;
import org.junit.Test;

public class OrderedIntTupleTest {

	
	
	@Test
	public void test() {
		OrderedIntTuple t = OrderedIntTuple.create(0, 0);
		assertFalse(t.swap);
		
		t = OrderedIntTuple.create(0, 1);
		assertFalse(t.swap);
		assertEquals(0, t.lo());
		assertEquals(1, t.hi());
		
		t = OrderedIntTuple.create(1, 0);
		assertTrue(t.swap);
		assertEquals(0, t.lo());
		assertEquals(1, t.hi());
		
	}

}
