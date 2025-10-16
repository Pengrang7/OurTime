@echo off
echo ==========================================
echo   OurTime 개발 서버 실행
echo ==========================================
echo.

REM Java 버전 확인
java -version
if %errorlevel% neq 0 (
    echo [ERROR] Java가 설치되어 있지 않습니다!
    echo Java 17 이상을 설치해주세요.
    pause
    exit /b 1
)

echo.
echo [1/2] 프로젝트 빌드 중...
echo.

REM IDE를 통한 빌드를 권장합니다
echo IntelliJ IDEA나 Eclipse에서 프로젝트를 열고
echo 메인 클래스(OurTimeApplication)를 실행하세요.
echo.
echo 또는 Gradle을 설치한 후:
echo gradle bootRun --args="--spring.profiles.active=dev"
echo.
pause
