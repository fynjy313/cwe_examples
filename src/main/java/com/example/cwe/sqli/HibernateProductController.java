package com.example.cwe.sqli;


import com.example.cwe.sqli.entity.Product;
import com.example.cwe.sqli.repository.HibernateProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
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

    /**
     * SQL injection unsafe.
     *
     * @param name Название товара
     * @code HQL_UNSAFE = "FROM Product t WHERE t.name='" + name + "'"
     * @injection name = %27%20or%20%271%27=%271
     */
    @GetMapping("/products-byName-unsafe/{name}")
    public List<Product> findByName_HQL_unsafe(@PathVariable String name) {
        return repository.findByName_HQL_unsafe(name);
    }

    /**
     * SQL injection unsafe.
     *
     * @param name Название товара
     * @code HQL_UNSAFE = "FROM Product t WHERE t.name='" + name + "'"
     * @injection %27%20or%20%271%27=%271
     */
    @GetMapping("/products-byName-class-unsafe/{name}")
    public List<Product> findProductByClassName_HQL_unsafe(@PathVariable String name) {
        return repository.findProductByClassName_HQL_unsafe(name);
    }

    /**
     * SQL injection unsafe.
     *
     * @param name Название товара
     * @code HQL_UNSAFE = "FROM Product t WHERE t.name='" + name + "'"
     * @injection %27%20or%20%271%27=%271
     */
    @GetMapping("/products-byName-session-unsafe/{name}")
    public List<Product> findByName_HQL_Session_unsafe(@PathVariable String name) {
        return repository.findByName_HQL_Session_unsafe(name);
    }

    /**
     * SQL injection safe. Positional parameters.
     *
     * @param name Название товара
     * @code HQL_SAFETY = "FROM Product t WHERE t.name=?1"
     */
///"FROM Product t WHERE t.name=?1"
    @GetMapping("/products-byName-safety/{name}")
    public List<Product> findByName_HQL_safety(@PathVariable String name) {
        return repository.findByName_HQL_safety(name);
    }

    /**
     * SQL injection safe. Named parameters.
     *
     * @param name Название товара
     * @code HQL_SAFETY_2 = "FROM Product t WHERE t.name = :paramName"
     */
    @GetMapping("/products-byName-session-safety/{name}")
    public List<Product> findByName_HQL_Session_safety(@PathVariable String name) {
        return repository.findByName_HQL_Session_safety(name);
    }

    /**
     * SQL injection safe. JPA Criteria API.
     *
     * @param name Название товара
     * @code Root<Product> root = cq.from(Product.class);
     * cq.select(root).where(cb.equal(root.get(Product_.name), name));
     */
    @GetMapping("/products-byName-criteria-api-safety/{name}")
    public List<Product> findByName_HQL_criteriaApi_safety(@PathVariable String name) {
        return repository.findByName_HQL_criteriaApi_safety(name);

    }

}
