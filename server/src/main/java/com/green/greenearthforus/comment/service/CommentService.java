package com.green.greenearthforus.comment.service;


import com.green.greenearthforus.exception.BusinessLogicException;
import com.green.greenearthforus.exception.ExceptionCode;
import com.green.greenearthforus.comment.dto.CommentDto;
import com.green.greenearthforus.comment.dto.CommentResponseDto;
import com.green.greenearthforus.comment.entity.Comment;
import com.green.greenearthforus.comment.mapper.CommentMapper;
import com.green.greenearthforus.comment.repository.CommentRepository;
import com.green.greenearthforus.post.entity.Post;
import com.green.greenearthforus.post.repository.PostRepository;
import com.green.greenearthforus.user.entity.User;
import com.green.greenearthforus.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final CommentMapper mapper;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository, CommentMapper mapper) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    // 댓글 생성
    public CommentResponseDto createComment(Long postId, Long userId, CommentDto commentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("User not found {0}", userId)));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found " + postId)); // user , post

        Comment comment = new Comment(); // 초기화
        comment.setUser(user);
        comment.setPost(post);
        comment.setBody(commentDto.getBody());
        comment.setCreatedAt(LocalDateTime.now());

        Comment save = commentRepository.save(comment);

        return mapper.commentToResponseDto(save);
    }

    private static final String USER_NOT_FOUND = "User not found";
    private static final String POST_NOT_FOUND = "Post not found";


    public List<CommentResponseDto> getCommentsByPostIdAndVerify(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found " + postId)); // user , post

        List<Comment> commentList = post.getComments();
        if(commentList.isEmpty()) throw new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND);

       return commentList.stream().map(comment -> {
           verifyComment(comment.getCommentId());
           return mapper.commentToResponseDto(comment);
               })
                .collect(Collectors.toList());
    }

    // 댓글 수정
    @Transactional
    public Comment updateComment(Long postId, Long userId, Long commentId, CommentDto commentDto) {
         userRepository.findById(userId)
                 .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND + userId));

         postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND + postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException("Comment not found" + commentId));

        if (!comment.getUser().getUserId().equals(userId)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_COMMENT_ACCESS); //
        }

        comment.setBody(commentDto.getBody());
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
       
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long postId, Long userId, Long commentId) {

        Comment comment = verifyComment(commentId); // 해당 댓글을 DB에서 조회하고 comment 변수에 저장함
        if (!comment.getPost().getPostId().equals(postId) || !comment.getUser().getUserId().equals(userId)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_COMMENT_ACCESS); // 댓글이 속한 게시글의 사용자와 user를 비교해서 같지 게시글 사용자에 속하지 않으면 엑세스 권한 없음
        }

        commentRepository.deleteById(commentId);
    }

    public void deleteAll(){
        commentRepository.deleteAll();
    }

    private Comment verifyComment(Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        return commentOptional.orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));
    }

}

