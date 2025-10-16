package com.ourtime.dto.group;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JoinGroupRequest {

    @NotBlank(message = "초대 코드는 필수입니다.")
    private String inviteCode;

}
