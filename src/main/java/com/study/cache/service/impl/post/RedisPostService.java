package com.study.cache.service.impl.post;

import com.study.cache.dto.PostDto;
import com.study.cache.repository.PostRepository;
import com.study.cache.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisPostService implements PostService {

    private final PostRepository postRepository;
    private final RedisTemplate<String, List<PostDto>> redisTemplate;
    private static final String CACHE_KEY = "posts::all";

//    @Cacheable(value = "posts", key = "'all'", cacheManager = "redisCacheManager", unless = "#result.isEmpty()")
//    public List<PostDto> getPosts() {
//        long startTime = System.currentTimeMillis();
//
//        log.info("Cache miss! Fetching from database...");
//        List<PostEntity> posts = postRepository.findAll();
//        List<PostDto> result = posts.stream()
//                .map(PostDto::from)
//                .collect(Collectors.toList());
//
//        long endTime = System.currentTimeMillis();
//        log.info("Time taken: {} ms", endTime - startTime);
//
//        return result;
//    }

    @Override
    public List<PostDto> getPosts() {
        List<PostDto> cachedPosts = redisTemplate.opsForValue().get(CACHE_KEY);

        if (cachedPosts != null) {
            log.info("Cache hit!");
            return cachedPosts;
        }

        log.info("Cache miss! Fetching from database...");
        List<PostDto> posts = postRepository.findAll()
                .stream()
                .map(PostDto::from)
                .collect(Collectors.toList());

        redisTemplate.opsForValue().set(CACHE_KEY, posts, Duration.ofMinutes(30));

        return posts;
    }

}