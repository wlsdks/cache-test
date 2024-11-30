package com.study.cache.config.init;

import com.study.cache.entity.*;
import com.study.cache.repository.*;
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
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final Random random = new Random();

    @Transactional
    @Override
    public void run(ApplicationArguments args) {
        log.info("ProductReviewInitializer 실행");

        long count = reviewRepository.count();
        log.info("현재 리뷰 수: {}", count);

        if (count == 0) {
            // Create brands
            List<BrandEntity> brands = new ArrayList<>();
            for (int i = 1; i <= 30; i++) {
                BrandEntity brand = BrandEntity.of("Brand_" + i, "Country_" + random.nextInt(50));
                brands.add(brand);
            }
            brandRepository.saveAll(brands);

            // Create categories
            List<CategoryEntity> categories = new ArrayList<>();
            for (int i = 1; i <= 50; i++) {
                CategoryEntity category = CategoryEntity.of("Category_" + i, "Description of Category_" + i);
                categories.add(category);
            }
            categoryRepository.saveAll(categories);

            // Create users
            List<UserEntity> users = new ArrayList<>();
            for (int i = 1; i <= 100; i++) {
                UserEntity user = UserEntity.of(
                        "user_" + i,
                        "user_" + i + "@example.com",
                        "User FullName " + i
                );
                users.add(user);
            }
            userRepository.saveAll(users);

            // Create products
            List<ProductEntity> products = new ArrayList<>();
            for (int i = 1; i <= 1000; i++) {
                BrandEntity brand = brands.get(random.nextInt(brands.size()));

                // Assign random categories to product
                Set<CategoryEntity> productCategories = new HashSet<>();
                int categoryCount = random.nextInt(3) + 1; // Each product has 1 to 3 categories
                for (int j = 0; j < categoryCount; j++) {
                    CategoryEntity category = categories.get(random.nextInt(categories.size()));
                    productCategories.add(category);
                }

                ProductEntity product = ProductEntity.of(
                        "Product_" + i,
                        "Description for Product_" + i,
                        brand,
                        productCategories
                );
                products.add(product);
            }
            productRepository.saveAll(products);

            // Create reviews
            List<ProductReviewEntity> reviews = new ArrayList<>();

            for (int i = 1; i <= 10000; i++) {
                // Get random product and user
                ProductEntity product = products.get(random.nextInt(products.size()));
                UserEntity user = users.get(random.nextInt(users.size()));

                // Generate image URLs
                List<String> imageUrls = new ArrayList<>();
                for (int j = 0; j < random.nextInt(5) + 1; j++) {
                    imageUrls.add("http://example.com/images/" + UUID.randomUUID() + ".jpg");
                }

                // Generate tags
                Set<String> tags = new HashSet<>();
                for (int j = 0; j < random.nextInt(5) + 1; j++) {
                    tags.add("tag_" + random.nextInt(100));
                }

                // Generate content
                String content = "This is a detailed review for product " + product.getName() + ".\n"
                        + "User experience and opinions are shared here.";

                // Additional info
                String additionalInfo = String.format("""
                                {
                                    "purchaseDate": "%s",
                                    "store": "Store_%d",
                                    "verified": %b
                                }
                                """,
                        "2024-" + (random.nextInt(12) + 1) + "-" + (random.nextInt(28) + 1),
                        random.nextInt(100),
                        random.nextBoolean()
                );

                ProductReviewEntity review = ProductReviewEntity.of(
                        content,
                        imageUrls,
                        random.nextInt(5) + 1,
                        tags,
                        additionalInfo,
                        product,
                        user
                );

                reviews.add(review);

                if (i % 1000 == 0) {
                    log.info("{}개의 리뷰 생성 중...", i);
                    reviewRepository.saveAll(reviews);
                    reviews.clear();
                }
            }
            // Save remaining reviews
            if (!reviews.isEmpty()) {
                reviewRepository.saveAll(reviews);
            }

            log.info("데이터 초기화 완료");
        }
    }

}