package com.springboot.peter.web.dto;

import com.springboot.peter.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.swing.text.html.parser.Entity;

@Getter
@NoArgsConstructor
public class PostsUpdateRequestDto {
    
    private Long id;
    private String title;
    private String content;
    private String author;

    public PostsUpdateRequestDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }

    @Builder
    public PostsUpdateRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
