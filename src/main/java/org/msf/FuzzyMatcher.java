package org.msf;

import org.msf.ds.BinaryFunction;
import org.msf.ds.DTM;
import org.msf.ds.FuzzySet;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static org.msf.CleanDoc.cleanDoc;
import static org.msf.LevenshteinDistance.computeLevenshteinDistance;
import static org.msf.ds.Fold.foldl;

public class FuzzyMatcher {
    private class Tuple<T> extends HashMap.SimpleImmutableEntry<T, Double> {
        private int numberOfElements;
        public Tuple(T key, Double value, int numberOfElements) {
            super(key, value);
            this.numberOfElements = numberOfElements;
        }

        public int getNumberOfElements() {
            return numberOfElements;
        }
    }

    public String match(DTM dtm, final String toBeMatched) {
        Map<String, FuzzySet<String>> fuzzySetMap = getFuzzySetMap(dtm);
        final FuzzySet<String> setToBeMatched = getFuzzySet(toBeMatched, dtm.getWords());
        Tuple<String> matchedTuple = foldl(fuzzySetMap.entrySet(), new BinaryFunction<Tuple<String>, Map.Entry<String, FuzzySet<String>>, Tuple<String>>() {
            @Override
            public Tuple<String> apply(Tuple<String> prevResult, Map.Entry<String, FuzzySet<String>> input) {
                Double newWeight = input.getValue().intersect(setToBeMatched).cardinality();
                return prevResult == null
                        || prevResult.getValue() < newWeight
                        || (prevResult.getValue().equals(newWeight) && prevResult.getNumberOfElements() > input.getValue().size())
                        ? new Tuple<>(input.getKey(), newWeight, input.getValue().size()) : prevResult;
            }
        }, null);
        return matchedTuple.getValue() > 0.0 ? matchedTuple.getKey() : null;
    }

    private String correctSpellingMistake(String stringToBeMatched, String[] bagOfWords) {
        double minThreshold = 1.0;
        String returnVal = stringToBeMatched;
        for (String word : bagOfWords) {
            int distance = computeLevenshteinDistance(word, stringToBeMatched);
            double threshold = (double)distance / (double)Math.max(word.length(), stringToBeMatched.length());
            if (minThreshold > threshold && threshold <= 0.25) {
                minThreshold = threshold;
                returnVal = word;
            }
        }
        return returnVal;
    }

    private FuzzySet<String> getFuzzySet(String toBeMatched, String[] bagOfWords) {
        toBeMatched = cleanDoc().apply(toBeMatched);
        String[] tokenizedStr = toBeMatched.split(" ");
        FuzzySet<String> set = new FuzzySet<>();
        for (String str : tokenizedStr) {
            str = correctSpellingMistake(str, bagOfWords);
            set.add(str, 1.0);
        }
        return set;
    }

    private Map<String, FuzzySet<String>> getFuzzySetMap(DTM dtm) {
        HashMap<String, FuzzySet<String>> docSetMap = newHashMap();
        String[] docs = dtm.getDocs();
        String[] words = dtm.getWords();
        Double[][] idf = dtm.getIdf();

        for (int i = 0; i < docs.length; i++) {
            FuzzySet<String> fuzzySet = new FuzzySet<>();
            for (int j = 0; j < words.length; j++) {
                fuzzySet.add(words[j], idf[i][j]);
            }
            docSetMap.put(docs[i], fuzzySet);
        }
        return docSetMap;
    }
}
