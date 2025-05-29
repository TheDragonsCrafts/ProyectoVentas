export type RolAdministrador = 'superadmin' | 'admin' | 'gerente';

export interface Administrador {
  id_administrador: number;
  nombre_completo: string;
  correo_electronico: string; // Used as username
  contrasena: string; // Hashed password
  rol: RolAdministrador;
  fecha_creacion: Date;
  fecha_actualizacion: Date;
  indicador_admin_maestro?: boolean; // Generated column, reflects if (rol === 'superadmin' && correo_electronico === 'maestro@maestro.cafeteria.com')
  activo?: boolean; // Added for logical delete support, assuming schema will be updated
}

// For registration, we might take a plain password and a flag for master admin
export interface AdministradorCreateDTO {
  nombre_completo: string;
  correo_electronico: string; // Will be the username
  password_plana: string; // Plain text password from user
  rol: RolAdministrador; // e.g., 'admin', 'superadmin'
  // The 'indicador_admin_maestro' is a generated column in the DB
  // Its value depends on the 'rol' and 'correo_electronico'.
  // Forcing a specific email for the true master admin is handled by the DB.
}

// For login
export interface AdministradorLoginDTO {
  correo_electronico: string; // Username
  password_plana: string; // Plain text password
}

// For JWT payload and returning to client (sensitive info removed)
export interface AdministradorPublic {
  id_administrador: number;
  nombre_completo: string;
  correo_electronico: string;
  rol: RolAdministrador;
  indicador_admin_maestro?: boolean;
  activo?: boolean; // Added for logical delete support
}
