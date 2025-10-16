package com.ourtime.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tags")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Tag extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(length = 7)
    private String color; // HEX 색상 코드 (예: #FF5733)

    @Builder.Default
    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemoryTag> memoryTags = new ArrayList<>();

    // 비즈니스 메서드
    public void updateInfo(String name, String color) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
        if (color != null) {
            this.color = color;
        }
    }

}
