package org.example.repository;

import org.example.entity.UserEntity;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryImplTest extends BaseDaoTest {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {

        userRepository = new UserRepositoryImpl(sessionFactory);

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            //Чистим таблицу перед каждым тестом, чтобы они не влияли друг на друга (Изоляция)
            session.createNativeQuery("TRUNCATE TABLE users CASCADE").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void save_ShouldPersistUserInDockerPostgres() {

        UserEntity user = new UserEntity();
        user.setUsername("DockerUser");
        user.setPassword("pass123");

        userRepository.save(user);

        assertNotNull(user.getId(), "База данных должна была сгенерировать ID для пользователя");

        Optional<UserEntity> fetchedUser = userRepository.findById(user.getId());
        assertTrue(fetchedUser.isPresent());
        assertEquals("DockerUser", fetchedUser.get().getUsername());
    }

    @Test
    void findAll_ShouldReturnAllSavedUsers() {
        UserEntity user1 = new UserEntity();
        user1.setUsername("User1");

        UserEntity user2 = new UserEntity();
        user2.setUsername("User2");

        userRepository.save(user1);
        userRepository.save(user2);

        List<UserEntity> users = userRepository.findAll();

        assertEquals(2, users.size());
    }
}
