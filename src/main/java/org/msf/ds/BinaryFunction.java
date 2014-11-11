package org.msf.ds;

public interface BinaryFunction<A, B, C> {
    C apply(A prevResult, B input);
}
