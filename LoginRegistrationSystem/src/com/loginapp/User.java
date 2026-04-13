package com.loginapp;

public class User {
    private final String username;
    private final String fullName;
    private final String passwordHash;

    public User(String username, String fullName, String passwordHash) {
        this.username = username;
        this.fullName = fullName;
        this.passwordHash = passwordHash;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
