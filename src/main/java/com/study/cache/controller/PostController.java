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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/posts")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final BasicPostService basicPostService;
    private final RedisPostService redisPostService;
    private final LocalCachePostService localCachePostService;

    /**
     * 캐시를 사용하지 않는 기본적인 방법으로 게시글을 조회합니다.
     *
     * @param page 페이지 번호 (0부터 시작)
     * @param size 페이지 당 게시글 수 (기본값: 20)
     * @return 게시글 목록
     */
    @GetMapping("/basic")
    public ResponseEntity<List<PostDto>> getPostsWithoutCache(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        List<PostDto> posts = basicPostService.getPosts(page, size);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noCache())
                .body(posts);
    }

    /**
     * Redis 캐시를 사용하여 게시글을 조회합니다.
     *
     * @param page 페이지 번호 (0부터 시작)
     * @param size 페이지 당 게시글 수 (기본값: 20)
     * @return 게시글 목록
     */
    @GetMapping("/redis")
    public ResponseEntity<List<PostDto>> getPostsWithRedisCache(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        List<PostDto> posts = redisPostService.getPosts(page, size);
        return ResponseEntity.ok(posts);
    }

    /**
     * Local 캐시를 사용하여 게시글을 조회합니다.
     *
     * @param page 페이지 번호 (0부터 시작)
     * @param size 페이지 당 게시글 수 (기본값: 20)
     * @return 게시글 목록
     */
    @GetMapping("/local")
    public ResponseEntity<List<PostDto>> getPostsWithLocalCache(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        List<PostDto> posts = localCachePostService.getPosts(page, size);
        return ResponseEntity.ok(posts);
    }

}