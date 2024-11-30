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

    @Column(columnDefinition = "TEXT")
    private String content;

    @ElementCollection
    @CollectionTable(name = "review_images", joinColumns = @JoinColumn(name = "review_id"))
    private List<String> imageUrls = new ArrayList<>();

    private Integer rating;

    @ElementCollection
    @CollectionTable(name = "review_tags", joinColumns = @JoinColumn(name = "review_id"))
    private Set<String> tags = new HashSet<>();

    @Column(columnDefinition = "TEXT")
    private String additionalInfo;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // New relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    // Factory method
    public static ProductReviewEntity of(
            String content,
            List<String> imageUrls,
            Integer rating,
            Set<String> tags,
            String additionalInfo,
            ProductEntity product,
            UserEntity user
    ) {
        return new ProductReviewEntity(
                null,
                content,
                imageUrls,
                rating,
                tags,
                additionalInfo,
                null,
                null,
                product,
                user
        );
    }

}