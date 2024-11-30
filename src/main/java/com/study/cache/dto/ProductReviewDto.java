package com.study.cache.dto;

import com.study.cache.entity.CategoryEntity;
import com.study.cache.entity.ProductReviewEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductReviewDto {

    private Long id;
    private String content;
    private List<String> imageUrls; // 컬렉션 필드는 JPQL에서 직접 전달할 수 없음
    private Integer rating;
    private String additionalInfo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 연관된 엔티티 정보
    private String productName;
    private String userName;
    private String brandName;
    private Set<String> categories; // 컬렉션 필드

    // **JPQL에서 사용하는 생성자**
    public ProductReviewDto(
            Long id,
            String content,
            Integer rating,
            String additionalInfo,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            String productName,
            String userName,
            String brandName
            // 컬렉션 필드는 포함하지 않습니다.
    ) {
        this.id = id;
        this.content = content;
        this.rating = rating;
        this.additionalInfo = additionalInfo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.productName = productName;
        this.userName = userName;
        this.brandName = brandName;
        // imageUrls와 categories는 별도로 설정해야 합니다.
    }

    // 엔티티를 받아서 DTO로 변환하는 메서드
    public static ProductReviewDto fromEntity(ProductReviewEntity entity) {
        ProductReviewDto dto = new ProductReviewDto(
                entity.getId(),
                entity.getContent(),
                entity.getRating(),
                entity.getAdditionalInfo(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getProduct().getName(),
                entity.getUser().getUsername(),
                entity.getProduct().getBrand().getName()
        );

        // 컬렉션 필드 설정
        dto.imageUrls = new ArrayList<>(entity.getImageUrls());
        dto.categories = entity.getProduct().getCategories().stream()
                .map(CategoryEntity::getName)
                .collect(Collectors.toSet());

        return dto;
    }

    public void changeImageUrls(ArrayList<String> strings) {
        this.imageUrls = strings;
    }

    public void changeCategories(Set<String> collect) {
        this.categories = collect;
    }

}