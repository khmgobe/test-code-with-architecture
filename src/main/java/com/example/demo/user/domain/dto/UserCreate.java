package com.example.demo.user.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserCreate {

    private final String email;
    private final String nickname;
    private final String address;

    @Builder
    private UserCreate(
        @JsonProperty("email") String email,
        @JsonProperty("nickname") String nickname,
        @JsonProperty("address") String address) {
        this.email = email;
        this.nickname = nickname;
        this.address = address;
    }

    public static UserCreate of(String email, String nickname, String address) {

        return UserCreate
                .builder()
                .email(email)
                .nickname(nickname)
                .address(address)
                .build();
    }

}
