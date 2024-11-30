package com.study.cache.service.impl.review;

import com.study.cache.dto.ProductReviewDto;
import com.study.cache.repository.ProductReviewRepository;
import com.study.cache.service.ProductReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class LocalCacheProductReviewService implements ProductReviewService {
    
    private final ProductReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "product_reviews", key = "'all'", cacheManager = "caffeineCacheManager")
    @Override
    public List<ProductReviewDto> getReviews() {
        log.info("Cache miss! Fetching from database...");

        return reviewRepository.findAll().stream()
                .map(ProductReviewDto::from)
                .collect(Collectors.toList());
    }

}