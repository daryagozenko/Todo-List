package com.gozenko.TodoList.services;

import com.gozenko.TodoList.models.User;
import com.gozenko.TodoList.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user){
        return userRepository.save(user);
    }

    public User findById(Integer id){
        if(id <= 0) throw new IllegalArgumentException("ID должно быть > 0");
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }


}
