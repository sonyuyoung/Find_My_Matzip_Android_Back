package com.matzip.repository;

import com.matzip.entity.Board;
import com.matzip.entity.Restaurant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, String>,
        QuerydslPredicateExecutor<Restaurant>, RestaurantRepositoryCustom{
     Restaurant findByresId(String resId);

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
}
