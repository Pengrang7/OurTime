# 🤝 기여 가이드

OurTime 프로젝트에 기여해주셔서 감사합니다! 이 문서는 프로젝트에 기여하는 방법을 안내합니다.

## 목차
- [행동 강령](#행동-강령)
- [기여 방법](#기여-방법)
- [개발 환경 설정](#개발-환경-설정)
- [코딩 컨벤션](#코딩-컨벤션)
- [커밋 메시지 규칙](#커밋-메시지-규칙)
- [Pull Request 프로세스](#pull-request-프로세스)
- [이슈 등록](#이슈-등록)

## 행동 강령

이 프로젝트는 모든 기여자가 존중받는 환경을 만들기 위해 노력합니다:

- 서로 존중하고 배려하는 언어 사용
- 다양한 관점과 경험 존중
- 건설적인 피드백 제공
- 커뮤니티의 최선의 이익을 위한 행동

## 기여 방법

### 버그 리포트
버그를 발견하셨나요? 다음 정보를 포함하여 Issue를 등록해주세요:
- 버그 설명
- 재현 단계
- 예상 동작
- 실제 동작
- 스크린샷 (선택사항)
- 환경 정보 (OS, Java 버전 등)

### 기능 제안
새로운 기능을 제안하고 싶으신가요?
- Feature Request 템플릿으로 Issue 등록
- 기능의 목적과 사용 사례 설명
- 가능하다면 UI/UX 목업 첨부

### 코드 기여
1. Issue 확인 또는 새로운 Issue 생성
2. Fork 후 개발
3. 테스트 작성
4. Pull Request 생성

## 개발 환경 설정

### 1. Repository Fork
```bash
# GitHub에서 Fork 버튼 클릭
# Fork한 저장소 클론
git clone https://github.com/YOUR_USERNAME/OurTime.git
cd OurTime
```

### 2. Upstream 설정
```bash
git remote add upstream https://github.com/ORIGINAL_OWNER/OurTime.git
git remote -v
```

### 3. 개발 브랜치 생성
```bash
git checkout -b feature/amazing-feature
```

### 4. 로컬 환경 설정
```bash
# 의존성 설치
./gradlew build

# 개발 모드 실행
./gradlew bootRun --args='--spring.profiles.active=dev'
```

## 코딩 컨벤션

### Java 코드 스타일

#### 1. 네이밍 규칙
```java
// 클래스: PascalCase
public class UserService { }

// 메서드, 변수: camelCase
public void getUserById() { }
private String userName;

// 상수: UPPER_SNAKE_CASE
private static final int MAX_SIZE = 100;

// 패키지: lowercase
package com.ourtime.service;
```

#### 2. 들여쓰기
- 4 스페이스 사용 (탭 사용 금지)
- IDE 자동 포맷 활용

#### 3. 주석
```java
/**
 * 사용자 정보를 조회합니다.
 * 
 * @param userId 사용자 ID
 * @return 사용자 정보
 * @throws UserNotFoundException 사용자를 찾을 수 없는 경우
 */
public UserResponse getUserById(Long userId) {
    // 구현
}
```

#### 4. 패키지 구조
```
com.ourtime
├── config      # 설정
├── controller  # REST API
├── domain      # 엔티티
├── dto         # DTO
├── exception   # 예외
├── repository  # Repository
├── security    # 보안
├── service     # 서비스
└── util        # 유틸리티
```

### Spring Boot 규칙

#### 1. Dependency Injection
```java
// Constructor Injection 사용 (권장)
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
}

// Field Injection 지양
// @Autowired
// private UserRepository userRepository;
```

#### 2. 트랜잭션
```java
@Service
@Transactional(readOnly = true)
public class UserService {
    
    @Transactional  // 쓰기 작업만 명시
    public void updateUser(Long userId, UpdateRequest request) {
        // 구현
    }
}
```

#### 3. 예외 처리
```java
// 커스텀 예외 사용
if (!userRepository.existsById(userId)) {
    throw new BusinessException(ErrorCode.USER_NOT_FOUND);
}

// Generic Exception 사용 금지
// throw new Exception("User not found");
```

## 커밋 메시지 규칙

### Conventional Commits 형식 사용

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Type
- `feat`: 새로운 기능
- `fix`: 버그 수정
- `docs`: 문서 변경
- `style`: 코드 포맷팅 (기능 변경 없음)
- `refactor`: 코드 리팩토링
- `test`: 테스트 추가/수정
- `chore`: 빌드 설정, 패키지 매니저 등

### 예시
```bash
# 기능 추가
git commit -m "feat(auth): JWT 토큰 갱신 기능 추가"

# 버그 수정
git commit -m "fix(memory): 이미지 업로드 시 파일 크기 검증 오류 수정"

# 문서 업데이트
git commit -m "docs(readme): 설치 가이드 추가"

# 리팩토링
git commit -m "refactor(service): UserService 메서드 분리"
```

### 상세 커밋 메시지
```bash
git commit -m "feat(group): 그룹 멤버 관리 기능 추가

- 그룹 멤버 목록 조회 API 구현
- 멤버 역할 변경 기능 추가
- 멤버 강제 퇴출 기능 추가 (ADMIN만 가능)

Closes #123"
```

## Pull Request 프로세스

### 1. 최신 코드 동기화
```bash
git fetch upstream
git checkout main
git merge upstream/main
git checkout feature/your-feature
git rebase main
```

### 2. 테스트 실행
```bash
# 모든 테스트 실행
./gradlew test

# 빌드 확인
./gradlew build
```

### 3. 변경사항 커밋
```bash
git add .
git commit -m "feat: your feature description"
git push origin feature/your-feature
```

### 4. Pull Request 생성
1. GitHub에서 "New Pull Request" 클릭
2. 템플릿에 따라 작성:
   - 변경 내용 설명
   - 관련 Issue 번호
   - 테스트 방법
   - 스크린샷 (UI 변경 시)

### PR 템플릿
```markdown
## 변경 내용
<!-- 이 PR이 무엇을 하는지 설명 -->

## 관련 Issue
<!-- Closes #123 -->

## 변경 유형
- [ ] 버그 수정
- [ ] 새로운 기능
- [ ] 문서 업데이트
- [ ] 리팩토링
- [ ] 테스트 추가

## 체크리스트
- [ ] 테스트를 추가/업데이트했습니다
- [ ] 문서를 업데이트했습니다
- [ ] 모든 테스트가 통과했습니다
- [ ] 코드 스타일을 준수했습니다
- [ ] 커밋 메시지가 규칙을 따릅니다

## 테스트 방법
<!-- 이 변경사항을 어떻게 테스트할 수 있는지 설명 -->

## 스크린샷
<!-- UI 변경이 있는 경우 -->
```

### 5. 코드 리뷰
- 리뷰어의 피드백에 적극적으로 응답
- 요청된 변경사항 반영
- 토론을 통한 합의점 도출

### 6. 머지
- 모든 리뷰 승인 후 Squash and Merge
- 브랜치 삭제

## 이슈 등록

### Bug Report
```markdown
**버그 설명**
명확하고 간결한 버그 설명

**재현 방법**
1. Go to '...'
2. Click on '....'
3. Scroll down to '....'
4. See error

**예상 동작**
무엇이 일어나야 하는가

**스크린샷**
해당하는 경우 추가

**환경**
- OS: [e.g. Windows 11]
- Java Version: [e.g. 17]
- Database: [e.g. MySQL 8.0]
```

### Feature Request
```markdown
**기능 설명**
새로운 기능에 대한 명확한 설명

**동기와 맥락**
이 기능이 왜 필요한가?

**제안하는 해결책**
어떻게 구현하면 좋을까?

**대안**
고려한 다른 해결책이 있는가?

**추가 정보**
기타 참고 자료
```

## 테스트 작성

### 단위 테스트
```java
@Test
@DisplayName("사용자 ID로 조회 - 성공")
void getUserById_Success() {
    // given
    Long userId = 1L;
    User user = createUser();
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    
    // when
    UserResponse result = userService.getUserById(userId);
    
    // then
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(userId);
}

@Test
@DisplayName("존재하지 않는 사용자 조회 - 실패")
void getUserById_NotFound() {
    // given
    Long userId = 999L;
    when(userRepository.findById(userId)).thenReturn(Optional.empty());
    
    // when & then
    assertThatThrownBy(() -> userService.getUserById(userId))
        .isInstanceOf(BusinessException.class)
        .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
}
```

### 통합 테스트
```java
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void signUp_Success() throws Exception {
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "email": "test@example.com",
                      "password": "password123",
                      "nickname": "테스터"
                    }
                    """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));
    }
}
```

## 문서화

### API 문서
- Swagger 어노테이션 사용
- 명확한 설명과 예시 제공

```java
@Operation(summary = "사용자 조회", description = "사용자 ID로 사용자 정보를 조회합니다.")
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "조회 성공"),
    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
})
@GetMapping("/{userId}")
public ApiResponse<UserResponse> getUserById(@PathVariable Long userId) {
    // 구현
}
```

### README 업데이트
- 새로운 기능 추가 시 README 업데이트
- API 엔드포인트 목록 갱신
- 설정 방법 문서화

## 질문이 있으신가요?

- GitHub Discussions에서 질문
- Issue로 문의
- 프로젝트 메인테이너에게 연락

## 감사합니다!

여러분의 기여가 OurTime을 더 나은 서비스로 만듭니다. 🙏

---

**Happy Coding! 🚀**
