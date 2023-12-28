package com.matzip.dto;

import com.matzip.entity.Comment;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class CommentDto {
    private Long commentId;
    private String commentWriter;
    private String commentContents;
    private LocalDateTime commentCreatedTime;
    private Long boardId;
    private int depth;
    //parentId를 사용하여 부모 댓글 ID를 나타내고,
    //이 필드가 null이면 해당 댓글은 부모 댓글(상위댓글)이라고 간주
    private Long parentId;

    private List<CommentDto> children;


    public CommentDto() {

    }
    public static Comment toEntity(CommentDto commentDto, Comment parentComment) {
        Comment comment = new Comment();
        comment.setCommentWriter(commentDto.getCommentWriter());
        comment.setCommentContents(commentDto.getCommentContents());
        comment.setParent(parentComment);
        return comment;
    }
    public static CommentDto toCommentDto(Comment comment, Long boardId, Long parentId,int depth) {
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentId(comment.getCommentId());
        commentDto.setCommentWriter(comment.getCommentWriter());
        commentDto.setCommentContents(comment.getCommentContents());
        commentDto.setCommentCreatedTime(comment.getRegTime());
        commentDto.setBoardId(boardId);
        commentDto.setParentId(parentId);


        // 부모 댓글의 ID와 연결된 자식 댓글들을 가져오기
        List<CommentDto> children = comment.getChildren().stream()
                .map(child -> toCommentDto(child, boardId, comment.getCommentId(), depth + 1))
                .collect(Collectors.toList());
        commentDto.setChildren(children);


        commentDto.setDepth(depth);

        return commentDto;
    }

    public static CommentDto fromEntity(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentId(comment.getCommentId());
        commentDto.setCommentWriter(comment.getCommentWriter());
        commentDto.setCommentContents(comment.getCommentContents());
        commentDto.setBoardId(comment.getBoard().getId());
        commentDto.setCommentCreatedTime(comment.getRegTime());
        commentDto.setParentId(comment.getParent() != null ? comment.getParent().getCommentId() : null);
        return commentDto;
    }



}
