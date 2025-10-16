package com.ourtime.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comments", indexes = {
    @Index(name = "idx_memory_id", columnList = "memory_id"),
    @Index(name = "idx_user_id", columnList = "user_id")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memory_id", nullable = false)
    private Memory memory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // 비즈니스 메서드
    public void updateContent(String newContent) {
        if (newContent != null && !newContent.isBlank()) {
            this.content = newContent;
        }
    }

}
