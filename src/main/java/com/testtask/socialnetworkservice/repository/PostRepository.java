package com.testtask.socialnetworkservice.repository;

import com.testtask.socialnetworkservice.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByTitle(String title);
}
