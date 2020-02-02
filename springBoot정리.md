## 스프링부트 프로젝트 시작하기
###ch1. 인텔리제이로 스프링부트 시작하기

1. gradle프로젝트로 만들기 (java)
2. build.gradle 파일에 스프링부트 관련된 설정 속성을 입력하기.
->  Spring initializer 를 사용해서 해도 되지만, 그럼 그레이들에서 어떻게 생성 되는지 잘 모름.

```gradle
//springboot의 의존성 관리를 위한 설정 코드
//start...
buildscript {
    ext {
        springBootVersion = '2.1.7.RELEASE'
    }
    repositories {
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
    }
}
//end...
```
- ext : build.gradle에서 사용하는 전역변수를 설정하겠다. 
    - springBootVersion이라는 전역변수를 생성, 2.1.7.RELEASE로 값을 할당함.


