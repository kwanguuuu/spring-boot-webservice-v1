package com.springboot.peter.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {
    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedBy
    private LocalDateTime modifiedDate;

    /**
     *  1. MappedSuperclass
     *  - jpa Entity 클래스들이 BaseTimeEntity를 상속할 경우 필드들도 컬럼으로 인식하게 함.
     *
     *  2. @CreatedDate
     *  - 엔티티가 생성되어 저장될 떄 시간이 자동 저장함.
     *
     *  3. @LastModifiedDate
     *  - 조회한 엔티티의 값을 변경할 때 시간이 자도 ㅇ저장 됩니다.
     */
}
