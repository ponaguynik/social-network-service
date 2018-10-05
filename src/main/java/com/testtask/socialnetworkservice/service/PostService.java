package com.testtask.socialnetworkservice.service;

import com.testtask.socialnetworkservice.dto.PostDto;

import java.util.List;

public interface PostService {

    List<PostDto> loadPostsByUrl(String url);

    void deleteById(Long id);

    List<PostDto> findAllByTitle(String title);
}
