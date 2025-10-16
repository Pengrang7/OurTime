package com.ourtime.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_group", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "group_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserGroup extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserGroupRole role;

    // 비즈니스 메서드
    public void changeRole(UserGroupRole newRole) {
        this.role = newRole;
    }

    public boolean isAdmin() {
        return this.role == UserGroupRole.ADMIN;
    }

}
