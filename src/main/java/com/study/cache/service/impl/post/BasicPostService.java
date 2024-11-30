package com.study.cache.service.impl.post;

import com.study.cache.dto.PostDto;
import com.study.cache.entity.PostEntity;
import com.study.cache.repository.PostRepository;
import com.study.cache.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BasicPostService implements PostService {

    private final PostRepository postRepository;

    @Override
    public List<PostDto> getPosts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<PostEntity> posts = postRepository.findAllByOffsetAndLimit(pageRequest);

        return posts.getContent().stream()
                .map(PostDto::from)
                .collect(Collectors.toList());
    }

}