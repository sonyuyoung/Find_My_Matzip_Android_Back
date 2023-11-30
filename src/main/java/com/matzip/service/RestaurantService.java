package com.matzip.service;

import com.matzip.dto.*;
import com.matzip.entity.Restaurant;
import com.matzip.entity.RestaurantImg;
import com.matzip.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantImgService restaurantImgService;
    private final FileService fileService;

    public List<RestaurantDto> findAll(){
        List<Restaurant> restaurantEntityList = restaurantRepository.findAll();
        List<RestaurantDto> restaurantDtoList = new ArrayList<>();;
        for (Restaurant restaurant : restaurantEntityList) {
            restaurantDtoList.add(RestaurantDto.restaurantDto(restaurant));
        }
        return restaurantDtoList;
    }

    public RestaurantDto findById(String resId) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(resId);
        if (optionalRestaurant.isPresent()){
            return RestaurantDto.restaurantDto(optionalRestaurant.get());
        } else {
            return null;
        }
    }

    //게시글 저장하기
    public String saveRestaurant(RestaurantFormDto restaurantFormDto, List<MultipartFile> restaurantImgFileList) throws Exception{

        System.out.println("여기서부터 오류발생 레스토랑서비스,,, 세이브레스토랑 시작");
        //게시글 등록
        Restaurant restaurant = restaurantFormDto.createRestaurant();
        System.out.println("레스토랑생성완료");
        restaurantRepository.save(restaurant);
        System.out.println("레스토랑저장완료");

        //이미지 등록
        for(int i=0;i<restaurantImgFileList.size();i++){
            RestaurantImg restaurantImg = new RestaurantImg();
            restaurantImg.setRestaurant(restaurant);

            if(i == 0)
                restaurantImg.setRepimgYn("Y");
            else
                restaurantImg.setRepimgYn("N");

            restaurantImgService.saveRestaurantImg(restaurantImg, restaurantImgFileList.get(i));
        }

        return restaurant.getResId();
    }

    // // 게시글 조회조건과 페이지 정보를 파라미터로 받아서 데이터조회하는 getAdminBoardPage() 메소드를 추가했다
    //데이터의 수정이 일어나지않고 조회만 하기때문에 readOnly 어노테이션 설정
    //BoardController로 이동해서 게시글 관리 화면 및 조회한 게시글 데이터를 화면에 전달하는 로직을 구현하자 108줄
    @Transactional(readOnly = true)
    public Page<Restaurant> getAdminRestaurantPage(RestaurantSearchDto restaurantSearchDto, Pageable pageable){
        return restaurantRepository.getAdminRestaurantPage(restaurantSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainRestaurantDto> getMainRestaurantPage(RestaurantSearchDto restaurantSearchDto, Pageable pageable){
        return restaurantRepository.getMainRestaurantPage(restaurantSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public RestaurantFormDto getRestaurantDtl(String resId){
        //해당 게시글의 이미지 조회, 등록순으로 가지고 오기 위해서 게시글이미지 아이디를 오름차순으로 가지고온다
        List<RestaurantImgDto> restaurantImgDtoList = new ArrayList<>();
        //식당 아이디를 통해  엔티티를 조회 . 존재하지않으면 오류 발생시키기
        Restaurant restaurant = restaurantRepository.findById(resId)
                .orElseThrow(EntityNotFoundException::new);
        RestaurantFormDto restaurantFormDto = RestaurantFormDto.of(restaurant);
        restaurantFormDto.setRestaurantImgDtoList(restaurantImgDtoList);
        return restaurantFormDto;
    }


    public List<RestaurantDto> getTop3RestaurantsByAvgScore() {
        Pageable pageable = PageRequest.of(0, 3); // 페이지 0, 사이즈 3인 페이지를 요청
        List<Object[]> ranking = restaurantRepository.findTopNByOrderByAvgScoreDesc(pageable);
        return convertToRestaurantDtoList(ranking);
    }


    //페이징 된 리스트
    public List<RestaurantDto> getAllPageRestaurantsByAvgScore(Pageable pageable) {
        //Pageable pageable = PageRequest.of(0, 271); // 전체식당 평점조회
        List<Object[]> ranking = restaurantRepository.findAllByOrderByAvgScoreDesc(pageable);
        return convertToRestaurantDtoList2(ranking);
    }

    //페이징x
    public List<RestaurantDto> getAllRestaurantsByAvgScore() {
        Pageable pageable = PageRequest.of(0, 271); // 전체식당 평점조회
        List<Object[]> ranking = restaurantRepository.findAllByOrderByAvgScoreDesc(pageable);
        return convertToRestaurantDtoList2(ranking);
    }

    public List<RestaurantDto> getSearchRestaurantsByAvgScore(Pageable pageable,String text) {
        //Pageable pageable = PageRequest.of(0, 271); // 검색된식당 평점조회
        List<Object[]> ranking = restaurantRepository.findSearchByOrderByAvgScoreDesc(pageable,text);
        return convertToRestaurantDtoList2(ranking);
    }

    private List<RestaurantDto> convertToRestaurantDtoList(List<Object[]> ranking) {
        // 변환 로직 구현
        List<RestaurantDto> restaurantDtoList = new ArrayList<>();
        for (Object[] result : ranking) {
            restaurantDtoList.add(new RestaurantDto((String) result[0], (String) result[1], (String) result[2], (Double) result[3]));
        }
        return restaurantDtoList;
    }

    private List<RestaurantDto> convertToRestaurantDtoList2(List<Object[]> ranking) {
        // 변환 로직 구현
        List<RestaurantDto> restaurantDtoList = new ArrayList<>();
        for (Object[] result : ranking) {
            restaurantDtoList.add(new RestaurantDto((String) result[0], (String) result[1], (String) result[2], (String) result[3], (String) result[4], (String) result[5], (String) result[6], (String) result[7], (String) result[8], (String) result[9], (String) result[10], (Double) result[11]));
        }
        return restaurantDtoList;
    }

    public Double getAverageScoreByResId(String resId) {
        return restaurantRepository.findAverageScoreByResId(resId);
    }



}