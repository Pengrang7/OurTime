package com.ourtime.controller;

import com.ourtime.dto.common.ApiResponse;
import com.ourtime.service.LikeService;
import com.ourtime.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Like", description = "좋아요 관련 API")
@RestController
@RequestMapping("/api/memories/{memoryId}/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @Operation(summary = "좋아요 토글", description = "추억에 좋아요를 추가하거나 취소합니다.")
    @PostMapping
    public ApiResponse<Void> toggleLike(@PathVariable Long memoryId) {
        Long userId = SecurityUtil.getCurrentUserId();
        likeService.toggleLike(memoryId, userId);
        return ApiResponse.success("좋아요가 처리되었습니다.");
    }

    @Operation(summary = "좋아요 개수 조회", description = "추억의 좋아요 개수를 조회합니다.")
    @GetMapping("/count")
    public ApiResponse<Long> getLikeCount(@PathVariable Long memoryId) {
        long count = likeService.getLikeCount(memoryId);
        return ApiResponse.success(count);
    }

    @Operation(summary = "좋아요 상태 확인", description = "내가 이 추억에 좋아요를 눌렀는지 확인합니다.")
    @GetMapping("/me")
    public ApiResponse<Boolean> isLikedByMe(@PathVariable Long memoryId) {
        Long userId = SecurityUtil.getCurrentUserId();
        boolean isLiked = likeService.isLikedByUser(memoryId, userId);
        return ApiResponse.success(isLiked);
    }

}
