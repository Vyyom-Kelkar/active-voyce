package com.example.vyyom.activevoyce;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Vyyom on 12/23/2017.
 *
 * Create User class.
 */

public class User implements Serializable {

    // Private variables
    private String userName;
    private String password;
    private int highScore;
    private int currentScore;
    private Set<String> completedWords;

    public User() {
        this.userName = "";
        this.highScore = 0;
        this.currentScore = 0;
        this.completedWords = new HashSet<>();
    }

    // Getter methods
    public String getUserName() {
        return this.userName;
    }

    String getPassword() {
        return this.password;
    }

    public int getHighScore() {
        return this.highScore;
    }

    public int getCurrentScore() {
        return this.currentScore;
    }

    public Set<String> getCompletedWords() {
        return this.completedWords;
    }

    // Setter methods
    public void setUserName(String username) {
        this.userName = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHighScore(Integer highScore) {
        this.highScore = highScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    void addCompletedWord(String completedWords) {
        this.completedWords.add(completedWords);
    }

    @Override
    public String toString() {
        String info = "User Name = " + getUserName() + "\nHigh Score = " + getHighScore() +
                "\nCurrent Score = " + getCurrentScore();
        StringBuilder stringBuilder = new StringBuilder();
        for(String word : getCompletedWords()) {
            stringBuilder.append(word).append("\n");
        }
        return info + "\n" + stringBuilder.toString();
    }
}
