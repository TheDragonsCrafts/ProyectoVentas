-- phpMyAdmin SQL Dump
-- versión 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3306
-- Tiempo de generación: 04-04-2025 a las 20:40:22
-- Versión del servidor: 8.2.0
-- Versión de PHP: 8.2.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4;

--
-- Base de datos: `cafeteria`
--

-- --------------------------------------------------------
-- Estructura de tabla para la tabla `administradores`
-- --------------------------------------------------------
DROP TABLE IF EXISTS `administradores`;
CREATE TABLE IF NOT EXISTS `administradores` (
  `id_administrador` int NOT NULL AUTO_INCREMENT,
  `nombre_usuario` varchar(50) NOT NULL,
  `hash_contraseña` varchar(255) NOT NULL,
  `nombre_completo` varchar(100) NOT NULL,
  `correo_electrónico` varchar(100) DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `es_admin_maestro` tinyint(1) NOT NULL DEFAULT '0',
  `indicador_admin_maestro` int GENERATED ALWAYS AS (IF(`es_admin_maestro`,1,NULL)) STORED COMMENT 'Columna auxiliar para restricción UNIQUE',
  `creado_en` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha y hora de creación',
  `actualizado_en` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha y hora de última actualización',
  PRIMARY KEY (`id_administrador`),
  UNIQUE KEY `uk_nombre_usuario` (`nombre_usuario`),
  UNIQUE KEY `uk_correo_electrónico` (`correo_electrónico`),
  UNIQUE KEY `uk_admin_maestro` (`indicador_admin_maestro`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------
-- Estructura de tabla para la tabla `productos`
-- --------------------------------------------------------
DROP TABLE IF EXISTS `productos`;
CREATE TABLE IF NOT EXISTS `productos` (
  `id_producto` int NOT NULL AUTO_INCREMENT COMMENT 'Identificador único del producto',
  `nombre` varchar(150) NOT NULL COMMENT 'Nombre del producto',
  `descripción` text COMMENT 'Descripción detallada (opcional)',
  `precio` decimal(10,2) NOT NULL COMMENT 'Precio de venta unitario',
  `cantidad` int NOT NULL DEFAULT '0' COMMENT 'Cantidad actual en inventario',
  `fecha_caducidad` date DEFAULT NULL COMMENT 'Fecha de caducidad (NULL si no aplica)',
  `activo` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'Disponibilidad (1=Activo, 0=Descontinuado)',
  `creado_en` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha y hora de creación',
  `actualizado_en` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha y hora de última actualización',
  PRIMARY KEY (`id_producto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------
-- Estructura de tabla para la tabla `ventas`
-- --------------------------------------------------------
DROP TABLE IF EXISTS `ventas`;
CREATE TABLE IF NOT EXISTS `ventas` (
  `id_venta` int NOT NULL AUTO_INCREMENT COMMENT 'Identificador único de la venta',
  `fecha_venta` datetime NOT NULL COMMENT 'Fecha y hora de la venta',
  `monto_total` decimal(12,2) NOT NULL COMMENT 'Monto total de la venta',
  `estado` enum('COMPLETADA','CANCELADA') NOT NULL DEFAULT 'COMPLETADA' COMMENT 'Estado de la venta',
  `id_administrador` int NOT NULL COMMENT 'ID del administrador que realizó la venta (FK)',
  `creado_en` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha y hora de creación del registro',
  `actualizado_en` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha y hora de última actualización',
  PRIMARY KEY (`id_venta`),
  KEY `fk_ventas_administradores` (`id_administrador`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------
-- Estructura de tabla para la tabla `detalles_venta`
-- (id_producto ahora admite NULL y FK con ON DELETE SET NULL)
-- --------------------------------------------------------
DROP TABLE IF EXISTS `detalles_venta`;
CREATE TABLE IF NOT EXISTS `detalles_venta` (
  `id_detalle_venta` int NOT NULL AUTO_INCREMENT COMMENT 'ID único de la línea de detalle',
  `id_venta` int NOT NULL COMMENT 'ID de la venta a la que pertenece (FK)',
  `id_producto` int NULL COMMENT 'ID del producto vendido (FK) — ahora admite NULL',
  `cantidad` int NOT NULL COMMENT 'Cantidad de este producto vendido',
  `precio_en_venta` decimal(10,2) NOT NULL COMMENT 'Precio unitario al momento de la venta',
  PRIMARY KEY (`id_detalle_venta`),
  UNIQUE KEY `uk_venta_producto` (`id_venta`,`id_producto`),
  KEY `fk_detallesventa_productos` (`id_producto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------
-- Restricciones para la tabla `ventas`
-- --------------------------------------------------------
ALTER TABLE `ventas`
  ADD CONSTRAINT `fk_ventas_administradores`
    FOREIGN KEY (`id_administrador`)
    REFERENCES `administradores` (`id_administrador`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- --------------------------------------------------------
-- Restricciones para la tabla `detalles_venta`
-- - FK a ventas con ON DELETE CASCADE
-- - FK a productos con ON DELETE SET NULL
-- --------------------------------------------------------
ALTER TABLE `detalles_venta`
  ADD CONSTRAINT `fk_detallesventa_ventas`
    FOREIGN KEY (`id_venta`)
    REFERENCES `ventas` (`id_venta`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_detallesventa_productos`
    FOREIGN KEY (`id_producto`)
    REFERENCES `productos` (`id_producto`)
    ON DELETE SET NULL
    ON UPDATE CASCADE;

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

