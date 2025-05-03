package datos;

import entidades.DetalleVenta;
import entidades.Venta;
import seguridad.ConexionBD;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

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
}
