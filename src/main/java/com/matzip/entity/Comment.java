package com.matzip.entity;


import com.matzip.dto.CommentDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "comment")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_Id") // Add this if column name is different
    private Long commentId;

    @Column(length = 20, nullable = true)
    private String commentWriter;

    @Column
    private String commentContents;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @PrePersist
    public void prePersist() {
        this.creationDate = LocalDateTime.now();
    }


    /* Board:Comment = 1:N */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    //부모 댓글의 id값을 가지고 있고, OneToMany관계로 자식 댓글 리스트를 가지고 있음
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;


    @Builder.Default
    @OneToMany(mappedBy = "parent", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Comment> children = new ArrayList<>();

    private String user_image;

    public void addChild(Comment child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
    }
    public static Comment toSaveEntity(CommentDto commentDto, Board board, Comment parent) {
        Comment comment = new Comment();
        comment.setCommentId(comment.getCommentId());
        comment.setCommentWriter(commentDto.getCommentWriter());
        comment.setCommentContents(commentDto.getCommentContents());
        comment.setBoard(board);
        comment.setParent(parent);
        comment.setUser_image(comment.getUser_image());
        comment.setChildren(comment.getChildren());
        // 부모 댓글이 있는 경우, 부모 댓글의 자식 댓글 리스트에 현재 댓글을 추가
//        if (parent != null) {
//            parent.addChild(comment);
//        }
        return comment;
    }
    public void update(CommentDto commentDto) {
        // 필요한 필드를 업데이트하세요.
        this.commentContents = commentDto.getCommentContents();
    }


}

