package org.msf.ds;

public class DTM {
    private String[] words;
    private String[] docs;
    private Double[][] idf;

    public DTM(String[] words, String[] docs, Double[][] idf) {
        this.words = words;
        this.docs = docs;
        this.idf = idf;
    }

    public String[] getWords() {
        return words;
    }

    public String[] getDocs() {
        return docs;
    }

    public Double[][] getIdf() {
        return idf;
    }
}
