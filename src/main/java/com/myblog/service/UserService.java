package com.myblog.service;

import com.myblog.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO findById(int id);
    List<UserDTO> findAll();
    void save(UserDTO userDTO);
    void update(UserDTO userDTO);
    void delete(int id);
}