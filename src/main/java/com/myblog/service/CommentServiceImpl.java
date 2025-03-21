package com.myblog.service;

import com.myblog.dto.CommentDTO;
import com.myblog.entity.Comment;
import com.myblog.mapper.CommentMapper;
import com.myblog.repository.CommentRepository;
import java.util.List;

public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public CommentDTO findById(int id) {
        return commentRepository.findById(id)
                .map(CommentMapper.INSTANCE::toDTO)
                .orElse(null);
    }

    @Override
    public List<CommentDTO> findAll() {
        List<Comment> comments = commentRepository.findAll();
        return CommentMapper.INSTANCE.toDTOList(comments);
    }

    @Override
    public List<CommentDTO> findByPostId(int postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return CommentMapper.INSTANCE.toDTOList(comments);
    }

    @Override
    public void save(CommentDTO commentDTO) {
        Comment comment = CommentMapper.INSTANCE.toEntity(commentDTO);
        commentRepository.save(comment);
    }

    @Override
    public void update(CommentDTO commentDTO) {
        Comment comment = CommentMapper.INSTANCE.toEntity(commentDTO);
        commentRepository.update(comment);
    }

    @Override
    public void delete(int id) {
        commentRepository.delete(id);
    }
}