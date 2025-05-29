import { RolAdministrador } from './Administrador';

export interface AdministradorUpdateDTO {
  nombre_completo?: string;
  correo_electronico?: string;
  password_plana?: string; // Plain text password, only if changing
  rol?: RolAdministrador;
  activo?: boolean; // For logical delete/activation
}
