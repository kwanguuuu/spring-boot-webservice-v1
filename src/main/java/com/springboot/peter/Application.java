package com.springboot.peter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//@EnableJpaAuditing
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        /**
         * @SpringBootApplication : 스프링부트 자동설정, spring bean 읽기 생성을 모두 자동으로 설정함
         * 스프링 부트가 프로젝트 실행시, @SpringBootApplication이 있는 곳부터 읽기 때문에 항상 최상단에 위치해야함.
         *
         * 내장 was를 실행함. - 톰캣 설치할 필요 없고, jar파일로 실행하면 됨.
         */
        SpringApplication.run(Application.class,args);
    }
}
