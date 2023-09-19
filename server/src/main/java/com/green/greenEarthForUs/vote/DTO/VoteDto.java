package com.green.greenEarthForUs.vote.DTO;

import lombok.Builder;
import lombok.Getter;

public class VoteDto {
    @Getter
    public static class Patch{
        private String voteType;
        private long voteId;

        public void addVoteId(long voteId){this.voteId = voteId;}
    }
    @Getter
    @Builder
    public static class Response{
        private long voteId;
        private long postId;
        private long userId;
        private long voteCount;
    }



}
