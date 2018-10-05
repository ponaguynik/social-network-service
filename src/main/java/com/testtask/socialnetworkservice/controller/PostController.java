package com.testtask.socialnetworkservice.controller;

import com.testtask.socialnetworkservice.dto.PostDto;
import com.testtask.socialnetworkservice.dto.RequestUrl;
import com.testtask.socialnetworkservice.model.User;
import com.testtask.socialnetworkservice.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@Slf4j
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(path = "/{postId}/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public User getPostAuthor(@PathVariable Long postId) {
        return postService.findPostAuthor(postId);
    }

    @GetMapping(params = "title", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<PostDto> findAllWithTitle(@RequestParam String title) {
        return postService.findAllByTitle(title);
    }

    @DeleteMapping(path = "/{postId}")
    public void deleteWithComments(@PathVariable Long postId) {
        postService.deleteById(postId);
    }

    @PostMapping(path = "/load",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<PostDto> load(@RequestBody RequestUrl requestUrl) {
        List<PostDto> postDtos = postService.loadPostsByUrl(requestUrl.getUrl());
        log.info("Loaded posts by URL '{}'", requestUrl);
        return postDtos;
    }
}
