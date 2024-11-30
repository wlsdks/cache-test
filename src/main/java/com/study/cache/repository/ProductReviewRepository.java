package com.study.cache.repository;

import com.study.cache.dto.ProductReviewDto;
import com.study.cache.entity.ProductReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductReviewRepository extends JpaRepository<ProductReviewEntity, Long> {

    @Query("""
            SELECT new com.study.cache.dto.ProductReviewDto(
                pr.id,
                pr.content,
                pr.rating,
                pr.additionalInfo,
                pr.createdAt,
                pr.updatedAt,
                p.name,
                u.username,
                b.name
            )
            FROM ProductReviewEntity pr
            JOIN pr.product p
            JOIN pr.user u
            JOIN p.brand b
            WHERE pr.rating >= :minRating
            """)
    Page<ProductReviewDto> findDetailedReviews(@Param("minRating") int minRating, Pageable pageable);

}