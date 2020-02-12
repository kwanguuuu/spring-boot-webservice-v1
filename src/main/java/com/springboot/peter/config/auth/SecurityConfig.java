package com.springboot.peter.config.auth;

import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.springboot.peter.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().headers().frameOptions().disable()
                .and()
                .authorizeRequests().antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
                .antMatchers(HttpMethod.POST,"/api/v1/**").hasRole(Role.USER.name())
                .anyRequest().authenticated()
                .and()
                .logout().logoutSuccessUrl("/")
                .and()
                .oauth2Login().userInfoEndpoint().userService(customOAuth2UserService);
    }

    /**
     *  1. @EnableWebSecurity
     *  - 스프링 시큐리티 설정을 활성화 시켜줌
     *
     *  2. csrf().disable().header().frameOptions().disable()
     *  - h2 디비 콘솔 사용위해 디스에이블 해줌. csrf - 사이트 위조요청,
     *
     *  3. authorizeRequests
     *  - url별 관리를 설정함.
     *  - authorizeRequests가 사용 되어야만 antMatchers 사용할 수 있음.
     *
     *  4. antMatchers
     *  - 권한관리 대상을 지정하는 옵션
     *  - url, http별로 관리가 가능함
     *  - '/'등 지정된 url은 permitAll() 옵션을 줘 전체권한 줌
     *  - post메소드 이면서 "/api/v1/**"주소를 가진 api는 user권한을 줄 수 있게 설정함
     *
     *  5. anyRequest
     *  - 설정한 url이외의 다른 값.
     *  - anyRequest()뒤에 .authenticated()가 와서, 나머지 url들은 인증된 사용자들 (로그인 한 사용자)에게만 허용
     *
     *  6. logout().logoutSuccessfulUrl('/')
     *  - 로그아웃 시 진입점 설명.
     *
     *  7. oauth2Login
     *  - 로그인 기능 설정 진입점.
     *
     *  8. userInfoEndpoint()
     *  - 로그인 성공 이후 사용자 정보를 가져올 때의 설정을 담당함
     *
     *  9. userService
     *  - 로그인 성공시 후속조치를 진행할 UserService 인터페이스의 구현체를 등록함
     *  - 리소스서버에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시할 수 있음.
    */
}

