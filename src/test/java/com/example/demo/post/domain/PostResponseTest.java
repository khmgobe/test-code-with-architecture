package com.example.demo.post.domain;

import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PostResponseTest {


    @Test
    void Post로_응답을_생성할_수_있다()  {

        // given
        Post post = Post
                .builder()
                .id(1L)
                .writer(User.of("test@gmail.com", "test_nickname", "seoul", UserStatus.ACTIVE, UUID.randomUUID().toString()))
                .content("test_content")
                .build();

        // when
        PostResponse postResponse = PostResponse.from(post);

        // then
        assertThat(postResponse.getContent()).isEqualTo("test_content");
    }
}
