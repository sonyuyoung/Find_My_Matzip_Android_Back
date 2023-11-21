package com.matzip.dto;

import com.matzip.entity.Restaurant;
import com.matzip.entity.Restaurant;
import com.matzip.entity.Users;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

//폼에서 입력한 내용(게시글 + 이미지) RestaurantFormDto에 담아서
// -> RestaurantController로 이동
@Getter @Setter
public class RestaurantFormDto {


    private String resId;

    @NotBlank(message = "식당 이름을 입력 바랍니다.")
    private String res_name;//식당 이름

    @NotBlank(message = "구군을 입력 바랍니다.")
    private String res_district; // 구군

    @NotBlank(message = "위도 입력 바랍니다.")
    private String res_lat;//위도

    @NotBlank(message = "경도 입력 바랍니다.")
    private String res_lng;//경도

    @NotBlank(message = "식당주소 입력 바랍니다.")
    private String res_address;//식당 주소

    @NotBlank(message = "식당 연락처 입력 바랍니다.")
    private String res_phone;//연락처

    @NotBlank(message = "운영 및 시간 입력 바랍니다.")
    private String operate_time;//운영 및 시간

    @NotBlank(message = "대표 메뉴 입력 바랍니다.")
    private String res_menu;//대표 메뉴

    @NotBlank(message = "식당 이미지 입력 바랍니다.")
    private String res_image; //식당 이미지 url

    @NotBlank(message = "식당 썸네일 입력 바랍니다.")
    private String res_thumbnail; //썸네일 이미지 url

    @NotBlank(message = "식당 소개 입력 바랍니다.")
    private String res_intro; //가게 소개



    //2. 리뷰 이미지 입력 부분
    private List<RestaurantImgDto> restaurantImgDtoList = new ArrayList<>();

    private List<Long> restaurantImgIds = new ArrayList<>();

    //모델 매퍼 선언
    private static ModelMapper modelMapper = new ModelMapper();

    //폼 데이터 자동 매핑 -> Restaurant객체 생성
    public Restaurant createRestaurant(){
        System.out.println("RestaurantFormDto/createRestaurant 넘어오는거까지는성공");
        Restaurant restaurant = modelMapper.map(this, Restaurant.class);
        System.out.println("modelMapper.map(this, Restaurant.class); 수행 후~~~~~================");
        return restaurant;
    }

    //restaurant객체를 restaurantFormDto로 변환 : service에서 data 전달 시 이용
    public static RestaurantFormDto of(Restaurant restaurant){
        return modelMapper.map(restaurant, RestaurantFormDto.class);
    }

}