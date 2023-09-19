package com.green.greenEarthForUs.vote.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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

// 관계매핑용
//    @OneToOne
//    private Post post;
//
//    @ManyToOne
//    private VoteUser voteUser;

}
