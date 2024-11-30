package com.study.cache.dto;

import com.study.cache.entity.PostEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PostDto {

    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;

    // factory method
    public static PostDto from(PostEntity postEntity) {
        return new PostDto(
                postEntity.getId(),
                postEntity.getTitle(),
                postEntity.getContent(),
                postEntity.getAuthor(),
                postEntity.getCreatedAt()
        );
    }

}
