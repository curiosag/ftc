package com.itscreening.tests.faker;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class BothifyTest {

    public void shouldEmbedRandomElementsInText() {
        //given
        Faker faker = new Faker();

        //when
        String result = faker.bothify("Test?#");

        //then
        assertThat(result).matches("Test[a-z][0-9]");
    }

}
