package entidades.dto;

import java.time.LocalDateTime;
import java.util.List;
import entidades.dto.DetalleVentaDisplayDTO;

public record VentaDisplayDTO(
    int idVenta,
    LocalDateTime fechaVenta,
    double montoTotal,
    String nombreAdministrador,
    List<DetalleVentaDisplayDTO> detalles,
    String estado
) {}
