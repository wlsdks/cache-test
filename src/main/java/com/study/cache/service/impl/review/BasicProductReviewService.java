package com.study.cache.service.impl.review;

import com.study.cache.dto.ProductReviewDto;
import com.study.cache.entity.ProductReviewEntity;
import com.study.cache.repository.ProductReviewRepository;
import com.study.cache.service.ProductReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicProductReviewService implements ProductReviewService {
    
    private final ProductReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    @Override
    public List<ProductReviewDto> getReviews() {
        List<ProductReviewEntity> reviews = reviewRepository.findAll();

        return reviews.stream()
                .map(ProductReviewDto::from)
                .collect(Collectors.toList());
    }

}