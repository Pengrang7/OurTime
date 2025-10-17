package com.ourtime.dto.memory;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ourtime.util.FlexibleDateTimeDeserializer;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class UpdateMemoryRequest {

    @Size(min = 1, max = 100, message = "제목은 1자 이상 100자 이하여야 합니다.")
    private String title;

    @Size(max = 2000, message = "내용은 2000자 이하여야 합니다.")
    private String description;

    private Double latitude;

    private Double longitude;

    @Size(max = 200, message = "장소명은 200자 이하여야 합니다.")
    private String locationName;

    @JsonDeserialize(using = FlexibleDateTimeDeserializer.class)
    private LocalDateTime visitedAt;

    private List<Long> tagIds;
    
    private List<String> tagNames;

}
