import express from 'express';
import bcrypt from 'bcryptjs';
import { query } from '../db/connection';
import { Administrador, AdministradorCreateDTO, AdministradorPublic, RolAdministrador } from '../types/Administrador';
import { authenticateToken, isMasterAdmin, AuthenticatedRequest } from '../middleware/authMiddleware';

const router = express.Router();

// Apply authentication and master admin authorization to all routes in this router
router.use(authenticateToken);
router.use(isMasterAdmin);

// GET /api/admins - List All Administrators
router.get('/', async (req: AuthenticatedRequest, res: express.Response) => {
  try {
    // Selecting all fields that are part of AdministradorPublic or needed for it.
    // Explicitly list columns to avoid sending sensitive data like 'contrasena'.
    const result = await query<Omit<Administrador, 'contrasena'>>(
      `SELECT 
         id_administrador, 
         nombre_completo, 
         correo_electronico, 
         rol, 
         fecha_creacion, 
         fecha_actualizacion, 
         indicador_admin_maestro,
         activo -- Added activo
       FROM administradores`
      // Optional: Add "WHERE activo = TRUE" if listing only active admins by default
    );

    const adminsPublic: AdministradorPublic[] = result.rows.map(admin => ({
      id_administrador: admin.id_administrador,
      nombre_completo: admin.nombre_completo,
      correo_electronico: admin.correo_electronico,
      rol: admin.rol,
      indicador_admin_maestro: admin.indicador_admin_maestro,
      activo: admin.activo, // Added activo
    }));
    
    res.status(200).json(adminsPublic);
  } catch (error) {
    console.error('Error al listar administradores:', error);
    res.status(500).json({ message: 'Error interno del servidor al listar administradores.' });
  }
});

// GET /api/admins/:id - Get Administrator by ID
router.get('/:id', async (req: AuthenticatedRequest, res: express.Response) => {
  const { id } = req.params;

  if (isNaN(parseInt(id, 10))) {
    return res.status(400).json({ message: 'ID de administrador inválido.' });
  }

  try {
    const result = await query<Omit<Administrador, 'contrasena'>>(
      `SELECT 
         id_administrador, 
         nombre_completo, 
         correo_electronico, 
         rol, 
         fecha_creacion, 
         fecha_actualizacion, 
         indicador_admin_maestro,
         activo -- Added activo
       FROM administradores 
       WHERE id_administrador = $1`,
      [parseInt(id, 10)]
    );

    if (result.rows.length === 0) {
      return res.status(404).json({ message: 'Administrador no encontrado.' });
    }

    const admin = result.rows[0];
    const adminPublic: AdministradorPublic = {
      id_administrador: admin.id_administrador,
      nombre_completo: admin.nombre_completo,
      correo_electronico: admin.correo_electronico,
      rol: admin.rol,
      indicador_admin_maestro: admin.indicador_admin_maestro,
      activo: admin.activo, // Added activo
    };

    res.status(200).json(adminPublic);
  } catch (error) {
    console.error('Error al obtener administrador por ID:', error);
    res.status(500).json({ message: 'Error interno del servidor al obtener administrador.' });
  }
});

// POST /api/admins - Create New Administrator (Master Admin only)
router.post('/', async (req: AuthenticatedRequest, res: express.Response) => {
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

  // The master admin status is determined by 'rol' and 'correo_electronico' via a generated column in the DB.
  // If the DB has a unique constraint on `indicador_admin_maestro` being true (e.g. via a partial unique index),
  // an attempt to create a second admin that would also result in `indicador_admin_maestro = true`
  // (i.e., rol='superadmin' and correo_electronico LIKE '%@maestro.cafeteria.com') will fail. This is expected.

  try {
    const salt = await bcrypt.genSalt(10);
    const contrasenaHasheada = await bcrypt.hash(password_plana, salt);

    const insertQuery = `
      INSERT INTO administradores (nombre_completo, correo_electronico, contrasena, rol, fecha_actualizacion)
      VALUES ($1, $2, $3, $4, CURRENT_TIMESTAMP)
      RETURNING id_administrador, nombre_completo, correo_electronico, rol, fecha_creacion, fecha_actualizacion, indicador_admin_maestro;
    `;
    
    const result = await query<Administrador>(insertQuery, [
      nombre_completo,
      correo_electronico,
      contrasenaHasheada,
      rol as RolAdministrador,
    ]);

    const nuevoAdmin = result.rows[0];
    const adminPublic: AdministradorPublic = {
      id_administrador: nuevoAdmin.id_administrador,
      nombre_completo: nuevoAdmin.nombre_completo,
      correo_electronico: nuevoAdmin.correo_electronico,
      rol: nuevoAdmin.rol,
      indicador_admin_maestro: nuevoAdmin.indicador_admin_maestro,
    };

    res.status(201).json(adminPublic);

  } catch (error: any) {
    if (error.code === '23505') { // Unique violation
      if (error.constraint === 'administradores_correo_electronico_key') {
        return res.status(409).json({ message: 'El correo electrónico ya está registrado.' });
      }
      // This would be the name of the unique partial index for the master admin if it exists
      // Example: if (error.constraint === 'idx_unique_true_admin_maestro') 
      if (error.detail && error.detail.includes('indicador_admin_maestro')) { // Heuristic
         return res.status(409).json({ message: 'Ya existe un Administrador Maestro con el correo electrónico especificado o se viola una restricción de unicidad del rol maestro.' });
      }
    }
    console.error('Error al crear administrador:', error);
    res.status(500).json({ message: 'Error interno del servidor al crear administrador.' });
  }
});

