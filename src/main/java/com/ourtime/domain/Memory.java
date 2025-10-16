package com.ourtime.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "memories", indexes = {
    @Index(name = "idx_group_id", columnList = "group_id"),
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_visited_at", columnList = "visited_at")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Memory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(length = 500)
    private String locationName;

    @Column(nullable = false)
    private LocalDateTime visitedAt;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "memory_images", joinColumns = @JoinColumn(name = "memory_id"))
    @Column(name = "image_url", length = 500)
    private List<String> imageUrls = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "memory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemoryTag> memoryTags = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "memory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "memory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    // 비즈니스 메서드
    public void updateContent(String title, String description, String locationName, LocalDateTime visitedAt) {
        if (title != null && !title.isBlank()) {
            this.title = title;
        }
        if (description != null) {
            this.description = description;
        }
        if (locationName != null) {
            this.locationName = locationName;
        }
        if (visitedAt != null) {
            this.visitedAt = visitedAt;
        }
    }

    public void updateLocation(Double latitude, Double longitude, String locationName) {
        if (latitude != null) {
            this.latitude = latitude;
        }
        if (longitude != null) {
            this.longitude = longitude;
        }
        if (locationName != null) {
            this.locationName = locationName;
        }
    }

    public void addImage(String imageUrl) {
        this.imageUrls.add(imageUrl);
    }

    public void removeImage(String imageUrl) {
        this.imageUrls.remove(imageUrl);
    }

    public int getLikeCount() {
        return this.likes.size();
    }

    public int getCommentCount() {
        return this.comments.size();
    }

}
