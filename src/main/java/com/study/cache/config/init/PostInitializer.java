package com.study.cache.config.init;

import com.study.cache.entity.PostEntity;
import com.study.cache.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class PostInitializer implements ApplicationRunner {

    private final PostRepository postRepository;

    @Transactional
    @Override
    public void run(ApplicationArguments args) {
        log.info("PostInitializer 실행");

        long count = postRepository.count();
        log.info("현재 게시글 수: {}", count);

        if (count == 0) {
            List<PostEntity> posts = new ArrayList<>();

            for (int i = 1; i <= 1000; i++) {
                PostEntity postEntity = PostEntity.of(
                        "테스트 게시글 " + i,
                        "이것은 테스트 게시글의 내용입니다. 게시글 번호는 " + i + "입니다. ".repeat(5),
                        "작성자" + i);

                posts.add(postEntity);

                if (i % 100 == 0) {
                    log.info("{}개의 게시글 생성", i);
                }
            }

            postRepository.saveAll(posts);
            log.info("게시글 저장 완료");

            long afterCount = postRepository.count();
            log.info("저장 후 게시글 수: {}", afterCount);
        }
    }

}