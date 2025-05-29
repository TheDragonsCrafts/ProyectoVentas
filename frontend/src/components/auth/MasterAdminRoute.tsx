import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuthStore } from '@/store/authStore';
import { toast } from 'sonner';

const MasterAdminRoute: React.FC = () => {
  const { isAuthenticated, user } = useAuthStore(state => ({
    isAuthenticated: state.isAuthenticated(),
    user: state.user,
  }));

  if (!isAuthenticated) {
    // This case should ideally be caught by a general ProtectedRoute if this route is nested
    // but as a safeguard:
    return <Navigate to="/login" replace />;
  }

  // Check if user object exists and if the role is 'MAESTRO'
  // The backend schema correction uses 'es_admin_maestro' boolean for the source of truth.
  // The 'rol' is derived or set consistently. The token also contains 'indicador_admin_maestro'.
  // For frontend, checking user.rol or a specific user.isMaster field is typical.
  // Let's assume 'rol' is reliable from the token/user object.
  // Or, if UserPublic has es_admin_maestro, use that: user?.es_admin_maestro === true
  
  if (user?.rol !== 'MAESTRO') {
    // Optional: Show a toast message or log for debugging
    toast.error("Acceso denegado. Se requiere rol de Administrador Maestro.");
    // Redirect to a general access page, like dashboard
    return <Navigate to="/dashboard" replace />;
  }

  return <Outlet />; // Renders the child component (AdminManagementPage)
};

export default MasterAdminRoute;
