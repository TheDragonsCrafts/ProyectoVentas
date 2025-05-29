import { EstadoVenta } from './Venta'; // Removed MetodoPago from import
import { DetalleVentaDisplayDTO } from './DetalleVentaDisplayDTO';

export interface VentaDisplayDTO {
  id_venta: number;
  fecha_venta: Date;
  total_venta: number;
  // metodo_pago: MetodoPago; // REMOVED
  id_administrador_venta?: number | null; 
  nombre_administrador_venta?: string | null; 
  estado: EstadoVenta; 
  detalles: DetalleVentaDisplayDTO[];
}
