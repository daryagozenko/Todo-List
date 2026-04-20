package com.gozenko.TodoList.services;

import com.gozenko.TodoList.models.User;
import com.gozenko.TodoList.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1,"Dasha","Gozenko",new ArrayList<>());
    }

    @Test
    void createUser_Success(){
        when(userRepository.save(any(User.class))).thenReturn(user);
        User resUser = userService.createUser(user);

        assertEquals(user.getId(), resUser.getId());
    }

    @Test
    void findUser_IllegalId(){
        assertThrows(IllegalArgumentException.class,
                () -> userService.findById(-5));
        verify(userRepository, never()).findById(any(Integer.class));
    }

    @Test
    void findUser_Success(){
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(user));
        User resUser = userService.findById(1);

        assertNotNull(resUser);
    }

    @Test
    void findUser_EntityNotFound(){
        when(userRepository.findById(any(Integer.class))).thenThrow(new EntityNotFoundException());

        assertThrows(EntityNotFoundException.class,
                () -> userService.findById(2));
        verify(userRepository).findById(any(Integer.class));
    }
}