// PUT /api/admins/:id - Update Administrator
router.put('/:id', async (req: AuthenticatedRequest, res: express.Response) => {
  const { id } = req.params;
  const adminIdToUpdate = parseInt(id, 10);

  if (isNaN(adminIdToUpdate)) {
    return res.status(400).json({ message: 'ID de administrador inválido.' });
  }

  const { 
    nombre_completo, 
    correo_electronico, 
    password_plana, 
    rol,
    activo 
  } = req.body as import('../types/AdministradorUpdateDTO').AdministradorUpdateDTO;

  if (!nombre_completo && !correo_electronico && !password_plana && !rol && typeof activo === 'undefined') {
    return res.status(400).json({ message: 'No se proporcionaron datos para actualizar.' });
  }

  try {
    // Check if admin exists
    const existingAdminResult = await query<Administrador>(
      'SELECT * FROM administradores WHERE id_administrador = $1',
      [adminIdToUpdate]
    );

    if (existingAdminResult.rows.length === 0) {
      return res.status(404).json({ message: 'Administrador no encontrado para actualizar.' });
    }
    const existingAdmin = existingAdminResult.rows[0];

    // Prevent master admin from deactivating or changing their own rol if they are the one identified by 'indicador_admin_maestro'
    // This check is based on the DB value of indicador_admin_maestro
    if (existingAdmin.indicador_admin_maestro === true) {
      if (typeof activo === 'boolean' && !activo) {
        return res.status(403).json({ message: 'El Administrador Maestro principal no puede ser desactivado.' });
      }
      if (rol && rol !== 'superadmin') {
        return res.status(403).json({ message: 'El Administrador Maestro principal no puede cambiar su rol a uno no-superadmin.'});
      }
    }
    
    // Build the update query dynamically
    const updateFields: string[] = [];
    const queryParams: any[] = [];
    let paramIndex = 1;

    if (nombre_completo) {
      updateFields.push(`nombre_completo = $${paramIndex++}`);
      queryParams.push(nombre_completo);
    }
    if (correo_electronico) {
      // If changing email of the master admin, it might affect their master status if the email is part of the generated column logic
      if (existingAdmin.indicador_admin_maestro === true && !correo_electronico.includes('@maestro.cafeteria.com')) {
         // This assumes the special email is part of the condition for indicador_admin_maestro
        return res.status(400).json({ message: "El correo electrónico del Administrador Maestro principal debe mantener el dominio '@maestro.cafeteria.com'." });
      }
      updateFields.push(`correo_electronico = $${paramIndex++}`);
      queryParams.push(correo_electronico);
    }
    if (password_plana) {
      const salt = await bcrypt.genSalt(10);
      const contrasenaHasheada = await bcrypt.hash(password_plana, salt);
      updateFields.push(`contrasena = $${paramIndex++}`);
      queryParams.push(contrasenaHasheada);
    }
    if (rol) {
      updateFields.push(`rol = $${paramIndex++}`);
      queryParams.push(rol as RolAdministrador);
    }
    if (typeof activo === 'boolean') {
      updateFields.push(`activo = $${paramIndex++}`);
      queryParams.push(activo);
    }

    if (updateFields.length === 0) {
      return res.status(400).json({ message: 'Ningún campo válido proporcionado para la actualización.' });
    }

    updateFields.push(`fecha_actualizacion = CURRENT_TIMESTAMP`);
    queryParams.push(adminIdToUpdate);

    const updateQuery = `
      UPDATE administradores
      SET ${updateFields.join(', ')}
      WHERE id_administrador = $${paramIndex}
      RETURNING id_administrador, nombre_completo, correo_electronico, rol, fecha_creacion, fecha_actualizacion, indicador_admin_maestro, activo;
    `;

    const result = await query<Administrador>(updateQuery, queryParams);

    if (result.rows.length === 0) {
      // Should not happen if initial check passed, but as a safeguard
      return res.status(404).json({ message: 'Administrador no encontrado después de la actualización.' });
    }
    
    const updatedAdmin = result.rows[0];
    const adminPublic: AdministradorPublic = {
      id_administrador: updatedAdmin.id_administrador,
      nombre_completo: updatedAdmin.nombre_completo,
      correo_electronico: updatedAdmin.correo_electronico,
      rol: updatedAdmin.rol,
      indicador_admin_maestro: updatedAdmin.indicador_admin_maestro,
      activo: updatedAdmin.activo,
    };

    res.status(200).json(adminPublic);

  } catch (error: any) {
    if (error.code === '23505') { // Unique violation
      if (error.constraint === 'administradores_correo_electronico_key') {
        return res.status(409).json({ message: 'El correo electrónico ya está en uso por otro administrador.' });
      }
      // Handle unique constraint violation for 'indicador_admin_maestro' if applicable
      if (error.detail && error.detail.includes('indicador_admin_maestro')) {
         return res.status(409).json({ message: 'Error de unicidad: No puede haber múltiples Administradores Maestros con el correo electrónico designado o se viola la restricción del rol maestro.' });
      }
    }
    console.error('Error al actualizar administrador:', error);
    res.status(500).json({ message: 'Error interno del servidor al actualizar administrador.' });
  }
});

