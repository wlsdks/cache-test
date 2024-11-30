package com.study.cache.service.impl;

import com.study.cache.dto.PostDto;
import com.study.cache.entity.PostEntity;
import com.study.cache.repository.PostRepository;
import com.study.cache.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LocalCachePostService implements PostService {

    private final PostRepository postRepository;

    @Cacheable(value = "posts", key = "'all'", cacheManager = "caffeineCacheManager")
    @Override
    public List<PostDto> getPosts() {
        List<PostEntity> posts = postRepository.findAll();

        return posts.stream()
                .map(PostDto::from)
                .collect(Collectors.toList());
    }

}