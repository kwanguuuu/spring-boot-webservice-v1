package com.springboot.peter.web;

import com.springboot.peter.domain.posts.Posts;
import com.springboot.peter.domain.posts.PostsRepository;
import com.springboot.peter.web.dto.PostsSaveRequestDto;
import com.springboot.peter.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @After
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

    @Test
    public void Posts_등록된다() {
        //given
        String title = "title";
        String content = "content";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();
        String url = "http://localhost:" + port + "/api/v1/posts/";

        //when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    public void post_수정() throws  Exception{
        //given
        Posts savePosts = postsRepository.save(Posts.builder()
                    .title("title")
                    .content("content")
                    .author("author")
                    .build());

        System.out.println("savePosts........    " + savePosts.toString());

        Long updateId = savePosts.getId();
        String updateTitle = "update title";
        String updateContent = "update Content";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                    .title(updateTitle)
                    .content(updateContent)
                    .build();

        System.out.println("requestDto........    " + requestDto);
        System.out.println("updateId : " + updateId);
        String url = "http://localhost:"+port+"/api/vi/posts/" + updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        //when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(updateTitle);
        assertThat(all.get(0).getContent()).isEqualTo(updateContent);
    }
}
