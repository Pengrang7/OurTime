package com.ourtime.service;

import com.ourtime.domain.GroupInvitation;
import com.ourtime.domain.GroupInvitation.InvitationStatus;
import com.ourtime.domain.User;
import com.ourtime.domain.UserGroup;
import com.ourtime.domain.UserGroupRole;
import com.ourtime.dto.group.GroupInvitationResponse;
import com.ourtime.exception.BusinessException;
import com.ourtime.exception.ErrorCode;
import com.ourtime.repository.GroupInvitationRepository;
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
public class GroupInvitationService {

    private final GroupInvitationRepository groupInvitationRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;

    /**
     * 내가 받은 초대 목록 조회
     */
    public List<GroupInvitationResponse> getMyInvitations(Long userId) {
        List<GroupInvitation> invitations = groupInvitationRepository.findByInviteeId(userId);
        return invitations.stream()
                .map(GroupInvitationResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 내가 받은 대기중인 초대 목록 조회
     */
    public List<GroupInvitationResponse> getMyPendingInvitations(Long userId) {
        List<GroupInvitation> invitations = groupInvitationRepository.findByInviteeIdAndStatus(
                userId, InvitationStatus.PENDING);
        return invitations.stream()
                .map(GroupInvitationResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 초대 수락
     */
    @Transactional
    public void acceptInvitation(Long invitationId, Long userId) {
        GroupInvitation invitation = groupInvitationRepository.findById(invitationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVITATION_NOT_FOUND));

        // 초대받은 사람인지 확인
        if (!invitation.getInvitee().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NOT_INVITATION_RECIPIENT);
        }

        // 이미 처리된 초대인지 확인
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new BusinessException(ErrorCode.INVITATION_ALREADY_PROCESSED);
        }

        // 초대 수락
        invitation.accept();

        // 그룹에 멤버로 추가
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        UserGroup userGroup = UserGroup.builder()
                .user(user)
                .group(invitation.getGroup())
                .role(UserGroupRole.MEMBER)
                .build();

        userGroupRepository.save(userGroup);

        log.info("초대 수락: 사용자 {} 그룹 {} 참여", userId, invitation.getGroup().getId());
    }

    /**
     * 초대 거절
     */
    @Transactional
    public void rejectInvitation(Long invitationId, Long userId) {
        GroupInvitation invitation = groupInvitationRepository.findById(invitationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVITATION_NOT_FOUND));

        // 초대받은 사람인지 확인
        if (!invitation.getInvitee().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NOT_INVITATION_RECIPIENT);
        }

        // 이미 처리된 초대인지 확인
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new BusinessException(ErrorCode.INVITATION_ALREADY_PROCESSED);
        }

        // 초대 거절
        invitation.reject();

        log.info("초대 거절: 사용자 {} 그룹 {} 초대 거절", userId, invitation.getGroup().getId());
    }
}

