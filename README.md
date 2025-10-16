# 🌟 OurTime

> **함께한 시간을 지도 위에 기록하고 공유하는 그룹형 추억 기록 서비스**

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## 📖 프로젝트 소개

OurTime은 소중한 사람들과 함께한 순간들을 지도 위에 기록하고 공유하는 서비스입니다.
커플, 가족, 친구들과의 특별한 추억을 위치 기반으로 저장하고, 시간이 지나도 그 순간을 생생하게 떠올릴 수 있습니다.

💡 핵심 아이디어

사용자(User) 는 여러 그룹(Group) 에 속할 수 있다.

각 그룹별로만 추억(Memory) 이 공유된다.

그룹 타입은 커플 / 가족 / 친구 / 팀 등으로 구분 가능.

모든 추억은 지도 기반(lat, lng)으로 기록된다.

🧱 주요 기능 설계
1️⃣ 사용자 관리

회원가입 / 로그인 (JWT 기반)

프로필 이미지, 닉네임, 이메일 관리

비밀번호 변경 / 탈퇴 API

2️⃣ 그룹 관리

그룹 생성 (name, type: 커플/가족/친구 등)

그룹 초대 코드 생성 및 공유

초대 코드로 그룹 참여

그룹별 역할(ADMIN, MEMBER) 구분

그룹 탈퇴, 삭제

🔹 한 사용자가 여러 그룹에 속할 수 있는 구조
→ N:N 매핑 (User ↔ Group via UserGroup)

3️⃣ 추억(Memory) 기록

위치 정보(lat, lng) + 내용 + 사진 + 날짜 기록

태그(데이트, 여행, 가족식사, 생일파티 등) 추가

작성자/작성일 자동 저장 (JPA Auditing)

그룹 내에서만 조회 가능

4️⃣ 댓글 / 공감 기능

추억 게시글에 댓글 작성 / 수정 / 삭제

좋아요(공감) 기능

실시간 알림 (선택적으로 WebSocket or polling)

5️⃣ 지도 기반 뷰

그룹별로 작성된 추억을 지도 마커로 표시

마커 클릭 시 사진 + 일기 요약 표시

태그, 기간, 작성자별 필터 제공

6️⃣ 이미지 업로드

AWS S3 또는 로컬 파일 업로드

MultipartFile 처리, URL 반환

7️⃣ 알림 기능 (선택)

“오늘은 1년 전 이 날 💌” → 과거 기록 리마인드 (스케줄러)

“새 추억이 추가됐어요” → 그룹 알림

🧩 ERD 구조 (핵심 설계)
User (1) ──< (N) UserGroup (N) >── (1) Group
                             │
                             │ 1:N
                             ▼
                           Memory ──< MemoryTag >── Tag
                             │
                             ├─< Comment
                             └─< Like

테이블	주요 컬럼
User	id, email, password, nickname, profileImage
Group	id, name, type(ENUM), inviteCode, createdBy
UserGroup	user_id, group_id, role
Memory	id, group_id, user_id, title, description, lat, lng, visitedAt, createdAt
Tag	id, name
MemoryTag	memory_id, tag_id
Comment	id, memory_id, user_id, content, createdAt
Like	id, memory_id, user_id
## ⚙️ 기술 스택

| 구분 | 기술 |
|------|------|
| Framework | Spring Boot 3.2.0 |
| Language | Java 17 |
| Build Tool | Gradle |
| ORM | JPA (Hibernate) + QueryDSL |
| Database | MySQL 8.0 / H2 (개발) |
| Security | Spring Security + JWT |
| File Storage | AWS S3 |
| API Docs | Springdoc OpenAPI (Swagger) |
| Dev Tools | Lombok, JUnit 5 |

## 🚀 시작하기

### 사전 요구사항
- Java 17 이상
- MySQL 8.0 (선택사항: H2 사용 가능)
- Gradle 8.5 이상
- AWS S3 계정 (이미지 업로드 기능 사용 시)

### 설치 및 실행

#### 1. 환경 변수 설정

프로젝트를 실행하기 전에 환경 변수를 설정해야 합니다.

