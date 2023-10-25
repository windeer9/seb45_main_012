package com.green.greenearthforus.vote.entity;

import com.green.greenearthforus.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class VoteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long voteUserId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "vote_id")
    private Vote vote;

    @Column
    private Boolean isLike;

}
