package com.example.demo.user.domain;

import com.example.demo.user.domain.dto.MyProfileResponse;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

public class MyProfileResponseTest {


    @Test
    void User_로_응답을_생성할_수_있다()  {

        // given
        User user = User.of("test@gmail.com", "test_nickname", "seoul", UserStatus.ACTIVE, UUID.randomUUID().toString());

        // when
        MyProfileResponse myProfileResponse = MyProfileResponse.from(user);

        // then
        assertThat(myProfileResponse.getEmail()).isEqualTo("test@gmail.com");
        assertThat(myProfileResponse.getNickname()).isEqualTo("test_nickname");
        assertThat(myProfileResponse.getAddress()).isEqualTo("seoul");
        assertThat(myProfileResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);


    }
}
