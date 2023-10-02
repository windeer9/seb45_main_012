package com.green.greenearthforus.vote.entity;

import com.green.greenearthforus.post.entity.Post;
import com.green.greenearthforus.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long voteId;
    @Column
    private long voteCount;
    @Column
    private String voteType;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL)
    private List<VoteUser> voteUsers;

}
