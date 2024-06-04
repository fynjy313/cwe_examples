package com.example.cwe.xml_injecions.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;
import java.util.UUID;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User {
    @XmlAttribute(name = "id")
    private UUID userId;
    private String username;
    private String password;
    private String group;
    private String email;

    public User(String username, String password, String group, UUID userId, String email) {
        this.username = username;
        this.password = password;
        this.group = group;
        this.userId = userId;
        this.email = email;
    }

    public User() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId && username.equals(user.username) && password.equals(user.password) && group.equals(user.group) && email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, group, userId, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", group='" + group + '\'' +
                ", userId=" + userId +
                ", email='" + email + '\'' +
                '}' +
                "\n";
    }

}