package org.msf;

import org.junit.Test;
import org.msf.ds.DTM;

import java.util.ArrayList;

import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.Assertions.assertThat;

public class IDFGeneratorTest {
    @Test
    public void shouldGenerateDTM() {
        ArrayList<String> docs = newArrayList("this     is  ABCD", "THis  is abc def", "not     any  of");

        DTM dtm = new IDFGenerator().generate(docs);

        assertThat(dtm.getDocs()).isEqualTo(docs.toArray());

        assertThat(dtm.getWords()).hasSize(8);
        String[] expectedBoW = {"not", "of", "abc", "is", "ani", "def", "abcd", "this"};
        assertThat(dtm.getWords()).contains(expectedBoW);

        assertThat(dtm.getIdf()[0]).contains(new Double[]{0.0, 0.0, 0.0, 0.5, 0.0, 0.0, 1.0, 0.5});
        assertThat(dtm.getIdf()[1]).contains(new Double[]{0.0, 0.0, 1.0, 0.5, 0.0, 1.0, 0.0, 0.5});
        assertThat(dtm.getIdf()[2]).contains(new Double[]{1.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0});
    }
}