// DELETE /api/admins/:id - Logically delete an Administrator
router.delete('/:id', async (req: AuthenticatedRequest, res: express.Response) => {
  const { id } = req.params;
  const adminIdToDelete = parseInt(id, 10);
  const currentUserId = req.user!.id_administrador; // From authenticateToken middleware

  if (isNaN(adminIdToDelete)) {
    return res.status(400).json({ message: 'ID de administrador inválido.' });
  }

  if (adminIdToDelete === currentUserId) {
    // More robust check: query the user to check their 'indicador_admin_maestro' status from DB
    const selfUser = await query<Administrador>('SELECT indicador_admin_maestro FROM administradores WHERE id_administrador = $1', [currentUserId]);
    if (selfUser.rows.length > 0 && selfUser.rows[0].indicador_admin_maestro === true) {
       return res.status(403).json({ message: 'El Administrador Maestro principal no puede eliminarse a sí mismo.' });
    }
    // Allow non-master admin to delete themselves if that's desired behavior,
    // or add specific check if req.user.id_administrador === adminIdToDelete for any user.
    // For now, let's assume deleting oneself (if not the master admin) is generally disallowed by this admin management endpoint.
     return res.status(403).json({ message: 'Los administradores no pueden eliminarse a sí mismos a través de esta función. Contacte a otro Administrador Maestro.' });
  }
  
  try {
    // Check if the admin to be deleted is the true master admin (based on DB)
    // This is an additional safeguard.
    const adminToModify = await query<Administrador>(
      'SELECT indicador_admin_maestro, activo FROM administradores WHERE id_administrador = $1', 
      [adminIdToDelete]
    );

    if (adminToModify.rows.length === 0) {
      return res.status(404).json({ message: 'Administrador no encontrado para eliminar.' });
    }

    if (adminToModify.rows[0].indicador_admin_maestro === true) {
      return res.status(403).json({ message: 'El Administrador Maestro principal no puede ser eliminado por esta vía.' });
    }
    
    // If admin is already inactive, perhaps return a specific message or just confirm the state
    if (adminToModify.rows[0].activo === false) {
        return res.status(200).json({ message: 'El administrador ya está inactivo.' });
    }

    // Perform logical delete by setting activo = false
    // Also update fecha_actualizacion
    const result = await query(
      'UPDATE administradores SET activo = $1, fecha_actualizacion = CURRENT_TIMESTAMP WHERE id_administrador = $2 RETURNING id_administrador;',
      [false, adminIdToDelete]
    );

    if (result.rowCount === 0) {
      // Should be caught by the check above, but as a safeguard
      return res.status(404).json({ message: 'Administrador no encontrado, no se pudo eliminar.' });
    }

    res.status(200).json({ message: 'Administrador desactivado (eliminado lógicamente) correctamente.' });

  } catch (error: any) {
    // Foreign key violations are less likely with logical delete.
    // If it were a physical delete, error.code '23503' (foreign_key_violation) would be relevant.
    console.error('Error al eliminar administrador:', error);
    res.status(500).json({ message: 'Error interno del servidor al eliminar administrador.' });
  }
});

export default router;
