package com.study.cache.service.impl.review;

import com.study.cache.dto.ProductReviewDto;
import com.study.cache.entity.CategoryEntity;
import com.study.cache.entity.ProductReviewEntity;
import com.study.cache.repository.ProductReviewRepository;
import com.study.cache.service.ProductReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BasicProductReviewService implements ProductReviewService {

    private final ProductReviewRepository reviewRepository;

    @Override
    public List<ProductReviewDto> getReviews(int page, int size, int minRating) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ProductReviewDto> pageResult = reviewRepository.findDetailedReviews(minRating, pageRequest);
        List<ProductReviewDto> dtos = pageResult.getContent();

        // 각 DTO에 대해 컬렉션 필드 설정
        dtos.forEach(dto -> {
            // 엔티티를 다시 조회하여 컬렉션 필드를 설정
            ProductReviewEntity entity = reviewRepository.findById(dto.getId()).orElseThrow();
            dto.changeImageUrls(new ArrayList<>(entity.getImageUrls()));
            dto.changeCategories(entity.getProduct().getCategories().stream()
                    .map(CategoryEntity::getName)
                    .collect(Collectors.toSet()));
        });

        return dtos;
    }

}