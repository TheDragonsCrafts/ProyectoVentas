package datos;

import entidades.Producto;
import seguridad.ConexionBD;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoDatos {

    /** Inserta un producto (el ID debe ser proporcionado por el llamador) */
    public void insertar(Producto p) throws SQLException {
        String sql = """
            INSERT INTO productos
              (id_producto, nombre, descripción, precio, cantidad, fecha_caducidad, activo)
            VALUES (?,?,?,?,?,?,?)
            """;
        try (PreparedStatement ps = ConexionBD.obtener()
                .prepareStatement(sql)) {
            cargar(ps, p, false); // 'false' meaning it's not an update
            ps.executeUpdate();
        }
    }

    /** Actualiza un producto existente */
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

    /** Elimina un producto por su ID */
    public void eliminar(int id) throws SQLException {
        try (PreparedStatement ps = ConexionBD.obtener()
                .prepareStatement("DELETE FROM productos WHERE id_producto=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /** Busca un producto por ID */
    public Optional<Producto> buscarPorId(int id) {
        String sql = "SELECT * FROM productos WHERE id_producto=?";
        try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapear(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /** Busca productos cuyo nombre contenga el patrón dado */
    public List<Producto> buscarPorNombre(String patron) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE nombre LIKE ?";
        try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql)) {
            ps.setString(1, "%" + patron + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /** Lista **todos** los productos */
    public List<Producto> listarProductos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /* ==== Helpers ==== */
    private void cargar(PreparedStatement ps, Producto p, boolean isUpdate) throws SQLException {
        // If it's an update, ID is the last parameter in WHERE clause
        // If it's an insert, ID is the first parameter
        if (isUpdate) {
            ps.setString(1, p.nombre());
            ps.setString(2, p.descripcion());
            ps.setDouble(3, p.precio());
            ps.setInt(4, p.cantidad());
            if (p.fechaCaducidad() != null) {
                ps.setDate(5, Date.valueOf(p.fechaCaducidad()));
            } else {
                ps.setNull(5, Types.DATE);
            }
            ps.setBoolean(6, p.activo());
            ps.setInt(7, p.id()); // For WHERE id_producto = ?
        } else { // Is Insert
            ps.setInt(1, p.id()); // New: id_producto for VALUES
            ps.setString(2, p.nombre());
            ps.setString(3, p.descripcion());
            ps.setDouble(4, p.precio());
            ps.setInt(5, p.cantidad());
            if (p.fechaCaducidad() != null) {
                ps.setDate(6, Date.valueOf(p.fechaCaducidad()));
            } else {
                ps.setNull(6, Types.DATE);
            }
            ps.setBoolean(7, p.activo());
        }
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

    public boolean idExiste(int id) {
        String sql = "SELECT COUNT(*) FROM productos WHERE id_producto=?";
        try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Or handle more gracefully
        }
        return false; // Default to false on error or if not found
    }
}

