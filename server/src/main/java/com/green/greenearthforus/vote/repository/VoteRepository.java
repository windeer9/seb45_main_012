package com.green.greenearthforus.vote.repository;

import com.green.greenearthforus.vote.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}
