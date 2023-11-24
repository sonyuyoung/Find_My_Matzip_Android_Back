package com.matzip.repository;


import com.matzip.constant.BoardViewStatus;
import com.matzip.dto.*;
import com.matzip.entity.Board;
import com.matzip.entity.QBoard;
import com.matzip.entity.QBoardImg;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;


// 무 조 건 파일명 뒤에 Impl 이라고 붙여줘야만 작동함 조심하셈


//보드 리파지토리를 상속받고
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    //동적으로 쿼리를 생성하기 위해서 JPAqueryFactory 클래스를 사용한다.
    private JPAQueryFactory queryFactory;

    //JPAqueryFactory의 생성자로 EntityManager (em) 객체를 넣어준다
    public BoardRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    //게시글 상태조건이 전체(null)일 경우에는 null을 리턴한다
    //결과적으로 게시글 상태조건이 null이 아니라 , VIEW / NOT_VIEW 상태라면 해당 조건의 게시글만 조회한다.
    //이부분을 수정해서 비밀글 로 할 수 있지 않을까 <<<<<<<<<<<<<<<<<<<<<<
    private BooleanExpression searchViewStatusEq(BoardViewStatus searchViewStatus){
        return searchViewStatus == null ? null : QBoard.board.boardViewStatus.eq(searchViewStatus);
    }


    //시간검색조건
    private BooleanExpression regDtsAfter(String searchDateType){

        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null){
            return null;
        } else if(StringUtils.equals("1d", searchDateType)){
            dateTime = dateTime.minusDays(1);
        } else if(StringUtils.equals("1w", searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        } else if(StringUtils.equals("1m", searchDateType)){
            dateTime = dateTime.minusMonths(1);
        } else if(StringUtils.equals("6m", searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }

        return QBoard.board.regTime.after(dateTime);
    }

    //리뷰제목, 리뷰작성자 아이디 에 검색어를 포함하고있는 게시글을 조회하도록 조건값을 반환한다.
    private BooleanExpression searchByLike(String searchBy, String searchQuery){

        if(StringUtils.equals("board_title", searchBy)){
            return QBoard.board.board_title.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createdBy", searchBy)){
            return QBoard.board.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    @Override
    public Page<Board> getAdminBoardPage(BoardSearchDto boardSearchDto, Pageable pageable) {

        // Querydsl
        // 통계 , 복잡한 쿼리 검색시 사용함. 1) 자동완성, 2) 문법체크등, ide 도움을 받기.
        //쿼리팩토리를 이용해서 쿼리 생성한다.
        //게시글 데이터를 조회하기 위해서 QBoard의 board를 지정한다
        QueryResults<Board> results = queryFactory
                .selectFrom(QBoard.board)
                // 조건절을 명시, 별말 없으면, and 조건으로
                .where(regDtsAfter(boardSearchDto.getSearchDateType()),
                        searchViewStatusEq(boardSearchDto.getSearchViewStatus()),
                        searchByLike(boardSearchDto.getSearchBy(),
                                boardSearchDto.getSearchQuery()))
                // 정렬 조건.,  최신 상품 순서로
                .orderBy(QBoard.board.id.desc())
                // 페이징의 페이지 번호 위치. 0
                .offset(pageable.getOffset())
                // 최대로 보여 줄 페이지 갯수, 6개
                .limit(pageable.getPageSize())
                // 호출시, 실행되어서 데이터 받아옴.
                .fetchResults();

        // 검색 조건에 의해서 검색 된 결과 데이터 들(페이징 처리가 됨.)
        List<Board> content = results.getResults();
        // 검색 조건의 결과의 총 갯수.
        long total = results.getTotal();
        // 검색 결과 데이터들과, 페이징의 조건, 전체 갯수를 반환.
        return new PageImpl<>(content, pageable, total);
    } // -> BoardRepository 인터페이스에서 BoardRepositoryCustom 인터페이스를 상속받으러 가자 교재 271p


    //
    private BooleanExpression boardTitleLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QBoard.board.board_title.like("%" + searchQuery + "%");
    }

    @Override
    public Page<MainBoardDto> getMainBoardPage(BoardSearchDto boardSearchDto, Pageable pageable) {
        QBoard board = QBoard.board;
        QBoardImg boardImg = QBoardImg.boardImg;

        QueryResults<MainBoardDto> results = queryFactory
                .select(
                        // @QueryProjection 의 생성자를 이용해서,
                        // 바로 검색 조건으로 자동 매핑을 해줌.
                        new QMainBoardDto(
                                board.id,
                                board.board_title,
                                board.content,
                                boardImg.imgUrl,
                                board.score)
                )
                .from(boardImg)
                .join(boardImg.board, board)
                .where(boardImg.repimgYn.eq("Y"))
                .where(boardTitleLike(boardSearchDto.getSearchQuery()))
                .orderBy(board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainBoardDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }
@Override
public List<MainBoardDto> getMainBoard(BoardSearchDto boardSearchDto) {
    QBoard board = QBoard.board;
    QBoardImg boardImg = QBoardImg.boardImg;

    List<MainBoardDto> results = queryFactory
            .select(
                    new QMainBoardDto(
                            board.id,
                            board.board_title,
                            board.content,
                            boardImg.imgUrl,
                            board.score)
            )
            .from(boardImg)
            .join(boardImg.board, board)
            .where(boardImg.repimgYn.eq("Y"))
            .where(boardTitleLike(boardSearchDto.getSearchQuery()))
            .orderBy(board.id.desc())
            .fetch();

    return results;
}

    @Override
    public Page<MainBoardDto> getBoardPageByResId(BoardSearchDto boardSearchDto, Pageable pageable,String resId) {
        QBoard board = QBoard.board;
        QBoardImg boardImg = QBoardImg.boardImg;

        QueryResults<MainBoardDto> results = queryFactory
                .select(
                        // @QueryProjection 의 생성자를 이용해서,
                        // 바로 검색 조건으로 자동 매핑을 해줌.
                        new QMainBoardDto(
                                board.id,
                                board.board_title,
                                board.content,
                                boardImg.imgUrl,
                                board.score)
                )
                .from(boardImg)
                .join(boardImg.board, board)
                .where(boardImg.repimgYn.eq("Y"))
                .where(boardTitleLike(boardSearchDto.getSearchQuery()))
                .where(board.resId.resId.eq(resId))
                .orderBy(board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainBoardDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainBoardDto> getBoardPageByUserId(BoardSearchDto boardSearchDto, Pageable pageable,String userId) {
        QBoard board = QBoard.board;
        QBoardImg boardImg = QBoardImg.boardImg;

        QueryResults<MainBoardDto> results = queryFactory
                .select(
                        // @QueryProjection 의 생성자를 이용해서,
                        // 바로 검색 조건으로 자동 매핑을 해줌.
                        new QMainBoardDto(
                                board.id,
                                board.board_title,
                                board.content,
                                boardImg.imgUrl,
                                board.score)
                )
                .from(boardImg)
                .join(boardImg.board, board)
                .where(boardImg.repimgYn.eq("Y"))
                .where(boardTitleLike(boardSearchDto.getSearchQuery()))
                .where(board.createdBy.eq(userId))
                .orderBy(board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainBoardDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }


    @Override
    public Page<MainBoardDto> getBoardPageByFollowList(BoardSearchDto boardSearchDto, Pageable pageable,List<String> toUserIdList) {
        QBoard board = QBoard.board;
        QBoardImg boardImg = QBoardImg.boardImg;

        QueryResults<MainBoardDto> results = queryFactory
                .select(
                        // @QueryProjection 의 생성자를 이용해서,
                        // 바로 검색 조건으로 자동 매핑을 해줌.
                        new QMainBoardDto(
                                board.id,
                                board.board_title,
                                board.content,
                                boardImg.imgUrl,
                                board.score)
                )
                .from(boardImg)
                .join(boardImg.board, board)
                .where(boardImg.repimgYn.eq("Y"))
                .where(boardTitleLike(boardSearchDto.getSearchQuery()))
                .where(board.createdBy.in(toUserIdList))
                .orderBy(board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainBoardDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }


}