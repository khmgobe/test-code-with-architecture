package com.example.demo.user.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdate {

    private final String nickname;
    private final String address;

    @Builder
    private UserUpdate(
        @JsonProperty("nickname") String nickname,
        @JsonProperty("address") String address) {
        this.nickname = nickname;
        this.address = address;
    }

    public static UserUpdate of(String nickname, String address) {
        return UserUpdate
                .builder()
                .nickname(nickname)
                .address(address)
                .build();
    }
}
