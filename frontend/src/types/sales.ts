import { Producto } from './Producto';

export interface SaleItemLine {
  producto: Producto; // Full product object
  cantidad: number;
  subtotal: number;
}

// SaleCreateDTO is already in Venta.ts (as VentaCreateDTO)
// VentaConDetalles is also in Venta.ts
// This file is primarily for frontend-specific view models for the sales page if needed.
// For now, SaleItemLine is the main one.
