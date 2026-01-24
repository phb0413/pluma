package com.park.pluma.repository;

import com.park.pluma.entity.Comment;
import com.park.pluma.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByPostId(Long postId, Pageable pageable);

    // 첫 요청 (cursor 없음)
    Slice<Comment> findByPostOrderByIdDesc(Post post, Pageable pageable);

    // 다음 요청 (cursor 있음)
    Slice<Comment> findByPostAndIdLessThanOrderByIdDesc(Post post, Long lastCommentId, Pageable pageable);
}
