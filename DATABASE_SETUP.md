# 🗄️ 데이터베이스 설정 가이드

OurTime 프로젝트의 데이터베이스를 설정하는 방법을 안내합니다.

## 📋 목차
- [방법 선택](#방법-선택)
- [Option 1: Docker Compose (추천)](#option-1-docker-compose-추천)
- [Option 2: 로컬 MySQL 설치](#option-2-로컬-mysql-설치)
- [Option 3: H2 메모리 DB](#option-3-h2-메모리-db-개발용)
- [데이터베이스 구조](#데이터베이스-구조)
- [문제 해결](#문제-해결)

## 방법 선택

### 🐳 Docker Compose (추천)
**장점**: 빠르고 쉬운 설정, 격리된 환경, 일관성
**적합**: 모든 개발자, 특히 초보자

### 💾 로컬 MySQL
**장점**: 완전한 제어, 영구 데이터 저장
**적합**: MySQL에 익숙한 개발자

### 🚀 H2 메모리 DB
**장점**: 별도 설치 불필요, 즉시 시작
**적합**: 빠른 테스트, 프로토타이핑

---

## Option 1: Docker Compose (추천)

### 1️⃣ Docker 설치
[Docker Desktop](https://www.docker.com/products/docker-desktop/) 다운로드 및 설치

### 2️⃣ 컨테이너 실행
```bash
# 프로젝트 루트 디렉토리에서
docker-compose up -d
```

### 3️⃣ 확인
```bash
# 컨테이너 상태 확인
docker-compose ps

# MySQL 접속 테스트
docker exec -it ourtime-mysql mysql -u ourtime_user -pourtime_pass ourtime
```

### 4️⃣ 애플리케이션 설정
`application.yml` 파일이 Docker 설정에 맞게 되어 있는지 확인:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ourtime
    username: ourtime_user
    password: ourtime_pass
```

### 5️⃣ 애플리케이션 실행
```bash
./gradlew bootRun
```

### 관리 도구
- **phpMyAdmin**: http://localhost:8081
  - 서버: mysql
  - 사용자: root
  - 비밀번호: rootpassword

### Docker 명령어
```bash
# 컨테이너 중지
docker-compose down

# 컨테이너 중지 및 볼륨 삭제 (데이터 초기화)
docker-compose down -v

# 로그 확인
docker-compose logs -f mysql

# MySQL 콘솔 접속
docker exec -it ourtime-mysql mysql -u root -prootpassword ourtime
```

---

## Option 2: 로컬 MySQL 설치

### 1️⃣ MySQL 설치

**Windows**:
1. [MySQL Installer](https://dev.mysql.com/downloads/installer/) 다운로드
2. "Developer Default" 선택하여 설치
3. Root 비밀번호 설정

**macOS**:
```bash
brew install mysql
brew services start mysql
mysql_secure_installation
```

**Linux (Ubuntu/Debian)**:
```bash
sudo apt update
sudo apt install mysql-server
sudo systemctl start mysql
sudo mysql_secure_installation
```

### 2️⃣ 데이터베이스 생성

**자동 설정 (권장)**:

**Linux/macOS**:
```bash
chmod +x scripts/create-database.sh
./scripts/create-database.sh
```

**Windows**:
```cmd
scripts\create-database.bat
```

**수동 설정**:
```bash
# MySQL 접속
mysql -u root -p

# 스크립트 실행
source src/main/resources/db/schema.sql
source src/main/resources/db/data.sql  # 샘플 데이터 (선택)
```

### 3️⃣ 사용자 생성 (선택사항)
```sql
CREATE USER 'ourtime_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON ourtime.* TO 'ourtime_user'@'localhost';
FLUSH PRIVILEGES;
```

### 4️⃣ application.yml 설정
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ourtime
    username: root  # 또는 ourtime_user
    password: your_password
```

---

## Option 3: H2 메모리 DB (개발용)

### 1️⃣ 프로필 설정
```bash
# H2 개발 모드로 실행
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### 2️⃣ H2 콘솔 접속
```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (비워두기)
```

### 특징
- ✅ 별도 설치 불필요
- ✅ 빠른 시작
- ⚠️ 재시작 시 데이터 초기화
- ⚠️ 프로덕션 환경 부적합

---

## 데이터베이스 구조

### ERD
```
User (사용자)
  ↓ 1:N
UserGroup (사용자-그룹 매핑)
  ↓ N:1
Group (그룹)
  ↓ 1:N
Memory (추억)
  ↓ 1:N
├─ MemoryTag → Tag (태그)
├─ MemoryImages (이미지)
├─ Comment (댓글)
└─ Like (좋아요)
```

### 테이블 목록

| 테이블 | 설명 | 주요 컬럼 |
|--------|------|-----------|
| `users` | 사용자 | id, email, password, nickname, profile_image |
| `groups` | 그룹 | id, name, type, invite_code, created_by |
| `user_group` | 사용자-그룹 매핑 | user_id, group_id, role |
| `memories` | 추억 | id, group_id, user_id, title, latitude, longitude, visited_at |
| `tags` | 태그 | id, name, color |
| `memory_tag` | 추억-태그 매핑 | memory_id, tag_id |
| `memory_images` | 추억 이미지 | memory_id, image_url |
| `comments` | 댓글 | id, memory_id, user_id, content |
| `likes` | 좋아요 | id, memory_id, user_id |

### 초기 태그 데이터
데이터베이스 생성 시 다음 태그가 자동으로 추가됩니다:
- 데이트, 여행, 가족, 친구, 생일
- 기념일, 맛집, 카페, 운동, 공부

---

## 샘플 데이터

### 테스트 계정
스키마와 함께 샘플 데이터를 로드하면 다음 계정을 사용할 수 있습니다:

| 이메일 | 비밀번호 | 닉네임 |
|--------|----------|--------|
| kim@example.com | password123 | 김철수 |
| lee@example.com | password123 | 이영희 |
| park@example.com | password123 | 박민수 |
| choi@example.com | password123 | 최지영 |

### 샘플 그룹
- **우리 가족** (FAMILY) - 김철수, 이영희
- **여행 동호회** (FRIEND) - 이영희, 박민수, 최지영
- **커플 다이어리** (COUPLE) - 박민수, 최지영

### 샘플 추억
- 제주도 가족 여행
- 부산 해운대 여행
- 경주 불국사 방문
- 한강 공원 데이트
- 등등...

---

## 데이터베이스 관리

### 백업
```bash
# Docker 사용 시
docker exec ourtime-mysql mysqldump -u root -prootpassword ourtime > backup.sql

# 로컬 MySQL
mysqldump -u root -p ourtime > backup.sql
```

### 복원
```bash
# Docker 사용 시
docker exec -i ourtime-mysql mysql -u root -prootpassword ourtime < backup.sql

# 로컬 MySQL
mysql -u root -p ourtime < backup.sql
```

### 데이터 초기화
```bash
# Docker 사용 시
docker-compose down -v
docker-compose up -d

# 로컬 MySQL
mysql -u root -p -e "DROP DATABASE IF EXISTS ourtime;"
./scripts/create-database.sh
```

---

## 문제 해결

### MySQL 연결 오류

**증상**: `Could not connect to MySQL`

**해결**:
```bash
# MySQL 서비스 확인
# Linux
sudo systemctl status mysql

# macOS
brew services list

# Windows
# 서비스 관리자에서 MySQL 확인

# 재시작
sudo systemctl restart mysql  # Linux
brew services restart mysql    # macOS
```

### 포트 충돌

**증상**: `Port 3306 is already in use`

**해결**:
```bash
# 사용 중인 프로세스 확인
lsof -i :3306  # Linux/macOS
netstat -ano | findstr :3306  # Windows

# Docker Compose 포트 변경
# docker-compose.yml에서 "3307:3306"으로 변경
```

### 인코딩 문제

**증상**: 한글이 깨져 보임

**해결**:
```sql
-- 데이터베이스 문자셋 확인
SHOW VARIABLES LIKE 'character%';

-- 테이블 문자셋 변경
ALTER DATABASE ourtime CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE users CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Docker 볼륨 권한 오류

**증상**: Permission denied

**해결**:
```bash
# 볼륨 삭제 후 재생성
docker-compose down -v
docker volume rm ourtime_mysql_data
docker-compose up -d
```

### 스키마 업데이트

**JPA 자동 생성 사용 (개발)**:
```yaml
# application.yml
spring:
  jpa:
    hibernate:
      ddl-auto: update  # 테이블 자동 생성/업데이트
```

**수동 마이그레이션 (프로덕션)**:
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate  # 스키마 검증만
```

---

## 프로덕션 환경 설정

### 보안 강화
```sql
-- 강력한 비밀번호 사용
CREATE USER 'ourtime_prod'@'localhost' IDENTIFIED BY 'very-strong-password-here';

-- 필요한 권한만 부여
GRANT SELECT, INSERT, UPDATE, DELETE ON ourtime.* TO 'ourtime_prod'@'localhost';

-- root 원격 접속 비활성화
DELETE FROM mysql.user WHERE User='root' AND Host NOT IN ('localhost', '127.0.0.1', '::1');
FLUSH PRIVILEGES;
```

### 성능 최적화
```sql
-- 인덱스 추가 (필요시)
CREATE INDEX idx_memory_date ON memories(visited_at, group_id);
CREATE INDEX idx_user_email ON users(email);

-- 쿼리 성능 분석
EXPLAIN SELECT * FROM memories WHERE group_id = 1;
```

### 백업 자동화
```bash
# Cron 작업 추가 (매일 새벽 2시 백업)
crontab -e

# 다음 줄 추가
0 2 * * * mysqldump -u backup_user -p'password' ourtime > /backups/ourtime_$(date +\%Y\%m\%d).sql
```

---

## 다음 단계

1. **데이터베이스 연결 테스트**
   ```bash
   ./gradlew bootRun
   curl http://localhost:8080/api/health
   ```

2. **Swagger로 API 테스트**
   http://localhost:8080/swagger-ui.html

3. **샘플 데이터로 로그인 테스트**
   - 이메일: kim@example.com
   - 비밀번호: password123

---

## 참고 자료

- [MySQL 공식 문서](https://dev.mysql.com/doc/)
- [Docker Compose 문서](https://docs.docker.com/compose/)
- [Spring Data JPA 문서](https://spring.io/projects/spring-data-jpa)

---

**문제가 해결되지 않으면 GitHub Issue를 등록해주세요!**
