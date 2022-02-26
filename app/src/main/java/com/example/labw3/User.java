package com.example.labw3.provider;

public class User {
    private int userId;
    private String userEmail;
    private String password;

    public User(String userEmail, String password)
    {
        this.userEmail = userEmail;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
