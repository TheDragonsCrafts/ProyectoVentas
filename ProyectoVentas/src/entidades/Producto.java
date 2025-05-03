package entidades;

import java.time.LocalDate;

public record Producto(
        int id,
        String nombre,
        String descripcion,
        double precio,
        int cantidad,
        LocalDate fechaCaducidad,
        boolean activo
) {}
