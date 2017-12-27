package com.example.vyyom.activevoyce;

/**
 * Created by Vyyom on 12/27/2017.
 *
 * This file creates the class to save CSV data to.
 */

public class WordCombinations {

    private String word;
    private String verb;
    private String preposition;
    private String synonym1;
    private String synonym2;

    public String getWord() {
        return word;
    }

    @CSVAnnotation.CSVSetter(info = "word")
    public void setWord(String word) {
        this.word = word;
    }

    public String getVerb() {
        return verb;
    }

    @CSVAnnotation.CSVSetter(info = "verb")
    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getPreposition() {
        return preposition;
    }

    @CSVAnnotation.CSVSetter(info = "preposition")
    public void setPreposition(String preposition) {
        this.preposition = preposition;
    }

    public String getSynonym1() {
        return synonym1;
    }

    @CSVAnnotation.CSVSetter(info = "synonym1")
    public void setSynonym1(String synonym1) {
        this.synonym1 = synonym1;
    }

    public String getSynonym2() {
        return synonym2;
    }

    @CSVAnnotation.CSVSetter(info = "synonym2")
    public void setSynonym2(String synonym2) {
        this.synonym2 = synonym2;
    }

    @Override
    public String toString() {
        return "word = " + word +
                ", verb = " + verb +
                ", preposition = " + preposition +
                ", synonym1 = " + synonym1 +
                ", synonym2 = " + synonym2 +
                ".";
    }
}
