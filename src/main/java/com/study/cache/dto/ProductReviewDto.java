package com.study.cache.dto;

import com.study.cache.entity.ProductReviewEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    public static ProductReviewDto from(ProductReviewEntity productReviewEntity) {
        return new ProductReviewDto(
                productReviewEntity.getId(),
                productReviewEntity.getProductName(),
                productReviewEntity.getContent(),
                productReviewEntity.getImageUrls(),
                productReviewEntity.getRating(),
                productReviewEntity.getUserName(),
                productReviewEntity.getTags(),
                productReviewEntity.getAdditionalInfo(),
                productReviewEntity.getCreatedAt(),
                productReviewEntity.getUpdatedAt()
        );
    }

}