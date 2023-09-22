package com.green.greenearthforus.post.repository;

import com.green.greenearthforus.post.entity.Post;
import com.green.greenearthforus.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByType(String typeName);

    List<Post> findByUser(User user);


}
