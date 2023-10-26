package com.example.weekdays.dto;


import com.example.weekdays.domain.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BoardDto {

    private Long id;
    private String title;
    private String writer;
    private String content;
    private Long count;
    private LocalDateTime createdDate; // 작성일자
    private LocalDateTime modifiedDate; //수정일자


    public Board toEntity() {
        // Board 클래스의 인스턴스를 반환합니다. DTO에서 엔터티로의 변환을 수행하는 데 사용됩니다.
     return  Board.builder()
                .title(title)
                .writer(writer)
                .content(content)
                .count(count)
                .build();

    }

    @Builder
    public BoardDto(Long id,String title, String content, String writer, Long count, LocalDateTime createdDate,LocalDateTime modifiedDate ){
        //id는 데이터베이스에서 자동으로 생성되기 때문에 직접 설정할 필요는 없다.
        this.id=id;
        this.title =title;
        this.content=content;
        this.writer=writer;
        this.count=count;
        this.createdDate=createdDate;
        this.modifiedDate=modifiedDate;

    }
}
