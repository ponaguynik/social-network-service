package com.testtask.socialnetworkservice.service;

import com.testtask.socialnetworkservice.dto.CommentDto;
import com.testtask.socialnetworkservice.model.User;

import java.util.List;

public interface CommentService {

    List<CommentDto> loadCommentsByUrl(String url);

    List<CommentDto> findAllByUserId(Long userId);

    long countWordInBodyOfAllComments(String word);

    User getCommentUser(Long commentId);
}
