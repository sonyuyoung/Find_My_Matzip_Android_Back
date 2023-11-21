package com.matzip.repository;


import com.matzip.entity.BoardImg;
import com.matzip.entity.RestaurantImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantImgRepository extends JpaRepository<RestaurantImg, Long> {

//    List<BoardImg> findByBoardIdOrderByIdAsc(Long boardId);

}