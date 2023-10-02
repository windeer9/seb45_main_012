package com.green.greenearthforus.vote.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class VoteDto {
    @Getter
    @Setter
    public static class Patch{
        private String voteType;
        private long voteId;

        public void addVoteId(long voteId){this.voteId = voteId;}
    }
    @Getter
    @Setter
    @Builder
    public static class Response{
        private long voteId;
        private long postId;
        private long userId;
        private long voteCount;
    }



}
