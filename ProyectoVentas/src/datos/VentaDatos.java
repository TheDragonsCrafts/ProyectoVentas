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
        }
    }

    public List<VentaDisplayDTO> obtenerVentasParaDisplay() throws SQLException {
        List<VentaDisplayDTO> ventasDisplay = new ArrayList<>();
        Map<Integer, List<DetalleVentaDisplayDTO>> detallesPorVentaId = new HashMap<>();

        String sqlDetalles = "SELECT dv.id_venta, p.nombre AS nombre_producto, dv.cantidad, dv.precio_en_venta " +
                             "FROM detalles_venta dv " +
                             "JOIN productos p ON dv.id_producto = p.id_producto";
                             // Fetches all details. Assumes products referenced in detalles_venta exist.
                             // Consider adding "WHERE dv.id_producto IS NOT NULL" if products can be deleted 
                             // and id_producto in detalles_venta can become NULL and you want to exclude those.

        Connection cx = null; // Declare outside try to be accessible in finally
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
            } // stmtDetalles and rsDetalles are closed here

            String sqlVentas = "SELECT v.id_venta, v.fecha_venta, v.monto_total, v.estado, a.nombre_completo AS nombre_administrador " +
                               "FROM ventas v " +
                               "JOIN administradores a ON v.id_administrador = a.id_administrador " +
                               "ORDER BY v.fecha_venta DESC";

            try (Statement stmtVentas = cx.createStatement();
                 ResultSet rsVentas = stmtVentas.executeQuery(sqlVentas)) {
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
            } // stmtVentas and rsVentas are closed here

        } finally {
            if (cx != null) {
                // Only call setAutoCommit if you changed it.
                // If ConexionBD.obtener() gives connections from a pool that might be in autoCommit(false)
                // by default for other operations, then it's safer to ensure it here.
                // However, for read operations like this, it's usually not changed.
                // For now, let's assume default autoCommit(true) is fine.
                // cx.setAutoCommit(true); // Optional, depending on connection pool and other uses
                cx.close(); // Return connection to the pool or close it
            }
        }
        return ventasDisplay;
    }
}
