package entidades;

public record DetalleVenta(
        int id,
        int idVenta,
        int idProducto,
        int cantidad,
        double precioUnitario
) {}

