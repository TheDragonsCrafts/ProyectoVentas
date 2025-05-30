import express from 'express';
import { query } from '../db/connection';
import { Producto, ProductoCreateDTO, ProductoUpdateDTO, CategoriaProducto } from '../types/Producto'; // Added ProductoUpdateDTO
import { authenticateToken, AuthenticatedRequest } from '../middleware/authMiddleware';

const router = express.Router();

// Apply authentication to all routes in this router
router.use(authenticateToken);

// GET /api/products - List All Products (with filtering and search)
router.get('/', async (req: AuthenticatedRequest, res: express.Response) => {
  const { activo, nombre, limit = 10, offset = 0 } = req.query; // 'activo' is now the standard

  let activoFilter: boolean | undefined; // Renamed from disponibilidadFilter
  if (typeof activo === 'string') {
    if (activo.toLowerCase() === 'true') {
      activoFilter = true;
    } else if (activo.toLowerCase() === 'false') {
      activoFilter = false;
    } else {
      return res.status(400).json({ message: "Valor inválido para 'activo'. Use 'true' o 'false'." });
    }
  } else {
    // Default to active products
    activoFilter = true;
  }

  const nombreSearch = typeof nombre === 'string' ? nombre : undefined;
  const numLimit = parseInt(limit as string, 10);
  const numOffset = parseInt(offset as string, 10);

  if (isNaN(numLimit) || isNaN(numOffset) || numLimit < 0 || numOffset < 0) {
    return res.status(400).json({ message: "Parámetros 'limit' y 'offset' deben ser números no negativos." });
  }

  try {
    let selectQuery = `
      SELECT 
        id_producto, 
        nombre_producto, 
        descripcion, 
        precio, 
        categoria, 
        activo, // Renamed from disponibilidad
        stock, 
        imagen_url, 
        fecha_caducidad, 
        fecha_creacion, 
        fecha_actualizacion
        // id_administrador_creador removed
      FROM productos
    `;
    const conditions: string[] = [];
    const queryParams: any[] = [];
    let paramIndex = 1;

    if (activoFilter !== undefined) {
      conditions.push(`activo = $${paramIndex++}`); // Renamed field
      queryParams.push(activoFilter);
    }

    if (nombreSearch) {
      conditions.push(`nombre_producto ILIKE $${paramIndex++}`);
      queryParams.push(`%${nombreSearch}%`);
    }

    if (conditions.length > 0) {
      selectQuery += ` WHERE ${conditions.join(' AND ')}`;
    }

    selectQuery += ` ORDER BY id_producto LIMIT $${paramIndex++} OFFSET $${paramIndex++}`;
    queryParams.push(numLimit, numOffset);
    
    const result = await query<Producto>(selectQuery, queryParams);
    
    // Also get total count for pagination purposes
    let countQuery = `SELECT COUNT(*) FROM productos`;
    if (conditions.length > 0) {
      // Rebuild conditions for count query, using same params up to that point
      const countConditions = conditions.map((cond, idx) => cond.replace(`$${idx + 1}`, `$${idx + 1}`));
      countQuery += ` WHERE ${countConditions.join(' AND ')}`;
    }
    const countResult = await query(countQuery, queryParams.slice(0, conditions.length)); // Use only relevant params for count

    res.status(200).json({
      data: result.rows,
      total: parseInt(countResult.rows[0].count, 10),
      limit: numLimit,
      offset: numOffset
    });

  } catch (error) {
    console.error('Error al listar productos:', error);
    res.status(500).json({ message: 'Error interno del servidor al listar productos.' });
  }
});

