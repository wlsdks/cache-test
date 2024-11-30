package com.study.cache.service.impl.review;

import com.study.cache.dto.ProductReviewDto;
import com.study.cache.repository.ProductReviewRepository;
import com.study.cache.service.ProductReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicProductReviewService implements ProductReviewService {

    private final ProductReviewRepository reviewRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductReviewDto> getReviews(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return reviewRepository.findAll(pageRequest).getContent()
                .stream()
                .map(ProductReviewDto::from)
                .collect(Collectors.toList());
    }

}