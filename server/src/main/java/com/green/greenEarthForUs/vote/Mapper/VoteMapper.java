package com.green.greenEarthForUs.vote.Mapper;

import com.green.greenEarthForUs.vote.DTO.VoteDto;
import com.green.greenEarthForUs.vote.Entity.Vote;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VoteMapper {
    Vote votePatchDToToVote(VoteDto.Patch requestBody);
    VoteDto.Response voteToVoteResponseDto(Vote vote);

}
