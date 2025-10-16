package com.ourtime.controller;

import com.ourtime.dto.common.ApiResponse;
import com.ourtime.dto.group.GroupInvitationResponse;
import com.ourtime.util.SecurityUtil;
import com.ourtime.service.GroupInvitationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "GroupInvitation", description = "그룹 초대 API")
@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
public class GroupInvitationController {

    private final GroupInvitationService groupInvitationService;

    @Operation(summary = "내가 받은 초대 목록 조회", description = "현재 로그인한 사용자가 받은 그룹 초대 목록을 조회합니다.")
    @GetMapping
    public ApiResponse<List<GroupInvitationResponse>> getMyInvitations() {
        Long userId = SecurityUtil.getCurrentUserId();
        List<GroupInvitationResponse> invitations = groupInvitationService.getMyInvitations(userId);
        return ApiResponse.success(invitations);
    }

    @Operation(summary = "대기중인 초대 목록 조회", description = "현재 로그인한 사용자가 받은 대기중인 그룹 초대 목록을 조회합니다.")
    @GetMapping("/pending")
    public ApiResponse<List<GroupInvitationResponse>> getMyPendingInvitations() {
        Long userId = SecurityUtil.getCurrentUserId();
        List<GroupInvitationResponse> invitations = groupInvitationService.getMyPendingInvitations(userId);
        return ApiResponse.success(invitations);
    }

    @Operation(summary = "초대 수락", description = "그룹 초대를 수락하고 그룹 멤버가 됩니다.")
    @PostMapping("/{invitationId}/accept")
    public ApiResponse<Void> acceptInvitation(@PathVariable Long invitationId) {
        Long userId = SecurityUtil.getCurrentUserId();
        groupInvitationService.acceptInvitation(invitationId, userId);
        return ApiResponse.success();
    }

    @Operation(summary = "초대 거절", description = "그룹 초대를 거절합니다.")
    @PostMapping("/{invitationId}/reject")
    public ApiResponse<Void> rejectInvitation(@PathVariable Long invitationId) {
        Long userId = SecurityUtil.getCurrentUserId();
        groupInvitationService.rejectInvitation(invitationId, userId);
        return ApiResponse.success();
    }
}

