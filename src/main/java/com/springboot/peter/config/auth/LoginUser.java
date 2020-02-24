package com.springboot.peter.config.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {
}

/**
 *  1.@Target(ElementType.PARAMETER)
 *  - 어노테이션이 생성될 수 있는 위치를 지정함.
 *  - parameter로 지정했으니, 메소드의 파라미터로 선언된 객체에서만 사용할 수 있음.
 *  - type등을 사용할 수 있음
 *
 *  2. @interface
 *  - 이 파일을 어노테이션 클래스로 지정함
 *  - LoginUser라는 이름을 가진 어노테이션이 생성됨.
 */
