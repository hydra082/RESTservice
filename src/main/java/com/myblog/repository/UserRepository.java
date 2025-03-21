package com.myblog.repository;

import com.myblog.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(int id);
    List<User> findAll();
    void save(User user);
    void update(User user);
    void delete(int id);
}