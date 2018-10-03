package com.testtask.socialnetworkservice.service;

import com.testtask.socialnetworkservice.dto.CommentDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> findAllByUserId(Long userId);

    List<CommentDto> loadCommentsByUrl(String url);

    long countWordInAllComments(String word);
}
