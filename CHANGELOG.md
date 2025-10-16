# 📝 변경 이력 (Changelog)

이 파일은 OurTime 프로젝트의 주요 변경사항을 기록합니다.

형식은 [Keep a Changelog](https://keepachangelog.com/ko/1.0.0/)를 따르며,
버전 관리는 [Semantic Versioning](https://semver.org/lang/ko/)을 따릅니다.

## [Unreleased]

### 계획 중
- 지도 뷰 프론트엔드 구현
- WebSocket 실시간 알림
- 이메일 알림 시스템
- OAuth2 소셜 로그인
- 추억 검색 기능

## [1.0.0] - 2025-10-16

### 추가됨 (Added)
- 🎉 초기 프로젝트 구조 생성
- 🔐 JWT 기반 인증 시스템
  - 회원가입 API
  - 로그인 API
  - 토큰 갱신 API
- 👤 사용자 관리 기능
  - 프로필 조회/수정
  - 비밀번호 변경
  - 회원 탈퇴
- 👥 그룹 관리 기능
  - 그룹 생성/수정/삭제
  - 초대 코드 시스템
  - 그룹 참여/탈퇴
  - 역할 관리 (ADMIN, MEMBER)
- 📷 추억 기록 기능
  - 위치 기반 추억 생성
  - 추억 조회/수정/삭제
  - 페이징 처리
  - 태그 필터링
- 💬 댓글 기능
  - 댓글 작성/수정/삭제
  - 댓글 목록 조회
- ❤️ 좋아요 기능
  - 좋아요 토글
  - 좋아요 개수 조회
- 🏷️ 태그 시스템
  - 태그 생성/삭제
  - 태그별 추억 필터링
- 📁 파일 업로드
  - AWS S3 연동
  - 프로필/그룹/추억 이미지 업로드
  - 파일 검증 (크기, 형식)
- ⏰ 스케줄러
  - "1년 전 오늘" 추억 리마인더
  - 통계 정보 업데이트 (예정)
- 📚 API 문서화
  - Swagger UI
  - API 상세 문서
- 🔧 설정 및 보안
  - Spring Security 설정
  - CORS 설정
  - 예외 처리 시스템
  - 로깅 설정

### 기술 스택
- Spring Boot 3.2.0
- Java 17
- JPA (Hibernate)
- Spring Security + JWT
- MySQL 8.0 / H2
- AWS S3
- Gradle 8.5
- Springdoc OpenAPI

### 데이터베이스
- User (사용자)
- Group (그룹)
- UserGroup (사용자-그룹 매핑)
- Memory (추억)
- Tag (태그)
- MemoryTag (추억-태그 매핑)
- Comment (댓글)
- Like (좋아요)

### 문서
- README.md - 프로젝트 소개 및 시작 가이드
- API_DOCUMENTATION.md - API 상세 문서
- INSTALLATION.md - 설치 가이드
- CONTRIBUTING.md - 기여 가이드
- CHANGELOG.md - 변경 이력

---

## 버전 형식 설명

### [Major.Minor.Patch]

- **Major**: 하위 호환성이 깨지는 변경
- **Minor**: 하위 호환성을 유지하는 기능 추가
- **Patch**: 하위 호환성을 유지하는 버그 수정

### 변경 유형

- **Added**: 새로운 기능
- **Changed**: 기존 기능 변경
- **Deprecated**: 곧 제거될 기능
- **Removed**: 제거된 기능
- **Fixed**: 버그 수정
- **Security**: 보안 관련 변경

---

**최초 릴리스**: 2025년 10월 16일