**백엔드 환경 변수:**
```bash
# 데이터베이스 설정
export SPRING_DATASOURCE_URL="jdbc:mysql://localhost:3306/ourtime?useSSL=false&serverTimezone=Asia/Seoul&characterEncoding=UTF-8"
export SPRING_DATASOURCE_USERNAME="root"
export SPRING_DATASOURCE_PASSWORD="your_password"

# JWT 설정
export JWT_SECRET="your_jwt_secret_key_here"

# AWS S3 설정 (파일 업로드용)
export AWS_ACCESS_KEY_ID="your_aws_access_key"
export AWS_SECRET_ACCESS_KEY="your_aws_secret_key"
export AWS_S3_BUCKET_NAME="your_s3_bucket_name"
```

**프론트엔드 환경 변수:**
```bash
# API URL
export REACT_APP_API_URL="http://localhost:8080/api"

# 네이버 지도 API
export REACT_APP_NAVER_MAP_CLIENT_ID="your_naver_map_client_id"
```

또는 설정 파일을 생성하여 설정할 수 있습니다:
- 백엔드: `env.example` 파일을 참고하여 `.env` 파일 생성
- 프론트엔드: `frontend/src/config.local.example.ts` 파일을 복사하여 `frontend/src/config.local.ts` 파일 생성 후 실제 값 입력

**프론트엔드 로컬 설정:**
```bash
# frontend/src/config.local.example.ts를 복사
cp frontend/src/config.local.example.ts frontend/src/config.local.ts

# config.local.ts 파일을 열고 실제 네이버 지도 클라이언트 ID 입력
```

#### 2. 프로젝트 클론
```bash
git clone <repository-url>
cd OurTime
```

#### 2. 데이터베이스 설정
**Option A: H2 (개발 환경 - 권장)**
```bash
# 별도 설정 불필요, 바로 실행 가능
./gradlew bootRun --args='--spring.profiles.active=dev'
```

**Option B: MySQL (프로덕션)**
```sql
-- MySQL에서 데이터베이스 생성
CREATE DATABASE ourtime CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 3. 환경 설정
`src/main/resources/application.yml` 파일을 수정하거나 환경 변수를 설정하세요:

```yaml
# MySQL 설정 (선택사항)
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ourtime
    username: your-username
    password: your-password

# AWS S3 설정 (이미지 업로드 사용 시)
spring.cloud.aws:
  credentials:
    access-key: your-aws-access-key
    secret-key: your-aws-secret-key
  s3:
    bucket: your-bucket-name

# JWT Secret (프로덕션 환경)
jwt:
  secret: your-secure-secret-key-minimum-256-bits
```

#### 4. 빌드 및 실행
```bash
# 빌드
./gradlew build

# 실행 (개발 모드 - H2)
./gradlew bootRun --args='--spring.profiles.active=dev'

