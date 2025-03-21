package com.myblog.service;

import com.myblog.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    CommentDTO findById(int id);
    List<CommentDTO> findAll();
    List<CommentDTO> findByPostId(int postId);
    void save(CommentDTO commentDTO);
    void update(CommentDTO commentDTO);
    void delete(int id);
}