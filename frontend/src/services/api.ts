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

// --- Admin Management Functions ---
// (Protected by MasterAdmin role on backend)

export const getAllAdmins = async (): Promise<UserPublic[]> => {
  const response = await apiClient.get<UserPublic[]>('/admins');
  return response.data;
};

export const getAdminById = async (id: number): Promise<UserPublic> => {
  const response = await apiClient.get<UserPublic>(`/admins/${id}`);
  return response.data;
};

export const createAdmin = async (adminData: import('@/types/user').AdminCreateDTO): Promise<UserPublic> => {
  const response = await apiClient.post<UserPublic>('/admins', adminData);
  return response.data;
};

export const updateAdmin = async (id: number, adminData: import('@/types/user').AdminUpdateDTO): Promise<UserPublic> => {
  const response = await apiClient.put<UserPublic>(`/admins/${id}`, adminData);
  return response.data;
};

export const deleteAdmin = async (id: number): Promise<void> => {
  // Backend returns 200 with message or 204. Axios will treat 204 as success with no data.
  await apiClient.delete(`/admins/${id}`); 
};


// You can add other API functions here, e.g., for products, sales, etc.

export default apiClient;
