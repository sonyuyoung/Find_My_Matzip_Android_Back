package com.matzip.repository;


import com.matzip.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

//보드 리파지토리 커스텀을 상속받았다 p271
public interface BoardRepository extends JpaRepository<Board, Long>,
        QuerydslPredicateExecutor<Board>, BoardRepositoryCustom {
    List<Board> findTop10ByOrderByScoreDesc();

    // 유저의 게시글 수
    @Query("select count(b) from Board b where b.createdBy = :userId")
    Integer countByUserId(@Param("userId") String userId);

    //테스트용 코드같은데... 맞다 하지만 이게 없으면 안된다.
    //왜냐 custom인터페이스를 상속받았기 때문에 여기엔 코드가 없어도 됨
    // 게시글 조회조건과 페이지 정보를 파라미터로 받아서 데이터조회하기 위해서 BoardService 110줄로 이동하자

//    List<Board> findByBoard_title(String board_title);
//
//    List<Board> findByItemNmOrItemDetail(String itemNm, String itemDetail);
//
//    List<Board> findByPriceLessThan(Integer price);
//
//    List<Board> findByPriceLessThanOrderByPriceDesc(Integer price);
//
//    @Query("select i from Item i where i.itemDetail like " +
//            "%:itemDetail% order by i.price desc")
//    List<Board> findByItemDetail(@Param("itemDetail") String itemDetail);
//
//    @Query(value="select * from item i where i.item_detail like " +
//            "%:itemDetail% order by i.price desc", nativeQuery = true)
//    List<Board> findByItemDetailByNative(@Param("itemDetail") String itemDetail);

}