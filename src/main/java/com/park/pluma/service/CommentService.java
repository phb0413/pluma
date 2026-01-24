package com.park.pluma.service;

import com.park.pluma.dto.CommentRequest;
import com.park.pluma.dto.CommentResponse;
import com.park.pluma.entity.Comment;
import com.park.pluma.entity.Post;
import com.park.pluma.entity.User;
import com.park.pluma.repository.CommentRepository;
import com.park.pluma.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    // 게시글 작성
    public CommentResponse createComment(Long postId, CommentRequest request, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다"));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .author(user)
                .post(post)
                .build();

        return new CommentResponse(commentRepository.save(comment));
    }

    // 댓글 조회
//    public Page<CommentResponse> getComments(
//            Long postId, int page, int size
//    ) {
//        postRepository.findById(postId)
//                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않는다"));
//
//        Pageable pageable = PageRequest.of(
//                page,
//                size,
//                Sort.by(Sort.Direction.ASC, "createdAt")
//        );
//
//        Page<Comment> comments = commentRepository.findByPostId(postId, pageable);
//        return comments.map(CommentResponse::new);
//    }

    // 댓글 조회
    public Slice<CommentResponse> getCommentsScroll(
            Long postId,
            Long lastCommentId,
            int size
    ) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않는다."));
        Pageable pageable = PageRequest.of(0, size);

        Slice<Comment> comments;

        if(lastCommentId == null) {
            comments = commentRepository.findByPostOrderByIdDesc(post, pageable);
        } else {
            comments = commentRepository.findByPostAndIdLessThanOrderByIdDesc(post, lastCommentId, pageable);
        }
        return comments.map(CommentResponse::new);
    }

    // 댓글 수정
    @Transactional
    public CommentResponse updateComment(Long commentId, CommentRequest request, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다"));

        if(!comment.getAuthor().getUsername().equals(user.getUsername())) {
            throw new RuntimeException("작성자만 수정할 수 있습니다");
        }

        comment.update(request.getContent());
        return new CommentResponse(comment);
    }

    // 댓글 삭제
    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다"));

        if(!comment.getAuthor().getUsername().equals(user.getUsername())) {
            throw new RuntimeException("작성자만 삭제할 수 있습니다");
        }

        commentRepository.delete(comment);
    }
}
