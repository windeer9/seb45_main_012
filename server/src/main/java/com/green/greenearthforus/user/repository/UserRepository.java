package com.green.greenearthforus.user.repository;

import com.green.greenearthforus.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);

    @Query("SELECT u FROM User u GROUP BY u.userId HAVING COUNT(u.userId) > 1")
    List<User> findDuplicateUsers();

    Optional<User> findByUserUseId(String userUseId);

}