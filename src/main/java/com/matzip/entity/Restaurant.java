package com.matzip.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.matzip.dto.RestaurantFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="restaurant")
@Getter
@Setter
@ToString
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // 프록시 객체를 직렬화에서 제외  대신에 레스토랑dto2만듦
public class Restaurant {

    @Id
    @Column(name="res_id", unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long resId;       //식당 id

    @Column(name="res_name", nullable = false)
    private String res_name;//식당 이름

    @Column
    private String res_district;//구군

    @Column
    private String res_lat;//위도

    @Column
    private String res_lng;//경도

    @Column
    private String res_address;//식당 주소

    @Column
    private String res_phone;//연락처

    @Column
    private String operate_time;//운영 및 시간

    @Column
    private String res_menu;//대표 메뉴

    @Column
    private String res_image; //식당 이미지 url

    @Column
    private String res_thumbnail; //썸네일 이미지 url

    @Column
    private String res_intro; //가게 소개

    @JsonIgnore
    @OneToMany(mappedBy = "restaurant")
    private List<Board> boards ;     // 레스토랑과 관련된 게시글 목록

    @Column
    private String avg_score; //평균 평점

    public static Restaurant createRestaurant(RestaurantFormDto restaurantFormDto) {
        Restaurant restaurant = new Restaurant();
        restaurant.setResId(restaurantFormDto.getResId());
        restaurant.setRes_name(restaurantFormDto.getRes_name());
        restaurant.setRes_district(restaurantFormDto.getRes_district());
        restaurant.setRes_lat(restaurantFormDto.getRes_lat());
        restaurant.setRes_lng(restaurantFormDto.getRes_lng());
        restaurant.setRes_address(restaurantFormDto.getRes_address());
        restaurant.setRes_phone(restaurantFormDto.getRes_phone());
        restaurant.setOperate_time(restaurantFormDto.getOperate_time());
        restaurant.setRes_menu(restaurantFormDto.getRes_menu());
        restaurant.setRes_image(restaurantFormDto.getRes_image());
        restaurant.setRes_thumbnail(restaurantFormDto.getRes_thumbnail());
        restaurant.setRes_intro(restaurantFormDto.getRes_intro());
        return restaurant;
    }

}