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


----

##3. 스프링부트에서  JPA로 데이터베이스 다루기

1. build.gradle에 Spring Data JPA 관련된 라이브러리 입력
2. Entity가 되는 것의 어노테이션들
    - @Entity
        - 테이블과 링크될 클래스 임을 나타냄.
        - 기본적으로 클래스의 카멜케이스 이름을 언더스코어네이밍(_)을 사용해 테이블 이름을 매칭함.
    - @Id
        - 해당 테이블의 PK필드를 나타냄
    - @GeneratedValue
        - PK생성 규칙을 나타냄
        - 스프링부트2.0에서부터는 GenerationType.IDENTITY 옵션을 추가해야 auto increment가 됨
    - @Column
        - 테이블의 컬럼을 나타내며 굳이 선언하지 않더라도 엔티티클래스의 필드는 컬럼이 됨.
        - 사용하는 이유는, 기본값 외에 추가로 변경이 필요한 옵션이 있으면 사용함.
        - ex. 문자열의 경우 varchar(255)가 기본인데, 사이즈를 늘리고 싶거나, 타입을 text로 변경하고 싶거나 등에 사용함
    - @NoAgrsContructor
        - 기본 생성자 자동 추가
        - public Post(){} 랑 같음
    - @Getter
        - 클래스 내 모든 필드의 getter 메소드 자동 추가
    - @Builder 
        - 해당 클래스의 빌더 패턴 클래스를 지정.
        - 생성자 상단에 선언 시, 생성자에 포함된 필드만 빌더에 포함

*** Entity 클래스에서는 절대 Setter 메소드를 만들지 않는다.
인스턴스의 값이 언제 어디서 변경되는지 파악이 어려워지기 때문에...


### JpaRepository 
Database를 접근하게해줄 DB Layer를 얘기함
생성 방법 : 인터페이스를 생성 후, JpaRepository<Entity 클래스, PK타입>을 상속함.
Entity클래스와 기본 EntityRepository는 같은 위치해야함.

### JpaRepositoryTests
중요 어노테이션
- @After
    - 단위테스트가 끝날 때마다 수행되는 메소드를 지정
    - 보통 배포 전 전체 테스트를 수행할 때, 테스트간 데이터 침범을 막기위해 사용함.

- postRepository.save()
    - 테이블에 insert/update 쿼리 실행함
- postRepository.findAll()
    - 테이블 posts에 있는 모든 데이터를 조회해오는 메소드

### application.properties, application.yml
스프링 부트 설정관련 옵션등을 작성함

### 등록/수정/조회 API만들기
API를 만들기 위해서 총 3개의 클래스가 필요함.

- Request를 받을 Dto
- API 요청을 받을 Controller
- 트랜잭션, 도메인 기능 간의 순서를 보장하는 Service

** 내가 알던 개념과 다른것..
- Service에서 비즈니스 로젝을 처리해야 한다는 것은 잘못된 얘기..
- Service는 트랜잭션, 도메인 간의 순서 보장의 역할을 해줘야함.

#### Spring Web 계층의 레이어별 설명 (5레이어)
- Web Layer
- Service Layer
- Repository Layer
- Dtos
- Domain Model

비즈니스 처리를 담당해야 할 곳은 Domain.
(기존 서비스로 처리하던 방식을 트랜잭션 스크립트라고 함.)



-----

## ch4. 머스테치로 화면 구성하기

### 템플릿 엔진.
- 지정된 템플릿 양식과 데이터가 합쳐저 문서를 출력하는 소프트웨어

서버 템플릿 엔진
- 서버쪽에서 구동. 서버에서 서버언어로 문자열을 만든 뒤, 문자열을 html로 변환
클라이언트 템플릿 엔진
- 브라우저 위에서 작동. (서버를 이미 벗어나 있음)

### 머스테치
대부분의 언어를 지원하는 템플릿 엔진. (루비, 자스, 파이썬, php, 펄 등)

자바 템플릿엔진의 단점
- jsp,velocity : 스프링 부트서 권장하지 않음
- freemarker : 너무 많은 기능을 지원함. 높은 자유도로 숙련도가 낮으면 view에 비즈니스 로직 많이 추가되는 경우도 잇음.
- thymeleaf : 문법이 어려운편, vue.js사용해 본 적 있으면 사용해 볼만함.

머스테치 장점
- 심플한 문법
- 로직코드를 사용할 수 없어 역할 분할 할 수 있음 (view / server)
- Mustache.js와 Mustache.java모두 있어, 하나의 문법으로 클라이언트/서버 템플릿 가능
- 인텔리제이 커뮤니티 버전에서도 설치 지원/


### 머스테치 템플릿 엔진으로 세팅하기
1. intellij에 머스테치 플러그인 설치 ( 편리하게 하기 위해서)
2. build.gradle에 추가
    - 기본 파일 위치 : src/main/resources/templates
    - 기본적으로 머스테치 스타터로 인하여 src/main/resources/templates로, 확장자는 .mustache가 붙음 ( 뷰 리졸버 )
    - 테스트


### 게시물 등록 화면 만들기



## 5. 스플이 시큐리티와 OAuth 2.0으로 로그인 구현하기
직접 로그인 구현 시 개발해야 하는 기능 ( 스프렝 시큐리티로 구현해야 하는 것은 제외)
- 로그인 보안
- 비밀번호 찾기
- 비밀번호 변경
- 회원가입시 이메일 혹은 전화번호 인증
- 회원정보 변경


### 스프링 부트 1.5 vs 스프링 부트 2.0
스프링부트 1.5 연동방법이 2.0에서 많이 변경되었지만, 설정방법에 크게 차이가 없음.
spring-security-oauth2-autoconfigure 라이브러리이용.
- spring-security-oauth2-autoconfigure 라이브러리 사용시, 스프링 부트2에서도 스프링 부트1.5에서 쓰던 설정을 그대로 할 수 있음.

책에서 할 것
Spring Security Oauth2 Client라이브 러리 사용
이유
- 기존 사용방식이 확장 포인트가 적절하게 오픈되어 있지 않아서, 직접 상속 또는 오버라이딩 해야하고, 신규 라이브러리의 경우 확장 포인트를 고려해 설계됨.
- 스프링 부트2 방식의 자료를 찾고 싶으면 spring-security-oauth2-autoconfigure 라이브 러리 사용과 application.properties 또는 application.yml설정을 비교.
    - 스프링부트 2: 클라이언트 인증정보만 입력
    - 스프링부트 1.5 : url주소를 모두 명시함.

-----

### 구글 서비스 등록
1. 구글클라우드에서, 프로젝트 신규 등록
2. 생성한 프로젝트에서 API 및 서비스 이동 > 사용자 인증정보 >사용자 인증 정보 만들기
3. OauthClientID만들기
4. application-oauth등록 -> 설정 후 application.properties가 읽을 수 있도록 인클루드 시킴
5. .gitignore등록

6. 회원정보를 가질 EntityClass등록
    - User.java
    - Role.java(EnumClass)

7. build.gradle에 의존성 추가해줌
    - compile('org.springframework.boot:spring-boot-starter-oauth2-client)

8. 시큐리티 관련 설정 시작.


