package com.myblog.mapper;

import com.myblog.dto.CommentDTO;
import com.myblog.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "user.id", target = "userId")
    CommentDTO toDTO(Comment comment);

    @Mapping(source = "postId", target = "post.id")
    @Mapping(source = "userId", target = "user.id")
    Comment toEntity(CommentDTO commentDTO);

    List<CommentDTO> toDTOList(List<Comment> comments);
}
