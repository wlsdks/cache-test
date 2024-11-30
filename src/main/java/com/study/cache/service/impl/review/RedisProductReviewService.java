package com.study.cache.service.impl.review;

import com.study.cache.dto.ProductReviewDto;
import com.study.cache.repository.ProductReviewRepository;
import com.study.cache.service.ProductReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisProductReviewService implements ProductReviewService {

    private final ProductReviewRepository reviewRepository;
    private final RedisTemplate<String, List<ProductReviewDto>> redisTemplate;

    @Override
    @Transactional(readOnly = true)
    public List<ProductReviewDto> getReviews(int page, int size) {
        String cacheKey = "product_reviews::page:" + page + "::size:" + size;

        @SuppressWarnings("unchecked")
        List<ProductReviewDto> cachedReviews = redisTemplate.opsForValue().get(cacheKey);

        if (cachedReviews != null) {
            log.info("Cache hit!");
            return cachedReviews;
        }

        log.info("Cache miss! Fetching from database...");
        PageRequest pageRequest = PageRequest.of(page, size);
        List<ProductReviewDto> reviews = reviewRepository.findAll(pageRequest).getContent()
                .stream()
                .map(ProductReviewDto::from)
                .collect(Collectors.toList());

        redisTemplate.opsForValue().set(cacheKey, reviews, Duration.ofMinutes(30));

        return reviews;
    }

}