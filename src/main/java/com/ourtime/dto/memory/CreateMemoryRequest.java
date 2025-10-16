package com.ourtime.dto.memory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreateMemoryRequest {

    @NotNull(message = "그룹 ID는 필수입니다.")
    private Long groupId;

    @NotBlank(message = "제목은 필수입니다.")
    @Size(min = 1, max = 100, message = "제목은 1자 이상 100자 이하여야 합니다.")
    private String title;

    @Size(max = 2000, message = "내용은 2000자 이하여야 합니다.")
    private String description;

    @NotNull(message = "위도는 필수입니다.")
    private Double latitude;

    @NotNull(message = "경도는 필수입니다.")
    private Double longitude;

    @Size(max = 200, message = "장소명은 200자 이하여야 합니다.")
    private String locationName;

    @NotNull(message = "방문 날짜는 필수입니다.")
    private LocalDateTime visitedAt;

    private List<String> imageUrls = new ArrayList<>();

    private List<Long> tagIds = new ArrayList<>();

}
