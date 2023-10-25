package com.green.greenearthforus.comment.dto;


import com.green.greenearthforus.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {

    private Long commentId;

    private Long userId;

    private Long postId;

    private String body;

    private LocalDateTime createdAt;

//    public CommentResponseDto(Comment comment) {
//        this.commentId = comment.getCommentId();
//        this.userId = comment.getUser().getUserId();
//        this.body = comment.getBody();
//        this.createdAt = comment.getCreatedAt();
//        this.postId = comment.getPost().getPostId();
//    }

}