// POST /api/products - Create New Product
router.post('/', async (req: AuthenticatedRequest, res: express.Response) => {
  const {
    id_producto, // Now required
    nombre_producto,
    descripcion,
    precio,
    categoria,
    stock, 
    fecha_caducidad, 
    activo = true // Default to true if not provided
  } = req.body as ProductoCreateDTO;
  
  // id_administrador_creador removed

  // Basic validation
  if (id_producto === undefined || !nombre_producto || precio === undefined || categoria === undefined || stock === undefined) {
    return res.status(400).json({ 
      message: "Campos obligatorios faltantes: id_producto, nombre_producto, precio, categoria, stock (cantidad)." 
    });
  }
  if (typeof id_producto !== 'number' || id_producto <= 0) {
    return res.status(400).json({ message: "id_producto debe ser un número entero positivo." });
  }
  if (typeof precio !== 'number' || precio < 0) {
    return res.status(400).json({ message: "Precio debe ser un número no negativo." });
  }
  if (typeof stock !== 'number' || stock < 0) {
    return res.status(400).json({ message: "Stock (cantidad) debe ser un número no negativo." });
  }
  // TODO: Validate categoria against CategoriaProducto ENUM values from types/Producto.ts

  let fechaCaducidadDate: Date | null = null;
  if (fecha_caducidad) {
    fechaCaducidadDate = new Date(fecha_caducidad);
    if (isNaN(fechaCaducidadDate.getTime())) {
      return res.status(400).json({ message: "Formato de fecha_caducidad inválido." });
    }
  }

  try {
    // Check if id_producto already exists (now that it's user-defined and PK)
    const idExistsResult = await query('SELECT 1 FROM productos WHERE id_producto = $1', [id_producto]);
    if (idExistsResult.rows.length > 0) {
      return res.status(409).json({ message: `El ID de producto ${id_producto} ya existe.` });
    }

    const insertQuery = `
      INSERT INTO productos (
        id_producto, nombre_producto, descripcion, precio, categoria, activo, 
        stock, fecha_caducidad, fecha_actualizacion
      )
      VALUES ($1, $2, $3, $4, $5, $6, $7, $8, CURRENT_TIMESTAMP)
      RETURNING *; 
    `;
    
    const result = await query<Producto>(insertQuery, [
      id_producto, // User-defined ID
      nombre_producto,
      descripcion,
      precio,
      categoria,
      activo, // Renamed from disponibilidad
      stock,
      fechaCaducidadDate // Use the Date object or null
      // id_administrador_creador removed
    ]);

    res.status(201).json(result.rows[0]);

  } catch (error: any) {
    if (error.code === '23505') { // unique_violation (for id_producto if somehow missed by check)
      return res.status(409).json({ message: `El ID de producto ${id_producto} ya existe (error de inserción).` });
    }
    if (error.code === '23514') { // check_violation (e.g. precio < 0)
        return res.status(400).json({ message: `Error de validación de datos: ${error.detail || error.message}`})
    }
    console.error('Error al crear producto:', error);
    res.status(500).json({ message: 'Error interno del servidor al crear producto.' });
  }
});

