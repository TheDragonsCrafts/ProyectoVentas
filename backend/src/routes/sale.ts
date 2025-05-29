import express, { Request, Response } from 'express';
import pool, { query } from '../db/connection'; 
import { authenticateToken, AuthenticatedRequest } from '../middleware/authMiddleware';
// Removed MetodoPago from import, Venta and DetalleVenta should be fine
import { Venta, DetalleVenta, VentaCreateDTO, VentaConDetalles } from '../types/Venta'; 
import { Producto } from '../types/Producto';

const router = express.Router();

// Apply authentication to all routes in this router
router.use(authenticateToken);

// Part 1: Create New Sale
router.post('/', async (req: AuthenticatedRequest, res: Response) => {
  // Removed metodo_pago from destructuring
  const { detalles } = req.body as VentaCreateDTO; 
  const id_administrador_venta = req.user!.id_administrador;

  // Removed validation for metodo_pago

  if (!detalles || !Array.isArray(detalles) || detalles.length === 0) {
    return res.status(400).json({ message: "La lista de detalles de venta no puede estar vacía." });
  }

  for (const detalle of detalles) {
    if (typeof detalle.id_producto !== 'number' || typeof detalle.cantidad !== 'number' || detalle.cantidad <= 0) {
      return res.status(400).json({ message: "Cada detalle debe tener un id_producto (número) y cantidad (número positivo)." });
    }
  }

  const client = await pool.connect();
  try {
    await client.query('BEGIN');

    let monto_total_calculado = 0;
    const detallesParaInsertar: Array<Omit<DetalleVenta, 'id_detalle_venta' | 'id_venta'>> = [];

    for (const item of detalles) {
      const productoResult = await client.query<Producto>(
        'SELECT id_producto, nombre_producto, precio, stock, activo, fecha_caducidad FROM productos WHERE id_producto = $1 FOR UPDATE', // Lock product row
        [item.id_producto]
      );

      if (productoResult.rows.length === 0) {
        throw new Error(`Producto con ID ${item.id_producto} no encontrado.`);
      }
      const producto = productoResult.rows[0];

      if (!producto.activo) {
        throw new Error(`Producto '${producto.nombre_producto}' (ID: ${item.id_producto}) no está activo.`);
      }

      if (producto.fecha_caducidad && new Date(producto.fecha_caducidad) < new Date()) {
        throw new Error(`Producto '${producto.nombre_producto}' (ID: ${item.id_producto}) ha expirado.`);
      }

      if (producto.stock === null || producto.stock === undefined || item.cantidad > producto.stock) {
        throw new Error(`Stock insuficiente para '${producto.nombre_producto}' (ID: ${item.id_producto}). Solicitado: ${item.cantidad}, Disponible: ${producto.stock}.`);
      }

      const precioEnVenta = producto.precio;
      const subtotalDetalle = precioEnVenta * item.cantidad;
      monto_total_calculado += subtotalDetalle;

      detallesParaInsertar.push({
        id_producto: item.id_producto,
        cantidad: item.cantidad,
        precio_unitario: precioEnVenta,
        subtotal: subtotalDetalle,
      });

      // Decrement stock
      await client.query(
        'UPDATE productos SET stock = stock - $1, fecha_actualizacion = CURRENT_TIMESTAMP WHERE id_producto = $2',
        [item.cantidad, item.id_producto]
      );
    }

    // Insert Venta
    const ventaResult = await client.query<Omit<Venta, 'detalles'>>( // Omit<Venta, 'detalles'> might be Venta if Venta itself doesn't have detalles
      `INSERT INTO ventas (id_administrador_venta, total_venta, fecha_venta, estado)
       VALUES ($1, $2, CURRENT_TIMESTAMP, 'COMPLETADA')
       RETURNING id_venta, fecha_venta, total_venta, id_administrador_venta, estado`, // Removed metodo_pago from RETURNING
      [id_administrador_venta, monto_total_calculado] // Removed metodo_pago from params
    );
    const nuevaVenta = ventaResult.rows[0];
    const id_venta_generada = nuevaVenta.id_venta;

    // Insert DetalleVenta
    const detallesInsertados: DetalleVenta[] = [];
    for (const detalleData of detallesParaInsertar) {
      const detalleResult = await client.query<DetalleVenta>(
        `INSERT INTO detalles_venta (id_venta, id_producto, cantidad, precio_unitario, subtotal)
         VALUES ($1, $2, $3, $4, $5)
         RETURNING id_detalle_venta, id_venta, id_producto, cantidad, precio_unitario, subtotal`,
        [id_venta_generada, detalleData.id_producto, detalleData.cantidad, detalleData.precio_unitario, detalleData.subtotal]
      );
      detallesInsertados.push(detalleResult.rows[0]);
    }

    await client.query('COMMIT');
    
    const ventaCompletaResponse: VentaConDetalles = {
        ...nuevaVenta, // spread nuevaVenta which no longer has metodo_pago
        detalles: detallesInsertados
    };

    res.status(201).json(ventaCompletaResponse);

  } catch (error: any) {
    await client.query('ROLLBACK');
    console.error('Error al crear venta:', error.message); // Log the specific error message
    // Check if error message is one of our custom messages, otherwise generic
    if (error.message.includes('Producto con ID') || error.message.includes('no está activo') || error.message.includes('ha expirado') || error.message.includes('Stock insuficiente')) {
        res.status(400).json({ message: error.message });
    } else {
        res.status(500).json({ message: 'Error interno del servidor al crear la venta.' });
    }
  } finally {
    client.release();
  }
});


