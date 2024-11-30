package com.study.cache.config.init;

import com.study.cache.entity.ProductReviewEntity;
import com.study.cache.repository.ProductReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductReviewInitializer implements ApplicationRunner {

    private final ProductReviewRepository reviewRepository;
    private final Random random = new Random();

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        log.info("ProductReviewInitializer 실행");

        long count = reviewRepository.count();
        log.info("현재 리뷰 수: {}", count);

        if (reviewRepository.count() == 0) {
            List<ProductReviewEntity> reviews = new ArrayList<>();

            for (int i = 1; i <= 1000; i++) {
                // 이미지 URL 더 많이 생성 (5-15개)
                List<String> imageUrls = new ArrayList<>();
                for (int j = 0; j < random.nextInt(10) + 5; j++) {
                    imageUrls.add("http://test-example.com/image" + random.nextInt(1000) +
                            ".jpg?size=large&quality=high&timestamp=" + System.currentTimeMillis() +
                            "&metadata=" + UUID.randomUUID());
                }

                // 태그 더 많이 생성 (10-20개)
                Set<String> tags = new HashSet<>();
                for (int j = 0; j < random.nextInt(10) + 10; j++) {
                    tags.add("detailed_tag_" + random.nextInt(100) + "_category_" +
                            random.nextInt(50) + "_type_" + random.nextInt(30));
                }

                // 훨씬 더 큰 텍스트 생성 (약 100KB)
                StringBuilder content = new StringBuilder();
                content.append("제품 상세 리뷰 시작\n".repeat(10));
                content.append("이 상품에 대한 상세한 리뷰입니다. ".repeat(200));
                content.append("\n상품의 장점:\n".repeat(5));
                content.append("이 상품의 주요 장점은 다음과 같습니다. ".repeat(100));
                content.append("\n상품의 단점:\n".repeat(5));
                content.append("개선이 필요한 부분은 다음과 같습니다. ".repeat(100));
                content.append("\n사용 경험:\n".repeat(5));
                content.append("실제 사용해본 경험을 공유합니다. ".repeat(100));
                content.append("\n추천 이유:\n".repeat(5));
                content.append("이 상품을 추천하는 이유는 다음과 같습니다. ".repeat(100));
                content.append("\n리뷰 번호 " + i + "의 상세 사용 후기입니다.\n".repeat(10));
                content.append("추가적인 상세 설명과 사용 팁입니다. ".repeat(200));

                // 더 복잡한 JSON 형태의 추가 정보
                String additionalInfo = String.format("""
                                {
                                    "reviewMetadata": {
                                        "purchaseInfo": {
                                            "date": "2024-%02d-%02d",
                                            "store": "Branch_%d",
                                            "paymentMethod": "Method_%d",
                                            "deliveryType": "Type_%d"
                                        },
                                        "verificationData": {
                                            "verified": %b,
                                            "verificationDate": "2024-%02d-%02d",
                                            "verificationMethod": "Method_%d",
                                            "verificationDetails": "Detail_%d"
                                        },
                                        "interactionMetrics": {
                                            "helpfulCount": %d,
                                            "reportCount": %d,
                                            "viewCount": %d,
                                            "commentCount": %d,
                                            "shareCount": %d
                                        },
                                        "detailedRatings": {
                                            "quality": %d,
                                            "price": %d,
                                            "delivery": %d,
                                            "packaging": %d,
                                            "satisfaction": %d,
                                            "durability": %d,
                                            "designRating": %d,
                                            "usability": %d,
                                            "customerService": %d
                                        },
                                        "productUsageInfo": {
                                            "usageDuration": %d,
                                            "usageFrequency": "Frequency_%d",
                                            "usageEnvironment": "Environment_%d",
                                            "userExpertiseLevel": "Level_%d"
                                        }
                                    }
                                }
                                """,
                        random.nextInt(12) + 1, random.nextInt(28) + 1,
                        random.nextInt(100), random.nextInt(10), random.nextInt(5),
                        random.nextBoolean(), random.nextInt(12) + 1, random.nextInt(28) + 1,
                        random.nextInt(5), random.nextInt(1000),
                        random.nextInt(1000), random.nextInt(50),
                        random.nextInt(10000), random.nextInt(500), random.nextInt(200),
                        random.nextInt(5) + 1, random.nextInt(5) + 1, random.nextInt(5) + 1,
                        random.nextInt(5) + 1, random.nextInt(5) + 1, random.nextInt(5) + 1,
                        random.nextInt(5) + 1, random.nextInt(5) + 1, random.nextInt(5) + 1,
                        random.nextInt(365) + 1, random.nextInt(10), random.nextInt(5),
                        random.nextInt(5)
                );

                ProductReviewEntity review = ProductReviewEntity.of(
                        "상품_" + i + "_카테고리_" + random.nextInt(50) + "_브랜드_" + random.nextInt(30),
                        content.toString(),
                        imageUrls,
                        random.nextInt(5) + 1,
                        "상세리뷰어_" + UUID.randomUUID().toString(),
                        tags,
                        additionalInfo
                );

                reviews.add(review);

                if (i % 100 == 0) {
                    log.info("{}개의 리뷰 생성 중...", i);
                }
            }

            reviewRepository.saveAll(reviews);
            log.info("리뷰 데이터 저장 완료");
        }
    }

}