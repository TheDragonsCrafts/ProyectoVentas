import axios from 'axios';
import { LoginDTO, RegisterDTO, AuthResponse, UserPublic } from '@/types/user'; // Using @ alias

const apiClient = axios.create({
  baseURL: 'http://localhost:3000/api', // Adjust if your backend port is different
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor to add JWT token to requests
import { useAuthStore } from '@/store/authStore'; // Import store for interceptor

apiClient.interceptors.request.use(
  (config) => {
    const token = useAuthStore.getState().token;
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export const loginUser = async (credentials: LoginDTO): Promise<AuthResponse> => {
  const response = await apiClient.post<AuthResponse>('/auth/login', credentials);
  return response.data;
};

export const registerUser = async (data: RegisterDTO): Promise<UserPublic> => {
  // Assuming the backend returns the created user (AdministradorPublic)
  // The current backend /api/auth/register returns AdministradorPublic
  const response = await apiClient.post<UserPublic>('/auth/register', data);
  return response.data;
};

// You can add other API functions here, e.g., for products, sales, etc.

export default apiClient;
