package org.msf.ds;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.min;
import static org.msf.ds.Fold.foldl;

public class FuzzySet<T> {
    private Map<T, Double> set = new HashMap<>();

    public FuzzySet<T> add(T element, double weight) {
        if (weight > 0.0) set.put(element, weight);
        return this;
    }

    public int size() {
        return set.size();
    }

    public FuzzySet<T> intersect(final FuzzySet<T> other) {
        return foldl(set.entrySet(), new BinaryFunction<FuzzySet<T>, Map.Entry<T, Double>, FuzzySet<T>>() {
            @Override
            public FuzzySet<T> apply(FuzzySet<T> acc, Map.Entry<T, Double> input) {
                T element = input.getKey();
                return other.contains(element) ?
                        acc.add(element, min(getWeight(element), other.getWeight(element))) : acc;
            }
        }, new FuzzySet<T>());
    }

    public Double getWeight(T element) {
        return set.get(element);
    }

    public boolean contains(T element) {
        return set.containsKey(element);
    }

    public boolean contains(T element, Double weight) {
        return set.containsKey(element) && set.get(element).equals(weight);
    }

    public Double cardinality() {
        return foldl(set.values(), new BinaryFunction<Double, Double, Double>() {
            @Override
            public Double apply(Double acc, Double input) {
                return acc + input;
            }
        }, 0.0);
    }

    public static <T> FuzzySet<T> fuzzySet() {
        return new FuzzySet<>();
    }
}
