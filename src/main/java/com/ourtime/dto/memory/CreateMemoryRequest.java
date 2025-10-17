package com.ourtime.dto.memory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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
    
    private List<String> tagNames = new ArrayList<>();
    
    private List<MultipartFile> images = new ArrayList<>();

    // Multipart 요청을 위한 생성자
    public CreateMemoryRequest(Long groupId, String title, String description,
                                Double latitude, Double longitude, String locationName,
                                String visitedAt, String tagNamesJson, List<MultipartFile> images) {
        this.groupId = groupId;
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationName = locationName;
        
        log.debug("CreateMemoryRequest 생성 - locationName: {}, tagNamesJson: {}", locationName, tagNamesJson);
        
        // visitedAt 파싱 (YYYY-MM-DD 형식)
        if (visitedAt != null && !visitedAt.isEmpty()) {
            LocalDate date = LocalDate.parse(visitedAt, DateTimeFormatter.ISO_DATE);
            this.visitedAt = date.atStartOfDay();
        }
        
        // tagNames JSON 파싱
        if (tagNamesJson != null && !tagNamesJson.isEmpty()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                this.tagNames = mapper.readValue(tagNamesJson, 
                    mapper.getTypeFactory().constructCollectionType(List.class, String.class));
                log.debug("태그 파싱 성공: {}", this.tagNames);
            } catch (JsonProcessingException e) {
                log.error("태그 파싱 실패: {}", tagNamesJson, e);
                this.tagNames = new ArrayList<>();
            }
        } else {
            this.tagNames = new ArrayList<>();
        }
        
        this.images = images != null ? images : new ArrayList<>();
        log.debug("이미지 개수: {}", this.images.size());
    }

}
