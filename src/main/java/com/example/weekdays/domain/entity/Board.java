package com.example.weekdays.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name="board")
public class Board extends Time{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(length = 50, nullable = false)
    private String title; //글 제목

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account writer; //작성자

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String content; //글 내용

    private Long count; //조회수


    @Builder
    public Board(String title, String content, Account writer, Long count){
        //id는 데이터베이스에서 자동으로 생성되기 때문에 직접 설정할 필요는 없다.
        this.title =title;
        this.content=content;
        this.writer=writer;
        this.count=count;

    }
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
