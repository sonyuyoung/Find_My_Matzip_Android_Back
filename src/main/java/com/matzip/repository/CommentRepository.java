package com.matzip.repository;

import com.matzip.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentsWithRepliesByBoardId(Long boardId);

    Page<Comment> findPageByBoardIdOrderByCreationDateAsc(Long boardId, Pageable pageable);

    List<Comment> findByBoardIdAndParent_CommentIdOrderByCommentIdDesc(Long boardId, Long parentId);



}
