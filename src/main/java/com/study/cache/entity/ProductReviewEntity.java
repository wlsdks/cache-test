package com.study.cache.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
@Table(name = "product_review")
public class ProductReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    @Column(columnDefinition = "TEXT")  // VARCHAR 대신 TEXT 타입 사용
    private String content;

    @ElementCollection
    @CollectionTable(name = "review_images")
    private List<String> imageUrls = new ArrayList<>();

    private Integer rating;

    private String userName;

    @ElementCollection
    @CollectionTable(name = "review_tags")
    private Set<String> tags = new HashSet<>();

    @Column(columnDefinition = "TEXT")
    private String additionalInfo;  // JSON 형태의 추가 정보 저장

    @CreatedDate
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // factory method
    public static ProductReviewEntity of(String productName, String content, List<String> imageUrls, Integer rating, String userName, Set<String> tags, String additionalInfo) {
        return new ProductReviewEntity(null, productName, content, imageUrls, rating, userName, tags, additionalInfo, null, null);
    }

}