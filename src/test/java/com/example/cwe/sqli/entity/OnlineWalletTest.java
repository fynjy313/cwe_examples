package com.example.cwe.sqli.entity;

import org.apache.commons.codec.digest.DigestUtils;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class OnlineWalletTest {

    @Test
    void md5PasswordGenerate() {// вычисляем md5
        ArrayList<String> list = new ArrayList<>(Arrays.asList("admin", "user", "vasya", "petya"));
        list.forEach(s -> System.out.println(s + "\t" + DigestUtils.md5Hex(s)));
    }

}