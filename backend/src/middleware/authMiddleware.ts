import { Request, Response, NextFunction } from 'express';
import jwt from 'jsonwebtoken';
import { RolAdministrador } from '../types/Administrador';

// JWT Secret: Should be in an environment variable
const JWT_SECRET = process.env.JWT_SECRET || 'your-very-secret-and-complex-key-dev';

export interface AuthenticatedRequest extends Request {
  user?: {
    id_administrador: number;
    correo_electronico: string;
    rol: RolAdministrador;
    indicador_admin_maestro: boolean;
  };
}

export const authenticateToken = (req: AuthenticatedRequest, res: Response, next: NextFunction) => {
  const authHeader = req.headers['authorization'];
  const token = authHeader && authHeader.split(' ')[1]; // Bearer TOKEN

  if (token == null) {
    return res.status(401).json({ message: 'No token proporcionado. Acceso denegado.' });
  }

  jwt.verify(token, JWT_SECRET, (err: any, user: any) => {
    if (err) {
      if (err.name === 'TokenExpiredError') {
        return res.status(401).json({ message: 'Token expirado.' });
      }
      if (err.name === 'JsonWebTokenError') {
        return res.status(403).json({ message: 'Token inválido.' });
      }
      return res.status(403).json({ message: 'Error al verificar el token.' });
    }
    req.user = user as AuthenticatedRequest['user'];
    next();
  });
};

export const isMasterAdmin = (req: AuthenticatedRequest, res: Response, next: NextFunction) => {
  if (!req.user) {
    // This should ideally not happen if authenticateToken is run first
    return res.status(401).json({ message: 'Usuario no autenticado.' });
  }
  
  // The 'indicador_admin_maestro' field in the token is used here.
  // This was set during login based on the database value.
  if (req.user.indicador_admin_maestro !== true) {
    return res.status(403).json({ message: 'Acceso denegado. Se requiere rol de Administrador Maestro.' });
  }
  next();
};

// Example of a role-based authorization middleware (optional, for general roles)
export const authorizeRoles = (...allowedRoles: RolAdministrador[]) => {
  return (req: AuthenticatedRequest, res: Response, next: NextFunction) => {
    if (!req.user || !req.user.rol) {
      return res.status(401).json({ message: 'Usuario no autenticado o rol no definido.' });
    }
    if (!allowedRoles.includes(req.user.rol)) {
      return res.status(403).json({ message: `Acceso denegado. Se requiere uno de los siguientes roles: ${allowedRoles.join(', ')}.` });
    }
    next();
  };
};
