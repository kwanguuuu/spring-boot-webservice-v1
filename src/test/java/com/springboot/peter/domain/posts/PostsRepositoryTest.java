package com.springboot.peter.domain.posts;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @After
    public void cleanup() {
        postsRepository.deleteAll();
    }

    @Test
    public void 게시글저장_불러오기() {
        String title = "테스트 게시글";
        String content = "테스트 본문";

        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author("kwanguuuu")
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);

    }

    @Test
    public void baseTimeEntity_사용() {
        LocalDateTime now = LocalDateTime.of(2020,02,9,0,0,0);
        postsRepository.save(Posts.builder()
                .author("author")
                .content("content")
                .title("title")
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll();
        System.out.println("================");
        postsList.forEach((e)-> System.out.println(e.getId()+"/"+e.getCreatedDate()+"/" + e.getModifiedDate()));
        System.out.println(postsList.size());
        System.out.println("================");
        //then
        Posts post = postsList.get(0);


        System.out.println(">>>>>>>>>>> createdDate= "+post.getCreatedDate() + ", modifiedDate=" + post.getModifiedDate());

        assertThat(post.getCreatedDate()).isAfter(now);
        assertThat(post.getModifiedDate()).isAfter(now);
    }
}
