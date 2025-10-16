package com.ourtime.controller;

import com.ourtime.dto.comment.CommentResponse;
import com.ourtime.dto.comment.CreateCommentRequest;
import com.ourtime.dto.comment.UpdateCommentRequest;
import com.ourtime.dto.common.ApiResponse;
import com.ourtime.service.CommentService;
import com.ourtime.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Comment", description = "댓글 관련 API")
@RestController
@RequestMapping("/api/memories/{memoryId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 작성", description = "추억에 댓글을 작성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CommentResponse> createComment(
            @PathVariable Long memoryId,
            @Valid @RequestBody CreateCommentRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        CommentResponse response = commentService.createComment(memoryId, userId, request);
        return ApiResponse.success(response, "댓글이 작성되었습니다.");
    }

    @Operation(summary = "댓글 목록 조회", description = "추억의 모든 댓글을 조회합니다.")
    @GetMapping
    public ApiResponse<List<CommentResponse>> getCommentsByMemoryId(@PathVariable Long memoryId) {
        Long userId = SecurityUtil.getCurrentUserId();
        List<CommentResponse> response = commentService.getCommentsByMemoryId(memoryId, userId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다. (작성자만 가능)")
    @PutMapping("/{commentId}")
    public ApiResponse<CommentResponse> updateComment(
            @PathVariable Long memoryId,
            @PathVariable Long commentId,
            @Valid @RequestBody UpdateCommentRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        CommentResponse response = commentService.updateComment(commentId, userId, request);
        return ApiResponse.success(response, "댓글이 수정되었습니다.");
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다. (작성자만 가능)")
    @DeleteMapping("/{commentId}")
    public ApiResponse<Void> deleteComment(
            @PathVariable Long memoryId,
            @PathVariable Long commentId) {
        Long userId = SecurityUtil.getCurrentUserId();
        commentService.deleteComment(commentId, userId);
        return ApiResponse.success("댓글이 삭제되었습니다.");
    }

}
