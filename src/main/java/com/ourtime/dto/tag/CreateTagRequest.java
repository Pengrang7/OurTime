package com.ourtime.dto.tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateTagRequest {

    @NotBlank(message = "태그 이름은 필수입니다.")
    @Size(min = 1, max = 20, message = "태그 이름은 1자 이상 20자 이하여야 합니다.")
    private String name;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "올바른 HEX 색상 코드 형식이 아닙니다. (예: #FF5733)")
    private String color;

}
