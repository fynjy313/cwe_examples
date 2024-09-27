package com.example.cwe.sensitive_information;

import com.example.cwe.path_traversal.PathTraversalController;
import com.example.cwe.sqli.entity.Product;
import com.example.cwe.sqli.repository.HibernateProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.hql.internal.ast.QuerySyntaxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * CWE-209: Generation of Error Message Containing Sensitive Information
 * <p>
 * https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.3-Release-Notes#changes-to-the-default-error-pages-content
 * server.error.include-message=always
 * server.error.include-binding-errors=always
 */
@RestController
@RequestMapping("error")
public class ErrorMessageController {
    @Autowired
    private HibernateProductRepository repository;

    @GetMapping("/")
    public String getError() {
        return 1 / 0 + "";
    }

    /**
     * SQL injection unsafe.
     *
     * @code HQL_UNSAFE = "FROM Product t WHERE t.name='" + name + "'"
     * @injection name = %27%20or%20%271%27=%271
     */
    @GetMapping("/sql")
    public List<Product> findByName_HQL_unsafe() {
        String name = "q' or 1=1'";
        return repository.findByName_HQL_unsafe(name);
    }

    /**
     * SQL injection unsafe.
     *
     * @param name Название товара
     * @code HQL_UNSAFE = "FROM Product t WHERE t.name='" + name + "'"
     * @injection name = q' or 1=1'
     */
    @GetMapping("/check_product/{name}")
    public String isProductExist(@PathVariable String name) {
        try {
            repository.findByName_HQL_unsafe(name);
            return "Found!";
        } catch (Exception e) {
            return "Товар не найден:" + e;
        }
    }


    private static final Logger logger = LogManager.getLogger(ErrorMessageController.class);

    @GetMapping("/check_product_safe/{name}")
    public String isProductExistSafety(@PathVariable String name) {
        try {
            repository.findByName_HQL_unsafe(name);
            return "Found!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "Товар не найден";
        }
    }


}
