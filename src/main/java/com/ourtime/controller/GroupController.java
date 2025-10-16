package com.ourtime.controller;

import com.ourtime.dto.common.ApiResponse;
import com.ourtime.dto.group.CreateGroupRequest;
import com.ourtime.dto.group.GroupCreateRequest;
import com.ourtime.dto.group.GroupResponse;
import com.ourtime.dto.group.JoinGroupRequest;
import com.ourtime.dto.group.UpdateGroupRequest;
import com.ourtime.service.GroupService;
import com.ourtime.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Group", description = "그룹 관련 API")
@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @Operation(summary = "그룹 생성", description = "새로운 그룹을 생성합니다. 생성자는 자동으로 ADMIN이 됩니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<GroupResponse> createGroup(@Valid @RequestBody CreateGroupRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        GroupResponse response = groupService.createGroup(userId, request);
        return ApiResponse.success(response, "그룹이 생성되었습니다.");
    }

    @Operation(summary = "그룹 생성 및 멤버 초대", description = "새로운 그룹을 생성하고 멤버를 초대합니다. 생성자는 자동으로 ADMIN이 됩니다.")
    @PostMapping("/with-invites")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<GroupResponse> createGroupWithInvites(@Valid @RequestBody GroupCreateRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        GroupResponse response = groupService.createGroupWithInvites(userId, request);
        return ApiResponse.success(response, "그룹이 생성되고 초대가 전송되었습니다.");
    }

    @Operation(summary = "내 그룹 목록 조회", description = "내가 속한 모든 그룹을 조회합니다.")
    @GetMapping
    public ApiResponse<List<GroupResponse>> getMyGroups() {
        Long userId = SecurityUtil.getCurrentUserId();
        List<GroupResponse> response = groupService.getMyGroups(userId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "그룹 상세 조회", description = "특정 그룹의 상세 정보를 조회합니다.")
    @GetMapping("/{groupId}")
    public ApiResponse<GroupResponse> getGroupById(@PathVariable Long groupId) {
        Long userId = SecurityUtil.getCurrentUserId();
        GroupResponse response = groupService.getGroupById(groupId, userId);
        return ApiResponse.success(response);
    }

    @Operation(summary = "그룹 정보 수정", description = "그룹의 정보를 수정합니다. (ADMIN 권한 필요)")
    @PutMapping("/{groupId}")
    public ApiResponse<GroupResponse> updateGroup(
            @PathVariable Long groupId,
            @Valid @RequestBody UpdateGroupRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        GroupResponse response = groupService.updateGroup(groupId, userId, request);
        return ApiResponse.success(response, "그룹 정보가 수정되었습니다.");
    }

    @Operation(summary = "초대 코드 재생성", description = "그룹의 초대 코드를 새로 생성합니다. (ADMIN 권한 필요)")
    @PostMapping("/{groupId}/invite-code")
    public ApiResponse<String> regenerateInviteCode(@PathVariable Long groupId) {
        Long userId = SecurityUtil.getCurrentUserId();
        String inviteCode = groupService.regenerateInviteCode(groupId, userId);
        return ApiResponse.success(inviteCode, "초대 코드가 재생성되었습니다.");
    }

    @Operation(summary = "그룹 참여", description = "초대 코드로 그룹에 참여합니다.")
    @PostMapping("/join")
    public ApiResponse<GroupResponse> joinGroup(@Valid @RequestBody JoinGroupRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        GroupResponse response = groupService.joinGroup(userId, request);
        return ApiResponse.success(response, "그룹에 참여했습니다.");
    }

    @Operation(summary = "그룹 탈퇴", description = "그룹에서 탈퇴합니다. (ADMIN은 탈퇴 불가)")
    @DeleteMapping("/{groupId}/leave")
    public ApiResponse<Void> leaveGroup(@PathVariable Long groupId) {
        Long userId = SecurityUtil.getCurrentUserId();
        groupService.leaveGroup(groupId, userId);
        return ApiResponse.success("그룹에서 탈퇴했습니다.");
    }

    @Operation(summary = "그룹 삭제", description = "그룹을 삭제합니다. (ADMIN 권한 필요)")
    @DeleteMapping("/{groupId}")
    public ApiResponse<Void> deleteGroup(@PathVariable Long groupId) {
        Long userId = SecurityUtil.getCurrentUserId();
        groupService.deleteGroup(groupId, userId);
        return ApiResponse.success("그룹이 삭제되었습니다.");
    }

}
