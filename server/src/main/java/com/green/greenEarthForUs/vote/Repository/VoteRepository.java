package com.green.greenEarthForUs.vote.Repository;

import com.green.greenEarthForUs.vote.Entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}
