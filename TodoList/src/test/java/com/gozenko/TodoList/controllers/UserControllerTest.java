package com.gozenko.TodoList.controllers;

import com.gozenko.TodoList.models.User;
import com.gozenko.TodoList.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1, "Dasha", "Gozenko", new ArrayList<>());
    }

    @Test
    void createUser_Success(){
        when(userService.createUser(any(User.class))).thenReturn(user);

        ResponseEntity<?> response = userController.createUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getUserById_Success(){
        when(userService.findById(any(Integer.class))).thenReturn(user);

        ResponseEntity<?> response = userController.getUserById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getUserById_Exception(){
        when(userService.findById(any(Integer.class))).thenThrow(new IllegalArgumentException("Invalid id"));

        ResponseEntity<?> response = userController.getUserById(-5);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}