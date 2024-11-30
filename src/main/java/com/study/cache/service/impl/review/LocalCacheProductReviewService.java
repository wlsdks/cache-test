package com.study.cache.service.impl.review;

import com.study.cache.dto.ProductReviewDto;
import com.study.cache.repository.ProductReviewRepository;
import com.study.cache.service.ProductReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class LocalCacheProductReviewService implements ProductReviewService {

    private final ProductReviewRepository reviewRepository;

    @Override
    @Cacheable(value = "product_reviews", key = "#page + '_' + #size", cacheManager = "caffeineCacheManager")
    @Transactional(readOnly = true)
    public List<ProductReviewDto> getReviews(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return reviewRepository.findAll(pageRequest).getContent()
                .stream()
                .map(ProductReviewDto::from)
                .collect(Collectors.toList());
    }

}