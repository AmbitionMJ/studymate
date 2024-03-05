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
    @Query("update Board p set p.count = p.count +1 where p.id = :id")
    int updateView(Long id);
}

