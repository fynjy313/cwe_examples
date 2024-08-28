package com.example.cwe.sqli.repository;

import com.example.cwe.sqli.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findProductsByName(String name);

    /**
     * We should write class name in the query not the table name !!! (except Native queries)
     */

//    JPQL
//    @Query("SELECT t FROM products t")
//    List<Product> findAll_JPQL();
//
//    Native Query with Positional Parameters
    @Query(value = "SELECT * FROM Products t WHERE t.name LIKE ?1", nativeQuery = true)
    List<Product> findByName_JPQL_native(String name);


    /*Positional Parameters: the parameters is referenced by their positions in the query
    (defined using ? followed by a number (?1, ?2, …).
    Spring Data JPA will automatically replace the value of each parameter in the same position.*/
    @Query("SELECT t FROM Product t WHERE t.name LIKE %?1%")
    List<Product> findByName_JPQL_pos_param(String name);

    //Named Parameters. A named parameter starts with : followed by the name of the parameter
    @Query("SELECT t FROM Product t WHERE t.name LIKE %:id%")
    List<Product> findByName_JPQL_name_param(@Param("id") String name);


}
