package com.springboot.peter.config.auth.dto;

import com.springboot.peter.domain.user.Role;
import com.springboot.peter.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST)
                .build();
    }

    /**
     *  1. of
     *  - OAuth2User에서 반환하는 사용자 정보가 Map이기 때문에, 값 하나하나를 변환해야 함.
     *  - Map타입의 attribute를 ofGoogle메소드에서 필드에 값 설정해줌.
     *
     *  2. toEntity
     *  - User의 Entity를 생성함
     *  - OAuthAttributes에서 엔티티를 생성하는 시점은 처음 가입할 때.
     *  - 가입할 때 기본권한을 Guest로 주기 위해서 Role권한 사용
     *  - 클래스 생성이 끝났으면 같은 패키지에 SessionUser를 생성.
     */
}
