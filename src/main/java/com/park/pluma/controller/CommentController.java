package com.park.pluma.controller;

import com.park.pluma.dto.CommentRequest;
import com.park.pluma.service.CommentService;
import com.park.pluma.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping
    public ResponseEntity<?> createComment(
            @PathVariable Long postId,
            @RequestBody CommentRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        return ResponseEntity.ok(
                commentService.createComment(postId, request, userDetails.getUser())
        );
    }

    // 댓글 조회
    @GetMapping("/scroll")
    public ResponseEntity<?> getCommentsScroll(
            @PathVariable Long postId,
            @RequestParam(required = false) Long lastCommentId,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(commentService.getCommentsScroll(postId, lastCommentId, size));
    }


    // 댓글 수정
    @PutMapping
    public ResponseEntity<?> updateComment(
            @PathVariable Long postId,
            @RequestBody CommentRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.ok(commentService.updateComment(postId, request, userDetails.getUser()));
    }

    // 댓글 삭제
    @DeleteMapping
    public ResponseEntity<?> deleteComment(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(postId, userDetails.getUser());
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }
}
