package com.matzip.repository;


import com.matzip.dto.RestaurantSearchDto;
import com.matzip.dto.MainRestaurantDto;
import com.matzip.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantRepositoryCustom {

    //게시글 조회 조건을 담고있는 RestaurantSearchDto 객체와 페이징 정보를 담고있는
    //pageable 객체를 파라미터로 받는 메소드를 정의한다 . 반환데이터로 Page<Restaurant> 객체를 반환한다.
    //RestaurantRepositoryCustomImpl 클래스로가서 인터페이스를 구현해준다
    Page<Restaurant> getAdminRestaurantPage(RestaurantSearchDto restaurantSearchDto, Pageable pageable);

    //메인페이지에 보여줄 게시글 리스트를 가져오는 메서드
    Page<MainRestaurantDto> getMainRestaurantPage(RestaurantSearchDto restaurantSearchDto, Pageable pageable);

}