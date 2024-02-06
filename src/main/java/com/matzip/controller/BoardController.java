package com.matzip.controller;


import com.matzip.constant.BoardViewStatus;
import com.matzip.dto.*;
import com.matzip.entity.*;
import com.matzip.service.*;
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

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final RestaurantService restaurantService;
    private final BoardImgService boardImgService;
    private final UsersService usersService;
    private final FeelingService feelingService;
    private final CommentService commentService;

    @GetMapping(value = {"/board/new","/board/new/{resId}"})
    public String boardForm(@PathVariable(name ="resId", required = false) Long resId,Model model){
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

    //이거쓸거임이거쓸거임이거쓸거임이거쓸거임이거쓸거임이거쓸거임이거쓸거임이거쓸거임이거쓸거임이거쓸거임이거쓸거임이거쓸거임이거쓸거임이거쓸거임
        @PostMapping("/board/new3/{resId}")
    public void createBoard3(@RequestBody Map<String,Object> boardFormDto, @PathVariable Long resId) throws Exception {
        // 클라이언트에서 전송된 데이터 추출
//        List<BoardImgDto> boardImgDtoList = (List<BoardImgDto>) boardFormDto.get("boardImgDtoList");
        List<Map<String, Object>> boardImgDtoList = (List<Map<String, Object>>) boardFormDto.get("boardImgDtoList");


            for( String key : boardFormDto.keySet() ){
        System.out.println( String.format("키 : %s, 값 : %s", key, boardFormDto.get(key)) );
}
        String userId = (String)boardFormDto.get("userId");
        String boardViewStatus = (String)boardFormDto.get("boardViewStatus");
        String boardTitle = (String)boardFormDto.get("boardTitle");
        String content = (String)boardFormDto.get("content");
        Integer score = (Integer) boardFormDto.get("score");

        System.out.println("=======================================================/board/new3/{resId} " );
        System.out.println("========================================================boardImgDtoList size: " + boardImgDtoList.size());
        System.out.println("=======================================================boardTitle : " + userId );
        System.out.println("=======================================================boardTitle : " + boardViewStatus );
        System.out.println("=======================================================boardTitle : " + boardTitle );
        System.out.println("=======================================================content : " + content );
        System.out.println("=======================================================content : " + score );

        // BoardFormDto 객체 생성 및 데이터 할당
        BoardFormDto boardFormDto23 = new BoardFormDto();
        boardFormDto23.setUser_id(userId);
        boardFormDto23.setBoard_title(boardTitle);
        boardFormDto23.setBoardViewStatus(BoardViewStatus.valueOf(boardViewStatus));
        boardFormDto23.setContent(content);
        boardFormDto23.setScore(score);
//        boardFormDto23.setBoardImgDtoList(boardImgDtoList);

        // 게시글에 관한 식당id 가져오기
        Restaurant restaurant = boardService.getBoardByResId(resId);
        Board board = Board.createBoard(boardFormDto23, restaurant);
        boardService.saveBoard(board);
        // 이미지 업로드 서비스 등록하기. 백구성.

            //보드 id값 가져오기위해서 추가해봄
            Long boardId = board.getId();
            String contents = board.getContent();
            System.out.println("=====================================================boardId 가져오기 : " +boardId);


// BoardImgDto 객체들을 저장할 리스트 생성
            List<BoardImgDto> boardImgDtoListToSave = new ArrayList<>();

            for (Map<String, Object> map : boardImgDtoList) {
                // map에서 필요한 데이터 추출하여 BoardImgDto 객체 생성
                Long id = null;
                Object idObj = map.get("id");
                if (idObj instanceof Long) {
                    id = (Long) idObj;
                } else if (idObj instanceof Integer) {
                    id = ((Integer) idObj).longValue(); // Integer에서 Long으로 변환
                }

                // 나머지 필요한 데이터를 추출
                String imgName = (String) map.get("imgName");
                String imgUrl = (String) map.get("imgUrl");
                String oriImgName = (String) map.get("oriImgName");
                String repImgYn = (String) map.get("repImgYn");

                // BoardImgDto 객체 생성 및 데이터 할당
                BoardImgDto boardImgDto = new BoardImgDto();
                boardImgDto.setId(id);
                boardImgDto.setImgName(imgName);
                boardImgDto.setImgUrl(imgUrl);
                boardImgDto.setOriImgName(oriImgName);
                boardImgDto.setRepImgYn(repImgYn);

//                // board_id 값 할당
                boardImgDto.setBoardId(boardId);



                // BoardImgDto 객체를 리스트에 추가
                boardImgDtoListToSave.add(boardImgDto);
            }

// 생성된 BoardImgDto 리스트를 boardImgService를 통해 저장
            boardImgService.createBoardImgList(boardImgDtoListToSave,board);
    }
    //이거쓸거임이거쓸거임이거쓸거임이거쓸거임이거쓸거임이거쓸거임이거쓸거임이거쓸거임이거쓸거임이거쓸거임이거쓸거임이거쓸거임이거쓸거임이거쓸거임이거쓸거임

    //안드로이드 rest 게시글 수정.
    @PutMapping(value = "/board/{boardId}/edit")
    public ResponseEntity<String> newBoardUpdate(@RequestBody Map<String,Object> boardFormDto, @PathVariable Long boardId) {
        // 게시글 ID를 사용하여 해당 게시글을 데이터베이스에서 찾음
        Board boardToUpdate = boardService.findBoardById(boardId);
        List<Map<String, Object>> boardImgDtoList = (List<Map<String, Object>>) boardFormDto.get("boardImgDtoList");

        if (boardToUpdate != null) {
            // 수정할 내용을 가져와서 해당 게시글에 적용
//            boardToUpdate.setBoard_title((String) boardFormDto.get("boardTitle"));
//            boardToUpdate.setContent((String)boardFormDto.get("content"));
//            boardToUpdate.setBoardViewStatus((BoardViewStatus) boardFormDto.get("boardViewStatus"));
//            boardToUpdate.setScore((Integer) boardFormDto.get("score"));
            String boardViewStatus = (String)boardFormDto.get("boardViewStatus");
            String boardTitle = (String)boardFormDto.get("boardTitle");
            String content = (String)boardFormDto.get("content");
            Integer score = (Integer) boardFormDto.get("score");

            boardToUpdate.setBoard_title(boardTitle);
            boardToUpdate.setBoardViewStatus(BoardViewStatus.valueOf(boardViewStatus));
            boardToUpdate.setContent(content);
            boardToUpdate.setScore(score);

            // 이미지 삭제 - 데이터베이스에서 관련된 이미지 레코드 삭제
            boardImgService.deleteImagesByBoardId(boardId);

            // BoardImgDto 객체들을 저장할 리스트 생성
            List<BoardImgDto> boardImgDtoListToSave = new ArrayList<>();

            for (Map<String, Object> map : boardImgDtoList) {
                // map에서 필요한 데이터 추출하여 BoardImgDto 객체 생성
                Long id = null;
                Object idObj = map.get("id");
                if (idObj instanceof Long) {
                    id = (Long) idObj;
                } else if (idObj instanceof Integer) {
                    id = ((Integer) idObj).longValue(); // Integer에서 Long으로 변환
                }

                // 나머지 필요한 데이터를 추출
                String imgName = (String) map.get("imgName");
                String imgUrl = (String) map.get("imgUrl");
                String oriImgName = (String) map.get("oriImgName");
                String repImgYn = (String) map.get("repImgYn");

                // BoardImgDto 객체 생성 및 데이터 할당
                BoardImgDto boardImgDto = new BoardImgDto();
                boardImgDto.setId(id);
                boardImgDto.setImgName(imgName);
                boardImgDto.setImgUrl(imgUrl);
                boardImgDto.setOriImgName(oriImgName);
                boardImgDto.setRepImgYn(repImgYn);
//                // board_id 값 할당
                boardImgDto.setBoardId(boardId);
                // BoardImgDto 객체를 리스트에 추가
                boardImgDtoListToSave.add(boardImgDto);
            }
            // 게시글을 저장하여 수정을 완료함
            boardService.saveBoard(boardToUpdate);
            boardImgService.createBoardImgList(boardImgDtoListToSave,boardToUpdate);

            return ResponseEntity.ok("게시글이 성공적으로 수정되었습니다.");
        } else {
            return ResponseEntity.notFound().build(); // 게시글을 찾지 못한 경우
        }
    }


    //게시글상세페이지 매핑 -> 쌤이 손대줌 rest
    @GetMapping(value = "/admin/board/{boardId}")
    public BoardFormDto boardDtl(@PathVariable("boardId") Long boardId, Model model){
        BoardFormDto boardFormDto = null;
        try {
             boardFormDto = boardService.getBoardDtl(boardId);
            model.addAttribute("boardFormDto", boardFormDto);
        } catch(EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
            model.addAttribute("boardFormDto", new BoardFormDto());
//            return "board/boardForm";
            return boardFormDto;
        }

        return boardFormDto;
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
//    @GetMapping(value = "/board/{boardId}")
//    public Map<String,Object> boardDtl(Model model, @PathVariable("boardId") Long boardId){
//
//        Map<String,Object> map = new HashMap<String,Object>();
//        System.out.println("확인 1: boardId(게시글id) :  " + boardId );
//        System.out.println("id에 해당하는 게시글상세페이지 = boardFormDto  ");
//
//        BoardFormDto boardFormDto = boardService.getBoardDtl(boardId);
//        System.out.println("확인 2: boardFormDto(식당 id)) :  " + boardFormDto.getResId() );
//
//        Users users = boardService.getUserByCreated(boardFormDto.getUser_id());
//        System.out.println("확인 3: users :  " + boardFormDto.getUser_id() );
//
//        Restaurant restaurant = boardService.getBoardByResId(boardFormDto.getResId());
//        System.out.println("확인 4: restaurant :  " + restaurant.getResId() );
//
//
////        model.addAttribute("users",users);
////        model.addAttribute("board", boardFormDto);
////        model.addAttribute("restaurant", restaurant);
//        map.put("users",users);
//        map.put("board",boardFormDto);
////      이거 주석하면 잘됨
//        map.put("restaurant",restaurant);
//
//        //이게 문제같다,..
////        map.put("restaurant",restaurant.getBoards());
//
////        이건 됨... getBoards 자체가 문제같은데
////        map.put("restaurant",restaurant.getRes_name());
////        map.put("restaurant",restaurant.getResId());
//
////        System.out.println("------------------------------" + restaurant.getResId());
////        System.out.println("------------------------------" + restaurant.getResId());
////        System.out.println("------------------------------" + restaurant.getResId());
////        System.out.println("------------------------------" + restaurant.getResId());
////        System.out.println("------------------------------" + restaurant.getResId());
////        System.out.println("------------------------------" + restaurant.getResId());
//
//
////        return "board/boardDtl";
//        System.out.println("확인 5: return map" );
//        return map;
//    }

    //게시글 상세페이지
    @GetMapping(value = "/board/{boardId}")
    public Map<String,Object> boardDtl(@PathVariable("boardId") Long boardId,
                                       Principal principal){

        Map<String,Object> map = new HashMap<>();
        BoardFormDto boardFormDto = boardService.getBoardDtl(boardId);
        Users users = boardService.getUserByCreated(boardFormDto.getUser_id());


        Restaurant restaurant = boardService.getBoardByResId(boardFormDto.getResId());
        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.RestaurantDto2(restaurant);

        //식당평균평점을 추가
        Double avgScore = restaurantService.getAverageScoreByResId(restaurant.getResId());
        restaurantDto.setAvgScore(avgScore);

        //로그인된 User
        Users loggedInUser = usersService.findByUserId(principal.getName());

        //좋아요 & 싫어요 갯수
        int likeCount = feelingService.countFeeling(boardFormDto.getId(),1);
        int dislikeCount = feelingService.countFeeling(boardFormDto.getId(),-1);

        Pageable pageable = PageRequest.of(0, 1000); // 여기서 5는 한 페이지에 보여줄 댓글 수를 나타냄
        Page<CommentDto> commentsPage = commentService.findAll(boardId, pageable);

        //나의 좋아요, 싫어요 표시 여부
        Feeling myFeeling = feelingService.getFeeling(boardFormDto.getId(),principal.getName());

        //상세 게시글에 필요한 감정표현 정보 모아서 저장 -> feelingDto
        FeelingBoardDtlDto feelingBoardDtlDto = new FeelingBoardDtlDto(myFeeling,likeCount,dislikeCount);

        map.put("commentsPage",commentsPage);
        map.put("loggedInUser",loggedInUser);
        map.put("users",users);
        map.put("board",boardFormDto);
        map.put("restaurant",restaurantDto);
        map.put("feelingBoardDtlDto",feelingBoardDtlDto);

        return map;
    }
    //게시글삭제
    @DeleteMapping(value = "/board/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long boardId) {
        boolean deleted = boardService.deleteBoardById(boardId);
        if (deleted) {
            return ResponseEntity.ok("Board with ID " + boardId + " has been deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Board with ID " + boardId + " not found.");
        }
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