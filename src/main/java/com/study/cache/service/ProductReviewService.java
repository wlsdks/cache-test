package com.study.cache.service;

import com.study.cache.dto.ProductReviewDto;

import java.util.List;

public interface ProductReviewService {

    List<ProductReviewDto> getReviews(int page, int size, int minRating);

}
