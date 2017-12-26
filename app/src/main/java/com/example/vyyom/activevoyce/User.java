package com.example.vyyom.activevoyce;

import java.io.Serializable;

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

    // Getter methods
    public String getUserName() {
        return userName;
    }

    String getPassword() {
        return password;
    }

    public Integer getHighScore() {
        return highScore;
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
