package com.matzip.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@Table(name = "feeling")
@Entity
@Getter @Setter
public class Feeling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board feelingBoard;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private Users feelingUsers;

    @Column
    private int feelNum; //1 : 좋아요, -1 : 싫어요

    //Feeling -> FeelingDto로 형 변환
//    private static ModelMapper modelMapper = new ModelMapper();
//    public static FeelingDto of(Feeling feeling){
//        return modelMapper.map(feeling, FeelingDto.class);
//    }


    public Feeling(Board feelingBoard, Users feelingUsers, int feelNum) {
        this.feelingBoard = feelingBoard;
        this.feelingUsers = feelingUsers;;
        this.feelNum = feelNum;
    }

    public void updateFeeling(int feelNum){
        this.feelNum = feelNum;
    }
}