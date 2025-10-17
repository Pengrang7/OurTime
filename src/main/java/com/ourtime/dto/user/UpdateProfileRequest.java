package com.ourtime.dto.user;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateProfileRequest {

    @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하여야 합니다.")
    private String nickname;

    @Size(max = 8, message = "사용자 태그는 8자 이하여야 합니다.")
    private String userTag;

    private String profileImage;

}
