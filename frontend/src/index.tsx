import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from 'react-query';
import { Toaster } from 'react-hot-toast';
import App from './App';
import './index.css';

// 설정 파일 import (로컬 설정이 있으면 우선 사용)
import { Config } from './config';

let config: Config;
try {
  // 로컬 설정 파일이 있으면 사용 (Git에 업로드되지 않음)
  config = require('./config.local').config;
} catch (e) {
  // 없으면 기본 설정 사용 (Git에 업로드됨)
  config = require('./config').config;
}

// 네이버 지도 API 동적 로드
const loadNaverMapAPI = () => {
  const clientId = config.naverMapClientId;
  
  // 인증 실패 처리 함수 설정
  (window as any).navermap_authFailure = function () {
    console.error('❌ 네이버 지도 API 인증 실패 - 클라이언트 ID와 도메인을 확인하세요');
  };
  
  // 이미 로드된 스크립트가 있는지 확인
  if (document.querySelector(`script[src*="ncpKeyId=${clientId}"]`)) {
    console.log('✅ 네이버 지도 API 이미 로드됨');
    return;
  }
  
  const script = document.createElement('script');
  script.src = `https://oapi.map.naver.com/openapi/v3/maps.js?ncpKeyId=${clientId}`;
  script.async = true;
  document.head.appendChild(script);
  
  script.onload = () => {
    console.log('✅ 네이버 지도 API 로드 완료');
  };
  
  script.onerror = () => {
    console.error('❌ 네이버 지도 API 로드 실패 - 클라이언트 ID를 확인하세요');
  };
};

// 컴포넌트 마운트 시 지도 API 로드
loadNaverMapAPI();

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 1,
      refetchOnWindowFocus: false,
    },
  },
});

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);

root.render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <App />
        <Toaster 
          position="top-right"
          toastOptions={{
            duration: 3000,
            style: {
              background: '#363636',
              color: '#fff',
            },
          }}
        />
      </BrowserRouter>
    </QueryClientProvider>
  </React.StrictMode>
);