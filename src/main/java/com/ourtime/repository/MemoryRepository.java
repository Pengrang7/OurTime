package com.ourtime.repository;

import com.ourtime.domain.Memory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemoryRepository extends JpaRepository<Memory, Long> {

    @Query("SELECT m FROM Memory m WHERE m.group.id = :groupId ORDER BY m.visitedAt DESC")
    Page<Memory> findAllByGroupId(@Param("groupId") Long groupId, Pageable pageable);

    @Query("SELECT m FROM Memory m WHERE m.group.id = :groupId ORDER BY m.visitedAt DESC")
    List<Memory> findAllByGroupId(@Param("groupId") Long groupId);

    @Query("SELECT m FROM Memory m WHERE m.user.id = :userId ORDER BY m.visitedAt DESC")
    Page<Memory> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT m FROM Memory m WHERE m.user.id = :userId ORDER BY m.visitedAt DESC")
    List<Memory> findAllByUserId(@Param("userId") Long userId);
    
    // 사용자가 속한 그룹들의 모든 메모리 조회
    @Query("SELECT DISTINCT m FROM Memory m " +
           "JOIN m.group g " +
           "JOIN g.userGroups ug " +
           "WHERE ug.user.id = :userId " +
           "ORDER BY m.visitedAt DESC")
    List<Memory> findAllByUserGroups(@Param("userId") Long userId);

    @Query("SELECT m FROM Memory m " +
           "WHERE m.group.id = :groupId " +
           "AND m.visitedAt BETWEEN :startDate AND :endDate " +
           "ORDER BY m.visitedAt DESC")
    List<Memory> findAllByGroupIdAndDateRange(@Param("groupId") Long groupId,
                                               @Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate);

    @Query("SELECT m FROM Memory m " +
           "JOIN m.memoryTags mt " +
           "WHERE m.group.id = :groupId AND mt.tag.id = :tagId " +
           "ORDER BY m.visitedAt DESC")
    List<Memory> findAllByGroupIdAndTagId(@Param("groupId") Long groupId, @Param("tagId") Long tagId);

    @Query("SELECT m FROM Memory m " +
           "WHERE m.id = :memoryId AND m.group.id = :groupId")
    Optional<Memory> findByIdAndGroupId(@Param("memoryId") Long memoryId, @Param("groupId") Long groupId);

    // 특정 날짜의 1년 전 추억 조회 (알림용)
    @Query("SELECT m FROM Memory m " +
           "WHERE FUNCTION('MONTH', m.visitedAt) = :month " +
           "AND FUNCTION('DAY', m.visitedAt) = :day " +
           "AND FUNCTION('YEAR', m.visitedAt) = :year")
    List<Memory> findMemoriesByDate(@Param("month") int month, 
                                     @Param("day") int day, 
                                     @Param("year") int year);

}
