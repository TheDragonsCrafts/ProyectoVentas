package entidades;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Representa una transacción de venta.
 * Contiene información sobre la fecha, monto total, el administrador que la realizó y los detalles de los productos vendidos.
 */
public record Venta(
        int id,
        LocalDateTime fecha,
        double montoTotal,
        int idAdministrador,
        List<DetalleVenta> detalles
) {}
