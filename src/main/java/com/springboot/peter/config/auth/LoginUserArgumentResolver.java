package com.springboot.peter.config.auth;

import com.springboot.peter.config.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.mail.Session;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final HttpSession httpSession;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
        boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());

        return isLoginUserAnnotation && isUserClass;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return httpSession.getAttribute("user");
    }
}
/**
 * HadlerMethodArgumentResolver인터페이스를 구현한 클래스
 * <p>
 * HandlerMethodArgumentResolver : 조건에 맞는 경우 메소드가 있다면, 음
 * 리졸버 구현체가 지정한 값으로 해당 메소드의 파라미터로 넘길 수 있다.
 *
 * 1. supportsParameter()
 * - 컨트롤러 메서드의 특정 파라미터를 지원하는지 판단합니다.
 * - 여기서는 @LoginUser 어노테이션이 붙어있고, 파라미터 클래스 타입이 SessionUser인 경우 true를 반환함.
 *
 * 2. resolveArgument()
 * - 파라미터에 전달할 객체를 생성합니다.
 * - 여기선 세션에 객체를 가져옴.
 *
 * -> supportParameter를 통과하면, resolveArgument에서 반환하는 값을 가져올 수 있음.
 */

