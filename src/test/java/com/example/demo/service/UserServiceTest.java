package com.example.demo.service;

import com.example.demo.exception.CertificationCodeNotMatchedException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.UserCreateDto;
import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.context.jdbc.Sql.*;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql",executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})

class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    void getByEmail_은_ACTIVE_상태인_유저를_찾아올_수_있다()  {

        // given
        String email = "khmgobe@gmail.com";

        // when
        UserEntity result = userService.getByEmail(email);

        // then
        assertThat(result.getNickname()).isEqualTo("khmgobe");
    }

    @Test
    void getByEmail_은_PENDING_상태인_유저를_찾아올_수_없다()  {

        // given
        String email = "khmgobe2@gmail.com";

        // then
        assertThatThrownBy(() -> {
            UserEntity result = userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById_는_ACTIVE_상태인_유저를_찾아올_수_있다()  {


        // when
        UserEntity result = userService.getById(1);

        // then
        assertThat(result.getNickname()).isEqualTo("khmgobe");
    }

    @Test
    void getById_는_PENDING_상태인_유저를_찾아올_수_없다()  {

        assertThatThrownBy(() -> {
            UserEntity result = userService.getById(2);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void userCreateDto_를_이용하여_유저를_생성_할_수_있다()  {

        // given
        UserCreateDto userCreateDto = UserCreateDto
                .builder()
                .email("khmgobe@gmail.com")
                .address("Seoul")
                .nickname("khm")
                .build();

        BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        // when
        UserEntity result = userService.create(userCreateDto);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);

    }

    @Test
    void userUpdateteDto_를_이용하여_유저를_수정할_수_있다()  {

        // given
        UserUpdateDto data = UserUpdateDto
                .builder()
                .address("Daegu1")
                .nickname("khmgobe12")
                .build();

        // when
        userService.update(1, data);

        // then
        UserEntity result = userService.getById(1);
        assertThat(result.getId()).isNotZero();
        assertThat(result.getAddress()).isEqualTo("Daegu1");
        assertThat(result.getNickname()).isEqualTo("khmgobe12");
    }

    @Test
    void user_를_로그인_시키면_마지막_로그인_시간이_변경된다()  {

        userService.login(1);

        // then
        UserEntity result = userService.getById(1);
        assertThat(result.getLastLoginAt()).isGreaterThan(0L);
    }


    @Test
    void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다()  {

        // when
        userService.verifyEmail(2,"aaaaaaaa-aaaa-aaaa-aaaaaaaaaa");
        
        // then
        UserEntity userEntity = userService.getById(2);
        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }
    
    
    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다() {

        assertThatThrownBy(() -> {
            userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaaaaaaaac");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}