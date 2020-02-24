package com.springboot.peter.web;

import com.springboot.peter.config.auth.SecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = HelloController.class,
excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
})
public class HelloControllTest {
    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(roles = "USER")
    public void hello가_리턴된다() throws Exception {
        String hello = "hello";
        mvc.perform(get("/hello")).andExpect(status().isOk()).andExpect(content().string(hello));
    }


    @Test
    @WithMockUser(roles = "USER")
    public void 롬복_dto리턴_테스트() throws Exception{
        String name = "hello";
        int amount = 10;

        mvc.perform(get("/hello/dto")
        .param("name",name)
        .param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is(name)))
                .andExpect(jsonPath("$.amount",is(amount)));

        /**
         * 1. .param
         * - api 테스트 할 때 사용될 요청 파라미터
         * - 값은 String만 허용함
         * - 숫자/날짜 등 -> 스트링으로 변경 후 사용해야 함.
         *
         * 2. jsonPath()
         * - json응답값을 필드별로 검증할 수 있는 메소드
         * - $를 기준으로 필드명을 명시함
         *
         */


    }

    /**
     *  코드 설명
     *  1. RunWith(SpringRunner.class)
     *  - JUnit에 내장된 실행자 외에 다른 실행자를 실행시킴(SpringRunner) -> springboot 테스트와 junit사이의 연결자
     *
     *  2. WebMvcTest
     *  - 여러 테스트 어노테이션 중, Web에 집중할 수 있는 어노테이션
     *  - 선언시 @Controoler, @ControllerAdvice등을 사용할 수 있음.
     *  - 단, @Service, Component, Repository등은 사용할 수 없음 -> 따로 설정해 줘야함.
     *
     *  3. @Autowired
     *  - bean을 자동 주입 받음
     *
     *  4. MockMvc
     *  - 웹API를 테스트 할 때 사용함
     *  - 스프링 MVC테스트의 시작점
     *  - 해당 클래스를 통해 get,post등에 대해 실행할 수 있음.
     *
     *  5. mvc.perform(get('/hello'))
     *  - MockMvc를 이용하여 /hello주소로 get 요청을 수행함.
     *  - 메소드 체이닝이 되니 순서를 잘 알것 mvc.perform().func1().func2()....
     *
     *  6. .andExpect(status().isOK())
     *  - mvc.perform의 결과를 입증함
     *  - http header의 status를 검증함. isOK() - 200
     *
     *  7. .andExpect(content().string(hello))
     *  - mvc.perform의 결과를 검증함
     *  - 응답 본문의 내용을 검증함
     *  - controller에서 hello를 리턴하기 떄문에 검증이 맞는지는 확인
     */
}
