package entidades;

import java.time.LocalDate;

/**
 * Representa un producto en el inventario.
 * Incluye detalles como precio, cantidad en stock y fecha de caducidad.
 */
public record Producto(
        int id,
        String nombre,
        String descripcion,
        double precio,
        int cantidad,
        LocalDate fechaCaducidad,
        boolean activo
) {}
