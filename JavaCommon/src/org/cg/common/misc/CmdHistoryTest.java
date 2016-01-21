package org.cg.common.misc;

import static org.junit.Assert.*;

import org.cg.common.io.StringStorage;
import org.junit.Test;

public class CmdHistoryTest {

	
	public class Dest implements CmdDestination
	{

		public String value = null;

		@Override
		public void set(String cmd) {
			value = cmd;
		}
	};

	Dest dest = new Dest();
	
	@Test
	public void testCmdHistory() {
		CmdHistory h = createHistory();
		
		assertTrue(h.isEmpty());
		h.add("1");
		assertFalse(h.isEmpty());
		
		assertFalse(h.next(dest));
		assertNull(dest.value);
		
		assertTrue(h.prev(dest));
		assertEquals("1", dest.value);
		
		assertFalse(h.prev(dest));
			
		h.add("2");
		assertFalse(h.next(dest));
		
		assertTrue(h.prev(dest));
		assertEquals("2", dest.value);
		assertTrue(h.prev(dest));
		assertEquals("1", dest.value);
		assertFalse(h.prev(dest));
		
		assertTrue(h.next(dest));
		assertEquals("2", dest.value);
		assertFalse(h.next(dest));
		assertTrue(h.prev(dest));
		assertEquals("1", dest.value);
		h.rewind();
		assertTrue(h.prev(dest));
		assertEquals("2", dest.value);
		
	}

	private CmdHistory createHistory() {
		return new CmdHistory(new StringStorage() {

			@Override
			public String read() {
				return null;
			}

			@Override
			public void write(String s) {
			}
		});
	};
}