// Part 2: Sales Reporting
router.get('/', async (req: AuthenticatedRequest, res: Response) => {
  const { adminId, fechaInicio, fechaFin, limit = 10, offset = 0 } = req.query;

  const numLimit = parseInt(limit as string, 10);
  const numOffset = parseInt(offset as string, 10);

  if (isNaN(numLimit) || isNaN(numOffset) || numLimit < 0 || numOffset < 0) {
    return res.status(400).json({ message: "Parámetros 'limit' y 'offset' deben ser números no negativos." });
  }

  // Validate adminId if provided
  let parsedAdminId: number | undefined;
  if (adminId) {
    parsedAdminId = parseInt(adminId as string, 10);
    if (isNaN(parsedAdminId)) {
      return res.status(400).json({ message: "adminId debe ser un número." });
    }
  }
  
  // Validate dates if provided
  let startDate: Date | undefined;
  let endDate: Date | undefined;
  if (fechaInicio) {
    startDate = new Date(fechaInicio as string);
    if (isNaN(startDate.getTime())) {
      return res.status(400).json({ message: "Formato de fechaInicio inválido. Use YYYY-MM-DD." });
    }
  }
  if (fechaFin) {
    endDate = new Date(fechaFin as string);
    if (isNaN(endDate.getTime())) {
      return res.status(400).json({ message: "Formato de fechaFin inválido. Use YYYY-MM-DD." });
    }
    // Set to end of day for inclusive range
    endDate.setHours(23, 59, 59, 999);
  }


  try {
    let salesQuery = `
      SELECT 
        v.id_venta, 
        v.fecha_venta, 
        v.total_venta, 
        -- v.metodo_pago, -- REMOVED
        v.id_administrador_venta, 
        v.estado,
        a.nombre_completo as nombre_administrador_venta
      FROM ventas v
      LEFT JOIN administradores a ON v.id_administrador_venta = a.id_administrador
    `;
    const conditions: string[] = [];
    const queryParams: any[] = [];
    let paramIndex = 1;

    if (parsedAdminId) {
      conditions.push(`v.id_administrador_venta = $${paramIndex++}`);
      queryParams.push(parsedAdminId);
    }
    if (startDate) {
      conditions.push(`v.fecha_venta >= $${paramIndex++}`);
      queryParams.push(startDate);
    }
    if (endDate) {
      conditions.push(`v.fecha_venta <= $${paramIndex++}`);
      queryParams.push(endDate);
    }

    if (conditions.length > 0) {
      salesQuery += ` WHERE ${conditions.join(' AND ')}`;
    }
    salesQuery += ` ORDER BY v.fecha_venta DESC LIMIT $${paramIndex++} OFFSET $${paramIndex++}`;
    queryParams.push(numLimit, numOffset);

    const salesResult = await query(salesQuery, queryParams);
    const ventasParaDisplay: import('../types/VentaDisplayDTO').VentaDisplayDTO[] = [];

    for (const ventaRow of salesResult.rows) {
      const detallesResult = await query(
        `SELECT 
           dv.id_producto,
           p.nombre_producto,
           dv.cantidad,
           dv.precio_unitario as precio_unitario_venta, 
           dv.subtotal
         FROM detalles_venta dv
         JOIN productos p ON dv.id_producto = p.id_producto
         WHERE dv.id_venta = $1`,
        [ventaRow.id_venta]
      );

      ventasParaDisplay.push({
        id_venta: ventaRow.id_venta,
        fecha_venta: ventaRow.fecha_venta,
        total_venta: ventaRow.total_venta,
        // metodo_pago: ventaRow.metodo_pago, // REMOVED
        id_administrador_venta: ventaRow.id_administrador_venta,
        nombre_administrador_venta: ventaRow.nombre_administrador_venta,
        estado: ventaRow.estado,
        detalles: detallesResult.rows,
      });
    }
    
    // Get total count for pagination
    let countQuery = `SELECT COUNT(*) FROM ventas v`;
    if (conditions.length > 0) {
       const countConditions = conditions.map((cond, idx) => {
         // Adjust param index for count query which doesn't have limit/offset
         return cond.replace(`$${idx + 1}`, `$${idx + 1}`);
       });
      countQuery += ` WHERE ${countConditions.join(' AND ')}`;
    }
    const countResult = await query(countQuery, queryParams.slice(0, conditions.length));


    res.status(200).json({
      data: ventasParaDisplay,
      total: parseInt(countResult.rows[0].count, 10),
      limit: numLimit,
      offset: numOffset
    });

  } catch (error: any) {
    console.error('Error al obtener historial de ventas:', error);
    res.status(500).json({ message: 'Error interno del servidor al obtener historial de ventas.' });
  }
});

export default router;
