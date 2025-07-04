package datos;

import entidades.Producto;
import seguridad.ConexionBD;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoDatos {

    /**
     * Inserta un nuevo producto en la base de datos.
     * @param p El producto a insertar.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
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

    /**
     * Actualiza un producto existente en la base de datos.
     * @param p El producto con los datos actualizados.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
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

    /**
     * Marca un producto como inactivo (eliminación lógica).
     * @param id El ID del producto a marcar como inactivo.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    public void eliminar(int id) throws SQLException {
        String sql = "UPDATE productos SET activo = ? WHERE id_producto = ?";
        try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql)) {
            ps.setBoolean(1, false); // Marca activo como false
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    /**
     * Busca un producto activo por su ID.
     * @param id El ID del producto a buscar.
     * @return Un Optional con el Producto si se encuentra y está activo, o vacío.
     */
    public Optional<Producto> buscarPorId(int id) {
        String sql = "SELECT * FROM productos WHERE id_producto=?";
        try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Producto producto = mapear(rs);
                if (producto.activo()) {
                    return Optional.of(producto);
                } else {
                    return Optional.empty(); // Producto inactivo, no se retorna
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Considerar loguear la excepción
        }
        return Optional.empty();
    }

    /**
     * Busca productos activos cuyo nombre contenga el patrón dado.
     * @param patron El patrón de búsqueda para el nombre.
     * @return Una lista de productos activos que coinciden con el patrón.
     */
    public List<Producto> buscarPorNombre(String patron) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE nombre LIKE ? AND activo = true";
        try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql)) {
            ps.setString(1, "%" + patron + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Considerar loguear la excepción
        }
        return lista;
    }

    /**
     * Lista todos los productos activos.
     * @return Una lista de todos los productos activos.
     */
    public List<Producto> listarProductos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE activo = true";
        try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Considerar loguear la excepción
        }
        return lista;
    }

    /**
     * Método auxiliar para cargar los parámetros de un Producto en un PreparedStatement.
     * @param ps El PreparedStatement a cargar.
     * @param p El Producto con los datos.
     * @param isUpdate Indica si la operación es una actualización (true) o inserción (false).
     * @throws SQLException Si ocurre un error al establecer los parámetros.
     */
    private void cargar(PreparedStatement ps, Producto p, boolean isUpdate) throws SQLException {
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

    /**
     * Mapea un ResultSet a un objeto Producto.
     * @param r El ResultSet a mapear.
     * @return El objeto Producto.
     * @throws SQLException Si ocurre un error al acceder a los datos del ResultSet.
     */
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

    /**
     * Verifica si un ID de producto ya existe en la base de datos.
     * @param id El ID del producto a verificar.
     * @return true si el ID existe, false en caso contrario.
     */
    public boolean idExiste(int id) {
        String sql = "SELECT COUNT(*) FROM productos WHERE id_producto=?";
        try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Considerar loguear la excepción
        }
        return false;
    }
}

