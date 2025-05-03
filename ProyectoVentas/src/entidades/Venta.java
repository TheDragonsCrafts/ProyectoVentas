package entidades;

import java.time.LocalDateTime;
import java.util.List;

public record Venta(
        int id,
        LocalDateTime fecha,
        double montoTotal,
        int idAdministrador,
        List<DetalleVenta> detalles
) {}
