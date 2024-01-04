package com.matzip.service;



import com.matzip.dto.BoardImgDto;
import com.matzip.entity.Board;
import com.matzip.entity.BoardImg;
import com.matzip.repository.BoardImgRepository;
import com.matzip.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardImgService {

    @Value("${boardImgLocation}")
    private String boardImgLocation;

    private final BoardImgRepository boardImgRepository;


    private final FileService fileService;


    public void createBoardImgList(List<BoardImgDto> boardImgDtoList, Board board) {
        List<BoardImg> boardImgList = new ArrayList<>();

        // boardImgDtoList를 기반으로 BoardImg 엔티티 리스트 생성
        for (BoardImgDto boardImgDto : boardImgDtoList) {
//            Board board = boardService.getBoardByBoardId(boardImgDto.getBoardId(),boardTitle);

            BoardImg newBoardImg = new BoardImg();
            newBoardImg.setOriImgName(boardImgDto.getOriImgName());
            newBoardImg.setImgName(boardImgDto.getImgName());
            newBoardImg.setImgUrl(boardImgDto.getImgUrl());
            newBoardImg.setRepimgYn(boardImgDto.getRepImgYn());
            newBoardImg.setBoard(board);
            // 필요한 다른 값들도 설정해야 할 수 있습니다.
            boardImgList.add(newBoardImg);
        }

        // 생성된 이미지 리스트 DB에 저장
        boardImgRepository.saveAll(boardImgList);
    }


//    //게시글에 이미지 업로드 및 저장
//    public void saveBoardImg(BoardImg boardImg, MultipartFile boardImgFile) throws Exception{
//        String oriImgName = boardImgFile.getOriginalFilename();
//        String imgName = "";
//        String imgUrl = "";
//
//        //파일 업로드
//        if(!StringUtils.isEmpty(oriImgName)){
//            imgName = fileService.uploadFile(boardImgLocation, oriImgName,
//                    boardImgFile.getBytes());
//            imgUrl = "/images/board/" + imgName;
//        }
//
//        //상품 이미지 정보 저장
//        boardImg.updateBoardImg(oriImgName, imgName, imgUrl);
//        boardImgRepository.save(boardImg);
//    }

    // 게시글 ID에 해당하는 이미지들 삭제
    public void deleteImagesByBoardId(Long boardId) {
        List<BoardImg> imagesToDelete = boardImgRepository.findByBoardId(boardId);

        if (!imagesToDelete.isEmpty()) {
            boardImgRepository.deleteAll(imagesToDelete);
        }
    }


// BoardImg 저장
public void saveBoardImg(BoardImg boardImg) {
    boardImgRepository.save(boardImg);
}

    //게시글 수정 -> 정상작동확인완료 -> board 엔티티로 이동해서 상품데이터를 업데이트하는 로직을 만든다
    public void updateBoardImg(Long boardImgId, MultipartFile boardImgFile) throws Exception{
        if(!boardImgFile.isEmpty()){
            BoardImg savedBoardImg = boardImgRepository.findById(boardImgId)
                    .orElseThrow(EntityNotFoundException::new);

            //기존 이미지 파일 삭제, 물리 저장소의 내용을 지우기.
            // 디비, 디비에는 삭제가 파일명을 대체.
            if(!StringUtils.isEmpty(savedBoardImg.getImgName())) {
                fileService.deleteFile(boardImgLocation+"/"+
                        savedBoardImg.getImgName());
            }

            String oriImgName = boardImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(boardImgLocation, oriImgName, boardImgFile.getBytes());
            String imgUrl = "/images/board/" + imgName;
            savedBoardImg.updateBoardImg(oriImgName, imgName, imgUrl);
        }
    }

}