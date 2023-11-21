package com.matzip.repository;


import com.matzip.dto.RestaurantSearchDto;
import com.matzip.dto.MainRestaurantDto;
import com.matzip.dto.QMainRestaurantDto;
import com.matzip.entity.QBoard;
import com.matzip.entity.Restaurant;
import com.matzip.entity.QRestaurant;
import com.matzip.entity.QRestaurantImg;
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

public class RestaurantRepositoryCustomImpl implements RestaurantRepositoryCustom {

    //동적으로 쿼리를 생성하기 위해서 JPAqueryFactory 클래스를 사용한다.
    private JPAQueryFactory queryFactory;

    //JPAqueryFactory의 생성자로 EntityManager (em) 객체를 넣어준다
    public RestaurantRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    //식당이름, 식당설명, 식당대표메뉴, 식당주소에 검색어를 포함하고있는 게시글을 조회하도록 조건값을 반환한다.
    private BooleanExpression searchByLike(String searchBy, String searchQuery){

        if(StringUtils.equals("res_name", searchBy)){
            return QRestaurant.restaurant.res_name.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("res_intro", searchBy)){
            return QRestaurant.restaurant.res_intro.like("%" + searchQuery + "%");
        }else if(StringUtils.equals("res_menu", searchBy)){
            return QRestaurant.restaurant.res_menu.like("%" + searchQuery + "%");
        }else if(StringUtils.equals("res_address", searchBy)){
            return QRestaurant.restaurant.res_address.like("%" + searchQuery + "%");
        }

        return null;
    }



    @Override
    public Page<Restaurant> getAdminRestaurantPage(RestaurantSearchDto restaurantSearchDto, Pageable pageable) {

        // Querydsl
        // 통계 , 복잡한 쿼리 검색시 사용함. 1) 자동완성, 2) 문법체크등, ide 도움을 받기.
        //쿼리팩토리를 이용해서 쿼리 생성한다.
        //게시글 데이터를 조회하기 위해서 QRestaurant의 restaurant를 지정한다
        QueryResults<Restaurant> results = queryFactory
                .selectFrom(QRestaurant.restaurant)
                // 조건절을 명시, 별말 없으면, and 조건으로
                .where(searchByLike(restaurantSearchDto.getSearchBy(),
                                restaurantSearchDto.getSearchQuery()))
                // 정렬 조건.,  최신 상품 순서로
                .orderBy(QRestaurant.restaurant.resId.desc())
                // 페이징의 페이지 번호 위치. 0
                .offset(pageable.getOffset())
                // 최대로 보여 줄 페이지 갯수, 6개
                .limit(pageable.getPageSize())
                // 호출시, 실행되어서 데이터 받아옴.
                .fetchResults();

        // 검색 조건에 의해서 검색 된 결과 데이터 들(페이징 처리가 됨.)
        List<Restaurant> content = results.getResults();
        // 검색 조건의 결과의 총 갯수.
        long total = results.getTotal();
        // 검색 결과 데이터들과, 페이징의 조건, 전체 갯수를 반환.
        return new PageImpl<>(content, pageable, total);
    } // -> RestaurantRepository 인터페이스에서 RestaurantRepositoryCustom 인터페이스를 상속받으러 가자 교재 271p


    //
    private BooleanExpression restaurantNameLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QRestaurant.restaurant.res_name.like("%" + searchQuery + "%");
    }

    @Override
    public Page<MainRestaurantDto> getMainRestaurantPage(RestaurantSearchDto restaurantSearchDto, Pageable pageable) {
        QRestaurant restaurant = QRestaurant.restaurant;
        QRestaurantImg restaurantImg = QRestaurantImg.restaurantImg;

        QueryResults<MainRestaurantDto> results = queryFactory
                .select(
                        // @QueryProjection 의 생성자를 이용해서,
                        // 바로 검색 조건으로 자동 매핑을 해줌.
                        new QMainRestaurantDto(
                                restaurant.resId,
                                restaurant.res_name,
                                restaurant.res_thumbnail,
                                restaurant.res_menu)
                )
                .from(restaurantImg)
                .join(restaurantImg.restaurant, restaurant)
                .where(restaurantImg.repimgYn.eq("Y"))
                .where(restaurantNameLike(restaurantSearchDto.getSearchQuery()))
                .orderBy(restaurant.resId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainRestaurantDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }


}