package com.ourtime.dto.group;

import com.ourtime.domain.GroupType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "그룹 생성 요청")
public class GroupCreateRequest {

    @NotBlank(message = "그룹 이름은 필수입니다")
    @Size(max = 100, message = "그룹 이름은 100자 이내여야 합니다")
    @Schema(description = "그룹 이름", example = "우리 가족")
    private String name;

    @NotNull(message = "그룹 타입은 필수입니다")
    @Schema(description = "그룹 타입", example = "FAMILY")
    private GroupType type;

    @Size(max = 500, message = "설명은 500자 이내여야 합니다")
    @Schema(description = "그룹 설명", example = "우리 가족의 추억을 기록하는 공간")
    private String description;

    @Schema(description = "초대할 멤버 이메일 목록", example = "[\"user1@example.com\", \"user2@example.com\"]")
    private List<String> inviteeEmails;
}

