package org.msf;

import com.google.common.io.Files;
import org.junit.Test;
import org.msf.ds.DTM;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class FuzzyMatcherTest {
    @Test
    public void shouldMatchTermsToExistingDocs() {
        String[] docs = {
                "Acute Flaccid Paralysis	Acute flacid paralysis",
                "Acute Jaundice (non-Malaria)	Jaundice	Jaundice",
                "AIDS, clinical	Clinical AIDS",
                "Dressings (new cases)	Dressings (new cases)	Dressings (new cases)",
                "Dressings (old cases)	Dressings (old cases)	Dressings (old cases)",
                "Anaemia	Anaemia (clinical)	Anaemia (clinical)	Anaemia"
        };
        DTM dtm = new IDFGenerator().generate(Arrays.asList(docs));

        String match = new FuzzyMatcher().match(dtm, "Anemia (non clinical)");

        assertThat(match).isEqualTo("Anaemia	Anaemia (clinical)	Anaemia (clinical)	Anaemia");

        match = new FuzzyMatcher().match(dtm, "Dressing (news cases)");
        assertThat(match).isEqualTo("Dressings (new cases)\tDressings (new cases)\tDressings (new cases)");
    }

//    @Test
//    @Ignore
//    public void shouldMatchFromTSV() throws URISyntaxException, IOException {
//        List<String> trainingSet = getLines("training.tsv");
//        DTM dtm = new IDFGenerator().generate(trainingSet);
//        List<String> testSet = getLines("test.tsv");
//        int failedCount = 0;
//        int totalCount = 0;
////        double minScaledScore = 1.1;
////        double maxScaledScore = -1.0;
////        double totalScaledScore = 0.0;
////        int totalCorrectCount = 0;
//
//        Set<String> errors = newHashSet();
//        for (String s : testSet) {
//            String[] testNames = s.split("\t");
//            String expected = testNames[0];
//            for (String testName : testNames) {
//                totalCount++;
//                if (!testName.trim().isEmpty()) {
//                    FuzzyMatcher.Tuple<String> match = new FuzzyMatcher().match(dtm, testName);
//                    try {
//                        assertThat(match.getKey()).startsWith(expected);
////                        double scaledScore = (double) match.getValue() / match.getNumberOfElements();
////                        minScaledScore = Math.min(minScaledScore, scaledScore);
////                        maxScaledScore = Math.max(maxScaledScore, scaledScore);
////                        totalScaledScore += scaledScore;
//////                        totalCorrectCount++;
////                        System.out.println("keyLength : " + match.getNumberOfElements()  + " score : " + match.getValue() + " scaled : " + scaledScore);
//                    } catch (AssertionError error) {
//                        failedCount++;
//                        errors.add(testName);
//                        System.err.printf("Key: %s     Actual: %s     Expected: %s     Score: %s%n", testName, match.getKey(), expected, match.getValue());
//                    }
//                }
//            }
//        }
//
////        System.out.println("==============================================================");
////        System.out.println("Min Scaled Score: " + minScaledScore);
////        System.out.println("Max Scaled Score: " + maxScaledScore);
////        System.out.println("Avg Scaled Score: " + (totalScaledScore / totalCorrectCount));
////        System.out.println("==============================================================");
//        System.out.printf("Failed %d out of %d%n", failedCount, totalCount);
//        System.out.printf("Failed set: %d%n", errors.size());
//        System.out.printf("Accuracy: %s", (double) (totalCount - failedCount) / (double) totalCount);
//    }

    private List<String> getLines(String fileName) throws IOException, URISyntaxException {
        URL trainingFile = this.getClass().getClassLoader().getResource(fileName);
        List<String> result = Files.readLines(Paths.get(trainingFile.toURI()).toFile(), Charset.defaultCharset());
        result.remove(0);
        return result;
    }
}