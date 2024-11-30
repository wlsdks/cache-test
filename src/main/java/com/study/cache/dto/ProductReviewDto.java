package com.study.cache.dto;

import com.study.cache.entity.ProductReviewEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ProductReviewDto {

    private Long id;
    private String productName;
    private String content;
    private List<String> imageUrls;
    private Integer rating;
    private String userName;
    private Set<String> tags;
    private String additionalInfo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // factory method
    public static ProductReviewDto of(
            Long id,
            String productName,
            String content,
            List<String> imageUrls,
            Integer rating,
            String userName,
            Set<String> tags,
            String additionalInfo,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new ProductReviewDto(
                id,
                productName,
                content,
                imageUrls,
                rating,
                userName,
                tags,
                additionalInfo,
                createdAt,
                updatedAt
        );
    }

    // factory method for entity to dto conversion
    public static ProductReviewDto from(ProductReviewEntity entity) {
        return of(
                entity.getId(),
                entity.getProductName(),
                entity.getContent(),
                new ArrayList<>(entity.getImageUrls()),  // 새로운 List로 복사
                entity.getRating(),
                entity.getUserName(),
                new HashSet<>(entity.getTags()),         // 새로운 Set으로 복사
                entity.getAdditionalInfo(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}