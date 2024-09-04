package com.example.cwe.sqli.entity;

import javax.persistence.Column;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Product.class)
public class Product_ {
    public static volatile SingularAttribute<Product, Integer> id;
    public static volatile SingularAttribute<Product, String> name;
    public static volatile SingularAttribute<Product, String> expirationDate;
    public static volatile SingularAttribute<Product, Integer> quantity;
    public static volatile SingularAttribute<Product, Double> price;

}
