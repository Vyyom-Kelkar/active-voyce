package com.example.vyyom.activevoyce;

import com.opencsv.bean.CsvBindByName;

/**
 * Created by Vyyom on 12/27/2017.
 *
 * This file creates the bean to save CSV data to.
 */

public class WordCombinations {

    @CsvBindByName
    private String word;

    @CsvBindByName
    private String verb;

    @CsvBindByName
    private String preposition;

    @CsvBindByName
    private String synonym1;

    @CsvBindByName
    private String synonym2;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getPreposition() {
        return preposition;
    }

    public void setPreposition(String preposition) {
        this.preposition = preposition;
    }

    public String getSynonym1() {
        return synonym1;
    }

    public void setSynonym1(String synonym1) {
        this.synonym1 = synonym1;
    }

    public String getSynonym2() {
        return synonym2;
    }

    public void setSynonym2(String synonym2) {
        this.synonym2 = synonym2;
    }
}
