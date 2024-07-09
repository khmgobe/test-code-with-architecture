package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolderTest;
import com.example.demo.mock.TestUuidHolderTest;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.dto.UserCreate;
import com.example.demo.user.domain.dto.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void init() {
        FakeMailSender fakeMailSender = new FakeMailSender();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();

        this.userService = UserService
                .builder()
                .uuidHolder(new TestUuidHolderTest("aaaa-aaaaaaaa-aaaa-aaaa-aaaaaaa"))
                .clockHolder(new TestClockHolderTest(1678530673958L))
                .userRepository(fakeUserRepository)
                .certificationService(new CertificationService(fakeMailSender))
                .build();

        fakeUserRepository.save(User.builder()
                        .id(1L)
                        .email("khmgobe@gmail.com")
                        .nickname("khmgobe")
                        .address("Seoul")
                        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaa")
                        .status(UserStatus.ACTIVE)
                        .lastLoginAt(0L)
                        .build());

        fakeUserRepository.save(User.builder()
                        .id(2L)
                        .email("khmgobe2@gmail.com")
                        .nickname("khmgobe2")
                        .address("Seoul")
                        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaaaaaaaa")
                        .status(UserStatus.PENDING)
                        .lastLoginAt(0L)
                        .build());

    }

    @Test
    void getByEmail_은_ACTIVE_상태인_유저를_찾아올_수_있다()  {

        // given
        String email = "khmgobe@gmail.com";

        // when
        User result = userService.getByEmail(email);

        // then
        assertThat(result.getNickname()).isEqualTo("khmgobe");
    }

    @Test
    void getByEmail_은_PENDING_상태인_유저를_찾아올_수_없다()  {

        // given
        String email = "khmgobe2@gmail.com";

        // then
        assertThatThrownBy(() -> {
            User result = userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById_는_ACTIVE_상태인_유저를_찾아올_수_있다()  {


        // when
        User result = userService.getById(1);

        // then
        assertThat(result.getNickname()).isEqualTo("khmgobe");
    }

    @Test
    void getById_는_PENDING_상태인_유저를_찾아올_수_없다()  {

        assertThatThrownBy(() -> {
            User result = userService.getById(2);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

//    @Test
//    void userCreateDto_를_이용하여_유저를_생성_할_수_있다()  {
//
//        // given
//        UserCreate userCreate = UserCreate
//                .builder()
//                .email("khmgobe@gmail.com")
//                .address("Seoul")
//                .nickname("khm")
//                .build();
//
//        // when
//        User result = userService.create(userCreate);
//
//        // then
//        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
//        assertThat(result.getCertificationCode()).isEqualTo("aaaa-aaaaaaaa-aaaa-aaaa-aaaaaaa");
//
//    }

    @Test
    void userUpdateteDto_를_이용하여_유저를_수정할_수_있다()  {

        // given
        UserUpdate data = UserUpdate
                .builder()
                .address("Seoul")
                .nickname("khmgobe")
                .build();

        // when
        userService.update(1, data);

        // then
        User result = userService.getById(1);
        assertThat(result.getId()).isNotZero();
        assertThat(result.getAddress()).isEqualTo("Seoul");
        assertThat(result.getNickname()).isEqualTo("khmgobe");
    }

    @Test
    void user_를_로그인_시키면_마지막_로그인_시간이_변경된다()  {

        userService.login(1);

        // then
        User user = userService.getById(1);
        assertThat(user.getLastLoginAt()).isEqualTo(1678530673958L);
    }


    @Test
    void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다()  {

        // when
        userService.verifyEmail(2,"aaaaaaaa-aaaa-aaaa-aaaaaaaaaa");
        
        // then
        User user = userService.getById(2);
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }
    
    
    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다() {

        assertThatThrownBy(() -> {
            userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaaaaaaaac");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}