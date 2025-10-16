#!/bin/bash

# OurTime Database 생성 스크립트
# MySQL에 데이터베이스와 스키마를 생성합니다.

set -e

echo "=========================================="
echo "  OurTime Database 생성 스크립트"
echo "=========================================="
echo ""

# 색상 코드
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 기본 설정
DB_HOST=${DB_HOST:-localhost}
DB_PORT=${DB_PORT:-3306}
DB_NAME=${DB_NAME:-ourtime}
DB_USER=${DB_USER:-root}

# MySQL 비밀번호 입력
echo -n "MySQL root 비밀번호를 입력하세요: "
read -s DB_PASSWORD
echo ""
echo ""

# MySQL 연결 테스트
echo "1. MySQL 연결 테스트..."
if mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASSWORD" -e "SELECT 1;" > /dev/null 2>&1; then
    echo -e "${GREEN}✓ MySQL 연결 성공!${NC}"
else
    echo -e "${RED}✗ MySQL 연결 실패!${NC}"
    echo "MySQL 서버가 실행 중인지 확인하고 비밀번호를 다시 확인하세요."
    exit 1
fi
echo ""

# 데이터베이스 생성
echo "2. 데이터베이스 생성..."
if mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASSWORD" -e "CREATE DATABASE IF NOT EXISTS $DB_NAME CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" > /dev/null 2>&1; then
    echo -e "${GREEN}✓ 데이터베이스 '$DB_NAME' 생성 완료!${NC}"
else
    echo -e "${RED}✗ 데이터베이스 생성 실패!${NC}"
    exit 1
fi
echo ""

# 스키마 생성
echo "3. 테이블 스키마 생성..."
if mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASSWORD" "$DB_NAME" < src/main/resources/db/schema.sql > /dev/null 2>&1; then
    echo -e "${GREEN}✓ 스키마 생성 완료!${NC}"
else
    echo -e "${YELLOW}⚠ 스키마 생성 중 경고 발생 (이미 존재할 수 있음)${NC}"
fi
echo ""

# 샘플 데이터 입력 (선택사항)
echo -n "샘플 데이터를 입력하시겠습니까? (y/N): "
read -r LOAD_SAMPLE_DATA
echo ""

if [[ "$LOAD_SAMPLE_DATA" =~ ^[Yy]$ ]]; then
    echo "4. 샘플 데이터 입력..."
    if mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASSWORD" "$DB_NAME" < src/main/resources/db/data.sql > /dev/null 2>&1; then
        echo -e "${GREEN}✓ 샘플 데이터 입력 완료!${NC}"
        echo ""
        echo -e "${YELLOW}테스트 계정 정보:${NC}"
        echo "  - 이메일: kim@example.com"
        echo "  - 비밀번호: password123"
    else
        echo -e "${RED}✗ 샘플 데이터 입력 실패!${NC}"
    fi
    echo ""
fi

# 완료 메시지
echo "=========================================="
echo -e "${GREEN}  데이터베이스 설정 완료!${NC}"
echo "=========================================="
echo ""
echo "다음 명령어로 애플리케이션을 실행하세요:"
echo "  ./gradlew bootRun"
echo ""
echo "또는 application.yml에서 데이터베이스 설정을 확인하세요:"
echo "  spring.datasource.url: jdbc:mysql://$DB_HOST:$DB_PORT/$DB_NAME"
echo "  spring.datasource.username: $DB_USER"
echo ""
