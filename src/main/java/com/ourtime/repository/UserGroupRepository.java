package com.ourtime.repository;

import com.ourtime.domain.UserGroup;
import com.ourtime.domain.UserGroupRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

    @Query("SELECT ug FROM UserGroup ug WHERE ug.user.id = :userId AND ug.group.id = :groupId")
    Optional<UserGroup> findByUserIdAndGroupId(@Param("userId") Long userId, @Param("groupId") Long groupId);

    @Query("SELECT ug FROM UserGroup ug WHERE ug.group.id = :groupId")
    List<UserGroup> findAllByGroupId(@Param("groupId") Long groupId);

    @Query("SELECT ug FROM UserGroup ug WHERE ug.user.id = :userId")
    List<UserGroup> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(ug) FROM UserGroup ug WHERE ug.group.id = :groupId")
    long countByGroupId(@Param("groupId") Long groupId);

    boolean existsByUserIdAndGroupId(Long userId, Long groupId);

    @Query("SELECT CASE WHEN COUNT(ug) > 0 THEN true ELSE false END " +
           "FROM UserGroup ug WHERE ug.user.id = :userId AND ug.group.id = :groupId AND ug.role = :role")
    boolean existsByUserIdAndGroupIdAndRole(@Param("userId") Long userId, 
                                           @Param("groupId") Long groupId, 
                                           @Param("role") UserGroupRole role);

}
