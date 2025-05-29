// Corresponds to backend's RolAdministrador after schema correction
export type UserRole = 'ESTANDAR' | 'MAESTRO';

// Corresponds to backend's AdministradorPublic interface
export interface UserPublic {
  id_administrador: number;
  nombre_completo: string;
  correo_electronico: string;
  rol: UserRole;
  activo?: boolean; // Assuming 'activo' status is returned
  // indicador_admin_maestro is used in token, might be part of user object from backend
  // For UI purposes, 'rol' is likely more directly useful than indicador_admin_maestro
  // Or, have a specific field like 'isMaster' derived from 'rol' or 'indicador_admin_maestro'
  es_admin_maestro?: boolean; // This is the direct flag from DB
}

export interface LoginDTO {
  correo_electronico: string;
  password_plana: string; // 'password_plana' matches backend DTO expectation
}

// Matches backend's AdministradorCreateDTO for /api/auth/register
export interface RegisterDTO {
  nombre_completo: string;
  correo_electronico: string;
  password_plana: string;
  rol: UserRole; // Backend /api/auth/register expects 'rol'
}

export interface AuthResponse {
  token: string;
  user: UserPublic;
}
