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

### 스프링 시큐리티 설정
1. build.gradle 에 스프링 시큐리티 관련 의존성 추가.
2. WebSecurityConfigurerAdapter를 상속받는 설정 클래스 추가.
    - @EnableWebSecurity
        - csrf().disable().header().frameOptions().disable()
        - authorizeRequest
        - 


### 로그인 테스트
1. {{#userName}}
-  머스태치는 다른언어와 같은 if문을 제공하지 않는다. true/false만 판단함
- 머스태치에서는 항상 최종값을 넘겨줘야함.

2. a href='/logout'
- 스프링 시큐리티에서 기본적으로 제공하는 로그아웃 url
- /logout은 컨트롤러를 만들 필요가 없음.

3. {{^userName}}
- 머스태치에서 해당 값이 존재하지 않을 땐 ^를 사용함.
- userNameㅇ ㅣ없으면 , login을 보여지도록 설정

4. a href='/oauth2/authorization/google'
- 시큐리티에서 기본적으로 제공하는 로그인 url
- 로그아웃과 마찬가지로 생성할 필요가 없음

5. 로그인 코드를 어노테이션 기반으로 변경
- LoginUser 인터페이스 생성과, LoginUserArgumentResolver 생성
- LoginUserArgumentResolver는 HadlerMethodArgumentResolver를 구현하는 클래스.
- supportsParameter 와  resolveArgument를 통해 파라미터에 값을 전달함.
- WebMvcConfigurer 를 상속밭은 클래스를 구현해, LoginUserArgumentResolver를 추가함.
- 필요한 부분에 controller 파라미터 추가.

### 로그인 세션 저장소를 db로 변경하기
1. 세션 저장을 위한 방식
- 톰캣(was) 세션 사용
    - 기본적인 방식
    - 2대 이상의 was 사용시 별도의 was 동기화 처리가 필요함.
- db세션 저장소로 사용
    - 여러 was 간의 공용 저장소로 사용 가능 (IO발생)
    - 백오피스에서 많이 사용하는 방식
- 메모리 db를 세션 저장소로 사용
    - b2c서비스에서 가장 많이 사용하는 방식
    - 실제 서비스로 사용하기 위해선 외부 메모리 서버가 필요함.


2. spring-session-jdbc 등록
- build.gradle에 spring-session-jdbc패키지 추가
- application.yml에 spring.session.store-type=jdbc추가
- h2 콘솔 확인.
    - Spring-session, SPRING-SESSION-ATTRIBUTES 테이블 확인.
### 네이버 로그인 추가

### 기존 테스트에 스프링 시큐리티 추가하기
- 시큐리티 옵션이 활성화 되면, 인증된 사용자만 API를 호출할 수 있음 -> 테스트 코드가 실행되지 않음.
밑의 항목들은 문제가 되는 부분들임. 시큐리티 적용후 테스트 하려면 확인해야함
1. CustomOAuth2UserService를 찾지 못함
    - No qualifying bean of type 'com.springboot.peter.config.auth.CustomOAuth2UserService'
    - 소셜로그인 값이 없기 떄문에 발생함. (src/main 과 src/test의 설정이 다르기 때문에.)
    - 테스트 환경을 위한 application.properties를 만듦

2. status 302
- 302: 리다이렉션 응답. 인증되지 않은 사용자의 요청을 이동시키기 때문에.
- build.gradle에 스프링 시큐리티 관련된 의존선 추가함.
- PostsApiControllerTest의 테스트에 임의 사용자 인증 추가

3. @WebMvcTest에서 CustomerOAuth2UserService를 찾을수 없음.


### AWS환경 만들기
1. 인스턴스 생성(EC2)
- Elastic Compute Clouse (ECC -> EC2)
- ec2 인스턴스 시작
- AMI생성(AMI1 -> 센토스6, AMI2 ->센토스7버전으로 사용)
- 규칙등 설정(보안규칙 중요)

2. 인스턴스 고정 아이피 할당(EIP)
- 인스턴스의 고정 아이피를 EIP라고함(Elastic IP)

3. ec2에 접속하기 위해선..
- ssh : ssh -i pem 키 위치 ec2탄력ip 를 입력해줘야함

4. 간편하게 ec2접속하기
    1. ~/.ssh/에 key.pem 복사해 이동해놓기
    2.  ~/.ssh/config 편집
    ```
    Host 본인이 원하는 호스트명
        HostName ec2의 탄력적 주소
        User ec2-user
        IdentifyFile ~/.ssh/pem키 이름
    ```
    3. chmod 700 ~/.ssh/config -> 실행권한 부여

### 아마존 리눅스 1 사용시 부여할 설정
- java8설치
- 타임존 변경
- 호스트네임변경

1. 자바8 설치
- sudo yum install -y java-1.8.0-openjdk-devel.x86_64
- 자바 버전8로 변경
    - /usr/sbin 의 alternatives --config java 명령 실해ㅑㅇ
    - 자바를 8로 변경
    - java 7 삭제 : sudo yum remove java-1.7.0

2. 타임존 설정
- UTC인 시간을 KST(한국시간)으로 변경
    - date 명령 입력 시, UTC인것 확인
    - sudo rm /etc/localtime
    - sudo -s ln /usr/share/zoneinfo/Asia/Seoul /etc/localtime
        - ln : 하드링크나 심볼릭 링크를 만들기 위한 명령어
        - ln -s : 심볼릭 링크를 만듦.

3. 호스트네임 변경
- ip로는 뭐하는 건지 잘 몰라서... 호스트이름을 주자
- /etc/sysconfig/network 파일이 호스트네임 관련된 정보를 가지고 있음
- sudo vim /etc/sysconfig/network 를 수정
- /etc/hosts 에 방금 등록한 hostname을 등록

### RDS 설정
1. RDS를 MariaDB를 설정해서 생성하기
    - 가격이 저렴
    - Amazon Aurora로 교체 용이함.
- 설정 중, 네트워크 : 퍼블릭 엑세스 가능하도록 변경

2. 운영에 맞는 파라미터 설정하기
- 아래의  항목들을 설정해줘야함.
    - 타임존
    - CharacterSet
    - Max Connection 
- 파라미터 그룹으로 이동
    - 설정할 파라미터그룹 생성
    - 파라미터 생성 후, time_zone 검색 후 설정
    1. timezone : 검색 후, asia/seoul로 변경
    2. 캐릭터 셋 설정
        - character-set-* : utf8mb4로 변경
        - collation_server,collation_connect : utf8mb4_general_ci로 변경
            - 안될 경우 재부팅 하구 해봐라
    3. max connection  설정

3. pc에서 RDS접속 가능하도록 설정하기.

- rds보안 그룹에 로컬 PC IP추가하기.
    - 인바운드 규칙에 로컬,  EC2 추가하기

4. 인텔리제이에 DB 연결 할 수 있을 설정
- 엔드포인트 설정 :aws-rds.czilemscsp9n.ap-northeast-2.rds.amazonaws.com


### 인텔리J 에서 DB 연결 설정하기

### ec2 에서 rds에 설치된 MySQL접속하기
- CLI 설치
    - sudo yum install mysql
- 로컬에서 접속하 듯, rds의 게정, 비밀번호, 호스트주소 사용해 rds 접속하기
    - inbound규칙이 잘 적용되었으면 접속 될 것
    -mysql -u kwanguuuu -p -h aws-rds.czilemscsp9n.ap-northeast-2.rds.amazonaws.com

### ec2에 프로젝트 배포
- 소스 가져올 수 있도록 git 설치
- sudo yum install git
- git --version
- 프로젝트 가져올 폴더 만들기.. mkdir ~/app/test

### 배포 스크립트 만들기
배포란?
    - 새 버전의 프로젝트를 받음 (git clone, git pull 등)
    - gradle이나 maven을 통해 프로젝트 테스트
    - ec2서버에서 해당 프로젝트 실행 및 재실행
=> 위의 과정을 쉘 스크립트를 작성해 해보자.

### 스프링 부트 프로젝트로 RDS 접근하기
수행해야 할 작업
- 테이블 생성 : H2 디비가 해주던 걸 MySQL에선 생성해 줘야함.
- 프로젝트 설정 : 드라이버 설정(추가)
- EC2설정 : EC2 내부 접속정보 설정

1. 테이블 생성
JPA 사용 테이블 : 테스트 코드 실행하여 로그를 이용하여 생성
    - 테스트 코드 실행 결과로 나온, user,posts 테이블 insert
스프링 세션 테이블 : schema-mysql.sql파일에서 확인
    - cmd+shift+o 로 schema-mysql.sql 검색 후, 해당 테이블 생성

2. 프로젝트 설정
- MariaDB 드라이버를 build.gradle에 추가
- 서버에서 구동할 환경을 추가함. (src/main/resources/application-real.properties)
- 서버에서 RDS접속정보도 추가함


### CI/CD로 배포 자동화 하기
이전 배포 : 사용자가 직접 ./deploy 실행 후, 변경된 것이 있으면 수정/배포

CI의 규칙
- 모든 소스코드가 살아있고(실행되고 있고) 누구든 현재의 소스에 접근할 수 있는 단일 지점을 유지할 것
- 빌드 프로세스를 자동화해서 누구든 소스로부터 시스템을 빌드하는 달일 명령어를 사용할 수 있게금 할 것.
- 테스팅을 자동화해서 단일 명령어로 언제든지 시스템에 대한 건전한 테스트 수트를 실행할 수 있게 할 것
- 누구나 현재 실행 파일을 얻으면 지금까지 가장 완전한 실행 파일을 얻었다는 확실을 하게 할 것.


트레비스 CI 연동하기. 
깃에서 제공하는 무료 CI 서비스. 젠킨스(설치형)이기때문에, EC2를 하려면 하나 더 필요함.

### travisCI 자동화 하기
1. travis웹 서비스 설정
- https://travis-ci.org에 깃헙 계정으로 로그인, settings로 이동
2. 프로젝트 검색 후, legacy services integration을 활성화
3. 프로젝트에 .yml(야믈) 파일 설정해줘야함.
    - yml : JSON에서 괄호를 제거한 것이랑 같은 형태를 띔.
    - build.gradle과 같은 위치에 .travis.yml 생성
    - 빌드 시, repository가 public이어야 함.

### travisCI 와 AWS S3 연동하기
S3 - aws 파일서버(스토리지) : 정적파일 관리/ 이미지 관리 등
* travis CI를 AWS 연동 시 구조
```
github -> travisCI -> S3로 .jar전달
                    -> aws CodeDeploy로 배포요청
                    -> S3 가 CodeDeploy에 jar 전달 -> AWS CodeDeploy 가 EC2에 배포
                    
```
과정
1. travisCI 와 S3 연동
    - aws 접근가능 권한 가진 key 발급(IAM)
    - IAM(Identity and Aceess Management)
        - IAM 페이지에서 사용자->사용자추가
        - 사용자 이름설정/ 엑세스 유형(프로그래밍 방식)
        - 권한: 기존정책 직접 연결 설정
            - AmazonS3FullAccess, CodeDeployFullAccess 체크 후 추가
            - 추가한 사용자 access key id를 사용함
    - travis-ci에 등록한 프로젝트의 environment variable에 
        - access_key, secret_key 등록
        - 등록된 key는 .travis.yml에서 사용할 수 있음.

2. s3 버킷사용 (s3 : 파일서버)
build 파일 저장용 ( 주로 aws 환경 사용하면, 첨부파일 등 저장용으로 씀)
- 중요 한 것은 버킷 보안의 퍼블릭 엑세스를 차단 할 것.

3. .travis.yml에 관련 코드 추가해주기

4. travis ci와 aws s3, CodeDeploy 연결하기
ec2가 CodeDeploy를 연동 받을 수 있게 설정하기.
- ec2에 iam 역할 추가하기.
    1. iam -> 역할 -> 역할만들기
    - 역할 : aws서비스에만 할당할 수 있는 권한( ec2, CodeDeploy, SQS등)
    - 사용자 : AWS 서비스 외에 사용할 수 있는 권한 (로컬PC, IDC 서버 등)
    2. AWS 서비스 -> EC2
    3. 권한
        - AmazoneEC2RoleForAWSCodeDeploy
        - EC2가 S3버켓에 다운로드 된 것들을 접근할 수 있게 함.EC2에 CodeDeploy Agent 역할 설정 필요
    4. 태그
        - 원하는 이름으로 태그 설정
    5. 역할
        - 생성할 역할의 이름을 등록하고, 등록정보 확인함
- 생성한 IAM역할을 EC2서비스에 등록함
    1. ec2 인스턴스에 들어가, 인스턴스> iam역할 바꾸기를 들어가 역할 설정. 이후 재부팅

- ec2에 codeDeploy 에이전트 설치
    1. ec2접속해서 해당 명령어 실행
        - aws s3 s3://aws-codedeploy-ap-northeast-2/latest/install . --region ap-northeast-2
    2. install  받은 실행에 권한이 없으므로, 권한 부여
        - chmod +x install
    3. 권한부여 받은 install 실행
        - sudo ./install auto
    4. 실행 확인
        - sudo service codedeploy-agent status


5. CodeDploy를 위한 권한생성
CodeDeploy에서 EC2에 접근하기 위해 IAM역할을 생성해줌
    - IAM > 역할 > 역할만들기 > CodeDeploy(기존 서비스에서) > 생성

6. Codedeploy 생성
AWS 배포 삼형제
    1. Code commit
        - 깃허브 같은 코드 저장소 역할을 함
        - 프라이빗 기능을 지원, 깃헙이랑 거의 기능 동일해 사용 x.

    2. CodeBuild
        - travisCI 처럼 빌드용 서비스
        - 젠킨스/팀시티에 대체되어 사용 x.

    3. CodeDeploy
        - AWS의 배포 서비스
        - 대체제가 없음.
        - AUtoScaling 그룹배포, 블루그린 배포, 롤링 배포, EC2 단독 배포등 기능 지원

- CodeDeploy 서비스로 이동해 생성함
    1. 애플리케이션 생성 
        - 컴퓨팅 플랫폼 : ec2로 설정
    2. 배포그룹 생성
        - 배포그룹 이름, 서비스 역할 설정
    3. 배포유형 
        - 현재위치(1대)
        - 블루.그린 (배포 2대 이상)
    4. 환경구성
        - EC2 인스턴스 체크
    5. 배포설정, 로드밸런서
        - 배포구성: 한번 배포할 때 몇대의 서버에 배포할 지결정함.
        - 로드밸런서 : 트래픽 관리 로드밸런싱

- travisCI, s3, codedeploy 연동
    1. s3에서 넘겨줄 zip 파일을 저장할 디렉토리를 ec2에 생성
        - mkdir ~/app/step2 && mkdir ~/app/step2/zip
        - travisCI의 빌드가 끝나면, s3의 zip 파일이 전송되고, 이 파일을 ec2의 ~/app/step2/zip으로 복사해 압축 풀 것.

    2. appspec.yml 작성
        - aws codedeploy 의 설정
    3. .travis.yml 에도 CodeDeploy관련 추가

### 배포 자동화 구성
실제 jar 를 배포해서 실행.

1. deploy.sh 추가.
- step2에서 실행 될 deploy.sh 를 생성함.
```
#!/bin/bash

REPOSITORY=/home/ec2-user/app/step2
PRJECT_NAME=spring-boot-webservice-v1

echo ">  Build 파일 복사"

cp $REPOSITORY/zip/*.zar $REPOSITORY/

echo "> 현재 구동중인 애플리케이션  pid 확인"

CURRENT_PID=$(pgrep -fl spring-boot-webservice-v1| grep jar | awk '{print $1}')

echo "> 현재 구동중인 애플리케이션 pid : $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 새 어플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar |tail -n 1)

echo "> JAR NAME: $JAR_NAME"

echo ">$JAR_NAME에 실행 권한 추가"

chomod +x $JAR_NAME

echo ">$JAR_NAME 실행"

nohup java -jar \
  -Dspring.config.location=classpath:/application.properties,classpath:/application-real.properties,/home/ec2-user/app/application-oauth.properties,/home/ec2-user/app/application-real-db.properties \
  -Dspring.profiles.active=real \
  $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
```

2. .travis.yml 수정
- 실제로 필요한 파일은 Jar, appspec.yml, 배포 스크립트 등임. 떄문에 프로젝트 모든 파일을 .zip으로 만들지 않기 위해 필요한 것만 선택할 수 있도록 수정
- before_deploy수정
    ```
    # .zip에 담을 파일들만 선택할 수 있도록 수정
    before_deploy:
        - mkdir -p before-deploy
        - cp scripts/*.sh before-deploy/
        - cp appspec.yml before-deploy/
        - cp build/libs/*.jar before-deploy/
        - cp before-deploy && zip -r before-deploy * # before-deploy로 이동 후 전체 압축
        - cd ../ && mkdir -p deploy   #상위 디렉토리로 이동 후 deploy디렉토리 생성
        - mv before-deploy/before-deploy.zip deploy/spring-boot-webservice-v1.zip   #deploy로 zip파일 이동
    ```

3. appspec.yml 수정
    - permission : codedeploy에서 ec2로 넘겨준 파일들 모두 권한을 부여함
    - hooks: codedeploy 단계에서 실행할 명령어를 지정함.