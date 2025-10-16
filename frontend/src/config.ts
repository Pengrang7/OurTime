// 환경 설정
// 이 파일은 Git에 업로드됩니다 (민감 정보 없음)

export interface Config {
  apiUrl: string;
  naverMapClientId: string;
}

export const config: Config = {
  // API URL
  apiUrl: process.env.REACT_APP_API_URL || 'http://localhost:8080/api',
  
  // 네이버 지도 클라이언트 ID
  // 실제 값은 config.local.ts 파일에서 오버라이드됩니다
  naverMapClientId: process.env.REACT_APP_NAVER_MAP_CLIENT_ID || 'YOUR_CLIENT_ID'
};

