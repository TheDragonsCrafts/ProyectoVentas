package datos;

import entidades.Administrador;
import seguridad.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdministradorDatos {

    /**
     * Busca un administrador por su nombre de usuario.
     */
    public Optional<Administrador> buscarPorUsuario(String usuario) {
        String sql = "SELECT * FROM administradores WHERE nombre_usuario = ?";
        try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql)) {
            ps.setString(1, usuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Administrador> buscarPorId(int idAdmin) {
        String sql = "SELECT * FROM administradores WHERE id_administrador = ?";
        try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql)) {
            ps.setInt(1, idAdmin);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging
        }
        return Optional.empty();
    }

    /**
     * Comprueba si ya existe un Administrador Maestro en la BD.
     */
    public boolean existeAdminMaestro() {
        String sql = "SELECT COUNT(*) FROM administradores WHERE es_admin_maestro = 1";
        try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Inserta un nuevo Administrador en la base de datos.
     */
    public void insertar(Administrador a) throws SQLException {
        String sql = """
            INSERT INTO administradores
            (nombre_usuario, hash_contraseña, nombre_completo, correo_electrónico,
             activo, es_admin_maestro)
            VALUES (?,?,?,?,?,?)
            """;
        try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql)) {
            ps.setString(1, a.usuario());
            ps.setString(2, a.hash());
            ps.setString(3, a.nombreCompleto());
            ps.setString(4, a.correo());
            ps.setBoolean(5, a.activo());
            ps.setBoolean(6, a.adminMaestro());
            ps.executeUpdate();
        }
    }

    /**
     * Actualiza un Administrador en la base de datos.
     * Este método actualiza todos los campos, incluyendo el hash de contraseña,
     * utilizando el accesor admin.passwordHash() y nombres de columna consistentes
     * con el método 'insertar'.
     */
    public boolean actualizar(Administrador admin) {
        // Using column names consistent with 'insertar' and 'mapear':
        // nombre_usuario, hash_contraseña, nombre_completo, correo_electrónico, es_admin_maestro, id_administrador
        String sql = "UPDATE administradores SET nombre_usuario = ?, hash_contraseña = ?, nombre_completo = ?, correo_electrónico = ?, es_admin_maestro = ? WHERE id_administrador = ?";

        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, admin.usuario());
            ps.setString(2, admin.hash()); // Reverted to admin.hash()
            ps.setString(3, admin.nombreCompleto());
            ps.setString(4, admin.correo());
            ps.setBoolean(5, admin.adminMaestro());
            ps.setInt(6, admin.id());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int idAdministrador) {
        String sql = "DELETE FROM administradores WHERE id_administrador = ?";
        try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql)) {
            ps.setInt(1, idAdministrador);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging
            // Check for foreign key constraint violation if needed, though for admins it might be less common
            // if (e.getSQLState().equals("23000")) { /* foreign key violation */ }
            return false;
        }
    }

    public List<Administrador> listarTodos() {
        List<Administrador> administradores = new ArrayList<>();
        String sql = "SELECT * FROM administradores";
        try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                administradores.add(mapear(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Consider logging the exception or throwing a custom exception
        }
        return administradores;
    }

    public List<Administrador> buscarPorTermino(String termino) {
        List<Administrador> administradores = new ArrayList<>();
        String sql = "SELECT * FROM administradores WHERE nombre_usuario LIKE ? OR nombre_completo LIKE ?";
        String searchTerm = "%" + termino + "%";
        try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql)) {
            ps.setString(1, searchTerm);
            ps.setString(2, searchTerm);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    administradores.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Consider logging the exception or throwing a custom exception
        }
        return administradores;
    }

    // The original 'actualizar' method with conditional password update has been removed
    // as per the decision to have a single 'actualizar' method that aligns with the subtask's requirements.

    public boolean actualizarEstadoActivo(int idAdministrador, boolean nuevoEstado) {
        String sql = "UPDATE administradores SET activo = ? WHERE id_administrador = ?";
        try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql)) {
            ps.setBoolean(1, nuevoEstado);
            ps.setInt(2, idAdministrador);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Consider logging the exception
            return false;
        }
    }

    /* ==== Helper ==== */
    private Administrador mapear(ResultSet r) throws SQLException {
        return new Administrador(
            r.getInt("id_administrador"),
            r.getString("nombre_usuario"),
            r.getString("hash_contraseña"),
            r.getString("nombre_completo"),
            r.getString("correo_electrónico"),
            r.getBoolean("activo"),
            r.getBoolean("es_admin_maestro")
        );
    }
}
