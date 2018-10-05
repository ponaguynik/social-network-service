package com.testtask.socialnetworkservice.service.impl;

import com.testtask.socialnetworkservice.dto.CommentDto;
import com.testtask.socialnetworkservice.exception.ResourceNotFoundException;
import com.testtask.socialnetworkservice.model.Comment;
import com.testtask.socialnetworkservice.model.Post;
import com.testtask.socialnetworkservice.model.User;
import com.testtask.socialnetworkservice.repository.CommentRepository;
import com.testtask.socialnetworkservice.repository.PostRepository;
import com.testtask.socialnetworkservice.repository.UserRepository;
import com.testtask.socialnetworkservice.service.CommentService;
import com.testtask.socialnetworkservice.service.ExternalDataLoadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ExternalDataLoadService<CommentDto> loadService;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,
                              ExternalDataLoadService<CommentDto> loadService,
                              PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.loadService = loadService;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public List<CommentDto> loadCommentsByUrl(String url) {
        List<CommentDto> commentDtos = loadService.loadFromUrl(url, CommentDto[].class);
        // TODO: 03-Oct-18 Do some validation
        List<Comment> comments = commentDtos.stream()
                .map(CommentDto::toComment)
                .map(this::findAndSetPost)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return commentRepository.saveAll(comments).stream()
                .map(CommentDto::fromComment)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findAllByUserId(Long userId) {
        return commentRepository.findAllByUserIdJoinEmail(userId).stream()
                .map(CommentDto::fromComment)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public long countWordInBodyOfAllComments(String word) {
        // do counting accordingly to the task requirement
        return commentRepository.findAll().stream()
                .map(Comment::getBody)
                .filter(body -> !StringUtils.isEmpty(body))
                .filter(body -> body.contains(word))
                .count();
    }

    @Transactional(readOnly = true)
    @Override
    public User getCommentUser(Long commentId) {
        return userRepository.findUserByCommentIdJoinEmail(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Author of comment with commentId " + commentId + " not found"));
    }

    private Comment findAndSetPost(Comment comment) {
        try {
            Post post = postRepository.findById(comment.getPost().getId()).orElseThrow(ResourceNotFoundException::new);
            comment.setPost(post);
            return comment;
        } catch (ResourceNotFoundException e) {
            log.warn("Post by id {} for comment with id {} not found", comment.getPost().getId(), comment.getId());
            return null;
        }
    }
}
