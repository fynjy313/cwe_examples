package com.example.cwe.sqli.repository;


import com.example.cwe.sqli.entity.Product;
import com.example.cwe.sqli.utils.HibernateSessionFactory;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@SuppressWarnings({"all"})
public class HibernateProductRepository {
    //TODO (SQL): Spring jdbcTemplate

    /*TODO: проверить! При эксплуатации HQLi атакующий не сможет прочитать содержимое таблиц, которые отличны от таблицы Products,
        к которой привязан (mapped) класс Product. При обращении в подзапросе к таблице, которая не связана с сущностью,
        генерируется исключение HibernateQueryException и запрос дальше не обрабатывается.*/

    @Autowired
    private EntityManager em;

    //@Autowired
    private Session session = HibernateSessionFactory.getSessionFactory().openSession();


    //http://localhost:7171/products_byNameHQLunsafe/salo' or '1'='1
    //http://localhost:7171/products_byNameHQLunsafe/salo%27%20or%20%271%27=%271
    private static final String HQL_UNSAFE = "FROM Product t WHERE t.name='";

    public List<Product> findByName_HQL_unsafe(String name) {
        return (List<Product>) em.createQuery(HQL_UNSAFE + name + "'").getResultList();
        //.getSingleResult() - усложнит, но не устранит инъекцию
    }


    public List<Product> findProductByClassName_HQL_unsafe(String name) {
        return (List<Product>) em.createQuery(HQL_UNSAFE + name + "'", Product.class).getResultList();
        //.getSingleResult() - усложнит, но не устранит инъекцию
    }

    public List<Product> findByName_HQL_Session_unsafe(String name) {
        return session.createQuery(HQL_UNSAFE + name + "'").list();
       /*Transaction tx = session.beginTransaction();
            CRUD
        tx.commit();
        session.close();*/
    }

    //https://overcoder.net/q/271300/%D0%BA%D0%B0%D0%BA-%D0%BF%D1%80%D0%B5%D0%B4%D0%BE%D1%82%D0%B2%D1%80%D0%B0%D1%82%D0%B8%D1%82%D1%8C-sql-%D0%B8%D0%BD%D1%8A%D0%B5%D0%BA%D1%86%D0%B8%D1%8E-%D1%81-%D0%BF%D0%BE%D0%BC%D0%BE%D1%89%D1%8C%D1%8E-jpa-%D0%B8-hibernate
   /* Named parameters
    Positional parameters
    Cписок названных параметров в HQL
    JavaBean в HQL*/
    private static final String HQL_SAFETY = "FROM Product t WHERE t.name=?1";

    public List<Product> findByName_HQL_safety(String name) {
        return (List<Product>) em.createQuery(HQL_SAFETY).setParameter(1, name).getResultList();
    }

    private static final String HQL_SAFETY_2 = "FROM Product t WHERE t.name = :paramName";

    public List<Product> findByName_HQL_Session_safety(String name) {
        return session.createQuery(HQL_SAFETY_2).setParameter("paramName", name).list();
    }
}
