package com.matzip.controller;

import com.matzip.dto.*;
import com.matzip.entity.Restaurant;
import com.matzip.service.BoardService;
import com.matzip.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final BoardService boardService;

    @GetMapping("/ranking")
    public List<RestaurantDto>  getTop3RestaurantsByAvgScore() {
        return restaurantService.getTop3RestaurantsByAvgScore();
    }

    //전체(페이징x)
    @GetMapping("/reswithscore")
    public List<RestaurantDto>  getAllRestaurantsByAvgScore() {
        return restaurantService.getAllRestaurantsByAvgScore();
    }

    //식당삭제
    @DeleteMapping(value = "/restaurant/{resId}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable Long resId) {
        boolean deleted = restaurantService.deleteRestaurantById(resId);
        if (deleted) {
            return ResponseEntity.ok("Restaurant with ID " + resId + " has been deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Restaurant with ID " + resId + " not found.");
        }
    }


    //페이징
    @GetMapping("/reswithscorePage")
    public ResponseEntity<List<RestaurantDto>>  getAllPageRestaurantsByAvgScore(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        List<RestaurantDto> resPage = restaurantService.getAllPageRestaurantsByAvgScore(pageable);
        return ResponseEntity.ok(resPage);
    }
    //    @GetMapping("/reswithscorePage")
//    public ResponseEntity<List<RestaurantDto>> getAllPageRestaurantsByAvgScore(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "6") int size
//    ) {
//        // 원래의 페이지 요청 객체 생성
//        Pageable originalPageable = PageRequest.of(page, size);
//
//        // 홀수 페이지를 가져옴
//        List<RestaurantDto> oddPage = restaurantService.getAllPageRestaurantsByAvgScore(originalPageable);
//
//        // 짝수 페이지를 가져오기 위해 페이지 번호를 1 증가시킨 새로운 페이지 요청 객체 생성
//        Pageable evenPageable = PageRequest.of(page + 1, size);
//        List<RestaurantDto> evenPage = restaurantService.getAllPageRestaurantsByAvgScore(evenPageable);
//
//        // 홀수 페이지와 짝수 페이지를 합쳐서 결과 반환
//        List<RestaurantDto> resPage = new ArrayList<>(oddPage);
//        resPage.addAll(evenPage);
//
//        return ResponseEntity.ok(resPage);
//    }

    //검색 결과 페이징
    @GetMapping("/reswithscore/{text}")
    public ResponseEntity<List<RestaurantDto>>  getSearchRestaurantsByAvgScore(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @PathVariable String text) {
        Pageable pageable = PageRequest.of(page, size);
        List<RestaurantDto> resPage = restaurantService.getSearchRestaurantsByAvgScore(pageable,text);
        return ResponseEntity.ok(resPage);
    }


    @GetMapping("/map")
    public String findAll(Model model){
        List<RestaurantDto> restaurantDtoList = restaurantService.findAll();
        model.addAttribute("restaurantList",restaurantDtoList);
        return "map/mapForm";
    }

    @PostMapping(value = "/restaurant/new")
    public void newRestaurant(@RequestBody RestaurantFormDto restaurantFormDto){
        Long resId = restaurantFormDto.getResId();
        Restaurant restaurant = Restaurant.createRestaurant(restaurantFormDto);
        restaurantFormDto.setResId(resId);
        restaurantService.saveRestaurant(restaurant);
    }

    @GetMapping(value = "/restaurant/main")
    public List<RestaurantDto> restaurantMain(){
        return restaurantService.findAll();

    }

    //원래 이걸로 하려고했는데 불러오는걸 못해서 바꿈 ,,, 확인해볼것
//    @GetMapping(value = "/restaurant/main")
//    public String restaurantMain(RestaurantSearchDto restaurantSearchDto, Optional<Integer> page, Model model){
//
//        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
//        Page<MainRestaurantDto> restaurants = restaurantService.getMainRestaurantPage(restaurantSearchDto, pageable);
//
//        model.addAttribute("restaurants", restaurants);
//        model.addAttribute("restaurantSearchDto", restaurantSearchDto);
//        model.addAttribute("maxPage", 5);
//
//        return "/restaurant/restaurant";
//    }

