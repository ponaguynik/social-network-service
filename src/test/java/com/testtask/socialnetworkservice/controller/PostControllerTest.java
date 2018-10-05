package com.testtask.socialnetworkservice.controller;

import com.testtask.socialnetworkservice.dto.PostDto;
import com.testtask.socialnetworkservice.dto.RequestUrl;
import com.testtask.socialnetworkservice.model.*;
import com.testtask.socialnetworkservice.repository.CommentRepository;
import com.testtask.socialnetworkservice.repository.PostRepository;
import com.testtask.socialnetworkservice.repository.UserRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostControllerTest extends AbstractControllerIntegrationTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void findAllWithTitleShouldReturnAllPostsWithTitle() throws Exception {
        String title = "Test Title";
        User user = givenUser(User.builder().id(1L).name("test").build());
        List<PostDto> expectedPosts = Arrays.asList(
                PostDto.fromPost(givenPost(Post.builder().id(1L).title(title).user(user).build())),
                PostDto.fromPost(givenPost(Post.builder().id(2L).title(title).user(user).build()))
        );
        PostDto.fromPost(givenPost(Post.builder().id(3L).title("Another Title").user(user).build()));

        mockMvc.perform(get("/posts")
                .param("title", title))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedPosts)));
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void deleteWithCommentsShouldDeletePostWithComments() throws Exception {
        User user = givenUser(User.builder().id(1L).name("test").build());
        Long postId = 1L;
        Post post = givenPost(Post.builder().id(postId).user(user).build());
        givenComment(Comment.builder().id(1L).name("name").body("body").post(post).build());
        givenComment(Comment.builder().id(2L).name("name").body("body").post(post).build());

        mockMvc.perform(delete("/posts/{postId}", postId))
                .andExpect(status().isOk());

        assertFalse(postRepository.findById(postId).isPresent());
        assertFalse(commentRepository.findById(1L).isPresent());
        assertFalse(commentRepository.findById(2L).isPresent());

        userRepository.delete(user);
    }

    @Test
    public void loadShouldLoadPostsByUrlToDb() throws Exception {
        User user = givenUser(User.builder().id(1L).name("test").build());
        List<Post> expectedPosts = Arrays.asList(
                Post.builder().id(1L).title("TestTitle").user(user).build(),
                Post.builder().id(2L).title("TestTitle").user(user).build()
        );
        String url = "http://localhost:8080/posts";
        List<PostDto> postDtos = expectedPosts.stream().map(PostDto::fromPost).collect(Collectors.toList());
        doReturn(postDtos.toArray(new PostDto[0])).when(restTemplate).getForObject(url, PostDto[].class);

        mockMvc.perform(post("/posts/load")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(objectMapper.writeValueAsString(new RequestUrl(url))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(objectMapper.writeValueAsString(postDtos)));

        assertEquals(expectedPosts, postRepository.findAll());
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