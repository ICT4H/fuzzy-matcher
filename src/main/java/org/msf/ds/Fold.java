package org.msf.ds;

import java.util.Iterator;

public class Fold {
    public static <A, B> A foldl(Iterable<B> xs, BinaryFunction<A, B, A> f, A seed) {
        return foldl(xs.iterator(), f, seed);
    }

    public static <A, B> A foldl(Iterator<B> xs, BinaryFunction<A, B, A> f, A seed) {
        return !xs.hasNext() ? seed : foldl(xs, f, f.apply(seed, xs.next()));
    }
}

