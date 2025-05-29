import express, { Request, Response } from 'express';
import bcrypt from 'bcryptjs';
import jwt from 'jsonwebtoken';
import { query } from '../db/connection';
import { Administrador, AdministradorCreateDTO, AdministradorPublic, RolAdministrador } from '../types/Administrador';

const router = express.Router();

// JWT Secret: Should be in an environment variable
const JWT_SECRET = process.env.JWT_SECRET || 'your-very-secret-and-complex-key-dev';

// POST /api/auth/register
router.post('/register', async (req: Request, res: Response) => {
  const { 
    nombre_completo, 
    correo_electronico, 
    password_plana, 
    rol 
  } = req.body as AdministradorCreateDTO;

  if (!nombre_completo || !correo_electronico || !password_plana || !rol) {
    return res.status(400).json({ message: 'Todos los campos son obligatorios: nombre_completo, correo_electronico, password_plana, rol.' });
  }

  if (!['superadmin', 'admin', 'gerente'].includes(rol)) {
    return res.status(400).json({ message: 'Rol inválido. Valores permitidos: superadmin, admin, gerente.' });
  }

  // Specific check for the unique "master" admin based on email and rol
  // The DB schema has a generated column `indicador_admin_maestro` and a CHECK constraint
  // to enforce that only one superadmin with a specific email pattern can be the "maestro".
  // If rol is 'superadmin' and email is the master email, it's the master.
  // If rol is 'superadmin' and email is NOT the master email, it's a regular superadmin.
  // The DB will enforce that no other superadmin can take the master email.

  try {
    // Hash the password
    const salt = await bcrypt.genSalt(10);
    const contrasenaHasheada = await bcrypt.hash(password_plana, salt);

    const insertQuery = `
      INSERT INTO administradores (nombre_completo, correo_electronico, contrasena, rol)
      VALUES ($1, $2, $3, $4)
      RETURNING id_administrador, nombre_completo, correo_electronico, rol, fecha_creacion, fecha_actualizacion, indicador_admin_maestro;
    `;
    
    const result = await query<Administrador>(insertQuery, [
      nombre_completo,
      correo_electronico,
      contrasenaHasheada,
      rol as RolAdministrador,
    ]);

    const nuevoAdmin = result.rows[0];

    // Prepare public data for response
    const adminPublic: AdministradorPublic = {
      id_administrador: nuevoAdmin.id_administrador,
      nombre_completo: nuevoAdmin.nombre_completo,
      correo_electronico: nuevoAdmin.correo_electronico,
      rol: nuevoAdmin.rol,
      indicador_admin_maestro: nuevoAdmin.indicador_admin_maestro,
    };

    res.status(201).json(adminPublic);

  } catch (error: any) {
    // Handle potential errors, e.g., unique constraint violation
    if (error.code === '23505') { // Unique violation in PostgreSQL
      if (error.constraint === 'administradores_correo_electronico_key') {
        return res.status(409).json({ message: 'El correo electrónico ya está registrado.' });
      }
      // Potentially other unique constraints if `nombre_usuario` was a separate unique field
    }
    console.error('Error en el registro de administrador:', error);
    res.status(500).json({ message: 'Error al registrar el administrador.' });
  }
});

// POST /api/auth/login
router.post('/login', async (req: Request, res: Response) => {
  const { correo_electronico, password_plana } = req.body;

  if (!correo_electronico || !password_plana) {
    return res.status(400).json({ message: 'Correo electrónico y contraseña son obligatorios.' });
  }

  try {
    const result = await query<Administrador>(
      'SELECT * FROM administradores WHERE correo_electronico = $1',
      [correo_electronico]
    );

    if (result.rows.length === 0) {
      return res.status(401).json({ message: 'Credenciales inválidas.' }); // User not found
    }

    const admin = result.rows[0];

    // TODO: Add a check for admin account status (e.g., is_active) if such a field exists

    const passwordMatch = await bcrypt.compare(password_plana, admin.contrasena);
    if (!passwordMatch) {
      return res.status(401).json({ message: 'Credenciales inválidas.' }); // Password incorrect
    }

    // Prepare public data and JWT payload
    const adminPublic: AdministradorPublic = {
      id_administrador: admin.id_administrador,
      nombre_completo: admin.nombre_completo,
      correo_electronico: admin.correo_electronico,
      rol: admin.rol,
      // indicador_admin_maestro is a generated column and should be fetched correctly
      // If it's not directly on 'admin' from the SELECT *, a specific query might be needed
      // or ensure the SELECT * gets it. The previous registration query does return it.
      indicador_admin_maestro: admin.indicador_admin_maestro 
    };
    
    // Ensure indicador_admin_maestro is part of the token payload
    const tokenPayload = {
      id_administrador: admin.id_administrador,
      correo_electronico: admin.correo_electronico, // Using email as username in token
      rol: admin.rol,
      indicador_admin_maestro: admin.indicador_admin_maestro === true // Ensure it's boolean
    };

    const token = jwt.sign(tokenPayload, JWT_SECRET, { expiresIn: '1h' }); // Token expires in 1 hour

    res.status(200).json({
      token,
      user: adminPublic,
    });

  } catch (error) {
    console.error('Error en el login:', error);
    res.status(500).json({ message: 'Error al intentar iniciar sesión.' });
  }
});

export default router;
