import { create } from 'zustand';
import { persist, createJSONStorage } from 'zustand/middleware';
import { UserPublic } from '@/types/user'; // Import the actual UserPublic type

interface AuthState {
  user: UserPublic | null;
  token: string | null;
  isAuthenticated: () => boolean; // Derived state
  login: (userData: UserPublic, token: string) => void;
  logout: () => void;
  // Add other methods like `setToken` if needed for initializing from storage without full login
}

export const useAuthStore = create(
  persist<AuthState>(
    (set, get) => ({
      user: null,
      token: null,
      isAuthenticated: () => {
        const token = get().token;
        // Potentially add token expiration check here if tokens have expiry
        return !!token;
      },
      login: (userData, tokenData) => set({ user: userData, token: tokenData }),
      logout: () => set({ user: null, token: null }),
    }),
    {
      name: 'auth-storage', // name of the item in localStorage
      storage: createJSONStorage(() => localStorage),
      // Only persist token and user, isAuthenticated is derived
      // partialize: (state) => ({ token: state.token, user: state.user }),
    }
  )
);

// Helper to initialize token for axios interceptor if needed upon app load
// This is if your apiClient is a separate instance and needs manual token setup
// import apiClient from '@/services/api'; 
// const initialToken = useAuthStore.getState().token;
// if (initialToken) {
//   apiClient.defaults.headers.common['Authorization'] = `Bearer ${initialToken}`;
// }

// useAuthStore.subscribe(state => {
//   if (state.token) {
//     apiClient.defaults.headers.common['Authorization'] = `Bearer ${state.token}`;
//   } else {
//     delete apiClient.defaults.headers.common['Authorization'];
//   }
// });
