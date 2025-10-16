package com.ourtime.dto.group;

import com.ourtime.domain.GroupType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateGroupRequest {

    @NotBlank(message = "그룹 이름은 필수입니다.")
    @Size(min = 2, max = 50, message = "그룹 이름은 2자 이상 50자 이하여야 합니다.")
    private String name;

    @NotNull(message = "그룹 타입은 필수입니다.")
    private GroupType type;

    @Size(max = 200, message = "그룹 설명은 200자 이하여야 합니다.")
    private String description;

    private String groupImage;

}
