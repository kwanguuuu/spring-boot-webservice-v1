package com.springboot.peter.service.posts;

import com.springboot.peter.domain.posts.Posts;
import com.springboot.peter.domain.posts.PostsRepository;
import com.springboot.peter.web.dto.PostsResponseDto;
import com.springboot.peter.web.dto.PostsSaveRequestDto;
import com.springboot.peter.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id="+ id));
        System.out.println(entity.getId());
        System.out.println(entity.getAuthor());
        System.out.println(entity.getContent());
        return new PostsResponseDto(entity);
    }

}
