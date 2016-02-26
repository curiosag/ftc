package org.cg.common.util;

import static org.junit.Assert.*;

import org.cg.common.structures.OrderedIntTuple;
import org.junit.Test;

public class StringUtilTest {
	
	@Test
	public void testCoalesce()
	{
		assertEquals("", StringUtil.coalesce("", ""));
		assertEquals("", StringUtil.coalesce("", " "));
		assertEquals("a", StringUtil.coalesce("a", ""));
		assertEquals("a", StringUtil.coalesce("a", "a"));
		assertEquals("a", StringUtil.coalesce("aa", "a"));
		assertEquals("abbbabbba", StringUtil.coalesce("aaabbbabbbaaaaaaaa", "a"));
	}
	
	@Test 
	public void testTimesN()
	{
		assertEquals("", StringUtil.timesN("aa", 0));
		assertEquals("a", StringUtil.timesN("a", 1));
		assertEquals("aa", StringUtil.timesN("a", 2));
		assertEquals("", StringUtil.timesN("", 2));
	}
	
	@Test
	public void testindexBeforeChar() {
		String val = "0 234 6";
		// "normal" case
		assertEquals(4, StringUtil.getIndexBeforeChar(val, ' ', 3, 1));
		assertEquals(2, StringUtil.getIndexBeforeChar(val, ' ', 3, -1));
		//empty string
		assertEquals(-1, StringUtil.getIndexBeforeChar("", ' ', 3, 1));
		assertEquals(-1, StringUtil.getIndexBeforeChar("", ' ', 3, -1));
		assertEquals(-1, StringUtil.getIndexBeforeChar(null, ' ', 3, 1));
		assertEquals(-1, StringUtil.getIndexBeforeChar(null, ' ', 3, -1));

		// left extreme case
		assertEquals(0, StringUtil.getIndexBeforeChar(val, ' ', 0, 1));
		assertEquals(0, StringUtil.getIndexBeforeChar(val, ' ', 0, -1));
		
		// right extreme case
		assertEquals(6, StringUtil.getIndexBeforeChar(val, ' ', 6, 1));
		assertEquals(6, StringUtil.getIndexBeforeChar(val, ' ', 6, -1));
		
		// index at char case
		assertEquals(1, StringUtil.getIndexBeforeChar(val, ' ', 1, 1));
		assertEquals(1, StringUtil.getIndexBeforeChar(val, ' ', 1, -1));
		
	}

	@Test
	public void testPeel() {
		assertEquals("", StringUtil.peel(""));
		assertEquals("", StringUtil.peel("."));
		assertEquals("", StringUtil.peel(".."));
		assertEquals("!", StringUtil.peel(".!."));
		assertEquals("!", StringUtil.peel(".!+"));
		assertEquals("!!", StringUtil.peel(".!!."));
		
		// overload 1
		assertEquals("", StringUtil.peel("", '\''));
		assertEquals(null, StringUtil.peel(null, '\''));
		assertEquals("", StringUtil.peel("'", '\''));
		assertEquals("", StringUtil.peel("''", '\''));
		assertEquals("a", StringUtil.peel("'a'", '\''));
	}
	
	@Test
	public void testReplace() {
		assertEquals("X", StringUtil.replace(null, interval(0, 0), "X"));
		assertEquals("X", StringUtil.replace("", interval(0, 0), "X"));
		assertEquals("X", StringUtil.replace("", interval(1, 1), "X"));

		assertEquals("X", StringUtil.replace("a", interval(0, 0), "X"));
		assertEquals("aX", StringUtil.replace("a", interval(1, 1), "X"));

		assertEquals("bXb", StringUtil.replace("bab", interval(1, 1), "X"));
		assertEquals("bXXb", StringUtil.replace("bab", interval(1, 1), "XX"));

		assertEquals("baXXXab", StringUtil.replace("baaaab", interval(2, 3), "XXX"));

	}

	@Test
	public void testInsert() {
		assertEquals("X", StringUtil.insert(null, 0, "X"));
		assertEquals("X", StringUtil.insert("", 0, "X"));
		assertEquals("X", StringUtil.insert("", 1, "X"));

		assertEquals("Xa", StringUtil.insert("a", 0, "X"));
		assertEquals("aX", StringUtil.insert("a", 1, "X"));

		assertEquals("bXb", StringUtil.insert("bb", 1, "X"));

	}

	private OrderedIntTuple interval(int lo, int hi) {
		return OrderedIntTuple.create(lo, hi);
	}

}
