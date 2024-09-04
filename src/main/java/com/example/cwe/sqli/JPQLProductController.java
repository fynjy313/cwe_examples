package com.example.cwe.sqli;

import com.example.cwe.sqli.entity.Product;
import com.example.cwe.sqli.service.JPAProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@RestController
@RequestMapping("sqli/jpql")
public class JPQLProductController {
    @Autowired
    private JPAProductService serviceJPA;


    @GetMapping("products")
    public List<Product> findAllProducts() {
        return serviceJPA.getProducts();
    }

    @GetMapping("product/id/{id}")
    public Product findProductById(@PathVariable int id) {
        return serviceJPA.getProductsById(id);
    }

    @GetMapping("product/name/{name}")
    public List<Product> findProductByName(@PathVariable String name) {
        return serviceJPA.getProductsByName(name);
    }

    // JPA example test
    @Autowired
    private EntityManager em;


    /**
     * SQL injection safe.
     * Native SQL queries
     * {@code @Query(value = "SELECT * FROM Products t WHERE t.name LIKE %?1%", nativeQuery = true)}
     *
     * @param name Название товара
     */
    @GetMapping("products-byName-native/{name}")
    public List<Product> findByName_JPQL_native(@PathVariable String name) {
        return serviceJPA.findByName_JPQL_native(name);
    }


    /**
     * SQL injection safe
     * Positional Parameters: the parameters is referenced by their positions in the query
     * (defined using ? followed by a number (?1, ?2, …).
     * Spring Data JPA will automatically replace the value of each parameter in the same position.
     * {@code @Query("SELECT t FROM Products t WHERE t.name LIKE %?1%")}
     *
     * @param name Название товара
     */
    @GetMapping("products-byName-posParam/{name}")
    public List<Product> findByName_JPQL_pos_param(@PathVariable String name) {
        return serviceJPA.findByName_JPQL_pos_param(name);
    }

    /**
     * SQL injection safe
     * Named Parameters. A named parameter starts with : followed by the name of the parameter
     * {@code @Query("SELECT t FROM Products t WHERE t.name LIKE %:id%")}
     *
     * @param name Название товара
     */
    @GetMapping("products-byName-nameParam/{name}")
    public List<Product> findByName_JPQL_name_param(@PathVariable String name) {
        return serviceJPA.findByName_JPQL_name_param(name);
    }

    //TODO?:
    /*https://www.baeldung.com/sql-injection
    public List<AccountDTO> unsafeJpaFindAccountsByCustomerId(String customerId) {
    String jql = "from Account where customerId = '" + customerId + "'";
    TypedQuery<Account> q = em.createQuery(jql, Account.class);
    return q.getResultList()
      .stream()
      .map(this::toAccountDTO)
      .collect(Collectors.toList());
}*/

    //TODO?:
    /*
https://cheatsheetseries.owasp.org/cheatsheets/SQL_Injection_Prevention_Cheat_Sheet.html
Hibernate Query Language (HQL) Prepared Statement (Named Parameters) Examples:


//First is an unsafe HQL Statement
Query unsafeHQLQuery = session.createQuery("from Inventory where productID='"+userSuppliedParameter+"'");
//Here is a safe version of the same query using named parameters
Query safeHQLQuery = session.createQuery("from Inventory where productID=:productid");
safeHQLQuery.setParameter("productid", userSuppliedParameter);
     */

    //https://overcoder.net/q/271300/%D0%BA%D0%B0%D0%BA-%D0%BF%D1%80%D0%B5%D0%B4%D0%BE%D1%82%D0%B2%D1%80%D0%B0%D1%82%D0%B8%D1%82%D1%8C-sql-%D0%B8%D0%BD%D1%8A%D0%B5%D0%BA%D1%86%D0%B8%D1%8E-%D1%81-%D0%BF%D0%BE%D0%BC%D0%BE%D1%89%D1%8C%D1%8E-jpa-%D0%B8-hibernate
    //https://mkyong.com/hibernate/hibernate-parameter-binding-examples/

    /*    //other crud...
    @PostMapping("/addProduct")
    public Product addProduct(@RequestBody Product product) {
        return serviceJPA.saveProduct(product);
    }

    @PostMapping("/addProducts")
    public List<Product> addProducts(@RequestBody List<Product> products) {
        return serviceJPA.saveProducts(products);
    }

    @PutMapping("/update")
    public Product updateProduct(@RequestBody Product product) {
        return serviceJPA.updateProduct(product);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable int id) {
        return serviceJPA.deleteProduct(id);
    }

    //JPQL
    @GetMapping("products-jpql")
    public ResponseEntity<List<Product>> findAllProductsWithJPQL() {
        List<Product> products = serviceJPA.findAll_JPQL();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/products_JPQL")
    public List<Product> findAllProductsWithJPQL() {
        return service.findAllWithJPQL();
    }*/

}