//    //식당상세페이지 매핑
//    @GetMapping(value = "/restaurant/{resId}")
//    public String restaurantDtl(Model model, @PathVariable("resId") Long resId){
//        RestaurantFormDto restaurantFormDto = restaurantService.getRestaurantDtl(resId);
//        model.addAttribute("restaurant", restaurantFormDto);
//        return "restaurant/restaurantDtl";
//    }

    //식당상세페이지 리뷰추가 매핑...
    // 근데 이거 뒤에 review 안붙어도 나오게 하고싶다. 그런데 그렇게하면
    // 리뷰 폼 등록할 때도 나오지 않을까...
//    @GetMapping(value = "/restaurant/{resId}")
//    public String sumResRivew(Model model, @PathVariable("resId") Long resId,Optional<Integer> page,BoardSearchDto boardSearchDto){
//        RestaurantFormDto restaurantFormDto = restaurantService.getRestaurantDtl(resId);
//        model.addAttribute("restaurant", restaurantFormDto);
//        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
//        /*Page<MainBoardDto> boards = boardService.getMainBoardPage(boardSearchDto, pageable);*/
//        Page<MainBoardDto> boards = boardService.getBoardPageByResId(boardSearchDto, pageable,resId);
//        model.addAttribute("boards", boards);
//        model.addAttribute("boardSearchDto", boardSearchDto);
//        model.addAttribute("maxPage", 5);
//        return "restaurant/restaurant_review";
//    }
    @GetMapping(value = "/restaurant/{resId}")
    public ResponseEntity<?> getRestaurantDetails(@PathVariable("resId") Long resId, BoardSearchDto boardSearchDto) {
        RestaurantFormDto restaurantFormDto = restaurantService.getRestaurantDtl(resId);

        //식당평균평점을 추가
        Double avgScore = restaurantService.getAverageScoreByResId(restaurantFormDto.getResId());
        restaurantFormDto.setAvgScore(avgScore);

        // 유의미한 응답을 반환하기 위해 데이터를 JSON 형식으로 매핑하여 반환합니다.
        return ResponseEntity.ok(restaurantFormDto);
    }


    //게시글 관리 화면 및 조회한 게시글 데이터를 화면에 전달하는 로직을 구현
    //value에 상품 관리 화면 진입 시 URL에 페이지 번호가 없는 경우와 페이지 번호가 있는 경우 2가지를 매핑한다.
    @GetMapping(value = {"/restaurants", "/restaurants/{page}"})
    public String restaurantManage(RestaurantSearchDto restaurantSearchDto, @PathVariable("page") Optional<Integer> page, Model model){

        //페이징을 위해서 PageRequest.of 메서드를 통해 Pageable 객체생성
        //첫번째 파라미터로는 조회할페이지번호 / 두번째 파라미터로는 한번에 가져올 데이터 수를 넣어준다
        //URL경로에 페이지 번호가 있으면 해당 페이지를 조회하도록 세팅하고 페이지 번호가 없으면 0페이지를 조회하도록한다.
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        //조회조건과 페이징 정보를 파라미터로 넘겨서 Page<Restaurant> 객체를 반환 받는다.
        Page<Restaurant> restaurants = restaurantService.getAdminRestaurantPage(restaurantSearchDto, pageable);

        //조회한 게시글 데이터 및 페이징 정보를 뷰에 전달
        model.addAttribute("restaurants", restaurants);
        //페이지 전환시 기존 검색조건을 유지한 채 이동할 수 있도록 뷰에 다시 전달
        model.addAttribute("restaurantSearchDto", restaurantSearchDto);
        //게시글 관리 메뉴 하단에 보여줄 페이지 번호의 최대 개수. 5로 설정했으므로 최대 5개의 이동할 페이지 번호만 보여준다.
        model.addAttribute("maxPage", 5);

        //template/restaurant 폴더 아래에 restaurantMng.html 파일을 생성한다.
        return "restaurant/restaurantMng";
    }



}