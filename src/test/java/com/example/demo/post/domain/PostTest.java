package com.example.demo.post.domain;

import com.example.demo.post.domain.dto.PostCreate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.Test;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class PostTest {


    @Test
    public void PostCreate으로_게시물을_만들_수_있다()  {

        // given

        User writer = User.of("test@gmail.com", "test_nickname", "seoul", UserStatus.ACTIVE, UUID.randomUUID().toString());

        PostCreate postCreate = PostCreate.of(1L, "test_content");



        // when
        Post post = Post.from(writer, postCreate);

        // then
        assertThat(post.getContent()).isEqualTo("test_content");
        assertThat(post.getWriter().getEmail()).isEqualTo("test@gmail.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("test_nickname");
    }
}
