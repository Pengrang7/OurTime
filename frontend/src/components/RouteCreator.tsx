import React, { useState } from 'react';
import styled from 'styled-components';
import { Memory } from '../types';

interface RouteCreatorProps {
  memories: Memory[];
  onRouteCreate: (route: Route) => void;
  onClose: () => void;
}

interface Route {
  id: string;
  name: string;
  description?: string;
  memoryIds: number[];
  createdAt: string;
}

const ModalOverlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10000;
`;

const ModalContent = styled.div`
  background: white;
  border-radius: 16px;
  padding: 24px;
  width: 90%;
  max-width: 800px;
  max-height: 80vh;
  overflow-y: auto;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
`;

const ModalHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
`;

const Title = styled.h2`
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: #333;
`;

const CloseButton = styled.button`
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #666;
  padding: 4px;
  
  &:hover {
    color: #333;
  }
`;

const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 20px;
`;

const FormGroup = styled.div`
  display: flex;
  flex-direction: column;
  gap: 8px;
`;

const Label = styled.label`
  font-weight: 500;
  color: #333;
  font-size: 14px;
`;

const Input = styled.input`
  padding: 12px 16px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 16px;
  transition: border-color 0.2s ease;

  &:focus {
    outline: none;
    border-color: #1976d2;
  }
`;

const TextArea = styled.textarea`
  padding: 12px 16px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 16px;
  min-height: 100px;
  resize: vertical;
  font-family: inherit;
  transition: border-color 0.2s ease;

  &:focus {
    outline: none;
    border-color: #1976d2;
  }
`;

const MemoryList = styled.div`
  display: flex;
  flex-direction: column;
  gap: 12px;
  max-height: 300px;
  overflow-y: auto;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 16px;
`;

const MemoryItem = styled.div<{ selected: boolean }>`
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
  background: ${props => props.selected ? '#e3f2fd' : 'transparent'};
  border: 1px solid ${props => props.selected ? '#1976d2' : 'transparent'};

  &:hover {
    background: ${props => props.selected ? '#e3f2fd' : '#f5f5f5'};
  }
`;

const Checkbox = styled.input`
  width: 18px;
  height: 18px;
  cursor: pointer;
`;

const MemoryInfo = styled.div`
  flex: 1;
`;

const MemoryTitle = styled.div`
  font-weight: 500;
  color: #333;
  margin-bottom: 4px;
`;

const MemoryMeta = styled.div`
  font-size: 12px;
  color: #666;
`;

const RoutePreview = styled.div`
  background: #f8f9fa;
  border-radius: 8px;
  padding: 16px;
  margin-top: 16px;
`;

const RoutePreviewTitle = styled.h4`
  margin: 0 0 12px 0;
  color: #333;
  font-size: 16px;
`;

const RouteSteps = styled.div`
  display: flex;
  flex-direction: column;
  gap: 8px;
`;

const RouteStep = styled.div`
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background: white;
  border-radius: 6px;
  font-size: 14px;
`;

const StepNumber = styled.div`
  background: #1976d2;
  color: white;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
`;

const ButtonGroup = styled.div`
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  margin-top: 24px;
`;

const Button = styled.button<{ variant?: 'primary' | 'secondary' }>`
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  border: none;

  ${props => props.variant === 'primary' ? `
    background: #1976d2;
    color: white;
    
    &:hover {
      background: #1565c0;
    }
    
    &:disabled {
      background: #ccc;
      cursor: not-allowed;
    }
  ` : `
    background: #f5f5f5;
    color: #333;
    
    &:hover {
      background: #e0e0e0;
    }
  `}
`;

const RouteCreator: React.FC<RouteCreatorProps> = ({
  memories,
  onRouteCreate,
  onClose
}) => {
  const [routeName, setRouteName] = useState('');
  const [routeDescription, setRouteDescription] = useState('');
  const [selectedMemories, setSelectedMemories] = useState<number[]>([]);

  const handleMemoryToggle = (memoryId: number) => {
    setSelectedMemories(prev => 
      prev.includes(memoryId)
        ? prev.filter(id => id !== memoryId)
        : [...prev, memoryId]
    );
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!routeName.trim()) {
      alert('루트 이름을 입력해주세요.');
      return;
    }

    if (selectedMemories.length < 2) {
      alert('최소 2개 이상의 메모리를 선택해주세요.');
      return;
    }

    const route: Route = {
      id: Date.now().toString(),
      name: routeName.trim(),
      description: routeDescription.trim() || undefined,
      memoryIds: selectedMemories,
      createdAt: new Date().toISOString()
    };

    onRouteCreate(route);
    onClose();
  };

  const selectedMemoryObjects = memories.filter(memory => 
    selectedMemories.includes(memory.id)
  );

  return (
    <ModalOverlay onClick={onClose}>
      <ModalContent onClick={(e) => e.stopPropagation()}>
        <ModalHeader>
          <Title>새 루트 생성</Title>
          <CloseButton onClick={onClose}>×</CloseButton>
        </ModalHeader>

        <Form onSubmit={handleSubmit}>
          <FormGroup>
            <Label>루트 이름 *</Label>
            <Input
              type="text"
              value={routeName}
              onChange={(e) => setRouteName(e.target.value)}
              placeholder="루트 이름을 입력하세요"
              required
            />
          </FormGroup>

          <FormGroup>
            <Label>루트 설명</Label>
            <TextArea
              value={routeDescription}
              onChange={(e) => setRouteDescription(e.target.value)}
              placeholder="루트에 대한 설명을 입력하세요"
            />
          </FormGroup>

          <FormGroup>
            <Label>메모리 선택 * (최소 2개)</Label>
            <MemoryList>
              {memories.map(memory => (
                <MemoryItem
                  key={memory.id}
                  selected={selectedMemories.includes(memory.id)}
                  onClick={() => handleMemoryToggle(memory.id)}
                >
                  <Checkbox
                    type="checkbox"
                    checked={selectedMemories.includes(memory.id)}
                    onChange={() => handleMemoryToggle(memory.id)}
                  />
                  <MemoryInfo>
                    <MemoryTitle>{memory.title}</MemoryTitle>
                    <MemoryMeta>
                      📍 {memory.locationName || '위치 정보 없음'} • 
                      📅 {new Date(memory.visitedAt).toLocaleDateString()}
                    </MemoryMeta>
                  </MemoryInfo>
                </MemoryItem>
              ))}
            </MemoryList>
          </FormGroup>

          {selectedMemories.length > 0 && (
            <RoutePreview>
              <RoutePreviewTitle>루트 미리보기</RoutePreviewTitle>
              <RouteSteps>
                {selectedMemoryObjects.map((memory, index) => (
                  <RouteStep key={memory.id}>
                    <StepNumber>{index + 1}</StepNumber>
                    <div>
                      <div style={{ fontWeight: '500' }}>{memory.title}</div>
                      <div style={{ fontSize: '12px', color: '#666' }}>
                        {memory.locationName || '위치 정보 없음'}
                      </div>
                    </div>
                  </RouteStep>
                ))}
              </RouteSteps>
            </RoutePreview>
          )}

          <ButtonGroup>
            <Button type="button" onClick={onClose}>
              취소
            </Button>
            <Button
              type="submit"
              variant="primary"
              disabled={!routeName.trim() || selectedMemories.length < 2}
            >
              루트 생성
            </Button>
          </ButtonGroup>
        </Form>
      </ModalContent>
    </ModalOverlay>
  );
};

export default RouteCreator;