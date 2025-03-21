package com.myblog.mapper;

import com.myblog.dto.PostDTO;
import com.myblog.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(source = "user.id", target = "userId")
    PostDTO toDTO(Post post);

    @Mapping(source = "userId", target = "user.id")
    Post toEntity(PostDTO postDTO);

    List<PostDTO> toDTOList(List<Post> posts);
}