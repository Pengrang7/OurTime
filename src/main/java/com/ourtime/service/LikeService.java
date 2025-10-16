package com.ourtime.service;

import com.ourtime.domain.Like;
import com.ourtime.domain.Memory;
import com.ourtime.domain.User;
import com.ourtime.exception.BusinessException;
import com.ourtime.exception.ErrorCode;
import com.ourtime.repository.LikeRepository;
import com.ourtime.repository.MemoryRepository;
import com.ourtime.repository.UserGroupRepository;
import com.ourtime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;
    private final MemoryRepository memoryRepository;
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;

    @Transactional
    public void toggleLike(Long memoryId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Memory memory = memoryRepository.findById(memoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMORY_NOT_FOUND));

        // 그룹 멤버인지 확인
        if (!userGroupRepository.existsByUserIdAndGroupId(userId, memory.getGroup().getId())) {
            throw new BusinessException(ErrorCode.NOT_GROUP_MEMBER);
        }

        // 이미 좋아요를 눌렀는지 확인
        if (likeRepository.existsByMemoryIdAndUserId(memoryId, userId)) {
            // 좋아요 취소
            likeRepository.deleteByMemoryIdAndUserId(memoryId, userId);
            log.info("좋아요 취소: memory={}, user={}", memoryId, userId);
        } else {
            // 좋아요 추가
            Like like = Like.builder()
                    .memory(memory)
                    .user(user)
                    .build();

            likeRepository.save(like);
            log.info("좋아요 추가: memory={}, user={}", memoryId, userId);
        }
    }

    public long getLikeCount(Long memoryId) {
        if (!memoryRepository.existsById(memoryId)) {
            throw new BusinessException(ErrorCode.MEMORY_NOT_FOUND);
        }

        return likeRepository.countByMemoryId(memoryId);
    }

    public boolean isLikedByUser(Long memoryId, Long userId) {
        if (!memoryRepository.existsById(memoryId)) {
            throw new BusinessException(ErrorCode.MEMORY_NOT_FOUND);
        }

        return likeRepository.existsByMemoryIdAndUserId(memoryId, userId);
    }

}
