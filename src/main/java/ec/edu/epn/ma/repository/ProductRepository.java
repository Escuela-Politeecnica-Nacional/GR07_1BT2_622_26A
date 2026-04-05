package ec.edu.epn.ma.repository;

import ec.edu.epn.ma.model.Product;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class ProductRepository {

    public List<Product> findAll(EntityManager entityManager) {
        return entityManager
                .createQuery("select p from Product p order by p.id", Product.class)
                .getResultList();
    }

    public Optional<Product> findById(EntityManager entityManager, Long id) {
        return Optional.ofNullable(entityManager.find(Product.class, id));
    }

    public void save(EntityManager entityManager, Product product) {
        entityManager.persist(product);
    }

    public Product update(EntityManager entityManager, Product product) {
        return entityManager.merge(product);
    }

    public void delete(EntityManager entityManager, Product product) {
        entityManager.remove(product);
    }
}

