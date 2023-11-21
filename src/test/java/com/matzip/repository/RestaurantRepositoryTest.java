package com.matzip.repository;

import com.matzip.entity.Restaurant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class RestaurantRepositoryTest {
    @Autowired
    RestaurantRepository restaurantRepository;

    @Test
    @DisplayName("식당저장테스트")
    public void createRestaurantTest(){
        Restaurant restaurant = new Restaurant();
        restaurant.setRes_name("돌곱창");
        restaurant.setRes_district("강서구");
        restaurant.setRes_lat("35.028156");
        restaurant.setRes_lng("128.81578");
        restaurant.setRes_address("강서구 가덕해안로 787-1");
        restaurant.setRes_phone("051-971-9925");
        restaurant.setOperate_time("11:00-15:30");
        restaurant.setRes_menu("블랙 쉬림프 버거, 클래식버거");
        restaurant.setRes_image("https://www.visitbusan.net/uploadImgs/files/cntnts/20230601155348503_ttiel");
        restaurant.setRes_thumbnail("https://www.visitbusan.net/uploadImgs/files/cntnts/20230601155348503_thumbL");
        restaurant.setRes_intro("조용한 어촌마을에 자리한 양식당으로 아담한 정원과 엔틱풍의 인테리어가 돋보인다.직접 구운 번으로 만든 수제버거가 대표메뉴이며 필라프, 샐러드 외에다양한 음료도 준비되어 있는 곳");
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        System.out.println(savedRestaurant.toString());
    }
}