package com.green.greenearthforus.vote.controller;

import com.green.greenearthforus.post.service.PostService;
import com.green.greenearthforus.user.entity.User;
import com.green.greenearthforus.user.repository.UserRepository;
import com.green.greenearthforus.user.service.UserService;
import com.green.greenearthforus.vote.dto.VoteDto;
import com.green.greenearthforus.vote.entity.Vote;
import com.green.greenearthforus.vote.entity.VoteUser;
import com.green.greenearthforus.vote.mapper.VoteMapper;
import com.green.greenearthforus.vote.repository.VoteRepository;
import com.green.greenearthforus.vote.service.VoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/vote")
public class VoteController {

    private final VoteService voteService;
    private final VoteMapper mapper;
    private final UserService userService;
    private final PostService postService;
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;


    public VoteController(VoteService voteService,
                          VoteMapper mapper,
                          PostService postService,
                          UserService userService,
                          VoteRepository repository,
                          UserRepository userRepository){
        this.voteService = voteService;
        this.mapper = mapper;
        this.postService = postService;
        this.userService = userService;
        this.voteRepository = repository;
        this.userRepository = userRepository;
    }

    @PostMapping("/{post_id}")
    public ResponseEntity<VoteDto.Response> postVote(@PathVariable("post_id") long postId){

        return new ResponseEntity<>(voteService.createVote(postId), HttpStatus.CREATED);
    }


    @PatchMapping("/{post_id}/{user_id}/{vote_id}")
    public ResponseEntity<VoteDto.Response> patchVote(@PathVariable("post_id") long postId,
                                    @PathVariable("user_id") long userId,
                                    @PathVariable("vote_id") long voteId
                                    ){
        postService.getPost(postId);   //post가 유효한지 확인하는 로직

        VoteDto.Response response = voteService.updateVote(userId, voteId);
        response.setUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{post_id}/{vote_id}")
    public ResponseEntity<VoteDto.Response> getVote(@PathVariable("post_id") long postId,
                                  @PathVariable("vote_id") long voteId){

        postService.getPost(postId);   //post가 유효한지 확인하는 로직
        Vote findVote = voteService.findVoteCount(voteId);
        VoteDto.Response response = mapper.voteToVoteResponseDto(findVote);
        response.setPostId(postId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{post_id}/{user_id}/{vote_id}")
    public ResponseEntity<Void> deleteVote(@PathVariable("post_id") long postId,
                                     @PathVariable("user_id") long userId,
                                     @PathVariable("vote_id") long voteId){

        postService.getPost(postId);   //post가 유효한지 확인하는 로직
        userService.getUser(userId);

        voteService.deleteVote(voteId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAll(){

        voteRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }





}
