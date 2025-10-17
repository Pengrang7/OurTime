import axios from 'axios';
import { 
  User, 
  Group, 
  Memory, 
  Comment, 
  CreateMemoryRequest, 
  CreateCommentRequest,
  LoginRequest,
  SignupRequest,
  AuthResponse 
} from '../types';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 요청 인터셉터 - 토큰 자동 추가
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 응답 인터셉터 - 토큰 만료 처리
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// 인증 API
export const authApi = {
  login: async (data: LoginRequest): Promise<AuthResponse> => {
    const response = await api.post('/auth/login', data);
    return response.data.data;
  },

  signup: async (data: SignupRequest): Promise<AuthResponse> => {
    const response = await api.post('/auth/signup', data);
    return response.data.data;
  },

  refreshToken: async (): Promise<AuthResponse> => {
    const refreshToken = localStorage.getItem('refreshToken');
    const response = await api.post('/auth/refresh', { refreshToken });
    return response.data.data;
  },

  logout: async (): Promise<void> => {
    await api.post('/auth/logout');
  },
};

// 사용자 API
export const userApi = {
  getProfile: async (): Promise<User> => {
    const response = await api.get('/users/me');
    return response.data.data;
  },

  updateProfile: async (data: Partial<User>): Promise<User> => {
    const response = await api.put('/users/me', data);
    return response.data.data;
  },

  updatePassword: async (data: { currentPassword: string; newPassword: string }): Promise<void> => {
    await api.put('/users/me/password', data);
  },
};

// 그룹 API
export const groupApi = {
  getGroups: async (): Promise<Group[]> => {
    const response = await api.get('/groups');
    return response.data.data;
  },

  createGroup: async (data: { name: string; type: string; description?: string }): Promise<Group> => {
    const response = await api.post('/groups', data);
    return response.data.data;
  },

  createGroupWithInvites: async (data: { 
    name: string; 
    type: string; 
    description?: string;
    inviteeEmails?: string[];
  }): Promise<Group> => {
    const response = await api.post('/groups/with-invites', data);
    return response.data.data;
  },

  joinGroup: async (inviteCode: string): Promise<Group> => {
    const response = await api.post('/groups/join', { inviteCode });
    return response.data.data;
  },

  updateGroup: async (groupId: number, data: Partial<Group>): Promise<Group> => {
    const response = await api.put(`/groups/${groupId}`, data);
    return response.data.data;
  },

  deleteGroup: async (groupId: number): Promise<void> => {
    await api.delete(`/groups/${groupId}`);
  },
};

// 메모리 API
export const memoryApi = {
  getMemories: async (groupId?: number): Promise<Memory[]> => {
    const url = groupId ? `/memories?groupId=${groupId}` : '/memories';
    const response = await api.get(url);
    return response.data.data;
  },

  getMemory: async (memoryId: number): Promise<Memory> => {
    const response = await api.get(`/memories/${memoryId}`);
    return response.data.data;
  },

  createMemory: async (data: CreateMemoryRequest): Promise<Memory> => {
    const formData = new FormData();
    formData.append('groupId', data.groupId.toString());
    formData.append('title', data.title);
    formData.append('latitude', data.latitude.toString());
    formData.append('longitude', data.longitude.toString());
    formData.append('visitedAt', data.visitedAt);
    
    if (data.description) formData.append('description', data.description);
    if (data.locationName) {
      formData.append('locationName', data.locationName);
      console.log('위치명 전송:', data.locationName);
    }
    if (data.tagNames && data.tagNames.length > 0) {
      const tagNamesJson = JSON.stringify(data.tagNames);
      formData.append('tagNames', tagNamesJson);
      console.log('태그 전송:', tagNamesJson);
    }
    if (data.images) {
      data.images.forEach(image => formData.append('images', image));
    }

    const response = await api.post('/memories', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data.data;
  },

  updateMemory: async (memoryId: number, data: Partial<CreateMemoryRequest>): Promise<Memory> => {
    console.log('메모리 업데이트 요청:', {
      memoryId,
      locationName: data.locationName,
      tagNames: data.tagNames,
      data
    });
    const response = await api.put(`/memories/${memoryId}`, data);
    return response.data.data;
  },

  deleteMemory: async (memoryId: number): Promise<void> => {
    await api.delete(`/memories/${memoryId}`);
  },

  likeMemory: async (memoryId: number): Promise<void> => {
    await api.post(`/memories/${memoryId}/like`);
  },

  unlikeMemory: async (memoryId: number): Promise<void> => {
    await api.delete(`/memories/${memoryId}/like`);
  },
};

// 댓글 API
export const commentApi = {
  getComments: async (memoryId: number): Promise<Comment[]> => {
    const response = await api.get(`/memories/${memoryId}/comments`);
    return response.data.data;
  },

  createComment: async (data: CreateCommentRequest): Promise<Comment> => {
    const response = await api.post('/comments', data);
    return response.data.data;
  },

  updateComment: async (commentId: number, content: string): Promise<Comment> => {
    const response = await api.put(`/comments/${commentId}`, { content });
    return response.data.data;
  },

  deleteComment: async (commentId: number): Promise<void> => {
    await api.delete(`/comments/${commentId}`);
  },
};

// 초대 API
export const invitationApi = {
  getInvitations: async (): Promise<any[]> => {
    const response = await api.get('/invitations');
    return response.data.data;
  },

  getPendingInvitations: async (): Promise<any[]> => {
    const response = await api.get('/invitations/pending');
    return response.data.data;
  },

  acceptInvitation: async (invitationId: number): Promise<void> => {
    await api.post(`/invitations/${invitationId}/accept`);
  },

  rejectInvitation: async (invitationId: number): Promise<void> => {
    await api.post(`/invitations/${invitationId}/reject`);
  },
};

// 파일 업로드 API
export const fileApi = {
  uploadImage: async (file: File): Promise<string> => {
    const formData = new FormData();
    formData.append('file', file);
    
    const response = await api.post('/files/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data.data.url;
  },
};

export default api;