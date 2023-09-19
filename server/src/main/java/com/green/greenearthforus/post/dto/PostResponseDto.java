package com.green.greenearthforus.post.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class PostResponseDto {

    private Long postId;

    private Long userId;
    private Long voteId;
    private String type;

    private String title;

    private String body;

    private String open;

    private String imageUrl;

    private LocalDateTime createdAt;

}
