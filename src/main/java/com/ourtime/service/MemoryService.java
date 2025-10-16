package com.ourtime.service;

import com.ourtime.domain.*;
import com.ourtime.dto.memory.CreateMemoryRequest;
import com.ourtime.dto.memory.MemoryResponse;
import com.ourtime.dto.memory.UpdateMemoryRequest;
import com.ourtime.exception.BusinessException;
import com.ourtime.exception.ErrorCode;
import com.ourtime.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoryService {

    private final MemoryRepository memoryRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final TagRepository tagRepository;
    private final MemoryTagRepository memoryTagRepository;

    @Transactional
    public MemoryResponse createMemory(Long userId, CreateMemoryRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new BusinessException(ErrorCode.GROUP_NOT_FOUND));

        // 그룹 멤버인지 확인
        if (!userGroupRepository.existsByUserIdAndGroupId(userId, request.getGroupId())) {
            throw new BusinessException(ErrorCode.NOT_GROUP_MEMBER);
        }

        // 추억 생성
        Memory memory = Memory.builder()
                .group(group)
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .locationName(request.getLocationName())
                .visitedAt(request.getVisitedAt())
                .imageUrls(request.getImageUrls())
                .build();

        Memory savedMemory = memoryRepository.save(memory);

        // 태그 추가
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            for (Long tagId : request.getTagIds()) {
                Tag tag = tagRepository.findById(tagId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.TAG_NOT_FOUND));

                MemoryTag memoryTag = MemoryTag.builder()
                        .memory(savedMemory)
                        .tag(tag)
                        .build();

                memoryTagRepository.save(memoryTag);
            }
        }

        log.info("새 추억 생성: {} by {}", savedMemory.getId(), userId);

        return MemoryResponse.from(savedMemory);
    }

    public List<MemoryResponse> getAllMemoriesByUserId(Long userId) {
        // 사용자가 속한 그룹들의 모든 메모리를 조회
        List<Memory> memories = memoryRepository.findAllByUserId(userId);
        return memories.stream()
                .map(MemoryResponse::from)
                .collect(Collectors.toList());
    }

    public MemoryResponse getMemoryById(Long memoryId, Long userId) {
        Memory memory = memoryRepository.findById(memoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMORY_NOT_FOUND));

        // 그룹 멤버인지 확인
        if (!userGroupRepository.existsByUserIdAndGroupId(userId, memory.getGroup().getId())) {
            throw new BusinessException(ErrorCode.NOT_GROUP_MEMBER);
        }

        return MemoryResponse.from(memory);
    }

    public Page<MemoryResponse> getMemoriesByGroupId(Long groupId, Long userId, Pageable pageable) {
        // 그룹 존재 확인
        if (!groupRepository.existsById(groupId)) {
            throw new BusinessException(ErrorCode.GROUP_NOT_FOUND);
        }

        // 그룹 멤버인지 확인
        if (!userGroupRepository.existsByUserIdAndGroupId(userId, groupId)) {
            throw new BusinessException(ErrorCode.NOT_GROUP_MEMBER);
        }

        Page<Memory> memories = memoryRepository.findAllByGroupId(groupId, pageable);
        return memories.map(MemoryResponse::from);
    }

    public List<MemoryResponse> getMemoriesByGroupIdAndTagId(Long groupId, Long tagId, Long userId) {
        // 그룹 존재 확인
        if (!groupRepository.existsById(groupId)) {
            throw new BusinessException(ErrorCode.GROUP_NOT_FOUND);
        }

        // 그룹 멤버인지 확인
        if (!userGroupRepository.existsByUserIdAndGroupId(userId, groupId)) {
            throw new BusinessException(ErrorCode.NOT_GROUP_MEMBER);
        }

        // 태그 존재 확인
        if (!tagRepository.existsById(tagId)) {
            throw new BusinessException(ErrorCode.TAG_NOT_FOUND);
        }

        List<Memory> memories = memoryRepository.findAllByGroupIdAndTagId(groupId, tagId);
        return memories.stream()
                .map(MemoryResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public MemoryResponse updateMemory(Long memoryId, Long userId, UpdateMemoryRequest request) {
        Memory memory = memoryRepository.findById(memoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMORY_NOT_FOUND));

        // 작성자인지 확인
        if (!memory.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NOT_MEMORY_OWNER);
        }

        // 내용 업데이트
        memory.updateContent(request.getTitle(), request.getDescription(), 
                            request.getLocationName(), request.getVisitedAt());

        // 위치 업데이트
        if (request.getLatitude() != null && request.getLongitude() != null) {
            memory.updateLocation(request.getLatitude(), request.getLongitude(), request.getLocationName());
        }

        // 태그 업데이트
        if (request.getTagIds() != null) {
            // 기존 태그 삭제
            memoryTagRepository.findAllByMemoryId(memoryId).forEach(memoryTagRepository::delete);

            // 새 태그 추가
            for (Long tagId : request.getTagIds()) {
                Tag tag = tagRepository.findById(tagId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.TAG_NOT_FOUND));

                MemoryTag memoryTag = MemoryTag.builder()
                        .memory(memory)
                        .tag(tag)
                        .build();

                memoryTagRepository.save(memoryTag);
            }
        }

        log.info("추억 업데이트: {}", memoryId);

        return MemoryResponse.from(memory);
    }

    @Transactional
    public void deleteMemory(Long memoryId, Long userId) {
        Memory memory = memoryRepository.findById(memoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMORY_NOT_FOUND));

        // 작성자인지 확인
        if (!memory.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NOT_MEMORY_OWNER);
        }

        memoryRepository.delete(memory);
        log.info("추억 삭제: {}", memoryId);
    }

}
