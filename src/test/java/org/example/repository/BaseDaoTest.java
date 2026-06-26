package org.example.repository;

import org.example.entity.BasketItemEntity;
import org.example.entity.ProductEntity;
import org.example.entity.UserEntity;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.hibernate.cfg.Configuration;


@Testcontainers
public class BaseDaoTest {
    @Container
    protected static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("test_shopping_db")
            .withUsername("test_user")
            .withPassword("test_pass");

    protected static SessionFactory sessionFactory;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(UserEntity.class);
        configuration.addAnnotatedClass(ProductEntity.class);
        configuration.addAnnotatedClass(BasketItemEntity.class);

        configuration.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", postgres.getUsername());
        configuration.setProperty("hibernate.connection.password", postgres.getPassword());
        configuration.setProperty("hibernate.connection.driver_class", postgres.getDriverClassName());

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        // Сама создаст таблицы при старте тестов
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.format_sql", "true");

        sessionFactory = configuration.buildSessionFactory();
    }

    @AfterAll
    static void afterAll() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
        postgres.stop(); // Гасим контейнер после всех тестов
    }

}
