package com.matzip.repository;


import com.matzip.dto.BoardSearchDto;
import com.matzip.dto.MainBoardDto;
import com.matzip.dto.NewMainBoardDto;
import com.matzip.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardRepositoryCustom {

    //게시글 조회 조건을 담고있는 BoardSearchDto 객체와 페이징 정보를 담고있는
    //pageable 객체를 파라미터로 받는 메소드를 정의한다 . 반환데이터로 Page<Board> 객체를 반환한다.
    //BoardRepositoryCustomImpl 클래스로가서 인터페이스를 구현해준다
    Page<Board> getAdminBoardPage(BoardSearchDto boardSearchDto, Pageable pageable);

    //메인페이지에 보여줄 게시글을 페이저블로 가져온다
    Page<MainBoardDto> getMainBoardPage(BoardSearchDto boardSearchDto, Pageable pageable);

    Page<NewMainBoardDto> getNewMainBoardPage(BoardSearchDto boardSearchDto, Pageable pageable);

    //검색된 게시글 조회
    Page<MainBoardDto> getSearchMainBoards(BoardSearchDto boardSearchDto, Pageable pageable,String text);


    List<MainBoardDto> getMainBoard(BoardSearchDto boardSearchDto);

    Page<MainBoardDto> getBoardPageByResId(BoardSearchDto boardSearchDto, Pageable pageable,String resId);

    Page<MainBoardDto> getBoardPageByUserId(BoardSearchDto boardSearchDto, Pageable pageable,String userId);

    //메인에서 팔로우한 사람들의 게시글 불러오기
    Page<MainBoardDto> getBoardPageByFollowList(BoardSearchDto boardSearchDto, Pageable pageable, List<String> fromUserIdList);

    Page<NewMainBoardDto> getNewBoardPageByFollowList(BoardSearchDto boardSearchDto, Pageable pageable, List<String> fromUserIdList);

}