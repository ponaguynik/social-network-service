package com.testtask.socialnetworkservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.testtask.socialnetworkservice.model.Post;
import com.testtask.socialnetworkservice.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {
    private Long id;
    private Long userId;
    private String title;
    private String body;

    public Post toPost() {
        return Post.builder()
                .id(id)
                .user(User.builder().id(userId).build())
                .title(title)
                .body(body)
                .build();
    }

    public static PostDto fromPost(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .userId(post.getUser() != null ? post.getUser().getId() : null)
                .title(post.getTitle())
                .body(post.getBody())
                .build();
    }
}