package com.example.weekdays.domain.repository;


import com.example.weekdays.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findById(Long id);




    @Modifying
    @Query("update Board p set p.viewCount = p.viewCount +1 where p.id = :id")
    int updateView(Long id);

    // update Board p : 수정할 엔터티는 Board 이고 p 라고 칭합니다.
    // set p.count = p.count + 1 : 엔터티 p의 count 속성을 1 증가 시킵니다.
    // where p.id = :id : 해당 게시글의 id와 일치하는 레코드에만 조회수를 증가 시킵니다.

}

