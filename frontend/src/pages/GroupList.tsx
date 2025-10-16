import React, { useState } from 'react';
import styled from 'styled-components';
import { useQuery, useMutation, useQueryClient } from 'react-query';
import { groupApi } from '../services/api';
import toast from 'react-hot-toast';
import GroupCreateModal from '../components/GroupCreateModal';

const GroupListContainer = styled.div`
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
`;

const Title = styled.h1`
  margin-bottom: 32px;
  color: #333;
  font-size: 32px;
  font-weight: 700;
`;

const GroupGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
`;

const GroupCard = styled.div`
  background: white;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  cursor: pointer;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  }
`;

const GroupName = styled.h3`
  margin: 0 0 12px 0;
  color: #333;
  font-size: 20px;
  font-weight: 600;
`;

const GroupType = styled.span<{ type: string }>`
  display: inline-block;
  padding: 4px 12px;
  border-radius: 16px;
  font-size: 12px;
  font-weight: 500;
  margin-bottom: 12px;
  
  ${props => {
    const colors = {
      COUPLE: { bg: '#ffebee', color: '#c62828' },
      FAMILY: { bg: '#e8f5e8', color: '#2e7d32' },
      FRIEND: { bg: '#e3f2fd', color: '#1976d2' },
      TEAM: { bg: '#fff3e0', color: '#f57c00' },
      ETC: { bg: '#f3e5f5', color: '#7b1fa2' }
    };
    const style = colors[props.type as keyof typeof colors] || colors.ETC;
    return `
      background: ${style.bg};
      color: ${style.color};
    `;
  }}
`;

const GroupDescription = styled.p`
  color: #666;
  font-size: 14px;
  line-height: 1.5;
  margin: 0 0 16px 0;
`;

const GroupInfo = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: #999;
`;

const GroupList: React.FC = () => {
  const queryClient = useQueryClient();
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);

  const { data: groups = [], isLoading } = useQuery(
    'groups',
    groupApi.getGroups,
    {
      onError: (error: any) => {
        toast.error('그룹 목록을 불러오는데 실패했습니다.');
        console.error('Groups fetch error:', error);
      }
    }
  );

  const deleteGroupMutation = useMutation(groupApi.deleteGroup, {
    onSuccess: () => {
      queryClient.invalidateQueries('groups');
      toast.success('그룹이 삭제되었습니다.');
    },
    onError: (error: any) => {
      toast.error('그룹 삭제에 실패했습니다.');
      console.error('Delete group error:', error);
    }
  });

  const handleDeleteGroup = (groupId: number, event: React.MouseEvent) => {
    event.stopPropagation();
    if (window.confirm('정말로 이 그룹을 삭제하시겠습니까?')) {
      deleteGroupMutation.mutate(groupId);
    }
  };

  const getGroupTypeLabel = (type: string) => {
    const labels = {
      COUPLE: '커플',
      FAMILY: '가족',
      FRIEND: '친구',
      TEAM: '팀',
      ETC: '기타'
    };
    return labels[type as keyof typeof labels] || '기타';
  };

  if (isLoading) {
    return (
      <GroupListContainer>
        <Title>그룹 목록</Title>
        <div style={{ textAlign: 'center', padding: '40px' }}>
          로딩 중...
        </div>
      </GroupListContainer>
    );
  }

  return (
    <GroupListContainer>
      <HeaderSection>
        <Title>그룹 목록</Title>
        <CreateButton onClick={() => setIsCreateModalOpen(true)}>
          + 새 그룹 만들기
        </CreateButton>
      </HeaderSection>
      
      {groups.length === 0 ? (
        <div style={{ 
          textAlign: 'center', 
          padding: '60px 20px',
          color: '#666'
        }}>
          <div style={{ fontSize: '48px', marginBottom: '16px' }}>👥</div>
          <h3>아직 그룹이 없습니다</h3>
          <p>새로운 그룹을 만들어보세요!</p>
        </div>
      ) : (
        <GroupGrid>
          {groups.map(group => (
            <GroupCard key={group.id}>
              <GroupType type={group.type}>
                {getGroupTypeLabel(group.type)}
              </GroupType>
              
              <GroupName>{group.name}</GroupName>
              
              {group.description && (
                <GroupDescription>{group.description}</GroupDescription>
              )}
              
              <GroupInfo>
                <span>초대 코드: {group.inviteCode}</span>
                <button
                  onClick={(e) => handleDeleteGroup(group.id, e)}
                  style={{
                    background: 'none',
                    border: 'none',
                    color: '#d32f2f',
                    cursor: 'pointer',
                    fontSize: '12px'
                  }}
                >
                  삭제
                </button>
              </GroupInfo>
            </GroupCard>
          ))}
        </GroupGrid>
      )}

      <GroupCreateModal 
        isOpen={isCreateModalOpen} 
        onClose={() => setIsCreateModalOpen(false)} 
      />
    </GroupListContainer>
  );
};

// 추가 스타일 컴포넌트
const HeaderSection = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
`;

const CreateButton = styled.button`
  background: #3b82f6;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 12px 24px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s;

  &:hover {
    background: #2563eb;
  }
`;

export default GroupList;