package org.cg.common.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.cg.common.structures.Tuple;
import org.junit.Test;

public class TestCollectionUtil {

	@Test
	public void testPartition() {
		List<Integer> l = new ArrayList<Integer>();
		
		Tuple<List<Integer>> r = CollectionUtil.partition(0, l);
		
		assertEquals(0, r.e1.size());
		assertEquals(0, r.e2.size());
		
		l.add(0);
		r = CollectionUtil.partition(0, l);
		assertEquals(0, r.e1.size());
		assertEquals(1, r.e2.size());
		
		r = CollectionUtil.partition(1, l);
		assertEquals(0, r.e1.size());
		assertEquals(0, r.e2.size());
		
		l.add(1);
		r = CollectionUtil.partition(0, l);
		assertEquals(0, r.e1.size());
		assertEquals(2, r.e2.size());
		
		r = CollectionUtil.partition(1, l);
		assertEquals(1, r.e1.size());
		assertEquals(1, r.e2.size());
		
		r = CollectionUtil.partition(2, l);
		assertEquals(0, r.e1.size());
		assertEquals(0, r.e2.size());
		
	}

}
