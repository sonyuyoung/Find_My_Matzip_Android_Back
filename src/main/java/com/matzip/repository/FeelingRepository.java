package com.matzip.repository;


import com.matzip.entity.Feeling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeelingRepository extends JpaRepository<Feeling, Long>{

     //팔로잉 리스트 호출
     @Query("select f " +
             "from Feeling f " +
             "where f.feelingBoard.id = :boardId and f.feelingUsers.userid = :usersId")
     Feeling fingByUsersIdAndBoardId(@Param("boardId") Long boardId,@Param("usersId") String usersId);

     //좋아요 or 싫어요 수
     //feelNum -> 1:좋아요, -1:싫어요
     @Query("select count(f) " +
             "from Feeling f " +
             "where f.feelingBoard.id = :boardId and f.feelNum = :feelNum")
     int countFeeling(@Param("boardId") Long boardId,@Param("feelNum") int feelNum);








}