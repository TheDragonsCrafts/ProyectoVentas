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
  rol: UserRole; 
}

// DTO for creating an Admin by a Master Admin (corresponds to backend's /api/admins POST)
export interface AdminCreateDTO {
  nombre_completo: string;
  correo_electronico: string;
  password_plana: string;
  rol: UserRole;
  // 'activo' typically defaults to true on backend or isn't set at creation by this DTO
}

// DTO for updating an Admin by a Master Admin (corresponds to backend's /api/admins/:id PUT)
export interface AdminUpdateDTO {
  nombre_completo?: string;
  correo_electronico?: string;
  password_plana?: string; // Optional: only if changing
  rol?: UserRole;
  activo?: boolean;
}

export interface AuthResponse {
  token: string;
  user: UserPublic;
}