# 또는 JAR 파일 실행
java -jar build/libs/ourtime-0.0.1-SNAPSHOT.jar
```

#### 5. 접속 확인
- **API 서버**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console (dev 모드)

## 📚 API 문서

자세한 API 문서는 다음을 참고하세요:
- **Swagger UI**: 애플리케이션 실행 후 http://localhost:8080/swagger-ui.html
- **상세 문서**: [API_DOCUMENTATION.md](API_DOCUMENTATION.md)

### 주요 API 엔드포인트

| 카테고리 | Method | Endpoint | 설명 |
|---------|--------|----------|------|
| 인증 | POST | `/api/auth/signup` | 회원가입 |
| 인증 | POST | `/api/auth/login` | 로그인 |
| 사용자 | GET | `/api/users/me` | 내 정보 조회 |
| 그룹 | POST | `/api/groups` | 그룹 생성 |
| 그룹 | POST | `/api/groups/join` | 그룹 참여 |
| 추억 | POST | `/api/memories` | 추억 생성 |
| 추억 | GET | `/api/memories/groups/{groupId}` | 그룹별 추억 조회 |
| 댓글 | POST | `/api/memories/{memoryId}/comments` | 댓글 작성 |
| 좋아요 | POST | `/api/memories/{memoryId}/likes` | 좋아요 토글 |
| 파일 | POST | `/api/files/memory` | 이미지 업로드 |

## 🎯 주요 기능

### ✅ 구현 완료
- [x] JWT 기반 사용자 인증
- [x] 그룹 생성 및 초대 코드 시스템
- [x] 위치 기반 추억 기록 (위도/경도)
- [x] 이미지 업로드 (AWS S3)
- [x] 댓글 및 좋아요 기능
- [x] 태그 시스템
- [x] 페이징 처리
- [x] 1년 전 추억 리마인더 스케줄러
- [x] 예외 처리 및 에러 핸들링
- [x] API 문서화 (Swagger)

### 📋 향후 계획
- [ ] 지도 뷰 프론트엔드
- [ ] WebSocket 실시간 알림
- [ ] 이메일 알림 시스템
- [ ] 그룹 통계 대시보드
- [ ] 추억 검색 기능
- [ ] 소셜 로그인 (OAuth2)

## 📁 프로젝트 구조

```
OurTime/
├── src/
│   ├── main/
│   │   ├── java/com/ourtime/
│   │   │   ├── config/          # 설정 파일
│   │   │   ├── controller/      # REST API 컨트롤러
│   │   │   ├── domain/          # JPA 엔티티
│   │   │   ├── dto/             # DTO 클래스
│   │   │   ├── exception/       # 예외 처리
│   │   │   ├── repository/      # JPA Repository
│   │   │   ├── scheduler/       # 스케줄러
│   │   │   ├── security/        # JWT 보안
│   │   │   ├── service/         # 비즈니스 로직
│   │   │   └── util/            # 유틸리티
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       └── application-test.yml
│   └── test/                    # 테스트 코드
├── build.gradle
├── API_DOCUMENTATION.md         # API 상세 문서
└── README.md
```

## 💻 개발 가이드

### H2 콘솔 접속 (개발 모드)
```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (비워두기)
```

### 테스트 실행
```bash
# 모든 테스트 실행
./gradlew test

# 특정 테스트 실행
./gradlew test --tests "com.ourtime.*"
```

### API 테스트
```bash
# 회원가입
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "nickname": "테스터"
  }'

# 로그인
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

## 🔒 보안

### JWT 인증
모든 보호된 엔드포인트는 JWT 토큰이 필요합니다:
```
Authorization: Bearer {your-access-token}
```

### 비밀번호 암호화
- BCrypt 알고리즘 사용
- Salt 자동 생성
- 평문 비밀번호는 저장되지 않음

## 🐛 트러블슈팅

### MySQL 연결 오류
```bash
# MySQL 서비스 상태 확인
sudo systemctl status mysql

# MySQL 재시작
sudo systemctl restart mysql
```

### Port 8080 already in use
```bash
# 포트 사용 프로세스 확인
lsof -i :8080

# 프로세스 종료
kill -9 <PID>
```

### AWS S3 업로드 오류
- AWS 자격 증명이 올바른지 확인
- S3 버킷 권한 확인 (public write 불필요)
- IAM 사용자에게 S3 PutObject 권한 부여

## 📈 성능 최적화

- **JPA 지연 로딩**: 연관 관계는 기본적으로 LAZY 로딩
- **인덱스**: 자주 조회되는 컬럼에 인덱스 설정
- **페이징**: 대용량 데이터는 페이징 처리
- **캐싱**: 향후 Redis 캐시 도입 예정

## 🤝 기여하기

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 라이선스

이 프로젝트는 MIT 라이선스를 따릅니다. 자세한 내용은 [LICENSE](LICENSE) 파일을 참고하세요.

## 📞 문의

프로젝트에 대한 문의사항이 있으시면 이슈를 등록해주세요.

## 🙏 감사의 말

이 프로젝트는 Spring Boot, Spring Security, JWT 등 오픈소스 커뮤니티의 훌륭한 도구들을 사용하여 만들어졌습니다.

---

**Made with ❤️ by OurTime Team**