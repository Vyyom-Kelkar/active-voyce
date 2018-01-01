package com.example.vyyom.activevoyce;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Vyyom on 12/23/2017.
 *
 * Create User class.
 */

public class User implements Serializable {

    // Private variables
    private String userName;
    private String password;
    private Integer highScore;

    public User() {
        this.userName = "";
        this.highScore = 0;
    }

    // Getter methods
    private String getUserName() {
        return this.userName;
    }

    String getPassword() {
        return this.password;
    }

    private Integer getHighScore() {
        return this.highScore;
    }

    public ArrayList<String> getInfo() {
        ArrayList<String> stringList = new ArrayList<>();
        stringList.add(this.getUserName());
        stringList.add(this.getHighScore().toString());
        return stringList;
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
}
