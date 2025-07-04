package datos;

import entidades.DetalleVenta;
import entidades.Venta;
import seguridad.ConexionBD;
import entidades.dto.DetalleVentaDisplayDTO;
import entidades.dto.VentaDisplayDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class VentaDatos {

    /**
     * Registra una nueva venta y sus detalles en la base de datos.
     * Realiza la operación como una transacción.
     * @param v La Venta a registrar, incluyendo sus detalles.
     * @return El ID autogenerado de la venta registrada.
     * @throws SQLException Si ocurre un error de acceso a la base de datos o la transacción falla.
     */
    public int registrarVenta(Venta v) throws SQLException {
        String sqlVenta = """
            INSERT INTO ventas (fecha_venta, monto_total, id_administrador)
            VALUES (?,?,?)
            """;
        String sqlDetalle = """
            INSERT INTO detalles_venta (id_venta, id_producto, cantidad, precio_en_venta)
            VALUES (?,?,?,?)
            """;

        Connection cx = ConexionBD.obtener();
        try {
            cx.setAutoCommit(false);

            int idVenta;
            try (PreparedStatement ps = cx.prepareStatement(
                    sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                ps.setTimestamp(1, Timestamp.valueOf(v.fecha()));
                ps.setDouble(2, v.montoTotal());
                ps.setInt(3, v.idAdministrador());
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                keys.next();
                idVenta = keys.getInt(1);
            }

            try (PreparedStatement ps = cx.prepareStatement(sqlDetalle)) {
                for (DetalleVenta d : v.detalles()) {
                    ps.setInt(1, idVenta);
                    ps.setInt(2, d.idProducto());
                    ps.setInt(3, d.cantidad());
                    ps.setDouble(4, d.precioUnitario());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            cx.commit();
            return idVenta;
        } catch (SQLException e) {
            cx.rollback();
            throw e;
        } finally {
            cx.setAutoCommit(true);
            // La conexión es manejada por ConexionBD, no se cierra aquí.
        }
    }

    /**
     * Obtiene una lista de ventas para mostrar en la UI, con filtros opcionales.
     * Incluye detalles de cada venta (nombre de producto, cantidad, precio).
     * @param nombreVendedor Filtro por nombre del vendedor (parcial, case-insensitive). Null o vacío para no filtrar.
     * @param fechaInicio Filtro por fecha de inicio (inclusiva). Null para no filtrar.
     * @param fechaFin Filtro por fecha de fin (inclusiva). Null para no filtrar.
     * @return Una lista de VentaDisplayDTO.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    public List<VentaDisplayDTO> obtenerVentasParaDisplay(String nombreVendedor, LocalDate fechaInicio, LocalDate fechaFin) throws SQLException {
        List<VentaDisplayDTO> ventasDisplay = new ArrayList<>();
        Map<Integer, List<DetalleVentaDisplayDTO>> detallesPorVentaId = new HashMap<>();
        List<Object> params = new ArrayList<>();

        // 1. Obtener todos los detalles de venta relevantes y mapearlos por id_venta.
        // Esta estrategia carga más detalles de los necesarios si hay muchos productos,
        // pero simplifica el ensamblaje posterior.
        // Una optimización sería obtener los ID de venta filtrados primero, luego sus detalles.
        String sqlDetalles = """
            SELECT dv.id_venta, p.nombre AS nombre_producto, dv.cantidad, dv.precio_en_venta
            FROM detalles_venta dv
            JOIN productos p ON dv.id_producto = p.id_producto
            """;
        // Si se quisiera optimizar, se podría añadir un JOIN con ventas y aplicar filtros aquí también.

        Connection cx = ConexionBD.obtener(); // Usar la misma conexión para todas las operaciones
        try {
            try (Statement stmtDetalles = cx.createStatement();
                 ResultSet rsDetalles = stmtDetalles.executeQuery(sqlDetalles)) {
                while (rsDetalles.next()) {
                    int idVenta = rsDetalles.getInt("id_venta");
                    String nombreProducto = rsDetalles.getString("nombre_producto");
                    int cantidad = rsDetalles.getInt("cantidad");
                    double precioEnVenta = rsDetalles.getDouble("precio_en_venta");
                    DetalleVentaDisplayDTO detalleDto = new DetalleVentaDisplayDTO(nombreProducto, cantidad, precioEnVenta);
                    detallesPorVentaId.computeIfAbsent(idVenta, k -> new ArrayList<>()).add(detalleDto);
                }
            }

            // 2. Construir y ejecutar la consulta de ventas con filtros dinámicos.
            StringBuilder sqlVentasBuilder = new StringBuilder(
                """
                SELECT v.id_venta, v.fecha_venta, v.monto_total, v.estado, a.nombre_completo AS nombre_administrador
                FROM ventas v
                JOIN administradores a ON v.id_administrador = a.id_administrador
                """
            );

            List<String> conditions = new ArrayList<>();
            if (nombreVendedor != null && !nombreVendedor.trim().isEmpty()) {
                conditions.add("a.nombre_completo LIKE ?");
                params.add("%" + nombreVendedor.trim() + "%");
            }
            if (fechaInicio != null) {
                conditions.add("DATE(v.fecha_venta) >= ?");
                params.add(Date.valueOf(fechaInicio));
            }
            if (fechaFin != null) {
                conditions.add("DATE(v.fecha_venta) <= ?");
                params.add(Date.valueOf(fechaFin));
            }

            if (!conditions.isEmpty()) {
                sqlVentasBuilder.append(" WHERE ").append(String.join(" AND ", conditions));
            }
            sqlVentasBuilder.append(" ORDER BY v.fecha_venta DESC");

            try (PreparedStatement psVentas = cx.prepareStatement(sqlVentasBuilder.toString())) {
                for (int i = 0; i < params.size(); i++) {
                    psVentas.setObject(i + 1, params.get(i));
                }

                try (ResultSet rsVentas = psVentas.executeQuery()) {
                    while (rsVentas.next()) {
                        int idVenta = rsVentas.getInt("id_venta");
                        java.sql.Timestamp fechaVentaTimestamp = rsVentas.getTimestamp("fecha_venta");
                        LocalDateTime fechaVenta = (fechaVentaTimestamp != null) ? fechaVentaTimestamp.toLocalDateTime() : null;
                        double montoTotal = rsVentas.getDouble("monto_total");
                        String estado = rsVentas.getString("estado"); // Asumiendo que 'estado' existe en la tabla ventas
                        String nombreAdministrador = rsVentas.getString("nombre_administrador");
                        List<DetalleVentaDisplayDTO> detalles = detallesPorVentaId.getOrDefault(idVenta, new ArrayList<>());
                        ventasDisplay.add(new VentaDisplayDTO(idVenta, fechaVenta, montoTotal, nombreAdministrador, detalles, estado));
                    }
                }
            }
        } catch (SQLException e) {
            // Considerar loguear la excepción
            throw e;
        }
        // La conexión es manejada por ConexionBD, no se cierra aquí.
        return ventasDisplay;
    }

    /**
     * Verifica si existen ventas asociadas a un administrador específico.
     * @param idAdministrador El ID del administrador a verificar.
     * @return true si existen ventas, false en caso contrario.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    public boolean existenVentasParaAdministrador(int idAdministrador) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ventas WHERE id_administrador = ?";
        try (Connection cx = ConexionBD.obtener(); // Obtener y cerrar la conexión en try-with-resources
             PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setInt(1, idAdministrador);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            // Considerar loguear la excepción
            throw e; // Relanzar para que la capa de servicio la maneje
        }
        return false;
    }
}
