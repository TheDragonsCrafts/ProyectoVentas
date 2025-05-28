package entidades.dto;

public record DetalleVentaDisplayDTO(
    String nombreProducto,
    int cantidad,
    double precioEnVenta
) {}
