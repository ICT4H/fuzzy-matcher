package org.msf;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import org.tartarus.snowball.ext.englishStemmer;

import java.util.ArrayList;

import static com.google.common.base.Joiner.on;
import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Lists.newArrayList;


public class CleanDoc implements Function<String, String> {

    public static CleanDoc cleanDoc() {
        return new CleanDoc();
    }

    @Override
    public String apply(String input) {
        input = input.replaceAll("[\"\\-\\(\\)\\{\\}\\+,;:/\\\\]", " ");
        input = input.replaceAll("(\\s)+", " ");
        input = input.trim();
        input = input.toLowerCase();
        input = input.replaceAll("^non(\\s)*", "non");
        input = input.replaceAll(" non(\\s)*", " non");
        return stem(input);
    }

    private String stem(String input) {
        ArrayList<String> strings = newArrayList(input.split(" "));
        final englishStemmer englishStemmer = new org.tartarus.snowball.ext.englishStemmer();
        FluentIterable<String> stemmedList = from(strings).transform(new Function<String, String>() {
            @Override
            public String apply(String input) {
                englishStemmer.setCurrent(input);
                englishStemmer.stem();
                return englishStemmer.getCurrent();
            }
        });
        return on(" ").join(stemmedList);
    }
}
