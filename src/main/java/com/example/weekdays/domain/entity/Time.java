package com.example.weekdays.domain.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass //이 엔터티는 부모클래스에서 사용됩니다.
@EntityListeners(AuditingEntityListener.class)
public class Time {


    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate; //생성한 시간

    @LastModifiedDate
    private LocalDateTime modifiedDate; //수정한 시간
}

