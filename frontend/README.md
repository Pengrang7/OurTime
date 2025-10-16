# OurTime Frontend

React + TypeScript + 네이버 지도 API를 사용한 OurTime 프론트엔드 애플리케이션입니다.

## 🚀 주요 기능

- **네이버 지도 연동**: 지도에서 직접 위치 선택 및 메모리 추가
- **실시간 메모리 관리**: 지도 클릭으로 메모리 생성, 수정, 삭제
- **이미지 업로드**: 다중 이미지 업로드 및 미리보기
- **댓글 시스템**: 메모리별 댓글 작성 및 관리
- **그룹 관리**: 그룹별 메모리 필터링 및 관리
- **반응형 디자인**: 모바일, 태블릿, 데스크톱 지원

## 🛠️ 기술 스택

- **Frontend**: React 18, TypeScript, Styled Components
- **상태 관리**: React Query (TanStack Query)
- **라우팅**: React Router v6
- **폼 관리**: React Hook Form
- **지도**: 네이버 지도 API
- **HTTP 클라이언트**: Axios
- **알림**: React Hot Toast

## 📦 설치 및 실행

### 1. 의존성 설치

```bash
cd frontend
npm install
```

### 2. 환경 변수 설정

`.env` 파일을 생성하고 다음 내용을 추가하세요:

```env
REACT_APP_API_URL=http://localhost:8080/api
REACT_APP_NAVER_MAP_CLIENT_ID=your_naver_map_client_id
```

### 3. 네이버 지도 API 설정

1. [네이버 클라우드 플랫폼](https://www.ncloud.com/)에서 계정 생성
2. Maps API 신청 및 클라이언트 ID 발급
3. `.env` 파일에 클라이언트 ID 설정
4. `public/index.html`에서 클라이언트 ID 업데이트

### 4. 애플리케이션 실행

```bash
npm start
```

브라우저에서 `http://localhost:3000`으로 접속하세요.

## 🗺️ 네이버 지도 사용법

### 지도 기능
- **위치 선택**: 지도를 클릭하여 메모리 위치 선택
- **마커 표시**: 기존 메모리들의 위치를 마커로 표시
- **그룹 필터**: 특정 그룹의 메모리만 표시
- **지도 컨트롤**: 줌, 이동, 지도 타입 변경

### 메모리 관리
- **생성**: 지도 클릭 → 메모리 모달 → 정보 입력 → 생성
- **수정**: 마커 클릭 → 메모리 모달 → 정보 수정 → 저장
- **삭제**: 메모리 상세 페이지에서 삭제 가능

## 📱 주요 페이지

### 1. 지도 페이지 (`/`)
- 네이버 지도와 메모리 마커 표시
- 지도 클릭으로 새 메모리 생성
- 그룹별 필터링 기능

### 2. 로그인 페이지 (`/login`)
- 이메일/비밀번호 로그인
- 폼 유효성 검사

### 3. 회원가입 페이지 (`/signup`)
- 새 계정 생성
- 이메일 중복 검사

### 4. 그룹 목록 (`/groups`)
- 가입된 그룹 목록 표시
- 그룹 생성 및 관리

### 5. 메모리 상세 (`/memory/:id`)
- 메모리 상세 정보 표시
- 댓글 작성 및 관리
- 좋아요 기능

## 🔧 개발 가이드

### 컴포넌트 구조
```
src/
├── components/          # 재사용 가능한 컴포넌트
│   ├── Header.tsx       # 헤더 컴포넌트
│   ├── NaverMap.tsx     # 네이버 지도 컴포넌트
│   └── MemoryModal.tsx  # 메모리 생성/수정 모달
├── pages/               # 페이지 컴포넌트
│   ├── MapView.tsx      # 지도 페이지
│   ├── Login.tsx        # 로그인 페이지
│   ├── Signup.tsx       # 회원가입 페이지
│   ├── GroupList.tsx    # 그룹 목록 페이지
│   └── MemoryDetail.tsx # 메모리 상세 페이지
├── services/            # API 서비스
│   └── api.ts           # API 클라이언트
├── types/               # TypeScript 타입 정의
│   └── index.ts         # 공통 타입
└── hooks/               # 커스텀 훅
```

### API 연동
- Axios를 사용한 HTTP 클라이언트
- React Query를 사용한 서버 상태 관리
- 자동 토큰 갱신 및 에러 처리

### 스타일링
- Styled Components를 사용한 CSS-in-JS
- 반응형 디자인 지원
- 일관된 디자인 시스템

## 🚀 배포

### 빌드
```bash
npm run build
```

### 환경별 설정
- **개발**: `npm start`
- **프로덕션**: `npm run build` 후 빌드 파일 서빙

## 🔒 보안

- JWT 토큰 기반 인증
- HTTPS 통신 (프로덕션)
- XSS 방지
- CSRF 보호

## 📞 지원

문제가 발생하거나 질문이 있으시면 이슈를 등록해주세요.

## 📄 라이선스

MIT License