package com.matzip.dto;

import com.matzip.entity.Restaurant;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Getter @Setter
public class RestaurantDto {

    private Long resId;       //식당 id

    private String res_name;//식당 이름

    private String res_district;//구군

    private String res_lat;//위도

    private String res_lng;//경도

    private String res_address;//식당 주소

    private String res_phone;//연락처

    private String operate_time;//운영 및 시간

    private String res_menu;//대표 메뉴

    private String res_image; //식당 이미지 url

    private String res_thumbnail; //썸네일 이미지 url

    private String res_intro; //가게 소개

    private Double avgScore;     // 평균 평점

    private static ModelMapper modelMapper = new ModelMapper();

    public RestaurantDto(Long resId, String res_thumbnail, String res_name, Double avgScore) {
        this.resId = resId;
        this.res_thumbnail = res_thumbnail;
        this.res_name = res_name;
        this.avgScore = avgScore;
    }

    public RestaurantDto() {

    }

    public RestaurantDto(Long resId, String res_name, String res_district, String res_lat, String res_lng, String res_address, String res_phone, String operate_time, String res_menu, String res_thumbnail, String res_intro, Double avgScore) {
        this.resId = resId;
        this.res_name = res_name;
        this.res_district = res_district;
        this.res_lat = res_lat;
        this.res_lng = res_lng;
        this.res_address = res_address;
        this.res_phone = res_phone;
        this.operate_time = operate_time;
        this.res_menu = res_menu;
        this.res_thumbnail = res_thumbnail;
        this.res_intro = res_intro;
        this.avgScore = avgScore;
    }


    public static RestaurantDto of(Restaurant restaurant) {
        return modelMapper.map(restaurant, RestaurantDto.class);
    }

    public  static RestaurantDto restaurantDto(Restaurant restaurant){
        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setResId(restaurant.getResId());
        restaurantDto.setRes_name(restaurant.getRes_name());
        restaurantDto.setRes_district(restaurant.getRes_district());
        restaurantDto.setRes_lat(restaurant.getRes_lat());
        restaurantDto.setRes_lng(restaurant.getRes_lng());
        restaurantDto.setRes_address(restaurant.getRes_address());
        restaurantDto.setRes_phone(restaurant.getRes_phone());
        restaurantDto.setOperate_time(restaurant.getOperate_time());
        restaurantDto.setRes_menu(restaurant.getRes_menu());
        restaurantDto.setRes_image(restaurant.getRes_image());
        restaurantDto.setRes_thumbnail(restaurant.getRes_thumbnail());
        restaurantDto.setRes_intro(restaurant.getRes_intro());

        return restaurantDto;

    }

    //1125 데이터 끌어오기위해 추가한 dto
//    @GetMapping(value = "/board/{boardId}")
//    public Map<String,Object> boardDtl(Model model, @PathVariable("boardId") Long boardId){
    public void RestaurantDto2(Restaurant restaurant) {
        this.resId = restaurant.getResId();
        this.res_name = restaurant.getRes_name();
        this.res_district = restaurant.getRes_district();
        this.res_lat = restaurant.getRes_lat();
        this.res_lng = restaurant.getRes_lng();
        this.res_address = restaurant.getRes_address();
        this.res_phone = restaurant.getRes_phone();
        this.operate_time = restaurant.getOperate_time();
        this.res_menu = restaurant.getRes_menu();
        this.res_image = restaurant.getRes_image();
        this.res_thumbnail = restaurant.getRes_thumbnail();
        this.res_intro = restaurant.getRes_intro();
    }

    public void setAvgScore(Double avgScore) {
        this.avgScore = avgScore;
    }
}