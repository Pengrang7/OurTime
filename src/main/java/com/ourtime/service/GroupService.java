package com.ourtime.service;

import com.ourtime.domain.Group;
import com.ourtime.domain.GroupInvitation;
import com.ourtime.domain.User;
import com.ourtime.domain.UserGroup;
import com.ourtime.domain.UserGroupRole;
import com.ourtime.dto.group.CreateGroupRequest;
import com.ourtime.dto.group.GroupCreateRequest;
import com.ourtime.dto.group.GroupResponse;
import com.ourtime.dto.group.JoinGroupRequest;
import com.ourtime.dto.group.UpdateGroupRequest;
import com.ourtime.exception.BusinessException;
import com.ourtime.exception.ErrorCode;
import com.ourtime.repository.GroupInvitationRepository;
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
    private final GroupInvitationRepository groupInvitationRepository;

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

    /**
     * 그룹 생성 및 멤버 초대
     */
    @Transactional
    public GroupResponse createGroupWithInvites(Long userId, GroupCreateRequest request) {
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 그룹 생성
        Group group = Group.builder()
                .name(request.getName())
                .type(request.getType())
                .description(request.getDescription())
                .createdBy(userId)
                .build();

        Group savedGroup = groupRepository.save(group);

        // 생성자를 ADMIN으로 그룹에 추가
        UserGroup userGroup = UserGroup.builder()
                .user(creator)
                .group(savedGroup)
                .role(UserGroupRole.ADMIN)
                .build();

        userGroupRepository.save(userGroup);

        // 멤버 초대
        if (request.getInviteeEmails() != null && !request.getInviteeEmails().isEmpty()) {
            for (String email : request.getInviteeEmails()) {
                User invitee = userRepository.findByEmail(email)
                        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, "초대할 사용자를 찾을 수 없습니다: " + email));

                // 이미 그룹 멤버인지 확인
                if (userGroupRepository.existsByUserIdAndGroupId(invitee.getId(), savedGroup.getId())) {
                    continue; // 이미 멤버면 건너뛰기
                }

                // 이미 초대된 적이 있는지 확인
                if (groupInvitationRepository.findPendingInvitation(savedGroup.getId(), invitee.getId()).isPresent()) {
                    continue; // 이미 초대되었으면 건너뛰기
                }

                // 초대 생성
                GroupInvitation invitation = GroupInvitation.builder()
                        .group(savedGroup)
                        .inviter(creator)
                        .invitee(invitee)
                        .build();

                groupInvitationRepository.save(invitation);
                log.info("그룹 {} 멤버 {} 초대", savedGroup.getId(), invitee.getEmail());
            }
        }

        log.info("새 그룹 생성 및 초대 완료: {} by {}", savedGroup.getId(), userId);

        int memberCount = (int) userGroupRepository.countByGroupId(savedGroup.getId());
        return GroupResponse.from(savedGroup, memberCount);
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
