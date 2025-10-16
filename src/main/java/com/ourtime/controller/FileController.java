package com.ourtime.controller;

import com.ourtime.dto.common.ApiResponse;
import com.ourtime.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "File", description = "파일 업로드 관련 API")
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final S3Service s3Service;

    @Operation(summary = "프로필 이미지 업로드", description = "프로필 이미지를 업로드하고 URL을 반환합니다.")
    @PostMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> uploadProfileImage(@RequestParam("file") MultipartFile file) {
        String fileUrl = s3Service.uploadFile(file, "profiles");
        return ApiResponse.success(fileUrl, "프로필 이미지가 업로드되었습니다.");
    }

    @Operation(summary = "그룹 이미지 업로드", description = "그룹 이미지를 업로드하고 URL을 반환합니다.")
    @PostMapping(value = "/group", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> uploadGroupImage(@RequestParam("file") MultipartFile file) {
        String fileUrl = s3Service.uploadFile(file, "groups");
        return ApiResponse.success(fileUrl, "그룹 이미지가 업로드되었습니다.");
    }

    @Operation(summary = "추억 이미지 업로드", description = "추억 이미지를 업로드하고 URL을 반환합니다.")
    @PostMapping(value = "/memory", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> uploadMemoryImage(@RequestParam("file") MultipartFile file) {
        String fileUrl = s3Service.uploadFile(file, "memories");
        return ApiResponse.success(fileUrl, "추억 이미지가 업로드되었습니다.");
    }

    @Operation(summary = "파일 삭제", description = "S3에서 파일을 삭제합니다.")
    @DeleteMapping
    public ApiResponse<Void> deleteFile(@RequestParam("fileUrl") String fileUrl) {
        s3Service.deleteFile(fileUrl);
        return ApiResponse.success("파일이 삭제되었습니다.");
    }

}
