package com.ourtime.service;

import com.ourtime.domain.Tag;
import com.ourtime.dto.tag.CreateTagRequest;
import com.ourtime.dto.tag.TagResponse;
import com.ourtime.exception.BusinessException;
import com.ourtime.exception.ErrorCode;
import com.ourtime.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;

    @Transactional
    public TagResponse createTag(CreateTagRequest request) {
        // 태그 이름 중복 체크
        if (tagRepository.existsByName(request.getName())) {
            throw new BusinessException(ErrorCode.TAG_ALREADY_EXISTS);
        }

        Tag tag = Tag.builder()
                .name(request.getName())
                .color(request.getColor())
                .build();

        Tag savedTag = tagRepository.save(tag);
        log.info("새 태그 생성: {}", savedTag.getName());

        return TagResponse.from(savedTag);
    }

    public List<TagResponse> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream()
                .map(TagResponse::from)
                .collect(Collectors.toList());
    }

    public TagResponse getTagById(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TAG_NOT_FOUND));
        return TagResponse.from(tag);
    }

    @Transactional
    public void deleteTag(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TAG_NOT_FOUND));

        tagRepository.delete(tag);
        log.info("태그 삭제: {}", tagId);
    }

}
