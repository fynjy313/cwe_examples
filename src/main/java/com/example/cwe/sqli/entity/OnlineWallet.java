package com.example.cwe.sqli.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Wallets")
public class OnlineWallet {
    @Id
    @GeneratedValue
    private int id;
    private String userName;
    private String email;
    private String password;
    private long cash;
}
