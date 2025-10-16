package com.ourtime.repository;

import com.ourtime.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByInviteCode(String inviteCode);

    @Query("SELECT g FROM Group g JOIN g.userGroups ug WHERE ug.user.id = :userId")
    List<Group> findAllByUserId(@Param("userId") Long userId);

    boolean existsByInviteCode(String inviteCode);

}
