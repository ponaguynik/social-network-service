package com.testtask.socialnetworkservice.service.impl;

import com.testtask.socialnetworkservice.dto.PostDto;
import com.testtask.socialnetworkservice.exception.ResourceNotFoundException;
import com.testtask.socialnetworkservice.model.Post;
import com.testtask.socialnetworkservice.model.User;
import com.testtask.socialnetworkservice.repository.PostRepository;
import com.testtask.socialnetworkservice.repository.UserRepository;
import com.testtask.socialnetworkservice.service.ExternalDataLoadService;
import com.testtask.socialnetworkservice.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final ExternalDataLoadService<PostDto> loadService;
    private final UserRepository userRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository,
                           ExternalDataLoadService<PostDto> loadService,
                           UserRepository userRepository) {
        this.postRepository = postRepository;
        this.loadService = loadService;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public List<PostDto> loadPostsByUrl(String url) {
        List<PostDto> postDtos = loadService.loadFromUrl(url, PostDto[].class);
        // TODO: 03-Oct-18 Do some validation
        List<Post> posts = postDtos.stream()
                .map(PostDto::toPost)
                .map(this::findAndSetUser)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return postRepository.saveAll(posts).stream()
                .map(PostDto::fromPost)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<PostDto> findAllByTitle(String title) {
        return postRepository.findAllByTitle(title).stream().map(PostDto::fromPost).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        try {
            postRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Post by id " + id + " not found!");
        }
    }

    private Post findAndSetUser(Post post) {
        try {
            User user = userRepository.findById(post.getUser().getId()).orElseThrow(ResourceNotFoundException::new);
            post.setUser(user);
            return post;
        } catch (ResourceNotFoundException e) {
            log.warn("User by id {} for post with id {} not found", post.getUser().getId(), post.getId());
            return null;
        }
    }
}
