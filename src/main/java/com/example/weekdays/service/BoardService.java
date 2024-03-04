package com.example.weekdays.service;

import com.example.weekdays.domain.entity.Account;
import com.example.weekdays.domain.entity.Board;
import com.example.weekdays.domain.repository.AccountRepository;
import com.example.weekdays.domain.repository.BoardRepository;
import com.example.weekdays.dto.BoardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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


        public Page<BoardDto> getPostPage(Pageable pageable) { //글 목록 - 페이징 기능 구현
        //한 페이지에 출력할 글의 개수를 10개로 지정합니다. 정렬은 id를 기준으로 내림차순으로 설정합니다.
        Pageable pageableWithSize10 = PageRequest.of(pageable.getPageNumber(), 10, Sort.by("id").descending());

        // 페이징된 글 목록을 조회하고, 각 글을 BoardDto로 변환하여 반환합니다.
            return boardRepository.findAll(pageableWithSize10).map(board -> BoardDto.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .count(board.getCount())
                    .writer(board.getWriter().getNickname())
                    .content(board.getContent())
                    .createdDate(board.getCreatedDate())
                    .build());
        }


    public BoardDto getPostDetail(Long id) { //게시글 상세보기
        Optional<Board> boardEntityWrapper = boardRepository.findById(id); // 주어진 ID에 해당하는 게시글을 찾습니다.

        if (boardEntityWrapper.isPresent()) { //값이 존재하는지 확인
            Board boardEntity = boardEntityWrapper.get(); //Optional 객체에서 실제 Board 엔터티를 가져옵니다. .get()메서드를 사용하여 실제 값을 추출


            return BoardDto.builder()
                    .id(boardEntity.getId())
                    .writer(boardEntity.getWriter().getNickname()) //조인 했기 때문에 Board 엔터티의 writer에 대한 정보를 가져오고, 작성자의 닉네임을 반환합니다.
                    // writer는 Board 엔터티에서 Account를 참조하고 있습니다.
                    .title(boardEntity.getTitle())
                    .content(boardEntity.getContent())
                    .count(boardEntity.getCount())
                    .createdDate(boardEntity.getCreatedDate())
                    .build();
        } else {
            throw new EntityNotFoundException("게시글을 찾을 수 없습니다." + id);
        }
    }

    public void BoardUpdate(Long id, BoardDto boardDto) {

        Board board = boardRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new); // id에 해당하는 게시물을 찾아옵니다. 찾지 못하면 예외를 발생시킵니다.

        board.update(boardDto.getTitle(), boardDto.getContent());

        boardRepository.save(board);

    }

    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);

    }
}





