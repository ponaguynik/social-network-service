package com.testtask.socialnetworkservice.repository;

import com.testtask.socialnetworkservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select user from User user join Comment comment on comment.email=user.email where comment.id=?1")
    Optional<User> findUserByCommentIdJoinEmail(Long commentId);
}
