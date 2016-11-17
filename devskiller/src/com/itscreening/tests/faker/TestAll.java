package com.itscreening.tests.faker;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestAll {
	private final LetterifyTest letterify = new LetterifyTest();
	private final NumerifyTest numerify = new NumerifyTest();
	private final BothifyTest bothify = new BothifyTest();

	@Test
	public void testLetterify() {
		letterify.shouldCreateRandomText();
		letterify.shouldEmbedRandomLetterInText();
	}

	public void testNumerify() {
		numerify.shouldCreateRandomNumber();
		numerify.shouldEmbedRandomNumberInText();
	}

	public void testBothify() {
		bothify.shouldEmbedRandomElementsInText();
	}

}
