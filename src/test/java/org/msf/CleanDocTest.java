package org.msf;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class CleanDocTest {
    @Test
    public void shouldCleanDoc() {
        String apply = new CleanDoc().apply("  AbC-(blah)  dEf   aDfd \\ foo   ");
        assertThat(apply).isEqualTo("abc blah def adfd foo");
    }

    @Test
    public void shouldClubNonWithTheNextWord() {
        String apply = new CleanDoc().apply("non a non-b def non b nonea bonon");
        assertThat(apply).isEqualTo("nona nonb def nonb nonea bonon");
    }
}