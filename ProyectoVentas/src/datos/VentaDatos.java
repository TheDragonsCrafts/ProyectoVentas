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
     * Crea una venta y sus detalles en una sola transacción.
     * Devuelve el ID autogenerado.
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
            // Consider returning cx to pool if applicable, for now, just ensuring autoCommit is reset.
        }
    }

    public List<VentaDisplayDTO> obtenerVentasParaDisplay(String nombreVendedor, LocalDate fechaInicio, LocalDate fechaFin) throws SQLException {
        List<VentaDisplayDTO> ventasDisplay = new ArrayList<>();
        Map<Integer, List<DetalleVentaDisplayDTO>> detallesPorVentaId = new HashMap<>();
        List<Object> params = new ArrayList<>();

        // Primero, obtener todos los detalles de venta.
        // Esto es ineficiente si hay muchas ventas y solo se filtran algunas,
        // pero simplifica la lógica ya que los detalles no se filtran directamente por fecha o vendedor.
        // Una optimización sería filtrar las ventas primero y luego obtener detalles solo para esas ventas.
        String sqlDetalles = "SELECT dv.id_venta, p.nombre AS nombre_producto, dv.cantidad, dv.precio_en_venta " +
                             "FROM detalles_venta dv " +
                             "JOIN productos p ON dv.id_producto = p.id_producto";

        Connection cx = null;
        try {
            cx = ConexionBD.obtener();
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

            // Construir la consulta de ventas dinámicamente
            StringBuilder sqlVentasBuilder = new StringBuilder(
                "SELECT v.id_venta, v.fecha_venta, v.monto_total, v.estado, a.nombre_completo AS nombre_administrador " +
                "FROM ventas v " +
                "JOIN administradores a ON v.id_administrador = a.id_administrador"
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
                sqlVentasBuilder.append(" WHERE ");
                sqlVentasBuilder.append(String.join(" AND ", conditions));
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
                        String estado = rsVentas.getString("estado");
                        String nombreAdministrador = rsVentas.getString("nombre_administrador");
                        List<DetalleVentaDisplayDTO> detalles = detallesPorVentaId.getOrDefault(idVenta, new ArrayList<>());
                        ventasDisplay.add(new VentaDisplayDTO(idVenta, fechaVenta, montoTotal, nombreAdministrador, detalles, estado));
                    }
                }
            }
        } finally {
            if (cx != null) {
                // Do not close the shared connection managed by ConexionBD.
                // Simply ensure any open resources have been released.
            }
        }
        return ventasDisplay;
    }
}
