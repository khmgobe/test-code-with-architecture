package com.example.demo.post.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostCreate {

    private final Long writerId;
    private final String content;

    @Builder
    private PostCreate(
        @JsonProperty("writerId") long writerId,
        @JsonProperty("content") String content) {
        this.writerId = writerId;
        this.content = content;
    }


    public static PostCreate of(Long writerId, String content) {
       return PostCreate
                .builder()
                .writerId(writerId)
                .content(content)
                .build();
    }
}
