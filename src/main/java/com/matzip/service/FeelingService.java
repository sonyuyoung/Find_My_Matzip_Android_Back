package com.matzip.service;

import com.matzip.entity.Board;
import com.matzip.entity.Feeling;
import com.matzip.entity.Users;
import com.matzip.repository.BoardRepository;
import com.matzip.repository.FeelingRepository;
import com.matzip.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeelingService {
    private final FeelingRepository feelingRepository;
    private final UsersRepository usersRepository;

    private final BoardRepository boardRepository;


    //좋아요 & 싫어요
    //return => -1:싫어요, 0:취소, +1:좋아요
    public int setFeeling(Long boardId,int newFeel,String usersId) {
        //기존의 감정표현 존재하는지 확인
        Feeling old = feelingRepository.fingByUsersIdAndBoardId(boardId,usersId);

        if(old==null){
            //존재하지 않음.
            Users users = usersRepository.findByUserid(usersId);
            Optional<Board> board = boardRepository.findById(boardId);

            //새로 등록 (newFeel -1:싫어요, +1:좋아요)
            Feeling feeling = new Feeling(board.get(),users,newFeel);
            feelingRepository.save(feeling);
            return newFeel;

        }else{
            //기존의 감정표현 존재 ->업데이트
            if(old.getFeelNum() == newFeel){
                //같은 버튼 또 눌렀을 때 -> 취소
                feelingRepository.deleteById(old.getId());
                return 0;
            }else{
                //변경된 경우(oldFeel != newFeel)
                old.updateFeeling(newFeel);
                feelingRepository.save(old);
                return newFeel;
            }
        }

    }

    //boardid,userid로 Feeling가져오기
    public Feeling getFeeling(Long boardId,String usersId) {
        return feelingRepository.fingByUsersIdAndBoardId(boardId,usersId);
    }


    //게시글의 좋아요 or 싫어요 수
    //feelNum -> 1:좋아요, -1:싫어요
    public int countFeeling(Long boardId,int feelNum) {
        return feelingRepository.countFeeling(boardId,feelNum);
    }

}