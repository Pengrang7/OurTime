package com.ourtime.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "groups")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Group extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GroupType type;

    @Column(nullable = false, unique = true, length = 100)
    private String inviteCode;

    @Column(nullable = false)
    private Long createdBy;

    @Column(length = 500)
    private String description;

    @Column(length = 500)
    private String groupImage;

    @Builder.Default
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserGroup> userGroups = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Memory> memories = new ArrayList<>();

    // 비즈니스 메서드
    public void updateInfo(String name, GroupType type, String description, String groupImage) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
        if (type != null) {
            this.type = type;
        }
        if (description != null) {
            this.description = description;
        }
        if (groupImage != null) {
            this.groupImage = groupImage;
        }
    }

    public void regenerateInviteCode() {
        this.inviteCode = UUID.randomUUID().toString();
    }

    @PrePersist
    private void generateInviteCode() {
        if (this.inviteCode == null) {
            this.inviteCode = UUID.randomUUID().toString();
        }
    }

}
