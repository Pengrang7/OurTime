import React, { useEffect, useRef, useState } from 'react';
import styled from 'styled-components';
import { Memory, Group } from '../types';

interface Route {
  id: string;
  name: string;
  description?: string;
  memoryIds: number[];
  createdAt: string;
}

interface NaverMapProps {
  memories: Memory[];
  groups: Group[];
  routes: Route[];
  onMapClick: (lat: number, lng: number) => void;
  onMemoryClick: (memory: Memory) => void;
  onCreateRoute?: () => void;
  onToggleRoutes?: () => void;
  selectedGroupId?: number;
  showRoutes?: boolean;
}

const MapErrorContainer = styled.div`
  text-align: center;
  padding: 40px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  max-width: 400px;
`;

const MapErrorTitle = styled.h3`
  color: #e74c3c;
  margin-bottom: 16px;
  font-size: 18px;
`;

const MapErrorMessage = styled.p`
  color: #666;
  margin-bottom: 20px;
  line-height: 1.5;
`;

const MapErrorButton = styled.button`
  background: #3498db;
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  
  &:hover {
    background: #2980b9;
  }
`;

const MapPlaceholder = styled.div`
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
  font-weight: 500;
`;

const MapContainer = styled.div`
  width: 100%;
  height: 100%;
  position: relative;
`;

const MapControls = styled.div`
  position: absolute;
  top: 20px;
  right: 20px;
  z-index: 1000;
  display: flex;
  flex-direction: column;
  gap: 10px;
`;

const RouteControls = styled.div`
  position: absolute;
  top: 20px;
  right: 200px;
  z-index: 1000;
  display: flex;
  flex-direction: column;
  gap: 10px;
`;

const ControlButton = styled.button`
  background: white;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 12px 16px;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s ease;

  &:hover {
    background: #f8f9fa;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }

  &:active {
    transform: translateY(1px);
  }
`;

const GroupFilter = styled.div`
  position: absolute;
  top: 20px;
  left: 20px;
  z-index: 1000;
  background: white;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  min-width: 200px;
`;

const GroupItem = styled.div<{ selected: boolean }>`
  padding: 8px 12px;
  border-radius: 6px;
  cursor: pointer;
  margin-bottom: 4px;
  background: ${props => props.selected ? '#e3f2fd' : 'transparent'};
  color: ${props => props.selected ? '#1976d2' : '#333'};
  font-weight: ${props => props.selected ? '600' : '400'};
  transition: all 0.2s ease;

  &:hover {
    background: ${props => props.selected ? '#e3f2fd' : '#f5f5f5'};
  }
`;

declare global {
  interface Window {
    naver: any;
  }
}

