package com.study.cache.controller;

import com.study.cache.dto.ProductReviewDto;
import com.study.cache.service.impl.review.BasicProductReviewService;
import com.study.cache.service.impl.review.LocalCacheProductReviewService;
import com.study.cache.service.impl.review.RedisProductReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@RestController
public class ProductReviewController {

    private final BasicProductReviewService basicReviewService;
    private final RedisProductReviewService redisReviewService;
    private final LocalCacheProductReviewService localCacheReviewService;

    /**
     * 캐시를 사용하지 않는 기본적인 방법으로 전체 리뷰를 조회합니다.
     *
     * @return 전체 리뷰 목록
     */
    @GetMapping("/basic")
    public ResponseEntity<List<ProductReviewDto>> getReviewsWithoutCache() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noCache())
                .body(basicReviewService.getReviews());
    }

    /**
     * Redis 캐시를 사용하여 전체 리뷰를 조회합니다.
     *
     * @return 전체 리뷰 목록
     */
    @GetMapping("/redis")
    public ResponseEntity<List<ProductReviewDto>> getReviewsWithRedisCache() {
        return ResponseEntity.ok(redisReviewService.getReviews());
    }

    /**
     * Local 캐시를 (CacheManager: Caffeine) 사용하여 전체 리뷰를 조회합니다.
     *
     * @return 전체 리뷰 목록
     */
    @GetMapping("/local")
    public ResponseEntity<List<ProductReviewDto>> getReviewsWithLocalCache() {
        return ResponseEntity.ok(localCacheReviewService.getReviews());
    }

}