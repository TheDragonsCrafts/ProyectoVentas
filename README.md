# Sistema de Gestión de Cafetería - Migración TypeScript

## Overview

Este proyecto es una migración y modernización de un sistema de gestión de inventario y ventas para una cafetería, originalmente desarrollado en Java con Ant y Swing. La nueva versión utiliza un stack tecnológico moderno con un backend en Node.js (Express, TypeScript, PostgreSQL) y un frontend en React (TypeScript, Vite, Tailwind CSS, ShadCN/UI).

El sistema permite gestionar administradores, inventario de productos, registrar ventas y ver historiales de ventas.

## Tech Stack

**Backend:**
*   Node.js (v20+)
*   Express.js
*   TypeScript
*   PostgreSQL
*   JWT (JSON Web Tokens) for authentication
*   bcryptjs for password hashing
*   `pg` for PostgreSQL connection

**Frontend:**
*   React (v18+)
*   TypeScript
*   Vite
*   Tailwind CSS
*   ShadCN/UI (component library)
*   Zustand (state management)
*   React Router DOM (routing)
*   Axios (HTTP client)
*   `lucide-react` (icons)
*   `date-fns` (date utility)
*   `lodash` (utilities, e.g., debounce)

**Monorepo Management:**
*   pnpm workspaces

## Features

*   **Autenticación:**
    *   Registro de administradores.
    *   Login con JWT.
    *   Sesión persistente en el frontend.
*   **Gestión de Administradores (solo para rol MAESTRO):**
    *   CRUD completo para cuentas de administrador.
    *   Distinción entre roles 'MAESTRO' y 'ESTANDAR'.
    *   Control de unicidad para el administrador MAESTRO principal.
    *   Activación/desactivación de cuentas.
*   **Gestión de Productos (Inventario):**
    *   CRUD completo para productos.
    *   IDs de producto definidos por el usuario (con chequeo de unicidad).
    *   Campos: Nombre, descripción, precio, categoría, cantidad (stock), fecha de caducidad (opcional), imagen URL (opcional), estado activo/inactivo.
    *   Búsqueda y filtrado de productos en la UI.
    *   Paginación.
*   **Registro de Ventas:**
    *   Interfaz para seleccionar productos y cantidades.
    *   Validación de stock y fecha de caducidad en tiempo real al agregar al carrito.
    *   Cálculo de subtotales y total general.
    *   Registro de la venta en la base de datos (con detalles) dentro de una transacción.
    *   Actualización (decremento) del stock de productos.
*   **Historial de Ventas:**
    *   Visualización de ventas pasadas con paginación.
    *   Filtros por vendedor (administrador) y rango de fechas.
    *   Vista detallada de cada venta (productos, cantidades, precios al momento de la venta, subtotales).
*   **UI Moderna y Responsiva:**
    *   Interfaz construida con React, TypeScript y ShadCN/UI.
    *   Diseño responsivo adaptado a diferentes tamaños de pantalla.
    *   Notificaciones (toasts) para feedback al usuario.

## Prerequisites

*   Node.js (v20.x o superior recomendado)
*   pnpm (v8.x o v9.x recomendado - la v10.10.0 fue usada en desarrollo)
*   PostgreSQL server (v12+ recomendado para columnas generadas, aunque el schema final usa `CASE WHEN` compatible con versiones anteriores)

## Setup and Installation

1.  **Clonar el Repositorio:**
    ```bash
    git clone <repository-url>
    cd <repository-name>
    ```

2.  **Database Setup:**
    *   Asegúrate de tener un servidor PostgreSQL corriendo.
    *   Crea una base de datos (ej. `cafeteria_db`).
    *   Ejecuta el script `backend/db/schema.sql` para crear las tablas y tipos necesarios.
        ```bash
        psql -U tu_usuario -d cafeteria_db -a -f backend/db/schema.sql
        ```
    *   Crea un archivo `.env` en el directorio `backend` basado en `backend/.env.example`. Configura tus credenciales de base de datos y un secreto para JWT.
        ```env
        # backend/.env
        DATABASE_URL="postgresql://USER:PASSWORD@HOST:PORT/DATABASE_NAME?schema=public"
        JWT_SECRET="tu_jwt_secret_aqui_muy_seguro"
        PORT=3000 # Puerto para el backend
        ```

3.  **Instalar Dependencias:**
    Desde la raíz del monorepo:
    ```bash
    pnpm install
    ```

## Running the Application

1.  **Backend:**
    Desde la raíz del monorepo:
    ```bash
    pnpm --filter backend dev
    ```
    El backend se ejecutará (por defecto) en `http://localhost:3000`.

2.  **Frontend:**
    Desde la raíz del monorepo:
    ```bash
    pnpm --filter frontend dev
    ```
    El frontend se ejecutará (por defecto) en `http://localhost:5173`.

## Project Structure

*   `/backend`: API desarrollada con Node.js, Express y TypeScript.
    *   `db/schema.sql`: Script DDL para la base de datos.
    *   `src/`: Código fuente del backend.
*   `/frontend`: Aplicación cliente desarrollada con React, Vite y TypeScript.
    *   `src/`: Código fuente del frontend.
    *   `components/ui/`: Componentes de ShadCN/UI.
*   `pnpm-workspace.yaml`: Define los workspaces del monorepo.

## Building for Production (Opcional)

1.  **Backend:**
    ```bash
    pnpm --filter backend build
    # Luego correr con: pnpm --filter backend start
    ```

2.  **Frontend:**
    ```bash
    pnpm --filter frontend build
    # Los archivos estáticos se generarán en frontend/dist
    ```

## Further Development / TODOs

*   Implementación de subida y gestión de imágenes para productos.
*   Reportes de ventas más avanzados (gráficos, exportaciones).
*   Pruebas unitarias y de integración para backend y frontend.
*   Manejo de roles y permisos más granular si es necesario (más allá de MAESTRO/ESTANDAR).
*   Internacionalización (i18n) para la UI.
*   Mejoras en la paginación y filtros de las tablas.
*   Optimización de consultas de base de datos para reportes complejos.
*   WebSockets para notificaciones en tiempo real (ej. actualización de stock).
*   Considerar el uso de refresh tokens para una gestión de sesión más robusta.
