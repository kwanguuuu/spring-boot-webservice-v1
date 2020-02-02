package com.springboot.peter.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class HelloResponseDto {

    private final String name;
    private final int amount;

    /**
     *  1. Getter
     *  - 선언된 모든 필드의 get 메소드를 생성해줌
     *
     *  2. RequiredArgsContructor
     *  - 선언된 모든 final필드가 포함된 생성자를 생성해줌
     *  - final이 없는 필드는 생성자에 포함되지 않는다.
     */
}
