package com.example.weekdays.domain.repository;


import com.example.weekdays.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface BoardRepository extends JpaRepository<Board, Long> {



}

