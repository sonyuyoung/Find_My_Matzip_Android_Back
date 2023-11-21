package com.matzip.service;



import com.matzip.entity.RestaurantImg;
import com.matzip.repository.RestaurantImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantImgService {

    @Value("${restaurantImgLocation}")
    private String restaurantImgLocation;

    private final RestaurantImgRepository restaurantImgRepository;

    private final FileService fileService;

    //게시글에 이미지 업로드 및 저장
    public void saveRestaurantImg(RestaurantImg restaurantImg, MultipartFile restaurantImgFile) throws Exception{
        String oriImgName = restaurantImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(restaurantImgLocation, oriImgName,
                    restaurantImgFile.getBytes());
            imgUrl = "/images/restaurant/" + imgName;
        }

        //상품 이미지 정보 저장
        restaurantImg.updateRestaurantImg(oriImgName, imgName, imgUrl);
        restaurantImgRepository.save(restaurantImg);
    }

    //게시글 수정 -> 정상작동확인완료 -> restaurant 엔티티로 이동해서 상품데이터를 업데이트하는 로직을 만든다
    public void updateRestaurantImg(Long restaurantImgId, MultipartFile restaurantImgFile) throws Exception{
        if(!restaurantImgFile.isEmpty()){
            RestaurantImg savedRestaurantImg = restaurantImgRepository.findById(restaurantImgId)
                    .orElseThrow(EntityNotFoundException::new);

            //기존 이미지 파일 삭제, 물리 저장소의 내용을 지우기.
            // 디비, 디비에는 삭제가 파일명을 대체.
            if(!StringUtils.isEmpty(savedRestaurantImg.getImgName())) {
                fileService.deleteFile(restaurantImgLocation+"/"+
                        savedRestaurantImg.getImgName());
            }

            String oriImgName = restaurantImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(restaurantImgLocation, oriImgName, restaurantImgFile.getBytes());
            String imgUrl = "/images/restaurant/" + imgName;
            savedRestaurantImg.updateRestaurantImg(oriImgName, imgName, imgUrl);
        }
    }

}