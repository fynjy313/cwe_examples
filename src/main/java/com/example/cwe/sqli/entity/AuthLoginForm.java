package com.example.cwe.sqli.entity;

public record AuthLoginForm(String username, String password) {

    @Override
    public String toString() {
        return "LoginForm{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
