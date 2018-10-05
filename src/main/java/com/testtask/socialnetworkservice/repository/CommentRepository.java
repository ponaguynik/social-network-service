package com.testtask.socialnetworkservice.repository;

import com.testtask.socialnetworkservice.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select comment from Comment comment join User user on user.email=comment.email where user.id=?1")
    List<Comment> findAllByUserIdJoinEmail(Long userId);
}
