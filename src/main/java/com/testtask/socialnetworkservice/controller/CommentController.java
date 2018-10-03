package com.testtask.socialnetworkservice.controller;

import com.testtask.socialnetworkservice.dto.CommentDto;
import com.testtask.socialnetworkservice.dto.RequestUrl;
import com.testtask.socialnetworkservice.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping(params = "userId")
    public List<CommentDto> findAllByUserId(@RequestParam Long userId) {
        return commentService.findAllByUserId(userId);
    }

    @PostMapping(path = "/count", params = "word")
    public Count countWordInAllComments(@RequestParam String word) {
        return Count.of(commentService.countWordInAllComments(word));
    }

    @PostMapping(path = "/load",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<CommentDto> load(@RequestBody RequestUrl requestUrl) {
        List<CommentDto> commentDtos = commentService.loadCommentsByUrl(requestUrl.getUrl());
        log.info("Loaded comments by URL '{}'", requestUrl);
        return commentDtos;
    }

    @Data
    @AllArgsConstructor
    static class Count {
        private long count;

        static Count of(long count) {
            return new Count(count);
        }
    }
}
