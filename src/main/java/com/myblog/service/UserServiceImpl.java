package com.myblog.service;

import com.myblog.dto.UserDTO;
import com.myblog.entity.User;
import com.myblog.mapper.UserMapper;
import com.myblog.repository.UserRepository;
import com.myblog.repository.UserRepositoryImpl;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO findById(int id) {
        return userRepository.findById(id)
                .map(UserMapper.INSTANCE::toDTO)
                .orElse(null);
    }

    @Override
    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();
        return UserMapper.INSTANCE.toDTOList(users);
    }

    @Override
    public void save(UserDTO userDTO) {
        User user = UserMapper.INSTANCE.toEntity(userDTO);
        userRepository.save(user);
    }

    @Override
    public void update(UserDTO userDTO) {
        User user = UserMapper.INSTANCE.toEntity(userDTO);
        userRepository.update(user);
    }

    @Override
    public void delete(int id) {
        userRepository.delete(id);
    }
}