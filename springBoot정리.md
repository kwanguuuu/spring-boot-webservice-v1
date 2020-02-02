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

//위에 선언한 플러그인 의존성들을 적용할 것인지를 결정하는 코드. 아래 네개의 플러그인은 필수 플러그인이라 항상 작성 해줘야함.
apply plugin: 'java'        //java plugin적용
apply plugin: 'eclipse'     //eclipse 플러그인 적용
apply plugin: 'org.springframework.boot'    //springboot프레임워크 적용
apply plugin: 'io.spring.dependency-management' //스프링 부트 의존성들을 관리해주는 플러그인

group 'com.spring-boot.peter'
version '1.0-SNAPSHOT'

sourceCompatibility = 1.8

repositories {
    mavenCentral()
}

dependencies {
    compile('org.springframework.boot:spring-boot-starter-web')
    testCompile('org.springframework.boot:spring-boot-starter-test')
}
```
> .gradle 파일 키워드 설명
- ext : build.gradle에서 전역변수를 설정하겠다는 의미. 변수명을 설정해서 문서에 적용할 수 있음 ${이러케}
- apply ~~ : 어떤 의존성을 적용할 것인지 선언
- repositories : 각종 의존성드을 어떤 원격 저장소에서 받을 지 정함
    - mavenCeltral() : 기본 저장소.
    - jcenter() : mevenCentral에 업데이트등이 힘들어, 공유하기가 쉬워 여기다 쓰는 추세.. jcenter -> mavenCeltral 라이브러리 업로드도 가능함.
- dependencies : 프로젝트 개발에 필요한 의존성을 선언할 수 있음. mavenCentral의존성을 인덱싱 해서 편함


3. 설정 완료후 git과 연동해보기
- command+shift+a 단축키 사용해서 , share project on github
4. 변경사항 git에 커밋하기
- commit : command + k
- push : command + shift + k


***그레이들 다운그레이드
- 그레이들 5.x.x버전에서 Querydsl과 롬복관련된 플러그인 설정방법이 변경 되어서 오류가 뜨는 것으로 확인.
1. alt+F12 (윈도우/맥 동일) 키로 해당 프로젝트 기준으로 터미널 생성
2. gradlew wrapper --gradle-version 4.10.2