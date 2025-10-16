package com.ourtime.repository;

import com.ourtime.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    @Query("SELECT l FROM Like l WHERE l.memory.id = :memoryId AND l.user.id = :userId")
    Optional<Like> findByMemoryIdAndUserId(@Param("memoryId") Long memoryId, @Param("userId") Long userId);

    @Query("SELECT COUNT(l) FROM Like l WHERE l.memory.id = :memoryId")
    long countByMemoryId(@Param("memoryId") Long memoryId);

    boolean existsByMemoryIdAndUserId(Long memoryId, Long userId);

    void deleteByMemoryIdAndUserId(Long memoryId, Long userId);

}
