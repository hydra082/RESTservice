package com.myblog.repository;

import com.myblog.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Optional<Comment> findById(int id);
    List<Comment> findAll();
    List<Comment> findByPostId(int postId);
    void save(Comment comment);
    void update(Comment comment);
    void delete(int id);
}