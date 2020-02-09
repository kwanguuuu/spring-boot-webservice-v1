package com.springboot.peter.web;

import com.springboot.peter.service.posts.PostsService;
import com.springboot.peter.web.dto.PostsResponseDto;
import com.springboot.peter.web.dto.PostsSaveRequestDto;
import com.springboot.peter.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto){
        return postsService.save(requestDto);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto) {
        System.out.println(id);
        return postsService.update(id, requestDto);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto get(@PathVariable Long id) {
        System.out.println(id);
        return postsService.findById(id);
    }

}
