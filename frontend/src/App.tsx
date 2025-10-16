import React from 'react';
import { Routes, Route } from 'react-router-dom';
import styled from 'styled-components';
import Header from './components/Header';
import MapView from './pages/MapView';
import Login from './pages/Login';
import Signup from './pages/Signup';
import GroupList from './pages/GroupList';
import MemoryDetail from './pages/MemoryDetail';
import Notifications from './pages/Notifications';

const AppContainer = styled.div`
  display: flex;
  flex-direction: column;
  height: 100vh;
  width: 100vw;
`;

const MainContent = styled.main`
  flex: 1;
  overflow: hidden;
`;

function App() {
  return (
    <AppContainer>
      <Header />
      <MainContent>
        <Routes>
          <Route path="/" element={<MapView />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/groups" element={<GroupList />} />
          <Route path="/notifications" element={<Notifications />} />
          <Route path="/memory/:id" element={<MemoryDetail />} />
        </Routes>
      </MainContent>
    </AppContainer>
  );
}

export default App;