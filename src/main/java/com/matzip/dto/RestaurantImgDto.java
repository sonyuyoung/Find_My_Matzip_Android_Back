package com.matzip.dto;

import com.matzip.entity.BoardImg;
import com.matzip.entity.Restaurant;
import com.matzip.entity.RestaurantImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter
public class RestaurantImgDto {

    private Long res_img_id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();

    public static RestaurantImgDto of(RestaurantImg restaurantImg) {
        return modelMapper.map(restaurantImg, RestaurantImgDto.class);
    }

}