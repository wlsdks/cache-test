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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@RestController
public class ProductReviewController {

    private final BasicProductReviewService basicReviewService;
    private final RedisProductReviewService redisReviewService;
    private final LocalCacheProductReviewService localCacheReviewService;

    @GetMapping("/basic")
    public ResponseEntity<List<ProductReviewDto>> getReviewsWithoutCache(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noCache())
                .body(basicReviewService.getReviews(page, size));
    }

    @GetMapping("/redis")
    public ResponseEntity<List<ProductReviewDto>> getReviewsWithRedisCache(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(redisReviewService.getReviews(page, size));
    }

    @GetMapping("/local")
    public ResponseEntity<List<ProductReviewDto>> getReviewsWithLocalCache(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(localCacheReviewService.getReviews(page, size));
    }

}