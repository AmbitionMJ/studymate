package com.example.weekdays.dto;


import com.example.weekdays.domain.entity.Account;
import com.example.weekdays.domain.entity.Board;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BoardDto {

    private Long id;
    private String title;
    private String writer;
    private String content;
    private Long viewCount;
    private LocalDateTime createdDate; // 작성일자
    private LocalDateTime modifiedDate; //수정일자

 public BoardDto(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.writer=board.getWriter().getNickname();
            this.viewCount = board.getViewCount();
            this.createdDate = board.getCreatedDate();
        }




    public Board toEntity() {
        // Board 클래스의 인스턴스를 반환합니다. DTO에서 엔터티로의 변환을 수행하는 데 사용됩니다.
     return  Board.builder()
                .title(title)
//                .writer(writer)  // service 레이어에서 별도로 등록 해줍니다.
                .content(content)
                .viewCount(viewCount)
                .build();

    }

    @Builder
    public BoardDto(Long id,String title, String content, String writer, Long viewCount, LocalDateTime createdDate,LocalDateTime modifiedDate ){
        //id는 데이터베이스에서 자동으로 생성되기 때문에 직접 설정할 필요는 없다.
        this.id=id;
        this.title =title;
        this.content=content;
        this.writer=writer;
        this.viewCount=viewCount;
        this.createdDate=createdDate;
        this.modifiedDate=modifiedDate;

    }


}
