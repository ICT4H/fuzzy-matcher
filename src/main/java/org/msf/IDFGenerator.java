package org.msf;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.msf.ds.BinaryFunction;
import org.msf.ds.DTM;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.msf.CleanDoc.cleanDoc;
import static org.msf.ds.Fold.foldl;


public class IDFGenerator {
    public DTM generate(List<String> docs) {
        List<String> cleansedDocs = from(docs).transform(cleanDoc()).toList();
        Map<String, Integer> bagOfWords = getBagOfWords(cleansedDocs);
        ArrayList<String> wordSet = newArrayList(bagOfWords.keySet());
        Double[][] idf = calculateFrequency(cleansedDocs, wordSet, bagOfWords);
        return new DTM(wordSet.toArray(new String[wordSet.size()]), docs.toArray(new String[docs.size()]), idf);
    }

    private Double[][] calculateFrequency(final List<String> docs, final ArrayList<String> wordSet, final Map<String, Integer> bagOfWords) {
        final BinaryFunction<ArrayList<Double[]>, String, ArrayList<Double[]>> calcucateWordFrequencyPerDoc = new BinaryFunction<ArrayList<Double[]>, String, ArrayList<Double[]>>() {
            @Override
            public ArrayList<Double[]> apply(ArrayList<Double[]> acc, final String doc) {
                BinaryFunction<ArrayList<Double>, String, ArrayList<Double>> calculateWordFrequency = new BinaryFunction<ArrayList<Double>, String, ArrayList<Double>>() {
                    @Override
                    public ArrayList<Double> apply(ArrayList<Double> prevResult, final String word) {
                        Double frequency = contains(doc, word) ? 1.0 / bagOfWords.get(word) : 0.0;
                        prevResult.add(frequency);
                        return prevResult;
                    }
                };

                ArrayList<Double> currentFreqs = foldl(wordSet, calculateWordFrequency, Lists.<Double>newArrayList());
                acc.add(currentFreqs.toArray(new Double[currentFreqs.size()]));
                return acc;
            }
        };

        List<Double[]> freq = foldl(docs, calcucateWordFrequencyPerDoc, Lists.<Double[]>newArrayList());
        return freq.toArray(new Double[][]{});
    }

    private boolean contains(String input, final String word) {
        return from(newArrayList(input.split(" "))).anyMatch(new Predicate<String>() {
            @Override
            public boolean apply(String token) {
                return token.equals(word);
            }
        });
    }

    private Map<String, Integer> getBagOfWords(List<String> docs) {
        Map<String, Integer> bagOfWords = foldl(docs, new BinaryFunction<Map<String, Integer>, String, Map<String, Integer>>() {
            @Override
            public Map<String, Integer> apply(Map<String, Integer> acc, String input) {
                String[] words = input.split(" ");
                Set<String> knownWords = newHashSet();
                for (String word : words) {
                    Integer count = fromNullable(acc.get(word)).or(0);
                    if(!knownWords.contains(word)) {
                        knownWords.add(word);
                        count++;
                    }
                    acc.put(word, count);
                }
                return acc;
            }
        }, Maps.<String, Integer>newHashMap());
        return bagOfWords;
    }
}
