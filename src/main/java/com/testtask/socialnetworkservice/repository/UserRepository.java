package com.testtask.socialnetworkservice.repository;

import com.testtask.socialnetworkservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select user from Post post join post.user user where post.id = ?1")
    Optional<User> findByPostId(Long postId);
}
