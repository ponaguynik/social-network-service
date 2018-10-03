package com.testtask.socialnetworkservice.service;

import com.testtask.socialnetworkservice.dto.PostDto;
import com.testtask.socialnetworkservice.model.Post;
import com.testtask.socialnetworkservice.model.User;

import java.util.List;

public interface PostService {

    List<PostDto> loadPostsByUrl(String url);

    Post findById(Long id);

    User findPostAuthor(Long id);

    void deleteById(Long id);

    List<PostDto> findAllByTitle(String title);
}
