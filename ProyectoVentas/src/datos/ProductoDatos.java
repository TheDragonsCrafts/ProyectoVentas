package datos;

import entidades.Producto;
import excepciones.BaseDatosException; 
import excepciones.ProductoNoEncontradoException;
import excepciones.StockInsuficienteException;
import seguridad.ConexionBD; 

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductoDatos {
    private static final Logger LOGGER = Logger.getLogger(ProductoDatos.class.getName());

    public void insertar(Producto p) throws BaseDatosException {
        String sql = """
            INSERT INTO productos
              (id_producto, nombre, descripción, precio, cantidad, fecha_caducidad, activo)
            VALUES (?,?,?,?,?,?,?)
            """;
        try (Connection cx = ConexionBD.getConnection(); 
             PreparedStatement ps = cx.prepareStatement(sql)) {
            cargar(ps, p, false); 
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de BD al insertar producto: " + p.nombre(), e);
            throw new BaseDatosException("Error al insertar producto: " + e.getMessage(), e);
        }
    }

    public void actualizar(Producto p) throws BaseDatosException {
        try (Connection cx = ConexionBD.getConnection(); 
             PreparedStatement ps = cx.prepareStatement(getSqlActualizar())) {
            cargar(ps, p, true);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de BD al actualizar producto: " + p.id(), e);
            throw new BaseDatosException("Error al actualizar producto: " + e.getMessage(), e);
        }
    }
    
    public void actualizar(Connection cx, Producto p) throws SQLException {
        // This method keeps throwing SQLException as it's used within a transaction
        // managed by a service layer, which should handle BaseDatosException wrapping if needed.
        try (PreparedStatement ps = cx.prepareStatement(getSqlActualizar())) {
            cargar(ps, p, true);
            ps.executeUpdate();
        }
    }

    public void actualizarStock(Connection cx, int idProducto, int cantidadADescontar) 
            throws SQLException, ProductoNoEncontradoException, StockInsuficienteException {
        // This method also keeps throwing specific exceptions as it's used within a transaction.
        if (buscarPorId(cx, idProducto).isEmpty()) { // buscarPorId(cx,...) already throws PNE
            // This line is defensive, PNE from buscarPorId should have been thrown.
            throw new ProductoNoEncontradoException("Producto con ID " + idProducto + " no encontrado para actualizar stock.");
        }

        String sql = "UPDATE productos SET cantidad = cantidad - ? WHERE id_producto = ? AND cantidad >= ?";
        try (PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setInt(1, cantidadADescontar);
            ps.setInt(2, idProducto);
            ps.setInt(3, cantidadADescontar); 
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows == 0) {
                throw new StockInsuficienteException("Stock insuficiente para el producto ID " + idProducto + ". No se pudo descontar " + cantidadADescontar + " unidades.");
            }
            LOGGER.log(Level.INFO, "Stock actualizado para producto ID {0}, cantidad descontada: {1}", new Object[]{idProducto, cantidadADescontar});
        }
    }

    public void eliminar(int id) throws BaseDatosException {
        try (Connection cx = ConexionBD.getConnection(); 
             PreparedStatement ps = cx.prepareStatement("DELETE FROM productos WHERE id_producto=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de BD al eliminar producto ID: " + id, e);
            throw new BaseDatosException("Error al eliminar producto: " + e.getMessage(), e);
        }
    }

    public Optional<Producto> buscarPorId(int id) throws ProductoNoEncontradoException, BaseDatosException {
        try (Connection cx = ConexionBD.getConnection(); 
             PreparedStatement ps = cx.prepareStatement(getSqlBuscarPorId())) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                } else {
                    throw new ProductoNoEncontradoException("Producto con ID " + id + " no encontrado.");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de BD al buscar producto por ID: " + id, e);
            throw new BaseDatosException("Error de BD al buscar producto ID " + id + ": " + e.getMessage(), e);
        }
    }

    public Optional<Producto> buscarPorId(Connection cx, int id) 
            throws SQLException, ProductoNoEncontradoException {
        // This method keeps throwing specific exceptions as it's used within a transaction.
        try (PreparedStatement ps = cx.prepareStatement(getSqlBuscarPorId())) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                } else {
                    throw new ProductoNoEncontradoException("Producto con ID " + id + " no encontrado (dentro de transacción).");
                }
            }
        } catch (SQLException e) { // SQLException from PreparedStatement or ResultSet ops
            LOGGER.log(Level.WARNING, "Error de SQL al buscar producto por ID " + id + " (dentro de transacción).", e);
            throw e; 
        }
    }

    public List<Producto> buscarPorNombre(String patron) throws BaseDatosException {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE nombre LIKE ?";
        try (Connection cx = ConexionBD.getConnection(); 
             PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setString(1, "%" + patron + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de BD al buscar producto por nombre con patrón: " + patron, e);
            throw new BaseDatosException("Error al buscar producto por nombre: " + e.getMessage(), e);
        }
        return lista;
    }

    public List<Producto> listarProductos() throws BaseDatosException {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        try (Connection cx = ConexionBD.getConnection(); 
             PreparedStatement ps = cx.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de BD al listar productos.", e);
            throw new BaseDatosException("Error al listar productos: " + e.getMessage(), e);
        }
        return lista;
    }

    private String getSqlActualizar() {
        return """
            UPDATE productos SET
              nombre=?, descripción=?, precio=?, cantidad=?, fecha_caducidad=?, activo=?
            WHERE id_producto=?
            """;
    }
    
    private String getSqlBuscarPorId() {
        return "SELECT * FROM productos WHERE id_producto=?";
    }

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
            ps.setInt(7, p.id()); 
        } else { 
            ps.setInt(1, p.id()); 
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

    public boolean idExiste(int id) throws BaseDatosException {
        String sql = "SELECT COUNT(*) FROM productos WHERE id_producto=?";
        try (Connection cx = ConexionBD.getConnection(); 
             PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de BD al verificar si ID de producto existe: " + id, e);
            throw new BaseDatosException("Error al verificar si ID de producto existe: " + e.getMessage(), e);
        }
        return false; // Should only be reached if COUNT(*) is 0
    }
}
