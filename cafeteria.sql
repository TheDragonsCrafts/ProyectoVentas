-- phpMyAdmin SQL Dump
-- version 5.2.1
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
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `cafeteria`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `administrators`
--

DROP TABLE IF EXISTS `administrators`;
CREATE TABLE IF NOT EXISTS `administrators` (
  `admin_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `is_master_admin` tinyint(1) NOT NULL DEFAULT '0',
  `master_admin_flag` int GENERATED ALWAYS AS (if(`is_master_admin`,1,NULL)) STORED COMMENT 'Columna auxiliar para restricción unique',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `uk_master_admin` (`master_admin_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `products`
--

DROP TABLE IF EXISTS `products`;
CREATE TABLE IF NOT EXISTS `products` (
  `product_id` int NOT NULL AUTO_INCREMENT COMMENT 'Identificador único del producto',
  `name` varchar(150) NOT NULL COMMENT 'Nombre del producto',
  `description` text COMMENT 'Descripción detallada (opcional)',
  `price` decimal(10,2) NOT NULL COMMENT 'Precio de venta unitario',
  `stock` int NOT NULL DEFAULT '0' COMMENT 'Cantidad actual en inventario',
  `expiry_date` date DEFAULT NULL COMMENT 'Fecha de caducidad (NULL si no aplica)',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'Disponibilidad del producto (TRUE=Activo, FALSE=Descontinuado)',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha y hora de creación',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha y hora de última actualización',
  PRIMARY KEY (`product_id`)
) ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sales`
--

DROP TABLE IF EXISTS `sales`;
CREATE TABLE IF NOT EXISTS `sales` (
  `sale_id` int NOT NULL AUTO_INCREMENT COMMENT 'Identificador único de la venta',
  `sale_date` datetime NOT NULL COMMENT 'Fecha y hora exacta de la venta',
  `total_amount` decimal(12,2) NOT NULL COMMENT 'Monto total de la venta',
  `status` enum('COMPLETED','CANCELLED') NOT NULL DEFAULT 'COMPLETED' COMMENT 'Estado de la venta',
  `admin_id` int NOT NULL COMMENT 'ID del administrador que realizó la venta (FK)',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha y hora de creación del registro',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha y hora de última actualización',
  PRIMARY KEY (`sale_id`),
  KEY `fk_sales_administrators` (`admin_id`)
) ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sale_details`
--

DROP TABLE IF EXISTS `sale_details`;
CREATE TABLE IF NOT EXISTS `sale_details` (
  `sale_detail_id` int NOT NULL AUTO_INCREMENT COMMENT 'ID único de la línea de detalle',
  `sale_id` int NOT NULL COMMENT 'ID de la venta a la que pertenece (FK)',
  `product_id` int NOT NULL COMMENT 'ID del producto vendido (FK)',
  `quantity` int NOT NULL COMMENT 'Cantidad de este producto vendido',
  `price_at_sale` decimal(10,2) NOT NULL COMMENT 'Precio unitario al momento de la venta',
  PRIMARY KEY (`sale_detail_id`),
  UNIQUE KEY `uk_sale_product` (`sale_id`,`product_id`) COMMENT 'Evita duplicar el mismo producto en una sola venta',
  KEY `fk_saledetails_products` (`product_id`)
) ;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `sales`
--
ALTER TABLE `sales`
  ADD CONSTRAINT `fk_sales_administrators` FOREIGN KEY (`admin_id`) REFERENCES `administrators` (`admin_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Filtros para la tabla `sale_details`
--
ALTER TABLE `sale_details`
  ADD CONSTRAINT `fk_saledetails_products` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE RESTRICT,
  ADD CONSTRAINT `fk_saledetails_sales` FOREIGN KEY (`sale_id`) REFERENCES `sales` (`sale_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
