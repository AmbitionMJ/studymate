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
import javax.servlet.http.HttpSession;
import java.util.*;


@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final AccountRepository accountRepository;
    private final HttpSession session;

    public void createBoard(BoardDto boardDto) { //글 등록
        Account writer = accountRepository.findByNickname(boardDto.getWriter());

        Board board = Board.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .writer(writer)
                .build();
        boardRepository.save(board);

    }


    //Pageable : Spring Data 에서 제공하는 페이징 및 정렬을 위한 인터페이스
    public Page<BoardDto> getPostPage(Pageable pageable) { //글 목록 - 페이징 기능 구현
        //한 페이지에 출력할 글의 개수를 10개로 지정합니다. 정렬은 id를 기준으로 내림차순으로 설정합니다.
        Pageable pageableWithSize10 = PageRequest.of(pageable.getPageNumber(), 10, Sort.by("id").descending());

        // 페이징된 글 목록을 조회하고, 각 글을 BoardDto로 변환하여 반환합니다.
        return boardRepository.findAll(pageableWithSize10).map(board -> BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .viewCount(board.getViewCount())
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
                    .viewCount(boardEntity.getViewCount())
                    .createdDate(boardEntity.getCreatedDate())
                    .build();
        } else {
            throw new EntityNotFoundException("게시글을 찾을 수 없습니다." + id);
        }
    }


    private void BoardDetail(Long id) { // 게시글 상세보기 - 조회수 기능 추가

        Optional<Board> board = boardRepository.findById(id); // ID를 사용하여 게시글을 조회합니다. 게시글이 존재하면 Optional로 감싸진 게시글 객체를 반환합니다.
        if (board.isPresent()) { //isPresent() 메서드는 Optional 객체 안에 값이 존재하는지 여부를 반환합니다.
            Board boardEntity = board.get(); // Optional 객체 안에 존재하는 게시글 객체를 가져옵니다.
            boardEntity.incrementViewCount(); // 게시글의 조회수를 증가시킵니다. (incrementViewCount()는 엔터티에 정의된 메서드이며 조회수를 1 증가시키는 역할을 합니다.)
            boardRepository.save(boardEntity); // 조회수 정보를 저장합니다.

        } else {
            throw new EntityNotFoundException("게시글을 찾을 수 없습니다." + id);
        }


    }

    public BoardDto getCompleteBoardDetail(Long id) { // 게시글 상세페이지 controller 호출
         if(!hasViewedPost(id)){ //해당 게시글이 이미 조회되었는지 확인합니다.
             BoardDetail(id); //해당 게시글이 조회되지 않았으면 게시글의 조회수를 증가시킵니다.
             addViewedPostToSession(id); //조회수를 증가시킨 후에 이미 조회되었음을 표시합니다.
         }

        return getPostDetail(id);
    }


    private boolean hasViewedPost(Long id) {  //세션에서 조회한 게시글인지 확인하는 메서드 markPostAsViewed에서 사용

        Set<Long> viewedPosts = Optional.ofNullable((Set<Long>) session.getAttribute("viewedPosts")).orElse(Collections.emptySet()); // 속성을 가져오는 부분은 추후에 해당 속성을 업데이트 하거나 사용할 때 유용하게 활용될 수 있도록 준비하는 단계입니다.
        //세션에서 viewedPosts 속성을 가져옵니다. Optional.ofNullable()은 가져온 속성이 null인지 확인하고, 그렇지 않은 경우에만 Optional 객체를 생성합니다. 가져온 속성을  set<Long> 타입으로 형변환 합니다.
        //.orElase(Collections.emptySet()) 가져온 속성이 null인 경우에는 빈 Set을 반환합니다. null 체크를 통해서 NullPointException을 방지합니다.

        return viewedPosts.contains(id); // 형변환된 조회한 게시글 집합에 지정된 ID가 포함되어 있는지 확인하고, 그 결과를 반환합니다.

    }


    private void addViewedPostToSession(Long id) {  //세션에 조회한 게시글을 추가하는 메서드
    Set<Long> viewedPosts = (Set<Long>) session.getAttribute("viewedPosts"); // 세션에서 viewedPosts 속성을 가져옵니다.
    if(viewedPosts == null) { // viewed 속성이 null 이라면
        viewedPosts = new HashSet<>(); // 새로운 HashSet을 생성하여 조회한 게시글의 ID를 관리할 준비를 합니다.

    }
    viewedPosts.add(id); // 조회한 게시글의 ID를 viewedPosts 속성에 추가합니다.
    session.setAttribute("viewedPosts", viewedPosts); // 세션에 있는 viewedPosts 속성을 업데이트 합니다.
}




    public void BoardUpdate(Long id, BoardDto boardDto) { //게시글 업데이트

        Board board = boardRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new); // id에 해당하는 게시물을 찾아옵니다. 찾지 못하면 예외를 발생시킵니다.

        board.update(boardDto.getTitle(), boardDto.getContent());

        boardRepository.save(board);

    }

    public void deleteBoard(Long id) { //게시글 삭제
        boardRepository.deleteById(id);

    }


}














