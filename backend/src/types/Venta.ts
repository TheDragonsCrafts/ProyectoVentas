import { DetalleVenta, DetalleVentaCreateDTO } from './DetalleVenta';

// export type MetodoPago = 'efectivo' | 'tarjeta_credito' | 'tarjeta_debito' | 'transferencia' | 'otro'; // REMOVED
export type EstadoVenta = 'COMPLETADA' | 'PENDIENTE' | 'CANCELADA';

export interface Venta {
  id_venta: number;
  fecha_venta: Date;
  total_venta: number; 
  // metodo_pago: MetodoPago; // REMOVED
  id_administrador_venta?: number | null; 
  estado: EstadoVenta;
  // Detalles are often fetched/handled separately or as part of a more complex type
}

// DTO for creating a new sale
export interface VentaCreateDTO {
  // metodo_pago: MetodoPago; // REMOVED
  detalles: DetalleVentaCreateDTO[];
  // id_administrador_venta is from token
  // total_venta is calculated by backend
  // estado defaults in DB or can be set by backend logic if needed initially
}

// DTO for the response of a created sale, including its details
export interface VentaConDetalles extends Venta {
  detalles: DetalleVenta[];
}

// Re-exporting for use in other modules if they import from '.../types/Venta'
export { DetalleVenta, DetalleVentaCreateDTO };
