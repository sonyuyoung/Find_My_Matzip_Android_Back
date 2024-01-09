package com.matzip.entity;


import com.matzip.dto.CommentDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Column
    private int depth;

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
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private Users users;

    public void addChild(Comment child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
    }

    public static Comment toSaveEntity(CommentDto commentDto, Board board, Comment parent) {
        Comment comment = new Comment();
        comment.setCommentWriter(commentDto.getCommentWriter());
        comment.setCommentContents(commentDto.getCommentContents());
        comment.setBoard(board);
        comment.setParent(parent);

        // 부모 댓글의 ID를 사용하여 부모 댓글을 찾음
        Comment parentComment = parent;
        int depth = 0;
        while (parentComment != null) {
            parentComment = parentComment.getParent();
            depth++;
        }

        // 깊이 정보 설정
        comment.setDepth(depth);

        // CommentDto에서 자식 댓글이 있는 경우 자식 댓글들을 설정
        if (commentDto.getChildren() != null && !commentDto.getChildren().isEmpty()) {
            // CommentDto의 자식 댓글들을 Comment 엔터티로 변환하고 리스트로 설정
            List<Comment> childComments = commentDto.getChildren().stream()
                    .map(childDto -> toSaveEntity(childDto, board, comment))
                    .collect(Collectors.toList());
            comment.setChildren(childComments);
        }

        // 여기서 사용자 이미지 설정
        comment.setUser_image(commentDto.getUserImage());
        return comment;
    }

    public void update(CommentDto commentDto) {
        // 필요한 필드를 업데이트하세요.
        this.commentContents = commentDto.getCommentContents();
    }


}

