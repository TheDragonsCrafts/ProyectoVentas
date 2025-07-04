package entidades.dto;

/**
 * DTO para mostrar la información de un detalle de venta de forma amigable en la UI.
 * Combina datos del producto con los detalles de la venta.
 */
public record DetalleVentaDisplayDTO(
    String nombreProducto,
    int cantidad,
    double precioEnVenta
) {}
