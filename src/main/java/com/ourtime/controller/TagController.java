package com.ourtime.controller;

import com.ourtime.dto.common.ApiResponse;
import com.ourtime.dto.tag.CreateTagRequest;
import com.ourtime.dto.tag.TagResponse;
import com.ourtime.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Tag", description = "태그 관련 API")
@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @Operation(summary = "태그 생성", description = "새로운 태그를 생성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TagResponse> createTag(@Valid @RequestBody CreateTagRequest request) {
        TagResponse response = tagService.createTag(request);
        return ApiResponse.success(response, "태그가 생성되었습니다.");
    }

    @Operation(summary = "태그 목록 조회", description = "모든 태그를 조회합니다.")
    @GetMapping
    public ApiResponse<List<TagResponse>> getAllTags() {
        List<TagResponse> response = tagService.getAllTags();
        return ApiResponse.success(response);
    }

    @Operation(summary = "태그 상세 조회", description = "특정 태그의 정보를 조회합니다.")
    @GetMapping("/{tagId}")
    public ApiResponse<TagResponse> getTagById(@PathVariable Long tagId) {
        TagResponse response = tagService.getTagById(tagId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "태그 삭제", description = "태그를 삭제합니다.")
    @DeleteMapping("/{tagId}")
    public ApiResponse<Void> deleteTag(@PathVariable Long tagId) {
        tagService.deleteTag(tagId);
        return ApiResponse.success("태그가 삭제되었습니다.");
    }

}
