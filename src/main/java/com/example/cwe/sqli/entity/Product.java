package com.example.cwe.sqli.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Products")
public class Product {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    @Column(name = "exp")
    private String expirationDate;
    private int quantity;
    private double price;
}
