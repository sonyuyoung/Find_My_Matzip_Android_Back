package com.matzip.service;



import com.matzip.dto.*;
import com.matzip.entity.Board;
import com.matzip.entity.BoardImg;
import com.matzip.entity.Restaurant;
import com.matzip.entity.Users;
import com.matzip.repository.BoardImgRepository;
import com.matzip.repository.BoardRepository;
import com.matzip.repository.RestaurantRepository;
import com.matzip.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    private final BoardImgService boardImgService;

    private final BoardImgRepository boardImgRepository;

    private final RestaurantRepository restaurantRepository;

    private final UsersRepository usersRepository;

    //팔로우한사람의 게시글 목록가져오기(Rest)
    @Transactional(readOnly = true)
    public Page<MainBoardDto> getBoardPageByFollowList(BoardSearchDto boardSearchDto, Pageable pageable,List<String> toUserIdList){
        return boardRepository.getBoardPageByFollowList(boardSearchDto, pageable,toUserIdList);
    }


    //게시글 저장하기
    public Long saveBoard(BoardFormDto boardFormDto, List<MultipartFile> boardImgFileList) throws Exception{

        System.out.println("여기서부터 오류발생 보드서비스,,, 세이브보드 시작");
        //게시글 등록
        //boardFormDto에는 id만 저장되어있으므로 레스토랑 객체 가져옴
        Restaurant restaurant = restaurantRepository.findByResId(boardFormDto.getResId());
        Board board = Board.createBoard(boardFormDto,restaurant);
        System.out.println("보드생성완료");
        boardRepository.save(board);
        System.out.println("보드저장완료");

        //이미지 등록
        for(int i=0;i<boardImgFileList.size();i++){
            BoardImg boardImg = new BoardImg();
            boardImg.setBoard(board);

            if(i == 0)
                boardImg.setRepimgYn("Y");
            else
                boardImg.setRepimgYn("N");

            boardImgService.saveBoardImg(boardImg, boardImgFileList.get(i));
        }

        return board.getId();
    }

    //게시글수정하기

    //@Transactional -> 트랜잭션 읽기전용설정 , JPA가 더티체킹을 수행하지 않아서 성능향상
    @Transactional(readOnly = true)
    public BoardFormDto getBoardDtl(Long boardId){
        //해당 게시글의 이미지 조회, 등록순으로 가지고 오기 위해서 게시글이미지 아이디를 오름차순으로 가지고온다
        List<BoardImg> boardImgList = boardImgRepository.findByBoardIdOrderByIdAsc(boardId);
        List<BoardImgDto> boardImgDtoList = new ArrayList<>();

        for (BoardImg boardImg : boardImgList) {
            //boardImg 엔티티를 boardImgDto 객체로 만들어서
            BoardImgDto boardImgDto = BoardImgDto.of(boardImg);
            //리스트에 추가
            boardImgDtoList.add(boardImgDto);
        }

        //게시글의 아이디를 통해 상품 엔티티를 조회 . 존재하지않으면 오류 발생시키기
        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);

        BoardFormDto boardFormDto = new BoardFormDto(board);
        boardFormDto.setBoardImgDtoList(boardImgDtoList);
        return boardFormDto;
    }
    //게시글 수정 페이지로 진입하기 위해서 BoardController 클래스에 코드추가

    // 게시글 수정을  처리하는 로직. -> 완성후 boardController로 이동 후 게시글URL을 추가해준다
    public Long updateBoard(BoardFormDto boardFormDto, List<MultipartFile> boardImgFileList) throws Exception{
        //상품 수정,
        //게시글 등록 화면으로부터 전달받은 게시글 아이디를 이용하여 게시글 엔티티를 조회한다.
        Board board = boardRepository.findById(boardFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        // 기존 아이템에 내용에 , 더티 체킹. 변경사항에 대해서, 영속성이 알아서 자동으로 처리.
        //등록화면으로 부터 전달받은 boardFormDto를 통해, 게시글 엔티티를 업데이트한다.
        board.updateBoard(boardFormDto);
        //게시글 이미지 아이디 리스트를 조회
        List<Long> boardImgIds = boardFormDto.getBoardImgIds();

        //이미지 등록
        for(int i=0;i<boardImgFileList.size();i++){
            //게시글이미지를 업데이트 하기 위해서 updateBoardImg()메서드에
            //게시글 이미지 아이디/게시글 이미지 파일정보를 파라미터로 전달한다.
            boardImgService.updateBoardImg(boardImgIds.get(i),
                    boardImgFileList.get(i));
        }

        return board.getId();
    }

    // // 게시글 조회조건과 페이지 정보를 파라미터로 받아서 데이터조회하는 getAdminBoardPage() 메소드를 추가했다
    //데이터의 수정이 일어나지않고 조회만 하기때문에 readOnly 어노테이션 설정
    //BoardController로 이동해서 게시글 관리 화면 및 조회한 게시글 데이터를 화면에 전달하는 로직을 구현하자 108줄
    @Transactional(readOnly = true)
    public Page<Board> getAdminBoardPage(BoardSearchDto boardSearchDto, Pageable pageable){
        return boardRepository.getAdminBoardPage(boardSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainBoardDto> getMainBoardPage(BoardSearchDto boardSearchDto, Pageable pageable){

        System.out.println("boardRepository.getMainBoardPage호출");
        return boardRepository.getMainBoardPage(boardSearchDto, pageable);
    }



    @Transactional(readOnly = true)
    public Page<MainBoardDto> getSearchMainBoards(BoardSearchDto boardSearchDto, Pageable pageable, String text){

        System.out.println("getSearchMainBoards 왔음 , text : "+text);
        return boardRepository.getSearchMainBoards(boardSearchDto, pageable,text);
    }



@Transactional(readOnly = true)
public List<MainBoardDto> getMainBoard(BoardSearchDto boardSearchDto){
    return boardRepository.getMainBoard(boardSearchDto);
}




    @Transactional(readOnly = true)
    public Page<MainBoardDto> getBoardPageByResId(BoardSearchDto boardSearchDto, Pageable pageable,String resId){
        return boardRepository.getBoardPageByResId(boardSearchDto, pageable,resId);
    }

    @Transactional(readOnly = true)
    public Page<MainBoardDto> getBoardPageByUserId(BoardSearchDto boardSearchDto, Pageable pageable,String userId){
        return boardRepository.getBoardPageByUserId(boardSearchDto, pageable,userId);
    }



    @Transactional(readOnly = true)
    public Restaurant getBoardByResId(String resId){
        return restaurantRepository.findByResId(resId);
    }



    @Transactional
    public void deleteBoard(Long boardId) {
        // 리뷰 ID로 리뷰를 찾아옴
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("삭제할 리뷰를 찾을 수 없습니다. ID: " + boardId));

        // 리뷰 삭제
        boardRepository.delete(board);
    }

    @Transactional
    public Users getUserByCreated(String userId) {
        // board의 userId로 user객체 찾기(작성자 정보 가져오기)
        Users users = usersRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("작성자를 찾을 수 없습니다. ID: " + userId));
        return users;
    }
    
    @Transactional
    public Integer countByUserId(String userId) {
        // userid로 사용자 게시글 갯수 가져오기
        int boardCount = boardRepository.countByUserId(userId);
        return boardCount;
    }

    // 모든 게시글을 가져오는 메서드
//    public List<BoardFormDto> findAllBoards() {
//        List<Board> boardList = boardRepository.findAll();
//        List<BoardFormDto> boardFormDtoList = new ArrayList<>();
//
//        for (Board board : boardList) {
//            boardFormDtoList.add(BoardFormDto.boardFormDto(board));
//        }
//
//        return boardFormDtoList;
//    }

}