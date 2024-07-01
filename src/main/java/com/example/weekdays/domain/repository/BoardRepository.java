package com.example.weekdays.domain.repository;


import com.example.weekdays.domain.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findById(Long id);


    // update Board p : 수정할 엔터티는 Board 이고 p 라고 칭합니다.
    // set p.count = p.count + 1 : 엔터티 p의 count 속성을 1 증가 시킵니다.
    // where p.id = :id : 해당 게시글의 id와 일치하는 레코드에만 조회수를 증가 시킵니다.

    @Modifying   //변경 작업을 할 때 사용하는 어노테이션
    @Query("update Board b set b.viewCount = b.viewCount +1 where b.id = :id")
    //
    int updateView(Long id);




    @Query("SELECT b FROM Board b WHERE " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.content) LIKE LOWER(CONCAT('%', :keyword, '%'))") //제목+내용으로 검색
    Page<Board> findByTitleOrContentContainingIgnoreCase(@Param("keyword") String keyword, Pageable pageable);


    @Query("SELECT b FROM Board b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%'))") //제목으로 검색
    //Board 엔터티는 b로 별칭을 지정합니다. //제목을 소문자로 변환합니다. //title 기준 검색어를 소문자로 변환하고 키워드가 포함된 모든 title 값을 찾습니다.
    Page<Board> findByTitleContainingIgnoreCase(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT b FROM Board b WHERE LOWER(b.content) LIKE LOWER(CONCAT('%', :keyword, '%'))") //내용으로 검색
    Page<Board> findByContentContainingIgnoreCase(String keyword, Pageable pageable);

    @Query("SELECT b FROM Board b WHERE LOWER(b.writer.nickname) LIKE LOWER(CONCAT('%', :keyword, '%'))") //글쓴이로 검색
    Page<Board> findByWriterContainingIgnoreCase(@Param("keyword") String keyword, Pageable pageable);



}

