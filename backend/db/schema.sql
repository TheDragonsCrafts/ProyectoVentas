-- Drop types if they exist, to change their values. This is suitable for a script that defines schema from scratch.
DROP TYPE IF EXISTS rol_administrador CASCADE; 
CREATE TYPE rol_administrador AS ENUM ('ESTANDAR', 'MAESTRO');

DROP TYPE IF EXISTS categoria_producto CASCADE;
CREATE TYPE categoria_producto AS ENUM ('bebida_caliente', 'bebida_fria', 'panaderia', 'pasteleria', 'snacks', 'otros');

-- DROP TYPE IF EXISTS metodo_pago CASCADE; -- REMOVED metodo_pago type
-- CREATE TYPE metodo_pago AS ENUM ('efectivo', 'tarjeta_credito', 'tarjeta_debito', 'transferencia', 'otro'); -- REMOVED

DROP TYPE IF EXISTS estado_venta CASCADE; -- ADDED estado_venta type
CREATE TYPE estado_venta AS ENUM ('COMPLETADA', 'PENDIENTE', 'CANCELADA'); -- ADDED


-- Tabla de Administradores
CREATE TABLE administradores (
    id_administrador INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    nombre_completo VARCHAR(150) NOT NULL,
    correo_electronico VARCHAR(150) UNIQUE NOT NULL, 
    contrasena VARCHAR(255) NOT NULL, 
    
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    es_admin_maestro BOOLEAN NOT NULL DEFAULT FALSE, 
    rol rol_administrador NOT NULL, 
    
    indicador_admin_maestro INTEGER GENERATED ALWAYS AS (CASE WHEN es_admin_maestro THEN 1 ELSE NULL END) STORED,
    
    fecha_creacion TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    
    UNIQUE (indicador_admin_maestro) 
);

-- Tabla de Productos
CREATE TABLE productos (
    id_producto INT PRIMARY KEY, 
    nombre_producto VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10, 2) NOT NULL CHECK (precio > 0),
    categoria categoria_producto NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE, 
    stock INT DEFAULT 0 CHECK (stock >= 0), 
    imagen_url VARCHAR(255),
    fecha_caducidad DATE NULL, 
    fecha_creacion TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Ventas
CREATE TABLE ventas (
    id_venta INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    fecha_venta TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    total_venta DECIMAL(10, 2) NOT NULL CHECK (total_venta >= 0),
    -- metodo_pago metodo_pago NOT NULL, -- REMOVED
    id_administrador_venta INT,
    estado estado_venta NOT NULL DEFAULT 'COMPLETADA', -- ADDED
    FOREIGN KEY (id_administrador_venta) REFERENCES administradores(id_administrador) ON DELETE SET NULL,
    CONSTRAINT chk_total_venta_positivo CHECK (total_venta >= 0)
);

-- Tabla de Detalles de Venta
CREATE TABLE detalles_venta (
    id_detalle_venta INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    id_venta INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    precio_unitario DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL, 
    FOREIGN KEY (id_venta) REFERENCES ventas(id_venta) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON DELETE RESTRICT,
    CONSTRAINT chk_cantidad_positiva CHECK (cantidad > 0),
    CONSTRAINT chk_subtotal_calculado CHECK (subtotal = cantidad * precio_unitario)
);

-- Índices
CREATE INDEX idx_admin_correo ON administradores(correo_electronico);
CREATE INDEX idx_admin_activo ON administradores(activo);

CREATE INDEX idx_productos_categoria ON productos(categoria);
CREATE INDEX idx_productos_activo ON productos(activo); 

CREATE INDEX idx_ventas_fecha ON ventas(fecha_venta);
CREATE INDEX idx_ventas_estado ON ventas(estado); -- ADDED index for estado
CREATE INDEX idx_detalles_venta_venta ON detalles_venta(id_venta);
CREATE INDEX idx_detalles_venta_producto ON detalles_venta(id_producto);

-- Note on nombre_usuario vs correo_electronico:
-- The task summary mentioned 'nombre_usuario VARCHAR(100) NOT NULL UNIQUE'.
-- This schema uses 'correo_electronico' as the unique identifier for login, similar to previous versions.
-- If 'nombre_usuario' is a separate concept (like a display username different from login email), it would need to be added.
-- For now, 'correo_electronico' serves as the unique login name.
-- The 'contrasena' column name is used instead of 'hash_contrasena' for consistency with previous schema versions by this worker.
-- VARCHAR lengths for nombre_completo and correo_electronico in administradores set to 150 as per summary.
-- id_administrador changed from INT GENERATED ALWAYS AS IDENTITY to SERIAL to match summary (then changed back, GENERATED is more modern).
-- The summary used SERIAL for id_administrador, but INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY is modern PostgreSQL and was used before. Sticking to GENERATED ALWAYS.
