package com.study.cache.service.impl.post;

import com.study.cache.dto.PostDto;
import com.study.cache.entity.PostEntity;
import com.study.cache.repository.PostRepository;
import com.study.cache.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisPostService implements PostService {

    private final PostRepository postRepository;

    @Cacheable(value = "posts", key = "'page_' + #page + '_size_' + #size", cacheManager = "redisCacheManager")
    @Override
    public List<PostDto> getPosts(int page, int size) {
        int offset = page * size;
        List<PostEntity> posts = postRepository.findAllByOffsetAndLimit(offset, size);

        return posts.stream()
                .map(PostDto::from)
                .collect(Collectors.toList());
    }

}