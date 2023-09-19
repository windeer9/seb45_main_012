package com.green.greenEarthForUs.vote.Service;

import com.green.greenEarthForUs.Exception.BusinessLogicException;
import com.green.greenEarthForUs.Exception.ExceptionCode;
import com.green.greenEarthForUs.vote.Entity.Vote;
import com.green.greenEarthForUs.vote.Repository.VoteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VoteService {

    private final VoteRepository voteRepository;

    public VoteService(VoteRepository voteRepository){
        this.voteRepository = voteRepository;
    }

    public Vote createVote(){
        Vote vote = new Vote();
        return voteRepository.save(vote);
    }

    public Vote updateVote(Vote vote){
        Vote findVote = findVerifiedVote(vote.getVoteId());
        long count = findVote.getVoteCount();
        Optional.ofNullable(vote.getVoteType())
                .ifPresent(voteType -> findVote.setVoteType(voteType));
        if(vote.getVoteType().equals("Like")) findVote.setVoteCount(count+1);
        if(vote.getVoteType().equals("DisLike")) findVote.setVoteCount(count-1);
        // 주어진 요청에 좋아요에 변화가 있으면 voteCount를 변경하고 저장하는 로직
        return voteRepository.save(findVote);
    }

    public Vote findVoteCount(long voteId){

        return voteRepository.findById(voteId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.VOTE_NOT_FOUND));
        //voteCount를 저장소에서 불러오는 요청에 대한 응답.
    }

    public void deleteVote(long voteId){
        Vote findVote = findVerifiedVote(voteId);
        voteRepository.delete(findVote);
    }

    public Vote findVerifiedVote(long voteId){
        Optional<Vote> optionalVote =
                voteRepository.findById(voteId);
        Vote findvote =
                optionalVote.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.VOTE_NOT_FOUND));
        return findvote;
    }
}
