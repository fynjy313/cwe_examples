package com.example.cwe.sqli.service;


import com.example.cwe.sqli.entity.Product;
import com.example.cwe.sqli.repository.JpaProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service annotation used to declare the class as a Service class which holds the business logic.
/*In our above Service class , we are injecting the instance of class StudentRepository using @Autowired annotation.

Spring Data JPA will automatically generate the proxy instance of the class StudentRepository and will inject it to
the instance of StudentService class.

The above methods is StudentService class are calling the JpaRepository’s methods to retrieve the Students/ delete
the Student / Create or Update the Student from the database.*/
@Service
public class JPAProductService {
    @Autowired
    private JpaProductRepository repository;

    public List<Product> getProducts() {
        return repository.findAll();
    }

    public Product getProductsById(int id) {
        return repository.findById(id).orElse(null);
    }

    public List<Product> getProductsByName(String name) {
        return repository.findProductsByName(name);
    }

    public String deleteProduct(int id) {
        repository.deleteById(id);
        return "product with id " + id + " removed";
    }

    public Product updateProduct(Product product) {
        Product existingProduct = repository.findById(product.getId()).orElse(null);
        assert existingProduct != null;
        existingProduct.setName(product.getName());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setPrice(product.getPrice());
        return repository.save(existingProduct);
    }

    //==============================================
    //JPQL
//    public List<Product> findAll_JPQL() {
//        return repository.findAll_JPQL();
//    }

    //Native JPQL
    public List<Product> findByName_JPQL_native(String name) {
        return repository.findByName_JPQL_native(name);
    }

    //Positional Parameters
    public List<Product> findByName_JPQL_pos_param(String name) {
        return repository.findByName_JPQL_pos_param(name);
    }

    //Named Parameters
    public List<Product> findByName_JPQL_name_param(String name) {
        return repository.findByName_JPQL_name_param(name);
    }

    //unsafeJpa
//    public List<Product> findByName_JPQL_unsafe(String name) {
//        String jql = "from Account where customerId = '" + name + "'";
//        TypedQuery<Product> q = em.createQuery(jql, Account.class);
//        return q.getResultList()
//                .stream()
//                .map(this::toAccountDTO)
//                .collect(Collectors.toList());}

    //other crud
    public Product saveProduct(Product product) {
        return repository.save(product);
    }

    public List<Product> saveProducts(List<Product> products) {
        return repository.saveAll(products);
    }
}
