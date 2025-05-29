export type CategoriaProducto = 'bebida_caliente' | 'bebida_fria' | 'panaderia' | 'pasteleria' | 'snacks' | 'otros';

export interface Producto {
  id_producto: number;
  nombre_producto: string;
  descripcion?: string | null; // TEXT can be null
  precio: number; 
  categoria: CategoriaProducto;
  activo: boolean; // Renamed from disponibilidad
  stock?: number | null; 
  imagen_url?: string | null;
  fecha_caducidad?: Date | null; 
  fecha_creacion: Date;
  fecha_actualizacion: Date;
  // id_administrador_creador removed
}

// DTO for creating a product
export interface ProductoCreateDTO {
  id_producto: number; // Now required, user-defined
  nombre_producto: string; 
  descripcion?: string | null;
  precio: number;
  categoria: CategoriaProducto;
  activo?: boolean; // Defaults to true if not provided in API logic
  stock?: number | null; 
  imagen_url?: string | null;
  fecha_caducidad?: string | null; 
}

// DTO for updating a product
export interface ProductoUpdateDTO {
  nombre_producto?: string; 
  descripcion?: string | null;
  precio?: number;
  categoria?: CategoriaProducto;
  activo?: boolean; 
  stock?: number | null; 
  imagen_url?: string | null;
  fecha_caducidad?: string | null; 
}
