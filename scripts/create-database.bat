@echo off
REM OurTime Database 생성 스크립트 (Windows)
REM MySQL에 데이터베이스와 스키마를 생성합니다.

setlocal enabledelayedexpansion

echo ==========================================
echo   OurTime Database 생성 스크립트
echo ==========================================
echo.

REM 기본 설정
set DB_HOST=localhost
set DB_PORT=3306
set DB_NAME=ourtime
set DB_USER=root

REM MySQL 비밀번호 입력
set /p DB_PASSWORD="MySQL root 비밀번호를 입력하세요: "
echo.

REM MySQL 연결 테스트
echo 1. MySQL 연결 테스트...
mysql -h %DB_HOST% -P %DB_PORT% -u %DB_USER% -p%DB_PASSWORD% -e "SELECT 1;" >nul 2>&1
if %errorlevel% equ 0 (
    echo [OK] MySQL 연결 성공!
) else (
    echo [ERROR] MySQL 연결 실패!
    echo MySQL 서버가 실행 중인지 확인하고 비밀번호를 다시 확인하세요.
    pause
    exit /b 1
)
echo.

REM 데이터베이스 생성
echo 2. 데이터베이스 생성...
mysql -h %DB_HOST% -P %DB_PORT% -u %DB_USER% -p%DB_PASSWORD% -e "CREATE DATABASE IF NOT EXISTS %DB_NAME% CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" >nul 2>&1
if %errorlevel% equ 0 (
    echo [OK] 데이터베이스 '%DB_NAME%' 생성 완료!
) else (
    echo [ERROR] 데이터베이스 생성 실패!
    pause
    exit /b 1
)
echo.

REM 스키마 생성
echo 3. 테이블 스키마 생성...
mysql -h %DB_HOST% -P %DB_PORT% -u %DB_USER% -p%DB_PASSWORD% %DB_NAME% < src\main\resources\db\schema.sql >nul 2>&1
if %errorlevel% equ 0 (
    echo [OK] 스키마 생성 완료!
) else (
    echo [WARNING] 스키마 생성 중 경고 발생 (이미 존재할 수 있음)
)
echo.

REM 샘플 데이터 입력 (선택사항)
set /p LOAD_SAMPLE="샘플 데이터를 입력하시겠습니까? (Y/N): "
echo.

if /i "%LOAD_SAMPLE%"=="Y" (
    echo 4. 샘플 데이터 입력...
    mysql -h %DB_HOST% -P %DB_PORT% -u %DB_USER% -p%DB_PASSWORD% %DB_NAME% < src\main\resources\db\data.sql >nul 2>&1
    if !errorlevel! equ 0 (
        echo [OK] 샘플 데이터 입력 완료!
        echo.
        echo 테스트 계정 정보:
        echo   - 이메일: kim@example.com
        echo   - 비밀번호: password123
    ) else (
        echo [ERROR] 샘플 데이터 입력 실패!
    )
    echo.
)

REM 완료 메시지
echo ==========================================
echo   데이터베이스 설정 완료!
echo ==========================================
echo.
echo 다음 명령어로 애플리케이션을 실행하세요:
echo   gradlew.bat bootRun
echo.
echo 또는 application.yml에서 데이터베이스 설정을 확인하세요:
echo   spring.datasource.url: jdbc:mysql://%DB_HOST%:%DB_PORT%/%DB_NAME%
echo   spring.datasource.username: %DB_USER%
echo.
pause
