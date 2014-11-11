package org.msf.ds;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.fest.assertions.Assertions.assertThat;

public class FuzzySetTest {
    @Test
    public void shouldCreate() {
        FuzzySet<Integer> set = new FuzzySet<Integer>();
        set.add(1, 0.5).add(2, 4);
        assertEquals(2, set.size());
    }

    @Test
    public void shouldIntersect() {
        FuzzySet<String> set1 = new FuzzySet<>();
        FuzzySet<String> set2 = new FuzzySet<>();
        set1.add("foo", 0.5).add("bar", 0.3);
        set2.add("foo", 0.2).add("bar", 0.7).add("cat", 0.5);

        FuzzySet<String> intersection = set1.intersect(set2);

        assertEquals(2, intersection.size());
        assertTrue(intersection.contains("foo", 0.2));
        assertTrue(intersection.contains("bar", 0.3));
    }

    @Test
    public void shouldIntersectWithEmpty() {
        FuzzySet<String> set1 = new FuzzySet<String>() {{
            add("foo", 0.5).add("bar", 0.3);
        }};

        FuzzySet<String> intersection = set1.intersect(new FuzzySet<String>());

        assertEquals(0, intersection.size());
    }

    @Test
    public void shouldNotAllowDuplicates() {
        FuzzySet<String> set = new FuzzySet<>();
        set.add("abcd", 1).add("abcd", 2);
        assertEquals(1, set.size());
        assertTrue(set.contains("abcd"));
    }

    @Test
    public void shouldNotAddElementsThatAreNotWorthIt() {
        FuzzySet<String> set = new FuzzySet<>();

        set.add("abcd", 0.0);

        assertEquals(0, set.size());
    }

    @Test
    public void shouldCalculateCardinality() {
        FuzzySet<String> set = new FuzzySet<String>() {{
            add("foo", 0.5).add("bar", 0.3);
        }};

        assertThat(set.cardinality()).isEqualTo(0.8);
    }
}