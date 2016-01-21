package org.cg.common.check;

public class Check {

	public static void fail(String msg)
	{
		throw new RuntimeException(msg);
	}
	
	public static void fail(Exception e)
	{
		throw new RuntimeException(String.format("Exception class: message:", e.getClass().getName(), e.getMessage()));
	}
	
	public static void notNull(Object o) {
		if (o == null)
			fail("unexpected null value");
	}

	public static void notEmpty(String s) {
		notNull(s);
		if (s.isEmpty())
			fail("unexpected empty string");
	}

	public static void isTrue(boolean b) {
		if (! b)
			fail("unexpected false value");
	}

	public static void isFalse(boolean b) {
		isTrue(!b);
	}

}
