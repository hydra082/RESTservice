package com.myblog.service;

import com.myblog.dto.PostDTO;

import java.util.List;

public interface PostService {
    PostDTO findById(int id);
    List<PostDTO> findAll();
    List<PostDTO> findByUserId(int userId);
    void save(PostDTO postDTO);
    void update(PostDTO postDTO);
    void delete(int id);
}