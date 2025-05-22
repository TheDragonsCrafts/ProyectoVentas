package datos;

import entidades.DetalleVenta;
import entidades.Venta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import seguridad.ConexionBD; // Used by methods not part of the main sale transaction

public class VentaDatos {

    private static final Logger LOGGER = Logger.getLogger(VentaDatos.class.getName());

    /**
     * Lista todas las ventas. Este método gestiona su propia conexión.
     */
    public static List<Venta> listarVentas() {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT id_venta, id_administrador, fecha_venta, total_venta FROM Venta";

        try (Connection cx = ConexionBD.getConnection(); // Manages its own connection
             PreparedStatement ps = cx.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Venta venta = new Venta();
                venta.setIdVenta(rs.getInt("id_venta"));
                venta.setIdAdministrador(rs.getInt("id_administrador"));
                venta.setFechaVenta(rs.getTimestamp("fecha_venta"));
                venta.setTotalVenta(rs.getDouble("total_venta"));
                ventas.add(venta);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al listar ventas", e);
        }
        return ventas;
    }

    /**
     * Obtiene una venta por su ID. Este método gestiona su propia conexión.
     */
    public static Venta obtenerVentaPorId(int idVenta) {
        Venta venta = null;
        String sql = "SELECT id_venta, id_administrador, fecha_venta, total_venta FROM Venta WHERE id_venta = ?";

        try (Connection cx = ConexionBD.getConnection(); // Manages its own connection
             PreparedStatement ps = cx.prepareStatement(sql)) {

            ps.setInt(1, idVenta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    venta = new Venta();
                    venta.setIdVenta(rs.getInt("id_venta"));
                    venta.setIdAdministrador(rs.getInt("id_administrador"));
                    venta.setFechaVenta(rs.getTimestamp("fecha_venta"));
                    venta.setTotalVenta(rs.getDouble("total_venta"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener venta por ID", e);
        }
        return venta;
    }
    
    /**
     * Registra una nueva venta y sus detalles utilizando una conexión existente (para transacciones).
     * No gestiona el estado de la transacción (commit, rollback, autoCommit).
     *
     * @param cx La conexión de base de datos activa y gestionada externamente.
     * @param venta El objeto Venta a registrar.
     * @param detallesVenta La lista de detalles de la venta.
     * @return El ID de la venta generada.
     * @throws SQLException Si ocurre algún error durante las operaciones de base de datos.
     */
    public static int registrarVenta(Connection cx, Venta venta, List<DetalleVenta> detallesVenta) throws SQLException {
        String sqlVenta = "INSERT INTO Venta (id_administrador, fecha_venta, total_venta) VALUES (?, ?, ?)";
        String sqlDetalle = "INSERT INTO DetalleVenta (id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        // No SQL for stock update here, it's handled by ServicioInventario

        int generatedVentaId = -1;

        try {
            // Registrar la venta y obtener el ID generado
            try (PreparedStatement psVenta = cx.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                psVenta.setInt(1, venta.getIdAdministrador());
                psVenta.setTimestamp(2, venta.getFechaVenta());
                psVenta.setDouble(3, venta.getTotalVenta());
                psVenta.executeUpdate();

                try (ResultSet generatedKeys = psVenta.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedVentaId = generatedKeys.getInt(1);
                        venta.setIdVenta(generatedVentaId); // Update the venta object with the new ID
                    } else {
                        // Log and prepare to re-throw
                        LOGGER.log(Level.SEVERE, "No se pudo obtener el ID de la venta generada.");
                        throw new SQLException("No se pudo obtener el ID de la venta generada.");
                    }
                }
            }

            // Registrar los detalles de la venta
            try (PreparedStatement psDetalle = cx.prepareStatement(sqlDetalle)) {
                for (DetalleVenta detalle : detallesVenta) {
                    psDetalle.setInt(1, venta.getIdVenta()); // Use the generated ID
                    psDetalle.setInt(2, detalle.getIdProducto());
                    psDetalle.setInt(3, detalle.getCantidad());
                    psDetalle.setDouble(4, detalle.getPrecioUnitario());
                    psDetalle.setDouble(5, detalle.getSubtotal());
                    psDetalle.addBatch();
                }
                psDetalle.executeBatch();
            }
            
            LOGGER.log(Level.INFO, "Venta y detalles registrados parcialmente (dentro de transacción) para ID de venta: {0}", generatedVentaId);
            return generatedVentaId;

        } catch (SQLException e) {
            // Log the error here for VentaDatos specific context
            LOGGER.log(Level.SEVERE, "Error al registrar venta o detalles (dentro de transacción). ID de venta (si generado): " + generatedVentaId, e);
            // Re-throw SQLException to allow ServicioVentas to handle transaction rollback
            throw e; 
        }
        // No finally block here to manage cx.setAutoCommit(true) as the connection is managed by the caller.
    }
}
