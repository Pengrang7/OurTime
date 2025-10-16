package com.ourtime.dto.group;

import com.ourtime.domain.Group;
import com.ourtime.domain.GroupType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupResponse {

    private Long id;
    private String name;
    private GroupType type;
    private String description;
    private String groupImage;
    private String inviteCode;
    private Long createdBy;
    private int memberCount;
    private LocalDateTime createdAt;

    public static GroupResponse from(Group group, int memberCount) {
        return GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .type(group.getType())
                .description(group.getDescription())
                .groupImage(group.getGroupImage())
                .inviteCode(group.getInviteCode())
                .createdBy(group.getCreatedBy())
                .memberCount(memberCount)
                .createdAt(group.getCreatedAt())
                .build();
    }

}
