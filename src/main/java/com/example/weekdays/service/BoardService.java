package com.example.weekdays.service;

import com.example.weekdays.domain.entity.Account;
import com.example.weekdays.domain.entity.Board;
import com.example.weekdays.domain.repository.AccountRepository;
import com.example.weekdays.domain.repository.BoardRepository;
import com.example.weekdays.dto.BoardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final AccountRepository accountRepository;

    public Long createBoard(BoardDto boardDto) { //글 등록
        Account writer = accountRepository.findByNickname(boardDto.getWriter());

        Board board = Board.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .writer(writer)
                .count(boardDto.getCount())

                .build();
        boardRepository.save(board);

        return board.getId();
    }

    public List<BoardDto> postList(Long id) { //글 목록

        List<Board> boardEntity = boardRepository.findAll();
        List<BoardDto> boardDtoList = new ArrayList<>();


        for (Board board : boardEntity) {
            BoardDto boardDto = BoardDto.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .count(board.getCount())
                    .writer(board.getWriter().getNickname())
                    .content(board.getContent())
                    .createdDate(board.getCreatedDate())

                    .build();

            boardDtoList.add(boardDto);

        }
        return boardDtoList;

    }

    public BoardDto getPostDetail(Long id) { //게시글 상세보기
        Optional<Board> boardEntityWrapper = boardRepository.findById(id);

        if (boardEntityWrapper.isPresent()) { //값이 존재하는지 확인
            Board boardEntity = boardEntityWrapper.get(); //Optional 객체에서 실제 Board 엔터티를 가져옵니다. .get()메서드를 사용하여 실제 값을 추출


            return BoardDto.builder()
                    .id(boardEntity.getId())
                    .writer(boardEntity.getWriter().getNickname())
                    .title(boardEntity.getTitle())
                    .content(boardEntity.getContent())
                    .count(boardEntity.getCount())
                    .createdDate(boardEntity.getCreatedDate())
                    .build();
        } else {
            throw new EntityNotFoundException("게시글을 찾을 수 없습니다." + id);
        }
    }
}


