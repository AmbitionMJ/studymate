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

        List<Board> boardEntity = boardRepository.findAll(); //게시글 엔터티를 모두 가져옵니다.
        List<BoardDto> boardDtoList = new ArrayList<>(); // BoardDto 객체들을 담을 리스트를 생성합니다.

        for (Board board : boardEntity) { // 가져온 게시글 엔터티 목록을 순회합니다.
            BoardDto boardDto = BoardDto.builder() // Board 엔터티를 BoardDto로 변환하는 작업
                    .id(board.getId()) //Board 엔터티의 ID를 BoardDto에 설정
                    .title(board.getTitle())
                    .count(board.getCount())
                    .writer(board.getWriter().getNickname())
                    .content(board.getContent())
                    .createdDate(board.getCreatedDate())

                    .build();

            boardDtoList.add(boardDto); //변환된 BoardDto를 리스트에 추가합니다.

        }
        return boardDtoList; // 변환된 BoardDto 리스트를 반환합니다.

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





