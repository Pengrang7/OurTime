package com.ourtime.controller;

import com.ourtime.dto.common.ApiResponse;
import com.ourtime.dto.common.PageResponse;
import com.ourtime.dto.memory.CreateMemoryRequest;
import com.ourtime.dto.memory.MemoryResponse;
import com.ourtime.dto.memory.UpdateMemoryRequest;
import com.ourtime.service.MemoryService;
import com.ourtime.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Memory", description = "추억 관련 API")
@RestController
@RequestMapping("/api/memories")
@RequiredArgsConstructor
public class MemoryController {

    private final MemoryService memoryService;

    @Operation(summary = "전체 추억 목록 조회", description = "현재 사용자가 접근 가능한 모든 추억을 조회합니다.")
    @GetMapping
    public ApiResponse<List<MemoryResponse>> getAllMemories() {
        Long userId = SecurityUtil.getCurrentUserId();
        List<MemoryResponse> response = memoryService.getAllMemoriesByUserId(userId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "추억 생성", description = "새로운 추억을 생성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<MemoryResponse> createMemory(@Valid @RequestBody CreateMemoryRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        MemoryResponse response = memoryService.createMemory(userId, request);
        return ApiResponse.success(response, "추억이 생성되었습니다.");
    }

    @Operation(summary = "추억 상세 조회", description = "특정 추억의 상세 정보를 조회합니다.")
    @GetMapping("/{memoryId}")
    public ApiResponse<MemoryResponse> getMemoryById(@PathVariable Long memoryId) {
        Long userId = SecurityUtil.getCurrentUserId();
        MemoryResponse response = memoryService.getMemoryById(memoryId, userId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "그룹별 추억 목록 조회", description = "특정 그룹의 모든 추억을 조회합니다. (페이징)")
    @GetMapping("/groups/{groupId}")
    public ApiResponse<PageResponse<MemoryResponse>> getMemoriesByGroupId(
            @PathVariable Long groupId,
            @PageableDefault(size = 20, sort = "visitedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Long userId = SecurityUtil.getCurrentUserId();
        Page<MemoryResponse> memories = memoryService.getMemoriesByGroupId(groupId, userId, pageable);
        return ApiResponse.success(PageResponse.of(memories));
    }

    @Operation(summary = "태그별 추억 조회", description = "특정 그룹에서 태그로 필터링된 추억을 조회합니다.")
    @GetMapping("/groups/{groupId}/tags/{tagId}")
    public ApiResponse<List<MemoryResponse>> getMemoriesByTag(
            @PathVariable Long groupId,
            @PathVariable Long tagId) {
        Long userId = SecurityUtil.getCurrentUserId();
        List<MemoryResponse> response = memoryService.getMemoriesByGroupIdAndTagId(groupId, tagId, userId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "추억 수정", description = "추억의 정보를 수정합니다. (작성자만 가능)")
    @PutMapping("/{memoryId}")
    public ApiResponse<MemoryResponse> updateMemory(
            @PathVariable Long memoryId,
            @Valid @RequestBody UpdateMemoryRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        MemoryResponse response = memoryService.updateMemory(memoryId, userId, request);
        return ApiResponse.success(response, "추억이 수정되었습니다.");
    }

    @Operation(summary = "추억 삭제", description = "추억을 삭제합니다. (작성자만 가능)")
    @DeleteMapping("/{memoryId}")
    public ApiResponse<Void> deleteMemory(@PathVariable Long memoryId) {
        Long userId = SecurityUtil.getCurrentUserId();
        memoryService.deleteMemory(memoryId, userId);
        return ApiResponse.success("추억이 삭제되었습니다.");
    }

}
