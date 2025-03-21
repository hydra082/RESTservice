package com.myblog.repository;

import com.myblog.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Optional<Post> findById(int id);
    List<Post> findAll();
    List<Post> findByUserId(int userId);
    void save(Post post);
    void update(Post post);
    void delete(int id);
}