package com.matzip.repository;

import com.matzip.entity.Board;
import com.matzip.entity.Restaurant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>,
        QuerydslPredicateExecutor<Restaurant>, RestaurantRepositoryCustom{
     Restaurant findByResId(Long resId);

//     @Query("SELECT r.resId, r.res_name, AVG(b.score) as avgScore " +
//             "FROM Restaurant r JOIN r.boards b " +
//             "GROUP BY r.resId, r.res_name " +
//             "ORDER BY avgScore DESC")
//     List<Object[]> findTopNByOrderByAvgScoreDesc(int n);
@Query("SELECT r.resId, r.res_thumbnail, r.res_name, AVG(b.score) as avgScore " +
        "FROM Restaurant r " +
        "JOIN r.boards b " +
        "GROUP BY r.resId, r.res_name " +
        "ORDER BY avgScore DESC")
List<Object[]> findTopNByOrderByAvgScoreDesc(Pageable pageable);

     @Query("SELECT r.resId, r.res_name, r.res_district, r.res_lat, r.res_lng, r.res_address, r.res_phone, r.operate_time, r.res_menu, r.res_thumbnail, r.res_intro, COALESCE(AVG(b.score), 0.0) as avgScore " +
             "FROM Restaurant r " +
             "LEFT JOIN r.boards b " +  // Use LEFT JOIN to include restaurants with no boards
             "GROUP BY r.resId, r.res_name")
     List<Object[]> findAllByOrderByAvgScoreDesc(Pageable pageable);

     @Query("SELECT r.resId, r.res_name, r.res_district, r.res_lat, r.res_lng, r.res_address, r.res_phone, r.operate_time, r.res_menu, r.res_thumbnail, r.res_intro, COALESCE(AVG(b.score), 0.0) as avgScore " +
             "FROM Restaurant r " +
             "LEFT JOIN r.boards b " +  // Use LEFT JOIN to include restaurants with no boards
             "WHERE r.res_name like %:text%  or r.res_menu like %:text% or r.res_address like %:text% "+
             "GROUP BY r.resId, r.res_name")
     List<Object[]> findSearchByOrderByAvgScoreDesc2(Pageable pageable, @Param("text") String text);

     //검색된 식당 조회
     @Query("SELECT r.resId, r.res_name, r.res_district, r.res_lat, r.res_lng, r.res_address, r.res_phone, r.operate_time, r.res_menu, r.res_thumbnail, r.res_intro,  AVG(b.score) as avgScore " +
             "FROM Restaurant r " +
             "JOIN r.boards b " +
             "WHERE r.res_name like %:text%  or r.res_menu like %:text% or r.res_address like %:text% "+
             "GROUP BY r.resId, r.res_name "+
             "ORDER BY avgScore DESC")
     List<Object[]> findSearchByOrderByAvgScoreDesc(Pageable pageable, @Param("text") String text);

     @Query("SELECT AVG(b.score) FROM Restaurant r JOIN r.boards b WHERE r.resId = :resId")
     Double findAverageScoreByResId(@Param("resId") Long resId);


     // Restaurant findByResAddress(String res_address);

}

