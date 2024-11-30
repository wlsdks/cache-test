package com.study.cache.controller;

import com.study.cache.dto.PostDto;
import com.study.cache.service.impl.post.BasicPostService;
import com.study.cache.service.impl.post.LocalCachePostService;
import com.study.cache.service.impl.post.RedisPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/posts")
@RequiredArgsConstructor
@RestController
public class PostController {

    // 인터페이스를 주입받아서 사용할수도 있지만 이 경우에는 각자 테스트를 위해 바로 구현체를 주입받았습니다.
    private final BasicPostService basicPostService;
    private final RedisPostService redisPostService;
    private final LocalCachePostService localCachePostService;

    /**
     * 캐시를 사용하지 않는 기본적인 방법으로 전체 게시글을 조회합니다.
     *
     * @return 전체 게시글 목록
     */
    @GetMapping("/basic")
    public ResponseEntity<List<PostDto>> getPostsWithoutCache() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noCache())
                .body(basicPostService.getPosts());
    }

    /**
     * Redis 캐시를 사용하여 전체 게시글을 조회합니다.
     *
     * @return 전체 게시글 목록
     */
    @GetMapping("/redis")
    public ResponseEntity<List<PostDto>> getPostsWithRedisCache() {
        return ResponseEntity.ok(redisPostService.getPosts());
    }

    /**
     * Local 캐시를 (CacheManager: Caffeine) 사용하여 전체 게시글을 조회합니다.
     *
     * @return 전체 게시글 목록
     */
    @GetMapping("/local")
    public ResponseEntity<List<PostDto>> getPostsWithLocalCache() {
        return ResponseEntity.ok(localCachePostService.getPosts());
    }

//    /**
//     * 게시글을 생성합니다.
//     *
//     * @param postDto - 게시글 정보
//     * @return 생성된 게시글 정보
//     */
//    @PostMapping
//    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
//        return ResponseEntity.ok(basicPostService.createPost(postDto));
//    }

}