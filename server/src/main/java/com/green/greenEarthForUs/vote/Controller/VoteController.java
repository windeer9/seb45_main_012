package com.green.greenEarthForUs.vote.Controller;

import com.green.greenEarthForUs.vote.DTO.VoteDto;
import com.green.greenEarthForUs.vote.Entity.Vote;
import com.green.greenEarthForUs.vote.Mapper.VoteMapper;
import com.green.greenEarthForUs.vote.Service.VoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Vote/")
public class VoteController {

    private final VoteService voteService;
    private final VoteMapper mapper;

    public VoteController(VoteService voteService,
                          VoteMapper mapper){
        this.voteService = voteService;
        this.mapper = mapper;
    }

    @PostMapping("{post_id}")
    public ResponseEntity postVote(@PathVariable long postId){
        //post가 유효한지 확인하는 로직 필요
        Vote createdVote = voteService.createVote();

        return new ResponseEntity(mapper.voteToVoteResponseDto(createdVote), HttpStatus.CREATED);
    }

    @PatchMapping("{post_id}/{user_id}/{vote_id}")
    public ResponseEntity patchVote(@PathVariable("post_id") long postId,
                                    @PathVariable("user_id") long userId,
                                    @PathVariable("vote_id") long voteId,
                                    @Validated @RequestBody VoteDto.Patch patch){
        //postId가 유효한지 확인하는 로직 필요
        //userId가 투표를 했는지 확인 필요

        //이전에 좋아요를 투표했는지 확인 후 좋아요가 중복이면 count를 하나 빼는 로직.
//        long count = voteService.findVoteCount(voteId).getVoteCount();
//        String type = verifiedVoteUserId(userId, voteId);
//        if(type.equals("Like")) voteService.findVoteCount(voteId).setVoteCount(count-1);

        patch.addVoteId(voteId);
        Vote updateVote = voteService.updateVote(mapper.votePatchDToToVote(patch));
        return ResponseEntity.ok(mapper.voteToVoteResponseDto(updateVote));
    }

    @GetMapping("{post_id}/{vote_id}")
    public ResponseEntity getVote(@PathVariable("post_id") long postId,
                                  @PathVariable("vote_id") long voteId){

        //postId가 유효한지 확인하는 로직 필요
        Vote findVote = voteService.findVoteCount(voteId);

        return ResponseEntity.ok(mapper.voteToVoteResponseDto(findVote));
    }

    @DeleteMapping("{post_id}/{user_id}/{vote_id}")
    public ResponseEntity deleteVote(@PathVariable("post_id") long postId,
                                     @PathVariable("user_id") long userId,
                                     @PathVariable("vote_id") long voteId){

        //postId가 유효한지 확인하는 로직 필요
        //userId가 삭제권한이 있는지 확인필요(?? 없어도될듯?)
        voteService.deleteVote(voteId);
        return ResponseEntity.noContent().build();
    }


//     유저가 이미 좋아요를 눌렀는지 확인하는 로직
//    private String verifiedVoteUserId(long userId, long voteId){
//        User user =  유저찾는 로직;
//        List<VoteUser> voteUserList = user.getVoteUsers();
//        String voteType = "";
//        for(VoteUser voteUser : voteUserList){
//            if(voteUser.getVote().getVoteId()==voteId){
//                voteType = voteUser.getVote().getVoteType();
//            }
//        }
//        return voteType;
//    }

}
