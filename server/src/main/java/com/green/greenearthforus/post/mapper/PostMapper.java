package com.green.greenearthforus.post.mapper;

import com.green.greenearthforus.post.dto.PostPostDto;
import com.green.greenearthforus.post.dto.PostResponseDto;
import com.green.greenearthforus.post.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post postResponseDtoToPost(PostResponseDto responseDto);
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "vote.voteId", target = "voteId")
    PostResponseDto postToPostResponseDto(Post post);
    Post postPostDtoToPost(PostPostDto postPostDto);
}




