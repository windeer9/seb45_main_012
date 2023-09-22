package com.green.greenearthforus.post.entity;

import com.green.greenearthforus.calendar.entity.Calendar;
import com.green.greenearthforus.comment.entity.Comment;
import com.green.greenearthforus.user.entity.User;
import com.green.greenearthforus.vote.entity.Vote;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(length = 1000)
    private String body;

    @Column
    private Boolean open;

    @Column(length = 500)
    private String imageUrl;

    @Column
    private String type; // type 게시글의 유형

    @Column
    private String title;

    @Column
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Vote vote;

    @ManyToOne
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
}
