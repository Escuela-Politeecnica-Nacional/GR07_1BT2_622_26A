package ec.edu.epn.ma;

import ec.edu.epn.ma.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTest {

    @Test
    void shouldPersistAndReadProducts() {

        try (EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ma-jpa-demo-test")) {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            try {
                entityManager.getTransaction().begin();
                entityManager.persist(new Product("Arroz", 10, new BigDecimal("2.50")));
                entityManager.getTransaction().commit();
            } finally {
                if (entityManager.isOpen()) {
                    entityManager.close();
                }
            }

            try (EntityManager verificationManager = entityManagerFactory.createEntityManager()) {
                long count = verificationManager
                        .createQuery("select count(p) from Product p", Long.class)
                        .getSingleResult();
                assertEquals(1L, count);
            }
        }
    }
}

