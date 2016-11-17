package com.itscreening.tests.faker;

import java.util.Random;

public class Faker {

	private final Random rand = new Random();
	private final static int LEN_NUMRANGE = 10;
	private final static int START_NUMRANGE = 48;
	private final static int LEN_CHARRANGE = 26;
	private final static int START_CHARRANGE = 97;

	private final static char PATTERN_NUM = '#';
	private final static char PATTERN_CHAR = '?';

	private char getRandChar() {
		return (char) (rand.nextInt(LEN_CHARRANGE - 1) + START_CHARRANGE);
	}

	private char getRandNum() {
		return (char) (rand.nextInt(LEN_NUMRANGE - 1) + START_NUMRANGE);
	}

	private String patch(String s) {
		if (s == null)
			return null;
		else {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < s.length(); i++)
				switch (s.charAt(i)) {
				case PATTERN_NUM:
					sb.append(getRandNum());
					break;
				case PATTERN_CHAR:
					sb.append(getRandChar());
					break;

				default:
					sb.append(s.charAt(i));
				}

			return sb.toString();
		}
	}

	public String letterify(String letterString) {
		return patch(letterString);
	}

	public String numerify(String numberString) {
		return patch(numberString);
	}

	public String bothify(String string) {
		return patch(string);
	}

}
