package com.ourtime.repository;

import com.ourtime.domain.MemoryTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemoryTagRepository extends JpaRepository<MemoryTag, Long> {

    @Query("SELECT mt FROM MemoryTag mt WHERE mt.memory.id = :memoryId")
    List<MemoryTag> findAllByMemoryId(@Param("memoryId") Long memoryId);

    @Query("SELECT mt FROM MemoryTag mt WHERE mt.tag.id = :tagId")
    List<MemoryTag> findAllByTagId(@Param("tagId") Long tagId);

    @Query("SELECT mt FROM MemoryTag mt WHERE mt.memory.id = :memoryId AND mt.tag.id = :tagId")
    Optional<MemoryTag> findByMemoryIdAndTagId(@Param("memoryId") Long memoryId, @Param("tagId") Long tagId);

    void deleteByMemoryIdAndTagId(Long memoryId, Long tagId);

}
