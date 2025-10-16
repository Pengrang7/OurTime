import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { useQuery, useMutation, useQueryClient } from 'react-query';
import { userApi, authApi } from '../services/api';
import toast from 'react-hot-toast';

const HeaderContainer = styled.header`
  background: white;
  border-bottom: 1px solid #e0e0e0;
  padding: 0 24px;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
`;

const Logo = styled(Link)`
  font-size: 24px;
  font-weight: 700;
  color: #1976d2;
  text-decoration: none;
  
  &:hover {
    color: #1565c0;
  }
`;

const Nav = styled.nav`
  display: flex;
  align-items: center;
  gap: 24px;
`;

const NavLink = styled(Link)`
  color: #333;
  text-decoration: none;
  font-weight: 500;
  padding: 8px 16px;
  border-radius: 8px;
  transition: all 0.2s ease;
  
  &:hover {
    background: #f5f5f5;
    color: #1976d2;
  }
`;

const UserMenu = styled.div`
  position: relative;
`;

const UserButton = styled.button`
  background: none;
  border: none;
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 8px;
  transition: background 0.2s ease;
  
  &:hover {
    background: #f5f5f5;
  }
`;

const UserAvatar = styled.div`
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: #1976d2;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 14px;
`;

const DropdownMenu = styled.div<{ isOpen: boolean }>`
  position: absolute;
  top: 100%;
  right: 0;
  background: white;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  min-width: 200px;
  z-index: 1000;
  display: ${props => props.isOpen ? 'block' : 'none'};
`;

const DropdownItem = styled.button`
  width: 100%;
  padding: 12px 16px;
  border: none;
  background: none;
  text-align: left;
  cursor: pointer;
  color: #333;
  transition: background 0.2s ease;
  
  &:hover {
    background: #f5f5f5;
  }
  
  &:first-child {
    border-radius: 8px 8px 0 0;
  }
  
  &:last-child {
    border-radius: 0 0 8px 8px;
    color: #d32f2f;
  }
`;

const AuthButtons = styled.div`
  display: flex;
  gap: 12px;
`;

const AuthButton = styled.button<{ variant: 'primary' | 'secondary' }>`
  padding: 8px 16px;
  border-radius: 8px;
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
  ` : `
    background: transparent;
    color: #1976d2;
    border: 1px solid #1976d2;
    
    &:hover {
      background: #f8f9fa;
    }
  `}
`;

const Header: React.FC = () => {
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const { data: user, isLoading } = useQuery(
    'userProfile',
    userApi.getProfile,
    {
      retry: false,
      onError: () => {
        // 인증되지 않은 사용자
      }
    }
  );

  const logoutMutation = useMutation(authApi.logout, {
    onSuccess: () => {
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
      queryClient.clear();
      toast.success('로그아웃되었습니다.');
      navigate('/');
    },
    onError: () => {
      toast.error('로그아웃에 실패했습니다.');
    }
  });

  const handleLogout = () => {
    logoutMutation.mutate();
    setIsDropdownOpen(false);
  };

  const isAuthenticated = !!user && !isLoading;

  return (
    <HeaderContainer>
      <Logo to="/">OurTime</Logo>
      
      <Nav>
        <NavLink to="/">지도</NavLink>
        <NavLink to="/groups">그룹</NavLink>
        {isAuthenticated && <NavLink to="/notifications">🔔 알림</NavLink>}
        
        {isAuthenticated ? (
          <UserMenu>
            <UserButton onClick={() => setIsDropdownOpen(!isDropdownOpen)}>
              <UserAvatar>
                {user.nickname.charAt(0).toUpperCase()}
              </UserAvatar>
              <span>{user.nickname}</span>
            </UserButton>
            
            <DropdownMenu isOpen={isDropdownOpen}>
              <DropdownItem onClick={() => navigate('/profile')}>
                프로필
              </DropdownItem>
              <DropdownItem onClick={() => navigate('/settings')}>
                설정
              </DropdownItem>
              <DropdownItem onClick={handleLogout}>
                로그아웃
              </DropdownItem>
            </DropdownMenu>
          </UserMenu>
        ) : (
          <AuthButtons>
            <AuthButton variant="secondary" onClick={() => navigate('/login')}>
              로그인
            </AuthButton>
            <AuthButton variant="primary" onClick={() => navigate('/signup')}>
              회원가입
            </AuthButton>
          </AuthButtons>
        )}
      </Nav>
    </HeaderContainer>
  );
};

export default Header;