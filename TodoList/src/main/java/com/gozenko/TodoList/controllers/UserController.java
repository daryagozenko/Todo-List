package com.gozenko.TodoList.controllers;

import com.gozenko.TodoList.DTO.UserResponse;
import com.gozenko.TodoList.models.User;
import com.gozenko.TodoList.services.UserService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody User user){
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponse(createdUser));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getUserById(@RequestParam Integer id){
        try{
            User createdUser = userService.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(new UserResponse(createdUser));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
