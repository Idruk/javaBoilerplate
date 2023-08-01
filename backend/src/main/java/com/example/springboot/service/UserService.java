package com.example.springboot.service;

import com.example.springboot.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAllUser();

    Optional<User> findById(long id);

    Optional<User> findUsername(String username);

    User saveUser(User user);

    User updateUser(User user);

    void deleteUser(long id);
}
