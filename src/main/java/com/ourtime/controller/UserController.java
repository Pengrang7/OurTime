package com.ourtime.controller;

import com.ourtime.dto.common.ApiResponse;
import com.ourtime.dto.user.UpdatePasswordRequest;
import com.ourtime.dto.user.UpdateProfileRequest;
import com.ourtime.dto.user.UserResponse;
import com.ourtime.service.UserService;
import com.ourtime.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "사용자 관련 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 정보를 조회합니다.")
    @GetMapping("/me")
    public ApiResponse<UserResponse> getMyInfo() {
        Long userId = SecurityUtil.getCurrentUserId();
        UserResponse response = userService.getUserById(userId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "사용자 정보 조회", description = "특정 사용자의 정보를 조회합니다.")
    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long userId) {
        UserResponse response = userService.getUserById(userId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "프로필 수정", description = "사용자의 프로필(닉네임, 프로필 이미지)을 수정합니다.")
    @PutMapping("/me")
    public ApiResponse<UserResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        UserResponse response = userService.updateProfile(userId, request);
        return ApiResponse.success(response, "프로필이 수정되었습니다.");
    }

    @Operation(summary = "비밀번호 변경", description = "사용자의 비밀번호를 변경합니다.")
    @PutMapping("/me/password")
    public ApiResponse<Void> updatePassword(@Valid @RequestBody UpdatePasswordRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        userService.updatePassword(userId, request);
        return ApiResponse.success("비밀번호가 변경되었습니다.");
    }

    @Operation(summary = "회원 탈퇴", description = "현재 로그인한 사용자를 삭제합니다.")
    @DeleteMapping("/me")
    public ApiResponse<Void> deleteUser() {
        Long userId = SecurityUtil.getCurrentUserId();
        userService.deleteUser(userId);
        return ApiResponse.success("회원 탈퇴가 완료되었습니다.");
    }

}
