package com.example.cwe.xml_injecions.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Users {
    @XmlElement(name = "user")
    private List<User> userList;

    public Users() {
        userList = new ArrayList<>();
    }

    public void addUser(User user) {
        userList.add(user);
    }

//    public List<_User> getUserList() {
//        if (userList == null)
//            userList = new ArrayList<>();
//        return this.userList;
//    }
//
//    public void setUserList(List<_User> userList) {
//        this.userList = userList;
//    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (User user : userList) {
            sb.append(user.toString()).append("\n");
        }
        return sb.toString().replaceAll("\n$", "");
    }
}
