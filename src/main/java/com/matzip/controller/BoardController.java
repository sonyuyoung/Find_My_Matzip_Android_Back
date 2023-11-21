package com.matzip.controller;


import com.matzip.dto.BoardFormDto;
import com.matzip.dto.BoardSearchDto;
import com.matzip.dto.RestaurantFormDto;
import com.matzip.entity.Board;
import com.matzip.entity.Restaurant;
import com.matzip.entity.Users;
import com.matzip.service.BoardService;
import com.matzip.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final RestaurantService restaurantService;

    @GetMapping(value = {"/board/new","/board/new/{resId}"})
    public String boardForm(@PathVariable(name ="resId", required = false) String resId,Model model){
        //식당검색해서 리뷰등록 누른 경우
        if(resId != null){
            BoardFormDto boardFormDto = new BoardFormDto();
            boardFormDto.setResId(resId);
            model.addAttribute("boardFormDto",boardFormDto);
            //리뷰작성페이지에 식당상세보기위해서 추가
            RestaurantFormDto restaurantFormDto = restaurantService.getRestaurantDtl(resId);
            model.addAttribute("restaurant", restaurantFormDto);
        }
        // 메뉴에서 리뷰등록 누른 경우(나중에 삭제 요망)
        else{
            model.addAttribute("boardFormDto",new BoardFormDto());
        }


        return "board/boardForm";
    }

    @PostMapping(value = "/board/new")
    public String boardNew(@Valid BoardFormDto boardFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("boardImgFile") List<MultipartFile> boardImgFileList){

        if(bindingResult.hasErrors()){
            return "board/boardForm";
        }

        if(boardImgFileList.get(0).isEmpty() && boardFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 리뷰 이미지는 필수 입력 값 입니다.");
            return "board/boardForm";
        }

        try {
            boardService.saveBoard(boardFormDto, boardImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "리뷰 등록 중 에러가 발생하였습니다.");
            return "board/boardForm";
        }

        return "redirect:/";
    }

    //게시글 수정 -> 상품이미지 수정을 위해서 BoardImgService 이동하자


    //게시글상세페이지 매핑
    @GetMapping(value = "/admin/board/{boardId}")
    public String boardDtl(@PathVariable("boardId") Long boardId, Model model){

        try {
            BoardFormDto boardFormDto = boardService.getBoardDtl(boardId);
            model.addAttribute("boardFormDto", boardFormDto);
        } catch(EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
            model.addAttribute("boardFormDto", new BoardFormDto());
            return "board/boardForm";
        }

        return "board/boardForm";
    }

    //게시글을 수정하는 URL을 추가 ->상품상세페이지에 진입하기 위해서 boardSerchDto를 추가
    @PostMapping(value = "/board/{boardId}")
    public String boardUpdate(@Valid BoardFormDto boardFormDto, BindingResult bindingResult,
                             @RequestParam("boardImgFile") List<MultipartFile> boardImgFileList, Model model){
        if(bindingResult.hasErrors()){
            return "board/boardForm";
        }

        if(boardImgFileList.get(0).isEmpty() && boardFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 리뷰 이미지는 필수 입력 값 입니다.");
            return "board/boardForm";
        }

        try {
            boardService.updateBoard(boardFormDto, boardImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "리뷰 수정 중 에러가 발생하였습니다.");
            return "board/boardForm";
        }

        return "redirect:/";
    }

    //게시글 관리 화면 및 조회한 게시글 데이터를 화면에 전달하는 로직을 구현
    //value에 상품 관리 화면 진입 시 URL에 페이지 번호가 없는 경우와 페이지 번호가 있는 경우 2가지를 매핑한다.
    @GetMapping(value = {"/admin/boards", "/admin/boards/{page}"})
    public String boardManage(BoardSearchDto boardSearchDto, @PathVariable("page") Optional<Integer> page, Model model){

        //페이징을 위해서 PageRequest.of 메서드를 통해 Pageable 객체생성
        //첫번째 파라미터로는 조회할페이지번호 / 두번째 파라미터로는 한번에 가져올 데이터 수를 넣어준다
        //URL경로에 페이지 번호가 있으면 해당 페이지를 조회하도록 세팅하고 페이지 번호가 없으면 0페이지를 조회하도록한다.
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        //조회조건과 페이징 정보를 파라미터로 넘겨서 Page<Board> 객체를 반환 받는다.
        Page<Board> boards = boardService.getAdminBoardPage(boardSearchDto, pageable);

        //조회한 게시글 데이터 및 페이징 정보를 뷰에 전달
        model.addAttribute("boards", boards);
        //페이지 전환시 기존 검색조건을 유지한 채 이동할 수 있도록 뷰에 다시 전달
        model.addAttribute("boardSearchDto", boardSearchDto);
        //게시글 관리 메뉴 하단에 보여줄 페이지 번호의 최대 개수. 5로 설정했으므로 최대 5개의 이동할 페이지 번호만 보여준다.
        model.addAttribute("maxPage", 5);

        //template/board 폴더 아래에 boardMng.html 파일을 생성한다.
        return "board/boardMng";
    }
    //게시글 상세페이지
    //상품을 가지고 오는 로직을 똑같이 사용
    //-> boardDtl로 가자
    @GetMapping(value = "/board/{boardId}")
    public String boardDtl(Model model, @PathVariable("boardId") Long boardId){

        BoardFormDto boardFormDto = boardService.getBoardDtl(boardId);
        Users users = boardService.getUserByCreated(boardFormDto.getUser_id());
        System.out.println("boardFormDto.getResId() : "+boardFormDto.getResId());
        Restaurant restaurant = boardService.getBoardByResId(boardFormDto.getResId());

        model.addAttribute("users",users);
        model.addAttribute("board", boardFormDto);
        model.addAttribute("restaurant", restaurant);
        System.out.println("------------------------------" + restaurant.getResId());
        System.out.println("------------------------------" + restaurant.getResId());
        System.out.println("------------------------------" + restaurant.getResId());
        System.out.println("------------------------------" + restaurant.getResId());System.out.println("------------------------------" + restaurant.getResId());
        System.out.println("------------------------------" + restaurant.getResId());


        return "board/boardDtl";
    }

    @GetMapping("/admin/board/delete/{boardId}")
    public String deleteBoard(@PathVariable Long boardId, Model model) {
        try {
            // 리뷰 ID를 사용하여 리뷰를 삭제
            boardService.deleteBoard(boardId);
            model.addAttribute("successMessage", "리뷰가 성공적으로 삭제되었습니다.");
        } catch (EntityNotFoundException e) {
            // 삭제할 리뷰를 찾지 못한 경우
            model.addAttribute("errorMessage", "삭제할 리뷰를 찾지 못했습니다.");
        } catch (Exception e) {
            // 기타 예외 처리
            model.addAttribute("errorMessage", "리뷰 삭제 중 오류가 발생했습니다.");
        }

        // 삭제 후 다시 리뷰 목록 페이지로 리다이렉트
        return "redirect:/admin/boards";
        //여기 /boards/면 안될 것 같아서 board로 수정
    }



}