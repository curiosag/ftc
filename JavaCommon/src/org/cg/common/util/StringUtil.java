package org.cg.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.List;

import org.cg.common.check.Check;
import org.cg.common.structures.OrderedIntTuple;

import com.google.common.base.Joiner;

public class StringUtil {

	public static String getStackTrace(Throwable t) {
	    StringWriter sw = new StringWriter();
	    t.printStackTrace(new PrintWriter(sw));
	    return sw.toString();
	}
	
	public static int getIndexBeforeChar(String text, char c, int startIndex, int increment) {
		if (StringUtil.emptyOrNull(text))
			return -1;

		int currIndex = startIndex;
		int maxIndex = text.length() - 1;

		Check.isTrue(currIndex >= 0 && currIndex <= maxIndex);

		if (text.charAt(startIndex) == c)
			return startIndex;

		while (currIndex >= 0 || currIndex < maxIndex) {
			int nextIndex = currIndex + increment;
			if (nextIndex < 0 || nextIndex > maxIndex || text.charAt(nextIndex) == c)
				return currIndex;
			else
				currIndex = currIndex + increment;
		}
		return startIndex;
	}

	/**
	 * clips 1 character from beginning and end of a string. returns "" if
	 * length is < 3
	 */
	public static String peel(String s) {
		String result = null;
		if (s.length() < 3)
			result = "";
		else if (s.length() >= 3)
			result = s.substring(1, s.length() - 1);
		return result;
	}

	/**
	 * 
	 * @param value
	 * @return	value without single quotes
	 */
	public static String stripQuotes(String value)
	{
		return StringUtil.peel(value, '\'');
	}
	
	/**
	 * clips character c from beginning and end of s, if any of them is there
	 * 
	 * @param s
	 * @param c
	 * @return clipped string
	 */
	public static String peel(String s, char c) {
		String result = s;
		if (s != null && s.length() > 1)
			result = result.charAt(0) == '\'' ? result.substring(1) : result;
		if (s != null && s.length() > 0)
			result = result.charAt(result.length() - 1) == '\'' ? result.substring(0, result.length() - 1) : result;

		return result;
	}

	public static String replace(String from, OrderedIntTuple clipInterval, String replacement) {
		Check.isTrue(clipInterval.hi() >= 0 && clipInterval.lo() >= 0);

		if (from == null || from.length() == 0)
			return replacement;

		if (max(from) < clipInterval.lo())
			return from + replacement;

		String front;
		if (clipInterval.lo() == 0)
			front = "";
		else
			front = from.substring(0, clipInterval.lo());

		String back;
		if (max(from) <= clipInterval.hi())
			back = "";
		else
			back = from.substring(clipInterval.hi() + 1);

		return front + replacement + back;
	}

	private static int max(String from) {
		return from.length() - 1;
	}

	public static boolean equalsAny(String value, String... comparisons) {
		for (String c : comparisons)
			if (value.equals(c))
				return true;
		return false;
	}

	public static String nonNull(String s) {
		if (s == null) {
			return "";
		}
		return s;
	}

	public static boolean nullableEqual(String s1, String s2) {
		return (s1 == null && s2 == null) || (s1 != null && s2 != null && s1.length() == s2.length() && s1.equals(s2));
	}

	public static boolean emptyOrNull(String s) {
		return s == null || s.length() == 0;
	}

	public static String quote(String value) {
		return "\"" + value + "\"";
	}

	public static String concat(String s1, String s2) {
		if (s1 == null && s2 == null)
			return null;
		else
			return s1 + s2;
	}

	public static <T> String toCsv(Iterable<T> items, String separator) {
		return Joiner.on(separator).join(items);
	}

	public static <T> String ToCsv(Enumeration<T> items, String separator) {
		return Joiner.on(separator).join(CollectionUtil.toList(items));
	}

	public static <T> String ToCsv(String[] items, String separator) {
		return Joiner.on(separator).join(items);
	}

	public static String ToCsv(List<String> items, String separator) {
		return Joiner.on(separator).join(items);
	}

	public static String insert(String from, int pos, String val) {

		Check.isTrue(pos >= 0);

		if (from == null || from.length() == 0)
			return val;

		return from.substring(0, pos) + val + from.substring(pos);
	}

	public static String timesN(String value, int n) {
		Check.notNull(value);
		Check.isTrue(n >= 0);
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++)
			sb.append(value);
		return sb.toString();
	}

	public static String coalesce(String value, String c) {
		Check.notNull(value);
		Check.notNull(c);
		
		if (c.length() == 0 || value.length() == 0)
			return value;
		
		String cc = timesN(c, 2);
		while (value.indexOf(cc) >= 0)
			value = value.replaceAll(cc, c);
		
		return value;
	}

}
