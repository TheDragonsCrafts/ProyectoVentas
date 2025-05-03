package datos;

import entidades.Producto;
import seguridad.ConexionBD;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoDatos {

    public void insertar(Producto p) throws SQLException {
        String sql = """
            INSERT INTO productos
            (nombre, descripción, precio, cantidad, fecha_caducidad, activo)
            VALUES (?,?,?,?,?,?)
            """;
        try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql)) {
            cargar(ps, p, false);
            ps.executeUpdate();
        }
    }

    public void actualizar(Producto p) throws SQLException {
        String sql = """
            UPDATE productos SET
              nombre=?, descripción=?, precio=?, cantidad=?, fecha_caducidad=?, activo=?
            WHERE id_producto=?
            """;
        try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql)) {
            cargar(ps, p, true);
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        try (PreparedStatement ps = ConexionBD.obtener()
                .prepareStatement("DELETE FROM productos WHERE id_producto=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Optional<Producto> buscarPorId(int id) {
        String sql = "SELECT * FROM productos WHERE id_producto=?";
        try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapear(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    public List<Producto> buscarPorNombre(String patron) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE nombre LIKE ?";
        try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql)) {
            ps.setString(1, "%" + patron + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    /* ==== Helpers ==== */
    private void cargar(PreparedStatement ps, Producto p, boolean conId) throws SQLException {
        ps.setString(1, p.nombre());
        ps.setString(2, p.descripcion());
        ps.setDouble(3, p.precio());
        ps.setInt(4, p.cantidad());
        if (p.fechaCaducidad() != null) ps.setDate(5, Date.valueOf(p.fechaCaducidad()));
        else ps.setNull(5, Types.DATE);
        ps.setBoolean(6, p.activo());
        if (conId) ps.setInt(7, p.id());
    }

    private Producto mapear(ResultSet r) throws SQLException {
        Date f = r.getDate("fecha_caducidad");
        return new Producto(
                r.getInt("id_producto"),
                r.getString("nombre"),
                r.getString("descripción"),
                r.getDouble("precio"),
                r.getInt("cantidad"),
                f != null ? f.toLocalDate() : null,
                r.getBoolean("activo")
        );
    }
}

