package com.green.greenearthforus.comment.mapper;



import com.green.greenearthforus.comment.dto.CommentResponseDto;
import com.green.greenearthforus.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "post.postId", target = "postId")
    CommentResponseDto commentToResponseDto(Comment comment);

}
