package entidades;

/**
 * Representa un ítem individual dentro de una Venta.
 * Contiene información sobre el producto vendido, cantidad y precio en el momento de la venta.
 */
public record DetalleVenta(
        int id, // ID del detalle de venta (PK)
        int idVenta, // FK a la tabla ventas
        int idProducto,
        int cantidad,
        double precioUnitario
) {}

