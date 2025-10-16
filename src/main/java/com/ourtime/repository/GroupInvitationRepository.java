package com.ourtime.repository;

import com.ourtime.domain.GroupInvitation;
import com.ourtime.domain.GroupInvitation.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupInvitationRepository extends JpaRepository<GroupInvitation, Long> {

    /**
     * 특정 사용자가 받은 초대 목록 조회
     */
    @Query("SELECT gi FROM GroupInvitation gi " +
           "JOIN FETCH gi.group " +
           "JOIN FETCH gi.inviter " +
           "WHERE gi.invitee.id = :userId " +
           "ORDER BY gi.createdAt DESC")
    List<GroupInvitation> findByInviteeId(@Param("userId") Long userId);

    /**
     * 특정 사용자가 받은 대기중인 초대 목록 조회
     */
    @Query("SELECT gi FROM GroupInvitation gi " +
           "JOIN FETCH gi.group " +
           "JOIN FETCH gi.inviter " +
           "WHERE gi.invitee.id = :userId " +
           "AND gi.status = :status " +
           "ORDER BY gi.createdAt DESC")
    List<GroupInvitation> findByInviteeIdAndStatus(
        @Param("userId") Long userId, 
        @Param("status") InvitationStatus status
    );

    /**
     * 이미 초대된 적이 있는지 확인 (중복 방지)
     */
    @Query("SELECT gi FROM GroupInvitation gi " +
           "WHERE gi.group.id = :groupId " +
           "AND gi.invitee.id = :inviteeId " +
           "AND gi.status = 'PENDING'")
    Optional<GroupInvitation> findPendingInvitation(
        @Param("groupId") Long groupId,
        @Param("inviteeId") Long inviteeId
    );

    /**
     * 그룹의 모든 초대 목록 조회
     */
    @Query("SELECT gi FROM GroupInvitation gi " +
           "JOIN FETCH gi.invitee " +
           "WHERE gi.group.id = :groupId " +
           "ORDER BY gi.createdAt DESC")
    List<GroupInvitation> findByGroupId(@Param("groupId") Long groupId);
}

