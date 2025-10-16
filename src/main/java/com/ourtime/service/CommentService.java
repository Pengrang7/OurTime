package com.ourtime.service;

import com.ourtime.domain.Comment;
import com.ourtime.domain.Memory;
import com.ourtime.domain.User;
import com.ourtime.dto.comment.CommentResponse;
import com.ourtime.dto.comment.CreateCommentRequest;
import com.ourtime.dto.comment.UpdateCommentRequest;
import com.ourtime.exception.BusinessException;
import com.ourtime.exception.ErrorCode;
import com.ourtime.repository.CommentRepository;
import com.ourtime.repository.MemoryRepository;
import com.ourtime.repository.UserGroupRepository;
import com.ourtime.repository.UserRepository;
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
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemoryRepository memoryRepository;
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;

    @Transactional
    public CommentResponse createComment(Long memoryId, Long userId, CreateCommentRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Memory memory = memoryRepository.findById(memoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMORY_NOT_FOUND));

        // 그룹 멤버인지 확인
        if (!userGroupRepository.existsByUserIdAndGroupId(userId, memory.getGroup().getId())) {
            throw new BusinessException(ErrorCode.NOT_GROUP_MEMBER);
        }

        Comment comment = Comment.builder()
                .memory(memory)
                .user(user)
                .content(request.getContent())
                .build();

        Comment savedComment = commentRepository.save(comment);
        log.info("새 댓글 생성: {} by {}", savedComment.getId(), userId);

        return CommentResponse.from(savedComment);
    }

    public List<CommentResponse> getCommentsByMemoryId(Long memoryId, Long userId) {
        Memory memory = memoryRepository.findById(memoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMORY_NOT_FOUND));

        // 그룹 멤버인지 확인
        if (!userGroupRepository.existsByUserIdAndGroupId(userId, memory.getGroup().getId())) {
            throw new BusinessException(ErrorCode.NOT_GROUP_MEMBER);
        }

        List<Comment> comments = commentRepository.findAllByMemoryId(memoryId);
        return comments.stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, Long userId, UpdateCommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        // 작성자인지 확인
        if (!comment.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NOT_COMMENT_OWNER);
        }

        comment.updateContent(request.getContent());
        log.info("댓글 업데이트: {}", commentId);

        return CommentResponse.from(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        // 작성자인지 확인
        if (!comment.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NOT_COMMENT_OWNER);
        }

        commentRepository.delete(comment);
        log.info("댓글 삭제: {}", commentId);
    }

}
