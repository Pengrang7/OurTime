package com.ourtime.dto.memory;

import com.ourtime.domain.Memory;
import com.ourtime.dto.tag.TagResponse;
import com.ourtime.dto.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoryResponse {

    private Long id;
    private Long groupId;
    private UserResponse user;
    private String title;
    private String description;
    private Double latitude;
    private Double longitude;
    private String locationName;
    private LocalDateTime visitedAt;
    private List<String> imageUrls;
    private List<TagResponse> tags;
    private int likeCount;
    private int commentCount;
    private LocalDateTime createdAt;

    public static MemoryResponse from(Memory memory) {
        return MemoryResponse.builder()
                .id(memory.getId())
                .groupId(memory.getGroup().getId())
                .user(UserResponse.from(memory.getUser()))
                .title(memory.getTitle())
                .description(memory.getDescription())
                .latitude(memory.getLatitude())
                .longitude(memory.getLongitude())
                .locationName(memory.getLocationName())
                .visitedAt(memory.getVisitedAt())
                .imageUrls(memory.getImageUrls())
                .tags(memory.getMemoryTags().stream()
                        .map(mt -> TagResponse.from(mt.getTag()))
                        .collect(Collectors.toList()))
                .likeCount(memory.getLikeCount())
                .commentCount(memory.getCommentCount())
                .createdAt(memory.getCreatedAt())
                .build();
    }

}
