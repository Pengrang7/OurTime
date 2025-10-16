package com.ourtime.dto.group;

import com.ourtime.domain.GroupInvitation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "그룹 초대 응답")
public class GroupInvitationResponse {

    @Schema(description = "초대 ID", example = "1")
    private Long id;

    @Schema(description = "그룹 ID", example = "1")
    private Long groupId;

    @Schema(description = "그룹 이름", example = "우리 가족")
    private String groupName;

    @Schema(description = "초대한 사람 ID", example = "1")
    private Long inviterId;

    @Schema(description = "초대한 사람 닉네임", example = "홍길동")
    private String inviterNickname;

    @Schema(description = "초대 상태", example = "PENDING")
    private GroupInvitation.InvitationStatus status;

    @Schema(description = "초대 생성 시간")
    private LocalDateTime createdAt;

    @Schema(description = "응답 시간")
    private LocalDateTime respondedAt;

    public static GroupInvitationResponse from(GroupInvitation invitation) {
        return GroupInvitationResponse.builder()
                .id(invitation.getId())
                .groupId(invitation.getGroup().getId())
                .groupName(invitation.getGroup().getName())
                .inviterId(invitation.getInviter().getId())
                .inviterNickname(invitation.getInviter().getNickname())
                .status(invitation.getStatus())
                .createdAt(invitation.getCreatedAt())
                .respondedAt(invitation.getRespondedAt())
                .build();
    }
}

