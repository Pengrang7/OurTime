package com.ourtime.service;

import com.ourtime.domain.Group;
import com.ourtime.domain.User;
import com.ourtime.domain.UserGroup;
import com.ourtime.domain.UserGroupRole;
import com.ourtime.dto.group.CreateGroupRequest;
import com.ourtime.dto.group.GroupResponse;
import com.ourtime.dto.group.JoinGroupRequest;
import com.ourtime.dto.group.UpdateGroupRequest;
import com.ourtime.exception.BusinessException;
import com.ourtime.exception.ErrorCode;
import com.ourtime.repository.GroupRepository;
import com.ourtime.repository.UserGroupRepository;
import com.ourtime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;

    @Transactional
    public GroupResponse createGroup(Long userId, CreateGroupRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 그룹 생성
        Group group = Group.builder()
                .name(request.getName())
                .type(request.getType())
                .description(request.getDescription())
                .groupImage(request.getGroupImage())
                .createdBy(userId)
                .build();

        Group savedGroup = groupRepository.save(group);

        // 생성자를 ADMIN으로 그룹에 추가
        UserGroup userGroup = UserGroup.builder()
                .user(user)
                .group(savedGroup)
                .role(UserGroupRole.ADMIN)
                .build();

        userGroupRepository.save(userGroup);

        log.info("새 그룹 생성: {} by {}", savedGroup.getId(), userId);

        return GroupResponse.from(savedGroup, 1);
    }

    public GroupResponse getGroupById(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BusinessException(ErrorCode.GROUP_NOT_FOUND));

        // 그룹 멤버인지 확인
        if (!userGroupRepository.existsByUserIdAndGroupId(userId, groupId)) {
            throw new BusinessException(ErrorCode.NOT_GROUP_MEMBER);
        }

        int memberCount = (int) userGroupRepository.countByGroupId(groupId);
        return GroupResponse.from(group, memberCount);
    }

    public List<GroupResponse> getMyGroups(Long userId) {
        List<Group> groups = groupRepository.findAllByUserId(userId);
        
        return groups.stream()
                .map(group -> {
                    int memberCount = (int) userGroupRepository.countByGroupId(group.getId());
                    return GroupResponse.from(group, memberCount);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public GroupResponse updateGroup(Long groupId, Long userId, UpdateGroupRequest request) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BusinessException(ErrorCode.GROUP_NOT_FOUND));

        // ADMIN 권한 확인
        if (!userGroupRepository.existsByUserIdAndGroupIdAndRole(userId, groupId, UserGroupRole.ADMIN)) {
            throw new BusinessException(ErrorCode.NOT_GROUP_ADMIN);
        }

        group.updateInfo(request.getName(), request.getType(), request.getDescription(), request.getGroupImage());
        log.info("그룹 정보 업데이트: {}", groupId);

        int memberCount = (int) userGroupRepository.countByGroupId(groupId);
        return GroupResponse.from(group, memberCount);
    }

    @Transactional
    public String regenerateInviteCode(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BusinessException(ErrorCode.GROUP_NOT_FOUND));

        // ADMIN 권한 확인
        if (!userGroupRepository.existsByUserIdAndGroupIdAndRole(userId, groupId, UserGroupRole.ADMIN)) {
            throw new BusinessException(ErrorCode.NOT_GROUP_ADMIN);
        }

        group.regenerateInviteCode();
        log.info("그룹 초대 코드 재생성: {}", groupId);

        return group.getInviteCode();
    }

    @Transactional
    public GroupResponse joinGroup(Long userId, JoinGroupRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Group group = groupRepository.findByInviteCode(request.getInviteCode())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INVITE_CODE));

        // 이미 그룹 멤버인지 확인
        if (userGroupRepository.existsByUserIdAndGroupId(userId, group.getId())) {
            throw new BusinessException(ErrorCode.ALREADY_GROUP_MEMBER);
        }

        // 그룹에 MEMBER로 추가
        UserGroup userGroup = UserGroup.builder()
                .user(user)
                .group(group)
                .role(UserGroupRole.MEMBER)
                .build();

        userGroupRepository.save(userGroup);

        log.info("사용자 {} 그룹 {} 참여", userId, group.getId());

        int memberCount = (int) userGroupRepository.countByGroupId(group.getId());
        return GroupResponse.from(group, memberCount);
    }

    @Transactional
    public void leaveGroup(Long groupId, Long userId) {
        UserGroup userGroup = userGroupRepository.findByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_GROUP_MEMBER));

        // ADMIN은 탈퇴 불가
        if (userGroup.isAdmin()) {
            throw new BusinessException(ErrorCode.CANNOT_LEAVE_AS_ADMIN);
        }

        userGroupRepository.delete(userGroup);
        log.info("사용자 {} 그룹 {} 탈퇴", userId, groupId);
    }

    @Transactional
    public void deleteGroup(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BusinessException(ErrorCode.GROUP_NOT_FOUND));

        // ADMIN 권한 확인
        if (!userGroupRepository.existsByUserIdAndGroupIdAndRole(userId, groupId, UserGroupRole.ADMIN)) {
            throw new BusinessException(ErrorCode.NOT_GROUP_ADMIN);
        }

        groupRepository.delete(group);
        log.info("그룹 삭제: {}", groupId);
    }

}
