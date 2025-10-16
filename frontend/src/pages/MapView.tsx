import React, { useState } from 'react';
import styled from 'styled-components';
import { useQuery } from 'react-query';
import NaverMap from '../components/NaverMap';
import MemoryModal from '../components/MemoryModal';
import RouteCreator from '../components/RouteCreator';
import { Memory } from '../types';
import { memoryApi, groupApi } from '../services/api';
import toast from 'react-hot-toast';

interface Route {
  id: string;
  name: string;
  description?: string;
  memoryIds: number[];
  createdAt: string;
}

const MapViewContainer = styled.div`
  width: 100%;
  height: 100%;
  position: relative;
`;

const LoadingOverlay = styled.div`
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
`;

const LoadingSpinner = styled.div`
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #1976d2;
  border-radius: 50%;
  animation: spin 1s linear infinite;

  @keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
  }
`;

const MapView: React.FC = () => {
  const [selectedLocation, setSelectedLocation] = useState<{ lat: number; lng: number } | null>(null);
  const [isMemoryModalOpen, setIsMemoryModalOpen] = useState(false);
  const [selectedMemory, setSelectedMemory] = useState<Memory | null>(null);
  const [isRouteCreatorOpen, setIsRouteCreatorOpen] = useState(false);
  const [routes, setRoutes] = useState<Route[]>([]);
  const [showRoutes, setShowRoutes] = useState(false);

  // 그룹 목록 조회
  const { data: groups = [], isLoading: groupsLoading } = useQuery(
    'groups',
    groupApi.getGroups,
    {
      onError: (error: any) => {
        toast.error('그룹 목록을 불러오는데 실패했습니다.');
        console.error('Groups fetch error:', error);
      }
    }
  );

  // 메모리 목록 조회
  const { data: memories = [], isLoading: memoriesLoading, refetch: refetchMemories } = useQuery(
    'memories',
    () => memoryApi.getMemories(),
    {
      onError: (error: any) => {
        toast.error('메모리 목록을 불러오는데 실패했습니다.');
        console.error('Memories fetch error:', error);
      }
    }
  );

  const handleMapClick = (lat: number, lng: number) => {
    setSelectedLocation({ lat, lng });
    setIsMemoryModalOpen(true);
    setSelectedMemory(null);
  };

  const handleMemoryClick = (memory: Memory) => {
    setSelectedMemory(memory);
    setIsMemoryModalOpen(true);
    setSelectedLocation(null);
  };

  const handleMemoryModalClose = () => {
    setIsMemoryModalOpen(false);
    setSelectedLocation(null);
    setSelectedMemory(null);
  };

  const handleMemoryCreated = () => {
    refetchMemories();
    toast.success('메모리가 성공적으로 생성되었습니다!');
  };

  const handleMemoryUpdated = () => {
    refetchMemories();
    toast.success('메모리가 성공적으로 수정되었습니다!');
  };

  const handleRouteCreate = (route: Route) => {
    setRoutes(prev => [...prev, route]);
    toast.success('루트가 성공적으로 생성되었습니다!');
  };

  const handleToggleRoutes = () => {
    setShowRoutes(!showRoutes);
  };

  const isLoading = groupsLoading || memoriesLoading;

  return (
    <MapViewContainer>
      {isLoading && (
        <LoadingOverlay>
          <LoadingSpinner />
        </LoadingOverlay>
      )}
      
      <NaverMap
        memories={memories}
        groups={groups}
        routes={routes}
        onMapClick={handleMapClick}
        onMemoryClick={handleMemoryClick}
        onCreateRoute={() => setIsRouteCreatorOpen(true)}
        onToggleRoutes={handleToggleRoutes}
        showRoutes={showRoutes}
      />

      {isMemoryModalOpen && (
        <MemoryModal
          isOpen={isMemoryModalOpen}
          onClose={handleMemoryModalClose}
          location={selectedLocation}
          memory={selectedMemory}
          groups={groups}
          onMemoryCreated={handleMemoryCreated}
          onMemoryUpdated={handleMemoryUpdated}
        />
      )}

      {isRouteCreatorOpen && (
        <RouteCreator
          memories={memories}
          onRouteCreate={handleRouteCreate}
          onClose={() => setIsRouteCreatorOpen(false)}
        />
      )}
    </MapViewContainer>
  );
};

export default MapView;