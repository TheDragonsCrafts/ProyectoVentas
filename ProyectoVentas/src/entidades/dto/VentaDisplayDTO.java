package entidades.dto;

import java.time.LocalDateTime;
import java.util.List;
// El import de DetalleVentaDisplayDTO no es necesario si está en el mismo paquete.
// import entidades.dto.DetalleVentaDisplayDTO; // Se puede omitir

/**
 * DTO para mostrar la información de una venta de forma completa en la UI.
 * Incluye datos del administrador, detalles de productos y estado de la venta.
 */
public record VentaDisplayDTO(
    int idVenta,
    LocalDateTime fechaVenta,
    double montoTotal,
    String nombreAdministrador,
    List<DetalleVentaDisplayDTO> detalles,
    String estado
) {}
