package org.example.service;

import org.example.entity.UserEntity;
import org.example.repository.UserRepository;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void create_ShouldSaveUserWithCorrectData() {
        String username = "testUser";
        String password = "securePass";

        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        userService.create(username, password);
        verify(userRepository, times(1)).save(userCaptor.capture());

        UserEntity savedUser = userCaptor.getValue();
        assertNotNull(savedUser);
        assertEquals(username, savedUser.getUsername());
        assertEquals(password, savedUser.getPassword());
    }

    @Test
    void updateUser_UserExists_ShouldTriggerUpdate() {
        UserEntity existingUser = new UserEntity();
        existingUser.setId(1L);
        existingUser.setUsername("OldName");

        UserEntity updateData = new UserEntity();
        updateData.setId(1L);
        updateData.setUsername("NewName");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        userService.updateUser(updateData);
        verify(userRepository, times(1)).update(updateData);

    }

    @Test
    void updateUser_userDoesNotExists_ShouldNotTriggerUpdate() {
        UserEntity updateData = new UserEntity();
        updateData.setId(99L);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        userService.updateUser(updateData);
        verify(userRepository, never()).update(any(UserEntity.class));
    }
}
