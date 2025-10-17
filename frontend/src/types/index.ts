export interface User {
  id: number;
  email: string;
  nickname: string;
  userTag?: string;
  profileImage?: string;
  createdAt: string;
  updatedAt: string;
}

export interface Group {
  id: number;
  name: string;
  type: 'COUPLE' | 'FAMILY' | 'FRIEND' | 'TEAM' | 'ETC';
  inviteCode: string;
  description?: string;
  groupImage?: string;
  createdBy: number;
  createdAt: string;
  updatedAt: string;
}

export interface Memory {
  id: number;
  groupId: number;
  user: User;
  title: string;
  description?: string;
  latitude: number;
  longitude: number;
  locationName?: string;
  visitedAt: string;
  imageUrls: string[];
  tags: Tag[];
  likeCount: number;
  commentCount: number;
  createdAt: string;
}

export interface Comment {
  id: number;
  memoryId: number;
  user: User;
  content: string;
  createdAt: string;
  updatedAt: string;
}

export interface Like {
  id: number;
  memoryId: number;
  userId: number;
  createdAt: string;
}

export interface Tag {
  id: number;
  name: string;
}

export interface CreateMemoryRequest {
  groupId: number;
  title: string;
  description?: string;
  latitude: number;
  longitude: number;
  locationName?: string;
  visitedAt: string;
  images?: File[];
  tagNames?: string[];
}

export interface CreateCommentRequest {
  memoryId: number;
  content: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface SignupRequest {
  email: string;
  password: string;
  nickname: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  user: User;
}

export interface Route {
  id: string;
  name: string;
  description?: string;
  memoryIds: number[];
  createdAt: string;
}