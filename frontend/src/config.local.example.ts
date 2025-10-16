// 로컬 개발 환경 설정 예제
// 이 파일을 복사하여 config.local.ts 파일을 만들고 실제 값을 입력하세요

import { config as baseConfig, Config } from './config';

// 로컬 개발용 실제 값으로 오버라이드
export const config: Config = {
  ...baseConfig,
  naverMapClientId: 'YOUR_NAVER_MAP_CLIENT_ID' // 실제 클라이언트 ID로 변경
};

