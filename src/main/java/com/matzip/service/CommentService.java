package com.matzip.service;

import com.matzip.dto.CommentDto;
import com.matzip.entity.Board;
import com.matzip.entity.Comment;
import com.matzip.repository.BoardRepository;
import com.matzip.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;


//
//    private List<CommentDto> getChildren(CommentDto parentDto, List<Comment> allComments) {
//        return allComments.stream()
//                .filter(c -> c.getParent() != null && c.getParent().getCommentId().equals(parentDto.getParentId()))
//                .map(this::convertCommentToDto)
//                .peek(childDto -> childDto.setChildren(getChildren(childDto, allComments)))
//                .collect(Collectors.toList());
//    }

//    private CommentDto convertCommentToDto(Comment comment) {
//        if (comment == null) {
//            return null;
//        }
//
//        // Comment 엔터티를 CommentDto로 변환하는 로직을 구현합니다.
//        // CommentDto.toCommentDto 메서드는 CommentDto로의 변환을 수행합니다.
//        // 해당 메서드의 사용 방법은 실제 구현에 따라 다를 수 있습니다.
//        // 여기서는 Comment 엔터티의 정보를 활용하여 CommentDto를 생성합니다.
//        return CommentDto.toCommentDto(comment, comment.getBoard().getId(), null, getDepth(comment));
//    }
//


    // save 메서드는 주어진 CommentDto 객체의 parentId를 확인하여
    // 부모 댓글의 유무에 따라 처리를 분기하고, 새로운 댓글을 저장하거나 대댓글을 저장하는 기능을 담당
    @Transactional
    public Long save(CommentDto commentDto) {
        if (commentDto.getParentId() != null) {
            // 부모 댓글이 있는 경우 대댓글 저장 로직 호출
            return saveReply(commentDto);
        } else {
            // 부모 댓글이 없는 경우 새로운 댓글 저장 로직 호출
            return saveComment(commentDto);
        }
    }

    //saveComment() 메서드는 새로운 댓글을 저장하는 로직을 유지
    private Long saveComment(CommentDto commentDto) {
        Long boardId = commentDto.getBoardId();

        if (boardId == null) {
            throw new IllegalArgumentException("Board ID must not be null");
        }

        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.orElseThrow(() -> new IllegalArgumentException("Board not found for id: " + boardId));

        Comment comment = Comment.toSaveEntity(commentDto, board, null);
        return commentRepository.save(comment).getCommentId();
    }
    //saveReply() 메서드는 부모 댓글이 있는 경우 호출 대댓글 저장에 관련된 로직을 담당
    public Long saveReply(CommentDto commentDto) {
        Board board = boardRepository.findById(commentDto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("Board not found for id: " + commentDto.getBoardId()));

        Comment parentComment = commentRepository.findById(commentDto.getParentId())
                .orElseThrow(() -> new IllegalArgumentException("Parent comment not found for id: " + commentDto.getParentId()));

        Comment replyComment = Comment.toSaveEntity(commentDto, board, parentComment);
        return commentRepository.save(replyComment).getCommentId();
    }


    public CommentDto findById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        return CommentDto.toCommentDto(comment, comment.getBoard().getId(), null, getDepth(comment));

    }

    private int getDepth(Comment comment) {
        int depth = 0;
        Comment parent = comment.getParent();

        while (parent != null) {
            depth++;
            parent = parent.getParent();
        }

        return depth;
    }

    public List<CommentDto> findByBoardId(Long boardId) {
        List<Comment> comments = commentRepository.findCommentsWithRepliesByBoardId(boardId);

        return comments.stream()
                .map((Comment commentEntity) -> CommentDto.toCommentDto(commentEntity, boardId, null, 0))
                .collect(Collectors.toList());
    }


    public CommentDto findById(Long commentId, Long boardId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        return CommentDto.toCommentDto(comment, boardId, null, 0);
    }

    public List<CommentDto> findByBoardId(Long boardId, Long parentId) {
        List<Comment> commentList = commentRepository.findByBoardIdAndParent_CommentIdOrderByCommentIdDesc(boardId, parentId);

        return commentList.stream()
                .map(commentEntity -> CommentDto.toCommentDto(commentEntity, boardId, parentId, 0))
                .collect(Collectors.toList());
    }


    public CommentDto update(Long commentId, CommentDto commentDto, Long parentCommentId) throws Exception {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new Exception("Comment not found"));

        Comment parentComment = null;
        if (parentCommentId != null) {
            parentComment = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
        }

        comment.update(commentDto);
        commentRepository.save(comment);
        return CommentDto.fromEntity(comment);
    }

    // 삭제 메서드
    public Long deleteComment(Long commentId) {
        Comment commentToDelete = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));

        Long itemId = commentToDelete.getBoard().getId(); // 해당 댓글의 상품 ID를 가져옵니다.
        commentRepository.delete(commentToDelete);

        return itemId; // 댓글이 삭제된 상품의 ID를 반환합니다.
    }

    // 페이징 메서드
    public Page<CommentDto> findCommentsByBoardId(Long boardId, Pageable pageable) {
        Page<Comment> commentEntitiesPage = commentRepository.findPageByBoardIdOrderByCreationDateAsc(boardId, pageable);
        return commentEntitiesPage.map(comment -> CommentDto.toCommentDto(comment, boardId, null, 0));
    }


    @Transactional
    public Page<CommentDto> findAll(Long boardId, Pageable pageable) {
        Page<Comment> allComments = commentRepository.findPageByBoardIdOrderByCreationDateAsc(boardId, pageable);

        // 중복 제거와 내림차순 정렬
        List<Comment> sortedComments = allComments.stream()
                .sorted(Comparator.comparing(Comment::getCommentId))
                .collect(Collectors.toList());

        // 부모 댓글 ID를 기준으로 자식 댓글들을 그룹화
        Map<Long, List<Comment>> commentsByParentId = sortedComments.stream()
                .filter(comment -> comment.getParent() != null)
                .collect(Collectors.groupingBy(comment -> comment.getParent().getCommentId()));

        // 최상위 댓글(부모 댓글이 없는 경우)부터 시작하여 계층 구조를 만듭
        List<CommentDto> result = sortedComments.stream()
                .filter(comment -> comment.getParent() == null)
                .map(parentComment -> {
                    // 최상위 댓글에 대한 CommentDto를 생성합니다.
                    CommentDto parentCommentDto = CommentDto.toCommentDto(parentComment, parentComment.getBoard().getId(), null, 0);
                    // 해당 최상위 댓글의 자식 댓글들을 가져와 계층 구조를 만듭니다.
                    List<CommentDto> children = getChildren(parentComment, commentsByParentId, new HashSet<>()); // 중복 방지용 Set 추가
                    parentCommentDto.setChildren(children);
                    return parentCommentDto;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(result, pageable, allComments.getTotalElements());
    }


private List<CommentDto> getChildren(Comment parentComment, Map<Long, List<Comment>> commentsByParentId, Set<Long> processedCommentIds) {
    List<Comment> childrenComments = commentsByParentId.getOrDefault(parentComment.getCommentId(), Collections.emptyList());

    return childrenComments.stream()
            .filter(childComment -> processedCommentIds.add(childComment.getCommentId())) // 중복 방지
            .map(childComment -> {
                int depth = getDepth(childComment);
                CommentDto childCommentDto = CommentDto.toCommentDto(childComment, parentComment.getBoard().getId(), parentComment.getCommentId(), depth);
                List<CommentDto> grandchildren = getChildren(childComment, commentsByParentId, processedCommentIds); // 여기도 변경
                childCommentDto.setChildren(grandchildren);
                return childCommentDto;
            })
            .collect(Collectors.toList());
}
}
