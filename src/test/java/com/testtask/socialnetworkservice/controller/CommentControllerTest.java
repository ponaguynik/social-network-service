package com.testtask.socialnetworkservice.controller;

import com.testtask.socialnetworkservice.dto.CommentDto;
import com.testtask.socialnetworkservice.dto.RequestUrl;
import com.testtask.socialnetworkservice.model.*;
import com.testtask.socialnetworkservice.repository.CommentRepository;
import com.testtask.socialnetworkservice.repository.PostRepository;
import com.testtask.socialnetworkservice.repository.UserRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CommentControllerTest extends AbstractControllerIntegrationTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void loadShouldLoadCommentsByUrlToDb() throws Exception {
        User user = givenUser(User.builder().id(1L).name("test").build());
        Post post = givenPost(Post.builder().id(1L).title("TestTitle").user(user).build());
        List<Comment> expectedComments = Arrays.asList(
                Comment.builder().id(1L).name("comment1").body("Now you're just").email("asdf@fasdf.com").post(post).build(),
                Comment.builder().id(2L).name("comment2").body("Somebody that I used to know").email("asdf@fasdf.com").post(post).build()
        );
        String url = "http://localhost:8080/comments";
        List<CommentDto> commentDtos = expectedComments.stream().map(CommentDto::fromComment).collect(Collectors.toList());
        doReturn(commentDtos.toArray(new CommentDto[0])).when(restTemplate).getForObject(url, CommentDto[].class);

        mockMvc.perform(post("/comments/load")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(new RequestUrl(url))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(objectMapper.writeValueAsString(commentDtos)));

        assertEquals(expectedComments, commentRepository.findAll());
    }

    @Test
    public void getCommentUserShouldReturnUserByEmail() throws Exception {
        User expectedUser = givenUser(User.builder().id(1L).name("test").email("t.man@test.com").build());
        User author = givenUser(User.builder().id(2L).name("author").email("author@post.com").build());
        Post post = givenPost(Post.builder().id(1L).title("TestTitle").user(author).build());
        Comment comment = givenComment(Comment.builder().id(1L).name("comment1").body("Somebody that I used to know").email(expectedUser.getEmail()).post(post).build());

        mockMvc.perform(get("/comments/{commentId}/user", comment.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedUser)));
    }

    @Test
    public void findAllByUserIdShouldReturnAllCommentsOfUser() throws Exception {
        User targetUser = givenUser(User.builder().id(1L).name("test").email("c.man@test.com").build());
        User anotherUser = givenUser(User.builder().id(2L).name("odd man").email("another@test.com").build());
        Post post1 = givenPost(Post.builder().id(1L).title("First Post").user(anotherUser).build());
        Comment expectedComment1 = givenComment(Comment.builder().id(1L).name("comment1").body("Flood").email(targetUser.getEmail()).post(post1).build());
        Comment post1Comment2 = givenComment(Comment.builder().id(2L).name("comment2").body("Flood").email(anotherUser.getEmail()).post(post1).build());
        Post post2 = givenPost(Post.builder().id(2L).title("Second Post").user(targetUser).build());
        Comment post2Comment1 = givenComment(Comment.builder().id(3L).name("comment1").body("Insulting message").email(anotherUser.getEmail()).post(post2).build());
        Comment expectedComment2 = givenComment(Comment.builder().id(4L).name("comment2").body("Insulting message").email(targetUser.getEmail()).post(post2).build());
        List<CommentDto> expectedCommentDtos = Stream.of(expectedComment1, expectedComment2)
                .map(CommentDto::fromComment)
                .collect(Collectors.toList());

        mockMvc.perform(get("/comments")
                .param("userId", String.valueOf(targetUser.getId())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedCommentDtos)));
    }

    @Test
    public void countWordInBodyOfAllCommentsShouldReturn3() throws Exception {
        User user = givenUser(User.builder().id(1L).name("test").build());
        Post post1 = givenPost(Post.builder().id(1L).title("First Post").user(user).build());
        Comment post1Comment1 = givenComment(Comment.builder().id(1L).name("comment1").body("Some error occurred during program execution").email("anonymous@mail.gz").post(post1).build());
        Comment post1Comment2 = givenComment(Comment.builder().id(2L).name("comment2").body("Flood").email("anonymous@mail.gz").post(post1).build());
        Post post2 = givenPost(Post.builder().id(2L).title("Second Post").user(user).build());
        Comment post2Comment1 = givenComment(Comment.builder().id(3L).name("comment1").body("Another error").email("notpolite@mail.gz").post(post2).build());
        Comment post2Comment2 = givenComment(Comment.builder().id(4L).name("comment2").body("Insulting message").email("notpolite@mail.gz").post(post2).build());
        User anotherUser = givenUser(User.builder().id(2L).name("odd man").build());
        Post anotherPost1 = givenPost(Post.builder().id(3L).title("Post of the Odd Man").user(anotherUser).build());
        Comment anotherComment = givenComment(Comment.builder().id(5L).name("comment1").body("Third error message").email("anonymous@mail.gz").post(anotherPost1).build());
        mockMvc.perform(post("/comments/count").param("wordInBody", "error"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.count").value(3));
    }

    private Comment givenComment(Comment comment) {
        return commentRepository.save(comment);
    }

    private User givenUser(User user) {
        if (user.getAddress() == null) {
            user.setAddress(Address.builder().city("TestCity").street("TestStreet").geo(Geo.builder().lat(1.124d).lng(4.124).build()).build());
        }
        return userRepository.save(user);
    }

    private Post givenPost(Post post) {
        return postRepository.save(post);
    }
}