package com.matzip.repository;


import com.matzip.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long>{

     //팔로잉 리스트 호출
     @Query("select f from Follow f where f.fromUser.userid = :fromUserId")
     List<Follow> findByFromUserId(@Param("fromUserId") String fromUserId);


     //팔로워 리스트 호출
     @Query("select f from Follow f where f.toUser.userid = :toUserId")
     List<Follow> findByToUserId(@Param("toUserId") String toUserId);

     //로그인 유저가 페이지 유저 팔로잉했는지 여부 호출
     @Query("select f from Follow f where f.toUser.userid = :toUserId and f.fromUser.userid = :loginUserId")
     Follow findByToUserIdAndFromUserId(@Param("toUserId") String toUserId,@Param("loginUserId") String loginUserId);

     // 팔로워 수 (follower)
     @Query("select count(f) from Follow f where f.toUser.userid = :toUserId")
     Integer countByToUser(@Param("toUserId") String toUserId);


     // 팔로우 수 (following)
     @Query("select count(f) from Follow f where f.fromUser.userid = :fromUserId")
     Integer countByFromUser(@Param("fromUserId") String fromUserId);


}