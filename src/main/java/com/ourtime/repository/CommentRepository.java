package com.ourtime.repository;

import com.ourtime.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.memory.id = :memoryId ORDER BY c.createdAt ASC")
    List<Comment> findAllByMemoryId(@Param("memoryId") Long memoryId);

    @Query("SELECT c FROM Comment c WHERE c.memory.id = :memoryId ORDER BY c.createdAt ASC")
    Page<Comment> findAllByMemoryId(@Param("memoryId") Long memoryId, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.user.id = :userId ORDER BY c.createdAt DESC")
    Page<Comment> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.id = :commentId AND c.user.id = :userId")
    Optional<Comment> findByIdAndUserId(@Param("commentId") Long commentId, @Param("userId") Long userId);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.memory.id = :memoryId")
    long countByMemoryId(@Param("memoryId") Long memoryId);

}
