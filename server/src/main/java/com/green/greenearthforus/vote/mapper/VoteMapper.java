package com.green.greenearthforus.vote.mapper;

import com.green.greenearthforus.vote.dto.VoteDto;
import com.green.greenearthforus.vote.entity.Vote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VoteMapper {
    Vote votePatchDToToVote(VoteDto.Patch requestBody);

    @Mapping(source = "post.postId", target = "postId")
    VoteDto.Response voteToVoteResponseDto(Vote vote);

}
