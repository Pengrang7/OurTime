# 🔧 OurTime 설치 가이드

이 문서는 OurTime 프로젝트를 로컬 환경에서 설치하고 실행하는 방법을 자세히 설명합니다.

## 목차
- [시스템 요구사항](#시스템-요구사항)
- [개발 환경 설정](#개발-환경-설정)
- [데이터베이스 설정](#데이터베이스-설정)
- [AWS S3 설정](#aws-s3-설정)
- [애플리케이션 실행](#애플리케이션-실행)
- [문제 해결](#문제-해결)

## 시스템 요구사항

### 필수 사항
- **Java**: JDK 17 이상
- **Gradle**: 8.5 이상 (Wrapper 포함)
- **Git**: 최신 버전

### 선택 사항
- **MySQL**: 8.0 이상 (프로덕션 환경)
- **AWS 계정**: S3 이미지 업로드 기능 사용 시
- **IDE**: IntelliJ IDEA, VS Code, Eclipse 등

## 개발 환경 설정

### 1. Java 설치 확인
```bash
java -version
# 출력 예: openjdk version "17.0.x"
```

Java가 설치되어 있지 않다면:
- **Windows**: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) 또는 [OpenJDK](https://adoptium.net/) 다운로드
- **macOS**: `brew install openjdk@17`
- **Linux**: `sudo apt install openjdk-17-jdk` (Ubuntu/Debian)

### 2. 프로젝트 클론
```bash
git clone <repository-url>
cd OurTime
```

### 3. Gradle 빌드 확인
```bash
./gradlew --version

# Windows의 경우
gradlew.bat --version
```

## 데이터베이스 설정

### Option 1: H2 데이터베이스 (개발 환경 - 권장)

**장점**: 별도 설치 불필요, 즉시 사용 가능

#### 실행 방법
```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

#### H2 콘솔 접속
```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (비워두기)
```

### Option 2: MySQL 데이터베이스 (프로덕션)

#### MySQL 설치
**Windows**:
1. [MySQL Installer](https://dev.mysql.com/downloads/installer/) 다운로드
2. 설치 시 "Developer Default" 선택
3. Root 비밀번호 설정

**macOS**:
```bash
brew install mysql
brew services start mysql
```

**Linux (Ubuntu/Debian)**:
```bash
sudo apt update
sudo apt install mysql-server
sudo systemctl start mysql
```

#### 데이터베이스 생성
```sql
# MySQL 접속
mysql -u root -p

# 데이터베이스 생성
CREATE DATABASE ourtime CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 사용자 생성 (선택사항)
CREATE USER 'ourtime_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON ourtime.* TO 'ourtime_user'@'localhost';
FLUSH PRIVILEGES;

# 종료
EXIT;
```

#### application.yml 설정
`src/main/resources/application.yml` 파일 수정:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ourtime?useSSL=false&serverTimezone=Asia/Seoul&characterEncoding=UTF-8
    username: root  # 또는 생성한 사용자명
    password: your_password
```

## AWS S3 설정

이미지 업로드 기능을 사용하려면 AWS S3를 설정해야 합니다.

### 1. AWS 계정 생성
[AWS 가입](https://aws.amazon.com/ko/free/)

### 2. S3 버킷 생성
1. AWS Console에서 S3 서비스 접속
2. "버킷 만들기" 클릭
3. 버킷 이름 입력 (예: `ourtime-bucket`)
4. 리전 선택 (예: `ap-northeast-2` - 서울)
5. "퍼블릭 액세스 차단" 설정 유지
6. 버킷 생성

### 3. IAM 사용자 생성
1. AWS Console에서 IAM 서비스 접속
2. "사용자" → "사용자 추가"
3. 사용자 이름 입력
4. "프로그래밍 방식 액세스" 선택
5. 권한: "AmazonS3FullAccess" 정책 연결
6. Access Key와 Secret Key 저장 (한 번만 표시됨!)

### 4. application.yml 설정
```yaml
spring:
  cloud:
    aws:
      credentials:
        access-key: YOUR_ACCESS_KEY
        secret-key: YOUR_SECRET_KEY
      s3:
        bucket: your-bucket-name
      region:
        static: ap-northeast-2
```

### 5. 환경 변수 설정 (권장)
보안을 위해 환경 변수 사용:
```bash
# Linux/macOS
export AWS_ACCESS_KEY=your-access-key
export AWS_SECRET_KEY=your-secret-key
export AWS_S3_BUCKET=your-bucket-name

# Windows (PowerShell)
$env:AWS_ACCESS_KEY="your-access-key"
$env:AWS_SECRET_KEY="your-secret-key"
$env:AWS_S3_BUCKET="your-bucket-name"
```

## 애플리케이션 실행

### 개발 모드 (H2)
```bash
# Gradle을 통한 실행
./gradlew bootRun --args='--spring.profiles.active=dev'

# 또는 IDE에서 실행
# Run Configuration에서 Active profiles: dev 설정
```

### 프로덕션 모드 (MySQL)
```bash
# 빌드
./gradlew clean build

# JAR 실행
java -jar build/libs/ourtime-0.0.1-SNAPSHOT.jar

# 또는 특정 프로필로 실행
java -jar build/libs/ourtime-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### 백그라운드 실행
```bash
# Linux/macOS
nohup java -jar build/libs/ourtime-0.0.1-SNAPSHOT.jar > app.log 2>&1 &

# 프로세스 확인
ps aux | grep ourtime

# 프로세스 종료
kill <PID>
```

## 실행 확인

### 1. 헬스 체크
```bash
curl http://localhost:8080/api/health
```

### 2. Swagger UI 접속
브라우저에서 http://localhost:8080/swagger-ui.html 접속

### 3. API 테스트
```bash
# 회원가입
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "nickname": "테스터"
  }'
```

## 문제 해결

### 1. 포트 충돌 (Port 8080 already in use)
**해결 방법 1**: 포트 변경
```yaml
# application.yml
server:
  port: 8081
```

**해결 방법 2**: 프로세스 종료
```bash
# Linux/macOS
lsof -i :8080
kill -9 <PID>

# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### 2. MySQL 연결 오류
**오류**: `Could not connect to MySQL`

**해결**:
```bash
# MySQL 서비스 확인
sudo systemctl status mysql  # Linux
brew services list            # macOS

# 재시작
sudo systemctl restart mysql  # Linux
brew services restart mysql   # macOS
```

### 3. Gradle 빌드 실패
```bash
# Gradle 캐시 삭제
./gradlew clean --refresh-dependencies

# 다시 빌드
./gradlew build
```

### 4. JWT 토큰 오류
**오류**: `Invalid JWT token`

**원인**: JWT secret이 너무 짧음 (최소 256비트 필요)

**해결**:
```yaml
jwt:
  secret: your-very-long-secret-key-minimum-256-bits-required-for-security
```

### 5. AWS S3 업로드 실패
**체크리스트**:
- [ ] AWS 자격 증명이 올바른가?
- [ ] S3 버킷 이름이 정확한가?
- [ ] IAM 사용자에게 S3 권한이 있는가?
- [ ] 리전 설정이 올바른가?

```bash
# AWS CLI로 연결 테스트
aws s3 ls s3://your-bucket-name --region ap-northeast-2
```

### 6. 한글 인코딩 문제
**MySQL 설정 확인**:
```sql
SHOW VARIABLES LIKE 'character%';
SHOW VARIABLES LIKE 'collation%';
```

**my.cnf 또는 my.ini 설정**:
```ini
[client]
default-character-set=utf8mb4

[mysql]
default-character-set=utf8mb4

[mysqld]
character-set-server=utf8mb4
collation-server=utf8mb4_unicode_ci
```

## IDE 설정

### IntelliJ IDEA
1. File → Open → OurTime 폴더 선택
2. Gradle 프로젝트로 자동 인식
3. JDK 17 설정: File → Project Structure → Project SDK
4. Run Configuration 추가:
   - Main class: `com.ourtime.OurTimeApplication`
   - Active profiles: `dev`

### VS Code
1. Extension 설치:
   - Java Extension Pack
   - Spring Boot Extension Pack
   - Gradle for Java
2. Gradle 프로젝트 인식 확인
3. `launch.json` 설정:
```json
{
  "type": "java",
  "name": "OurTime",
  "request": "launch",
  "mainClass": "com.ourtime.OurTimeApplication",
  "args": "--spring.profiles.active=dev"
}
```

## Docker 실행 (선택사항)

Docker를 사용하여 컨테이너로 실행할 수도 있습니다.

### Dockerfile 생성
```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/ourtime-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 빌드 및 실행
```bash
# 애플리케이션 빌드
./gradlew build

# Docker 이미지 빌드
docker build -t ourtime:latest .

# 컨테이너 실행
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=dev \
  ourtime:latest
```

## 다음 단계

설치가 완료되었다면:
1. [API_DOCUMENTATION.md](API_DOCUMENTATION.md)에서 API 사용법 확인
2. Swagger UI에서 API 테스트
3. 프론트엔드 개발 시작

## 도움이 필요하신가요?

- GitHub Issues에 질문 등록
- [API 문서](API_DOCUMENTATION.md) 참고
- [README.md](README.md) 재확인

---

**설치 과정에서 문제가 발생하면 Issue를 등록해주세요!**
