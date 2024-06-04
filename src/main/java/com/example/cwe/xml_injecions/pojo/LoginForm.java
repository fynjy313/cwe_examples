package com.example.cwe.xml_injecions.pojo;


public record LoginForm(String username, String password, String email) {

    @Override
    public String toString() {
        return "LoginForm{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
