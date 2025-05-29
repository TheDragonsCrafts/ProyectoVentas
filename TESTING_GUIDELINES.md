# Testing Guidelines

This document provides guidelines for manually testing the key functionalities of the Cafeteria Management System.

## Backend API Tests (using Postman, Insomnia, or similar)

It's recommended to test each API endpoint. Ensure requests are made with appropriate authentication tokens (JWT) where required.

### Authentication (`/api/auth`)
- **User Registration (`POST /register`):**
    - Register a new 'ESTANDAR' admin.
    - Register a new 'MAESTRO' admin (ensure only one can be effectively the "master" if that's the intended DB constraint based on `es_admin_maestro`).
    - Attempt to register with an existing `correo_electronico`. Expect conflict error.
    - Test with missing required fields. Expect validation error.
- **User Login (`POST /login`):**
    - Login with correct credentials. Expect success, JWT token, and user data.
    - Login with incorrect password. Expect unauthorized error.
    - Login with non-existent email. Expect unauthorized error.
    - (If applicable) Login with an inactive admin account. Expect unauthorized error.
- **Token Handling:**
    - Access protected routes without a token. Expect 401 Unauthorized.
    - Access protected routes with an invalid/expired token. Expect 401/403.

### Administrator Management (`/api/admins`) - Requires MAESTRO role token
- **List All Admins (`GET /`):** Verify list is returned.
- **Get Admin by ID (`GET /:id`):** Test with valid and invalid IDs.
- **Create New Admin (`POST /`):**
    - Create 'ESTANDAR' and 'MAESTRO' admins.
    - Test unique email constraint.
    - Test creation of second MAESTRO (should fail if DB constraint `UNIQUE (indicador_admin_maestro)` is active).
- **Update Admin (`PUT /:id`):**
    - Update name, email, role, active status.
    - Update password (provide `password_plana`).
    - Attempt to change MAESTRO role or deactivate the main MAESTRO admin (expect specific errors or prevention).
- **Delete Admin (`DELETE /:id`):**
    - Logically delete (deactivate) an admin. Verify `activo` status changes.
    - Attempt to delete the main MAESTRO admin (expect prevention).

### Product Management (`/api/products`) - Requires authenticated admin token
- **List Products (`GET /`):**
    - Test without filters (defaults to active products).
    - Filter by `nombre` (partial, case-insensitive).
    - Filter by `activo=true` and `activo=false`.
    - Test pagination (`limit`, `offset`).
- **Get Product by ID (`GET /:id`):** Only returns active products. Test with active, inactive, and non-existent IDs.
- **Create Product (`POST /`):**
    - Provide all required fields including user-defined `id_producto`.
    - Test `id_producto` uniqueness constraint (expect 409 if ID exists).
    - Test field validations (e.g., negative price/stock).
- **Update Product (`PUT /:id`):** Update various fields.
- **Delete Product (`DELETE /:id`):** Logically delete/reactivate. Verify `activo` status changes and product is returned.

### Sales (`/api/sales`) - Requires authenticated admin token
- **Create New Sale (`POST /`):**
    - Valid sale with multiple items. Verify stock deduction in `productos` table.
    - Attempt sale with insufficient stock for an item. Expect error, no changes to stock or sales tables.
    - Attempt sale with an inactive product. Expect error.
    *   Attempt sale with an expired product. Expect error.
    - Verify `total_venta` is calculated correctly.
    - Verify `id_administrador_venta` is recorded.
    - Verify `detalles_venta` records are created correctly with price at time of sale.
    - (Difficult to test externally) Verify transaction atomicity: if any part fails (e.g., stock update for one item), the entire sale (header and all details, all stock updates) should be rolled back.
- **Get Sales History (`GET /`):**
    - Test without filters.
    - Filter by `adminId`.
    - Filter by `fechaInicio` and `fechaFin`.
    - Combine filters.
    - Test pagination.
    - Verify response structure includes admin name and sale details with product names.

## Frontend E2E Tests (Manual Click-Through)

### General
- **Responsiveness:** Check major pages (Login, Dashboard, Admin Mgmt, Product Mgmt, Sales, Sales History) on various screen sizes (desktop, tablet, mobile).
- **Navigation:** Test all sidebar links and button navigations.
- **Error Handling:** Ensure API errors are gracefully handled and user-friendly messages or toasts are shown.

### Authentication
- **Login:**
    - Successful login redirects to dashboard.
    - Failed login shows an error message.
- **Logout:**
    - Logs out user and redirects to login page.
    - Clears user session from storage.
- **Session Persistence:** After login, refresh the page. User should remain logged in.
- **Protected Routes:** Attempt to access protected routes (e.g., `/dashboard`) directly without being logged in. Expect redirection to `/login`.

### Admin Management (as MAESTRO Admin)
- **View List:** Navigate to "Gestión Admins". Verify admins are listed with correct details.
- **Create Admin:**
    - Open "Crear Nuevo Administrador" dialog.
    - Fill form with valid data (ESTANDAR and MAESTRO roles). Submit. Verify success toast and list updates.
    - Test form validations (required fields, email format, password match).
    - Attempt to create admin with existing email. Expect error toast.
- **Edit Admin:**
    - Click "Editar" for an admin. Dialog opens with pre-filled data.
    - Change details (name, email, role, active status). Save. Verify success and list updates.
    - Change password. Save. Verify login with new password is possible.
    - Attempt to make a regular admin a MAESTRO if one already exists (if this is constrained).
- **Delete Admin (Deactivate):**
    - Click "Eliminar" for an ESTANDAR admin. Confirm in dialog. Verify admin becomes inactive (or is visually marked) and success toast.
    - Attempt to delete the MAESTRO admin logged in (if it's the only one or the special one). UI might prevent or backend error shown.
- **Access Control:**
    - Log in as an 'ESTANDAR' admin. The "Gestión Admins" link in sidebar should not be visible.
    - Attempt to navigate to `/admin/users` directly as 'ESTANDAR' admin. Expect redirection or "Access Denied" message.

### Product Management (Inventario - as any Admin)
- **View List (`/products`):**
    - Verify products are listed.
    - Test search by name.
    - Test filter by "Mostrar solo activos" switch.
    - Test pagination.
- **Create Product (`/products/new`):**
    - Fill form with valid data (including user-defined ID, all fields like category, optional expiry date). Submit. Verify success toast and navigation back to list.
    - Test form validations (required ID, name, positive numbers for price/stock, valid date).
    - Attempt to create product with an existing ID. Expect error toast.
- **Edit Product (`/products/edit/:id`):**
    - Navigate by clicking "Editar". Form should be pre-filled.
    *   Modify fields. Save. Verify success and list updates.
- **Delete Product (Deactivate/Activate):**
    - Click "Desactivar" (or "Activar") for a product. Confirm in dialog. Verify status change in list and success toast.

### Sales UI (`/sales/new`)
- **Product Selection:**
    - Search for products. Verify combobox shows results with stock/price.
    - Select a product.
- **Add to Cart:**
    - Enter quantity. Click "Agregar Producto".
    - Item appears in "Productos en Venta Actual" table with correct price/subtotal.
    - Total amount updates.
    - Test adding same product again (should update quantity or show message).
    - Test adding with quantity exceeding stock. Expect error toast.
    - Test adding expired product. Expect error toast.
- **Manage Cart:**
    - Edit quantity of an item in the cart. Test stock validation. Total updates.
    - Remove an item from the cart. Total updates.
- **Submit Sale:**
    - Click "Registrar Venta" with items in cart. Expect success toast with Sale ID. Cart clears.
    - Click "Registrar Venta" with empty cart. Expect error.
- **Cancel Sale:**
    - Add items to cart. Click "Cancelar Venta". Confirm. Cart clears.

### Sales History (`/sales/history`)
- **View List:** Verify sales are listed with correct details (ID, date, vendor, total, status).
- **Filtering:**
    - Filter by vendor (select admin from dropdown).
    - Filter by date range using date pickers.
    - Combine filters.
    - Click "Limpiar Filtros".
- **Pagination:** Test navigating through pages of sales.
- **View Details:**
    - Click "Ver Detalles" for a sale. Modal appears.
    *   Verify all sale details (header info, items table with product name, quantity, price at sale, subtotal) are correct.

This list covers the main functionalities. Additional edge cases and usability checks should also be performed.`TESTING_GUIDELINES.md` created.

**Step 3: Create Main Project `README.md`**
