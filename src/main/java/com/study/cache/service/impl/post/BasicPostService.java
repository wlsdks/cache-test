package com.study.cache.service.impl.post;

import com.study.cache.dto.PostDto;
import com.study.cache.entity.PostEntity;
import com.study.cache.repository.PostRepository;
import com.study.cache.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BasicPostService implements PostService {

    private final PostRepository postRepository;

    @Override
    public List<PostDto> getPosts(int page, int size) {
        int offset = page * size;
        List<PostEntity> posts = postRepository.findAllByOffsetAndLimit(offset, size);

        return posts.stream()
                .map(PostDto::from)
                .collect(Collectors.toList());
    }

}