const NaverMap: React.FC<NaverMapProps> = ({
  memories,
  groups,
  routes,
  onMapClick,
  onMemoryClick,
  onCreateRoute,
  onToggleRoutes,
  selectedGroupId,
  showRoutes = false
}) => {
  const mapRef = useRef<HTMLDivElement>(null);
  const mapInstance = useRef<any>(null);
  const markers = useRef<any[]>([]);
  const polylines = useRef<any[]>([]);
  const [selectedGroup, setSelectedGroup] = useState<number | null>(selectedGroupId || null);
  const [mapLoadError, setMapLoadError] = useState(false);

  useEffect(() => {
    if (!mapRef.current) return;

    // 네이버 지도 API가 로드될 때까지 대기
    const initMap = () => {
      if (!window.naver || !window.naver.maps) {
        console.log('⏳ 네이버 지도 API 로드 대기 중...');
        setTimeout(initMap, 100); // 100ms 후 다시 시도
        return;
      }

      let map;
      try {
        // 네이버 지도 초기화
        map = new window.naver.maps.Map(mapRef.current, {
          center: new window.naver.maps.LatLng(37.5665, 126.9780), // 서울 중심
          zoom: 13,
          mapTypeControl: true,
          mapTypeControlOptions: {
            style: window.naver.maps.MapTypeControlStyle.BUTTON,
            position: window.naver.maps.Position.TOP_RIGHT
          },
          zoomControl: true,
          zoomControlOptions: {
            style: window.naver.maps.ZoomControlStyle.SMALL,
            position: window.naver.maps.Position.RIGHT_CENTER
          }
        });

        mapInstance.current = map;
        setMapLoadError(false);
        console.log('🗺️ 네이버 지도 초기화 성공');

        // 지도 클릭 이벤트
        window.naver.maps.Event.addListener(map, 'click', (e: any) => {
          const lat = e.coord.lat();
          const lng = e.coord.lng();
          onMapClick(lat, lng);
        });
      } catch (error) {
        console.error('네이버 지도 초기화 실패:', error);
        setMapLoadError(true);
        return;
      }
    };

    initMap();

    return () => {
      // 정리
      markers.current.forEach(marker => marker.setMap(null));
      markers.current = [];
      polylines.current.forEach(polyline => polyline.setMap(null));
      polylines.current = [];
    };
  }, []);

  useEffect(() => {
    if (!mapInstance.current) return;

    // 기존 마커와 폴리라인 제거
    markers.current.forEach(marker => marker.setMap(null));
    markers.current = [];
    polylines.current.forEach(polyline => polyline.setMap(null));
    polylines.current = [];

    // 필터링된 메모리들로 마커 생성
    const filteredMemories = selectedGroup 
      ? memories.filter(memory => memory.groupId === selectedGroup)
      : memories;

    filteredMemories.forEach(memory => {
      const marker = new window.naver.maps.Marker({
        position: new window.naver.maps.LatLng(memory.latitude, memory.longitude),
        map: mapInstance.current,
        title: `${memory.title} (by ${memory.user.nickname})`,
        icon: {
          content: `
            <div style="
              background: #1976d2;
              color: white;
              padding: 8px 12px;
              border-radius: 20px;
              font-size: 12px;
              font-weight: 600;
              box-shadow: 0 2px 8px rgba(0,0,0,0.2);
              border: 2px solid white;
              cursor: pointer;
            ">
              📍 ${memory.title}
              <div style="font-size: 10px; opacity: 0.9; margin-top: 2px;">by ${memory.user.nickname}</div>
            </div>
          `,
          anchor: new window.naver.maps.Point(0, 0)
        }
      });

      // 마커 클릭 이벤트
      window.naver.maps.Event.addListener(marker, 'click', () => {
        onMemoryClick(memory);
      });

      markers.current.push(marker);
    });

    // 루트 표시
    if (showRoutes && routes.length > 0) {
      routes.forEach(route => {
        const routeMemories = memories.filter(memory => 
          route.memoryIds.includes(memory.id)
        ).sort((a, b) => {
          const indexA = route.memoryIds.indexOf(a.id);
          const indexB = route.memoryIds.indexOf(b.id);
          return indexA - indexB;
        });

        if (routeMemories.length >= 2) {
          const path = routeMemories.map(memory => 
            new window.naver.maps.LatLng(memory.latitude, memory.longitude)
          );

          const polyline = new window.naver.maps.Polyline({
            map: mapInstance.current,
            path: path,
            strokeColor: '#FF6B6B',
            strokeOpacity: 0.8,
            strokeWeight: 4,
            strokeStyle: 'solid'
          });

          polylines.current.push(polyline);
        }
      });
    }
  }, [memories, selectedGroup, routes, showRoutes]);

  const handleGroupSelect = (groupId: number | null) => {
    setSelectedGroup(groupId);
  };

  const centerOnSeoul = () => {
    if (mapInstance.current) {
      mapInstance.current.setCenter(new window.naver.maps.LatLng(37.5665, 126.9780));
      mapInstance.current.setZoom(13);
    }
  };

  const fitBounds = () => {
    if (mapInstance.current && markers.current.length > 0) {
      const bounds = new window.naver.maps.LatLngBounds();
      markers.current.forEach(marker => {
        bounds.extend(marker.getPosition());
      });
      mapInstance.current.fitBounds(bounds);
    }
  };

  return (
    <MapContainer>
      {mapLoadError ? (
        <MapErrorContainer>
          <MapErrorTitle>🗺️ 지도를 불러올 수 없습니다</MapErrorTitle>
          <MapErrorMessage>
            네이버 지도 API 인증에 실패했습니다.<br/>
            개발 중이므로 다른 기능들을 먼저 테스트해보세요!
          </MapErrorMessage>
          <MapErrorButton onClick={() => window.location.reload()}>
            새로고침
          </MapErrorButton>
        </MapErrorContainer>
      ) : (
        <>
          <div ref={mapRef} className="naver-map" />
          
          <GroupFilter>
            <h3 style={{ margin: '0 0 12px 0', fontSize: '16px', fontWeight: '600' }}>
              그룹 필터
            </h3>
            <GroupItem 
              selected={selectedGroup === null}
              onClick={() => handleGroupSelect(null)}
            >
              전체 보기
            </GroupItem>
            {groups.map(group => (
              <GroupItem
                key={group.id}
                selected={selectedGroup === group.id}
                onClick={() => handleGroupSelect(group.id)}
              >
                {group.name}
              </GroupItem>
            ))}
          </GroupFilter>

          <RouteControls>
            <ControlButton onClick={onCreateRoute}>
              🗺️ 루트 생성
            </ControlButton>
            <ControlButton onClick={onToggleRoutes}>
              {showRoutes ? '🚫 루트 숨기기' : '👁️ 루트 보기'}
            </ControlButton>
          </RouteControls>

          <MapControls>
            <ControlButton onClick={centerOnSeoul}>
              🏠 서울 중심
            </ControlButton>
            <ControlButton onClick={fitBounds}>
              📍 모든 메모리 보기
            </ControlButton>
          </MapControls>
        </>
      )}
    </MapContainer>
  );
};

export default NaverMap;