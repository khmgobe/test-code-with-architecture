package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.TestClockHolderTest;
import com.example.demo.mock.TestUuidHolderTest;
import com.example.demo.user.domain.dto.UserCreate;
import com.example.demo.user.domain.dto.UserUpdate;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {


    @Test
    public void User_는_UserCreate_객체로_생성할_수_있다()  {

        // given
        UserCreate userCreate = UserCreate.of("test@gmail.com", "test_nickname", "test_address");

        // when
        User user = User.from(userCreate,new TestUuidHolderTest("aaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaaaaaa"));

        // then
        assertThat(user.getEmail()).isEqualTo("test@gmail.com");
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
    }

    @Test
    public void User_는_UserUpdate_객체로_수정할_수_있다()  {


        // given

        User user = User.of("test@gmail.com", "test_nickname", "seoul", UserStatus.ACTIVE, UUID.randomUUID().toString());

        UserUpdate userUpdate = UserUpdate.of("test_nickname", "test_address");

        // when
        User updateUser = user.update(userUpdate);

        // then
        assertThat(updateUser.getAddress()).isEqualTo("test_address");
    }

    @Test
    public void User_는_로그인을_할_수_있고_로그인시_마지막_로그인_시간이_변경된다()  {


        // given
        User user = User
                        .builder()
                        .id(1L)
                        .email("test@gmail.com")
                        .nickname("test_nickname")
                        .address("seoul")
                        .status(UserStatus.ACTIVE)
                        .lastLoginAt(100L)
                        .build();

        // when
        user = user.login(new TestClockHolderTest(1678530673958L));

        // then
        assertThat(user.getLastLoginAt()).isEqualTo(1678530673958L);
    }


    @Test
    public void User는_인증_코드로_계정을_활성화_할_수_있다()  {

        // given
        User user = User
                .builder()
                .id(1L)
                .email("test@gmail.com")
                .nickname("test_nickname")
                .address("seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaaa-aaaaaaa-aaaa-aaaa-aaaaaaaa")
                .build();

        // when
        user = user.certificate("aaaa-aaaaaaa-aaaa-aaaa-aaaaaaaa");

        // then
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    public void 잘못된_인증_코드로_계정을_활성화_하려하면_에러를_던진다()  {

        // given
        User user = User
                .builder()
                .id(1L)
                .email("test@gmail.com")
                .nickname("test_nickname")
                .address("seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaaa-aaaaaaa-aaaa-aaaa-aaaaaaaa")
                .build();

        // when
        // then
        assertThatThrownBy(() ->
            user.certificate("aaaa-aaaaaaa-aaaa-aaaa-aaaaaaak")
        ).isInstanceOf(CertificationCodeNotMatchedException.class);
    }


}
