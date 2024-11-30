package com.study.cache.service.impl.review;

import com.study.cache.dto.ProductReviewDto;
import com.study.cache.repository.ProductReviewRepository;
import com.study.cache.service.ProductReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisProductReviewService implements ProductReviewService {

    private final ProductReviewRepository reviewRepository;
    private final RedisTemplate<String, List<ProductReviewDto>> redisTemplate;
    private static final String CACHE_KEY = "product_reviews::all";

    @Override
    public List<ProductReviewDto> getReviews() {
        List<ProductReviewDto> cachedReviews = redisTemplate.opsForValue().get(CACHE_KEY);

        if (cachedReviews != null) {
            log.info("Cache hit!");
            return cachedReviews;
        }

        log.info("Cache miss! Fetching from database...");
        List<ProductReviewDto> reviews = reviewRepository.findAll().stream()
                .map(ProductReviewDto::from)
                .collect(Collectors.toList());

        redisTemplate.opsForValue().set(CACHE_KEY, reviews, Duration.ofMinutes(30));

        return reviews;
    }

}