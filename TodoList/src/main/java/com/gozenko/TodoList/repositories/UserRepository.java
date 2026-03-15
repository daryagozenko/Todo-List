package com.gozenko.TodoList.repositories;

import com.gozenko.TodoList.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer>{

}
