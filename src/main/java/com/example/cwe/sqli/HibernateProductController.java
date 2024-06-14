package com.example.cwe.sqli;


import com.example.cwe.sqli.entity.Product;
import com.example.cwe.sqli.repository.HibernateProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * SQL injection:
 * http://localhost:7171/sqli/hql/products-byName-unsafe/juice' or '1'='1
 * http://localhost:7171/sqli/hql/products-byName-session-unsafe/juice' or '1'='1
 * url-encoded:     %27%20or%20%271%27=%271
 */
@RestController
@RequestMapping("sqli/hql")
public class HibernateProductController {
    //https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/chapters/query/hql/HQL.html#:~:text=The%20Hibernate%20Query%20Language%20(HQL,reverse%20is%20not%20true%20however.
    //difference between JPQL and HQL!
    @Autowired
    private HibernateProductRepository repository;


    //%27%20or%20%271%27=%271
    @GetMapping("/products-byName-unsafe/{name}")
    public List<Product> findByName_HQL_unsafe(@PathVariable String name) {
        return repository.findByName_HQL_unsafe(name);
    }

    @GetMapping("/products-byName-safety/{name}")
    public List<Product> findByName_HQL_safety(@PathVariable String name) {
        return repository.findByName_HQL_safety(name);
    }


    @GetMapping("/products-byName-session-unsafe/{name}")
    public List<Product> findByName_HQL_Session_unsafe(@PathVariable String name) {
        return repository.findByName_HQL_Session_unsafe(name);
    }

    @GetMapping("/products-byName-session-safety/{name}")
    public List<Product> findByName_HQL_Session_safety(@PathVariable String name) {
        return repository.findByName_HQL_Session_safety(name);
    }

}