// PUT /api/products/:id - Update Product
router.put('/:id', async (req: AuthenticatedRequest, res: express.Response) => {
  const pathProductId = parseInt(req.params.id, 10);
  if (isNaN(pathProductId)) {
    return res.status(400).json({ message: 'ID de producto en URL inválido.' });
  }

  const {
    nombre_producto,
    descripcion,
    precio,
    categoria,
    stock, 
    fecha_caducidad,
    activo, // Renamed from disponibilidad
  } = req.body as ProductoUpdateDTO;

  // id_administrador_actualizador removed (no longer tracking who updated)

  // Basic validation for provided fields
  if (precio !== undefined && (typeof precio !== 'number' || precio < 0)) {
    return res.status(400).json({ message: "Precio debe ser un número no negativo." });
  }
  if (stock !== undefined && (typeof stock !== 'number' || stock < 0)) {
    return res.status(400).json({ message: "Stock (cantidad) debe ser un número no negativo." });
  }
  // TODO: Validate categoria if provided

  let fechaCaducidadDate: Date | null | undefined = undefined; // undefined means not provided
  if (fecha_caducidad !== undefined) {
      if (fecha_caducidad === null) {
          fechaCaducidadDate = null;
      } else {
          fechaCaducidadDate = new Date(fecha_caducidad);
          if (isNaN(fechaCaducidadDate.getTime())) {
            return res.status(400).json({ message: "Formato de fecha_caducidad inválido." });
          }
      }
  }
  
  try {
    // Check if product exists first
    const existingProductResult = await query<Producto>(
      'SELECT id_producto FROM productos WHERE id_producto = $1',
      [pathProductId]
    );
    if (existingProductResult.rows.length === 0) {
      return res.status(404).json({ message: 'Producto no encontrado para actualizar.' });
    }

    const updateFields: string[] = [];
    const queryParams: any[] = [];
    let paramIndex = 1;

    if (nombre_producto !== undefined) {
      updateFields.push(`nombre_producto = $${paramIndex++}`);
      queryParams.push(nombre_producto);
    }
    if (descripcion !== undefined) {
      updateFields.push(`descripcion = $${paramIndex++}`);
      queryParams.push(descripcion);
    }
    if (precio !== undefined) {
      updateFields.push(`precio = $${paramIndex++}`);
      queryParams.push(precio);
    }
    if (categoria !== undefined) {
      updateFields.push(`categoria = $${paramIndex++}`);
      queryParams.push(categoria);
    }
    if (stock !== undefined) {
      updateFields.push(`stock = $${paramIndex++}`);
      queryParams.push(stock);
    }
    if (fechaCaducidadDate !== undefined) { // Handles null or valid date
      updateFields.push(`fecha_caducidad = $${paramIndex++}`);
      queryParams.push(fechaCaducidadDate);
    }
    if (activo !== undefined) {
      updateFields.push(`activo = $${paramIndex++}`); // Renamed from disponibilidad
      queryParams.push(activo);
    }

    if (updateFields.length === 0) {
      return res.status(400).json({ message: 'No se proporcionaron campos para actualizar.' });
    }

    // Always update fecha_actualizacion
    updateFields.push(`fecha_actualizacion = CURRENT_TIMESTAMP`);
    // id_administrador_creador/actualizador removed
    
    queryParams.push(pathProductId); // For the WHERE clause

    const updateQuery = `
      UPDATE productos
      SET ${updateFields.join(', ')}
      WHERE id_producto = $${paramIndex}
      RETURNING *;
    `;

    const result = await query<Producto>(updateQuery, queryParams);

    if (result.rows.length === 0) {
      return res.status(404).json({ message: 'Producto no encontrado después de intentar actualizar (inesperado).' });
    }

    res.status(200).json(result.rows[0]);

  } catch (error: any) {
    if (error.code === '23514') { // check_violation
        return res.status(400).json({ message: `Error de validación de datos: ${error.detail || error.message}`})
    }
    console.error('Error al actualizar producto:', error);
    res.status(500).json({ message: 'Error interno del servidor al actualizar producto.' });
  }
});

// DELETE /api/products/:id - Logically delete a product
router.delete('/:id', async (req: AuthenticatedRequest, res: express.Response) => {
  const productId = parseInt(req.params.id, 10);
  if (isNaN(productId)) {
    return res.status(400).json({ message: 'ID de producto inválido.' });
  }

  // id_administrador_actualizador removed

  try {
    // Check if product exists
    const productCheck = await query<Producto>(
      'SELECT activo FROM productos WHERE id_producto = $1', // check 'activo' column
      [productId]
    );

    if (productCheck.rows.length === 0) {
      return res.status(404).json({ message: 'Producto no encontrado.' });
    }

    if (productCheck.rows[0].activo === false) { // check 'activo'
      // Fetch the full product details to return
      const currentProductState = await query<Producto>('SELECT * FROM productos WHERE id_producto = $1', [productId]);
      return res.status(200).json({ message: 'El producto ya está inactivo.', product: currentProductState.rows[0] });
    }

    // Perform logical delete
    const updateQuery = `
      UPDATE productos
      SET 
        activo = FALSE, -- Renamed from disponibilidad
        fecha_actualizacion = CURRENT_TIMESTAMP
        -- id_administrador_creador removed
      WHERE id_producto = $1
      RETURNING *;
    `;
    const result = await query<Producto>(updateQuery, [productId]);

    if (result.rows.length === 0) {
      return res.status(404).json({ message: 'Producto no encontrado durante la eliminación (inesperado).' });
    }
    
    res.status(200).json({ message: 'Producto desactivado (eliminado lógicamente) correctamente.', product: result.rows[0] });

  } catch (error) {
    console.error('Error al eliminar lógicamente producto:', error);
    res.status(500).json({ message: 'Error interno del servidor al eliminar producto.' });
  }
});


// GET /api/products/:id - Get Product by ID (only if active)
router.get('/:id', async (req: AuthenticatedRequest, res: express.Response) => {
  const { id } = req.params;
});

export default router;
