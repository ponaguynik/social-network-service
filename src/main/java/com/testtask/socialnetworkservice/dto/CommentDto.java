package com.testtask.socialnetworkservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.testtask.socialnetworkservice.model.Comment;
import com.testtask.socialnetworkservice.model.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentDto {
    private Long id;
    private Long postId;
    private String name;
    private String email;
    private String body;

    public static CommentDto fromComment(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .postId(comment.getPost() != null ? comment.getPost().getId() : null)
                .name(comment.getName())
                .email(comment.getEmail())
                .body(comment.getBody())
                .build();
    }

    public Comment toComment() {
        return Comment.builder()
                .id(id)
                .post(Post.builder().id(postId).build())
                .name(name)
                .body(body)
                .email(email)
                .build();
    }
}
