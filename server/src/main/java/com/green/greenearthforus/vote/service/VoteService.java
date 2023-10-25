package com.green.greenearthforus.vote.service;

import com.green.greenearthforus.exception.BusinessLogicException;
import com.green.greenearthforus.exception.ExceptionCode;
import com.green.greenearthforus.post.entity.Post;
import com.green.greenearthforus.post.repository.PostRepository;
import com.green.greenearthforus.user.entity.User;
import com.green.greenearthforus.user.repository.UserRepository;
import com.green.greenearthforus.user.service.UserService;
import com.green.greenearthforus.vote.dto.VoteDto;
import com.green.greenearthforus.vote.entity.Vote;
import com.green.greenearthforus.vote.entity.VoteUser;
import com.green.greenearthforus.vote.mapper.VoteMapper;
import com.green.greenearthforus.vote.repository.VoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final VoteMapper mapper;
    private final UserService userService;
    private final UserRepository userRepository;

    public VoteService(VoteRepository voteRepository,
                       PostRepository postRepository,
                       VoteMapper mapper,
                       UserRepository userRepository,
                       UserService userService) {
        this.voteRepository = voteRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public VoteDto.Response createVote(long postId) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.VOTE_NOT_FOUND));//post가 유효한지 확인하는 로직

        if (findPost.getVote() != null) throw new BusinessLogicException(ExceptionCode.VOTE_EXISTS);

        Vote vote = new Vote();
        vote.setPost(findPost);

        return mapper.voteToVoteResponseDto(voteRepository.save(vote));
    }

    public VoteDto.Response updateVote(long userId, long voteId) {

        VoteDto.Response response;
        User user = userService.getUser(userId);
        Vote findVote = findVerifiedVote(voteId);
        long count = findVote.getVoteCount();
        Optional<VoteUser> findVoteUser;

        if (user.getVoteUsers() != null && findVote.getVoteUsers() != null) {
            findVoteUser = user.getVoteUsers().stream()
                    .filter(voteUser -> voteUser.getVote().getVoteId() == findVote.getVoteId())
                    .findFirst();
                if(findVoteUser.isPresent()) {
                    if (findVoteUser.get().getIsLike() == null || !(findVoteUser.get().getIsLike())) {
                        findVoteUser.get().setIsLike(true);
                        findVote.getVoteUsers().add(findVoteUser.get());
                        user.getVoteUsers().add(findVoteUser.get());
                        findVote.setVoteCount(count + 1);
                    } else {
                        findVoteUser.get().setIsLike(false);
                        findVote.getVoteUsers().add(findVoteUser.get());
                        user.getVoteUsers().add(findVoteUser.get());
                        findVote.setVoteCount(count - 1);
                    }
                }else{
                    VoteUser voteUser = new VoteUser();
                    voteUser.setUser(user);
                    voteUser.setVote(findVote);
                    voteUser.setIsLike(true);
                    findVote.getVoteUsers().add(voteUser);
                    user.getVoteUsers().add(voteUser);
                    findVote.setVoteCount(count + 1);
                }
        } else {
            VoteUser voteUser = new VoteUser();
            voteUser.setUser(user);
            voteUser.setVote(findVote);
            voteUser.setIsLike(true);
            findVote.getVoteUsers().add(voteUser);
            user.getVoteUsers().add(voteUser);
            findVote.setVoteCount(count + 1);
        }
        userRepository.save(user);
        response = mapper.voteToVoteResponseDto(voteRepository.save(findVote));
        return response;
    }

    public Vote findVoteCount(long voteId){

        return findVerifiedVote(voteId);
    }

    public void deleteVote(long voteId){
        Vote findVote = findVerifiedVote(voteId);
        voteRepository.delete(findVote);
    }

    public Vote findVerifiedVote(long voteId){
        Optional<Vote> optionalVote =
                voteRepository.findById(voteId);
        return optionalVote.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.VOTE_NOT_FOUND));
    }

}
