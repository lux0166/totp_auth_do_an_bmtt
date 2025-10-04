package com.example.totpdemo;

public class User {
    private String username;
    private String password;
    private String secret;
    private boolean mfaEnabled;

    public User(String username, String password, String secret) {
        this.username = username;
        this.password = password;
        this.secret = secret;
        this.mfaEnabled = false; // Initially MFA is not enabled
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public boolean isMfaEnabled() {
        return mfaEnabled;
    }

    public void setMfaEnabled(boolean mfaEnabled) {
        this.mfaEnabled = mfaEnabled;
    }
}
