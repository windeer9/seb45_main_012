package com.green.greenearthforus.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class PostPatchDto {

    private String type;

    private String title;

    private String imageUrls;

    private String body;

    private Boolean open;

}
