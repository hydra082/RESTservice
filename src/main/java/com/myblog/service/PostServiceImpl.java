package com.myblog.service;

import com.myblog.dto.PostDTO;
import com.myblog.entity.Post;
import com.myblog.mapper.PostMapper;
import com.myblog.repository.PostRepository;
import com.myblog.repository.PostRepositoryImpl;

import java.util.List;

public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    @Override
    public PostDTO findById(int id) {
        return postRepository.findById(id)
                .map(PostMapper.INSTANCE::toDTO)
                .orElse(null);
    }

    @Override
    public List<PostDTO> findAll() {
        List<Post> posts = postRepository.findAll();
        return PostMapper.INSTANCE.toDTOList(posts);
    }

    @Override
    public List<PostDTO> findByUserId(int userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        return PostMapper.INSTANCE.toDTOList(posts);
    }


    @Override
    public void save(PostDTO postDTO) {
        Post post = PostMapper.INSTANCE.toEntity(postDTO);
        postRepository.save(post);
    }

    @Override
    public void update(PostDTO postDTO) {
        Post post = PostMapper.INSTANCE.toEntity(postDTO);
        postRepository.update(post);
    }

    @Override
    public void delete(int id) {
        postRepository.delete(id);
    }
}