export interface DetalleVenta {
  id_detalle_venta: number;
  id_venta: number;
  id_producto: number;
  cantidad: number;
  precio_unitario: number; // DECIMAL(10, 2) - Price at the time of sale
  subtotal: number; // DECIMAL(10, 2) - Calculated: cantidad * precio_unitario
}

// For creating a new sale, we'd typically take product ID and quantity.
// Precio unitario would be fetched from the Product table at the time of sale creation.
// Subtotal would be calculated.
export interface DetalleVentaCreateDTO {
  id_producto: number;
  cantidad: number;
  // precio_unitario and subtotal will be determined by the backend
  // when the sale is processed, by looking up the current product price.
}
