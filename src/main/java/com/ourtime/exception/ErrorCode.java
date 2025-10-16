package com.ourtime.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C001", "내부 서버 오류가 발생했습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C002", "잘못된 입력값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C003", "허용되지 않은 메서드입니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C004", "잘못된 타입입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "C005", "접근 권한이 없습니다."),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "U002", "이미 존재하는 이메일입니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "U003", "이미 존재하는 닉네임입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "U004", "잘못된 비밀번호입니다."),

    // Auth
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A001", "인증이 필요합니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A002", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "A003", "만료된 토큰입니다."),

    // Group
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "G001", "그룹을 찾을 수 없습니다."),
    INVALID_INVITE_CODE(HttpStatus.BAD_REQUEST, "G002", "유효하지 않은 초대 코드입니다."),
    ALREADY_GROUP_MEMBER(HttpStatus.CONFLICT, "G003", "이미 그룹의 멤버입니다."),
    NOT_GROUP_MEMBER(HttpStatus.FORBIDDEN, "G004", "그룹의 멤버가 아닙니다."),
    NOT_GROUP_ADMIN(HttpStatus.FORBIDDEN, "G005", "그룹의 관리자가 아닙니다."),
    CANNOT_LEAVE_AS_ADMIN(HttpStatus.BAD_REQUEST, "G006", "관리자는 그룹을 탈퇴할 수 없습니다. 먼저 다른 멤버에게 관리자 권한을 이양하세요."),
    
    // Group Invitation
    INVITATION_NOT_FOUND(HttpStatus.NOT_FOUND, "GI001", "초대를 찾을 수 없습니다."),
    NOT_INVITATION_RECIPIENT(HttpStatus.FORBIDDEN, "GI002", "초대받은 사람이 아닙니다."),
    INVITATION_ALREADY_PROCESSED(HttpStatus.BAD_REQUEST, "GI003", "이미 처리된 초대입니다."),

    // Memory
    MEMORY_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "추억을 찾을 수 없습니다."),
    NOT_MEMORY_OWNER(HttpStatus.FORBIDDEN, "M002", "추억의 작성자가 아닙니다."),

    // Comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "CM001", "댓글을 찾을 수 없습니다."),
    NOT_COMMENT_OWNER(HttpStatus.FORBIDDEN, "CM002", "댓글의 작성자가 아닙니다."),

    // Tag
    TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "T001", "태그를 찾을 수 없습니다."),
    TAG_ALREADY_EXISTS(HttpStatus.CONFLICT, "T002", "이미 존재하는 태그입니다."),

    // File
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "F001", "파일 업로드에 실패했습니다."),
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "F002", "지원하지 않는 파일 형식입니다."),
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "F003", "파일 크기가 너무 큽니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

}
