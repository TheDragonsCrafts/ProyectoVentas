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
     * @param usuario El nombre de usuario a buscar.
     * @return Un Optional con el Administrador si se encuentra, o vacío.
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

    /**
     * Busca un administrador por su ID.
     * @param idAdmin El ID del administrador a buscar.
     * @return Un Optional con el Administrador si se encuentra, o vacío.
     */
    public Optional<Administrador> buscarPorId(int idAdmin) {
        String sql = "SELECT * FROM administradores WHERE id_administrador = ?";
        try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql)) {
            ps.setInt(1, idAdmin);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Considerar loguear la excepción
        }
        return Optional.empty();
    }

    /**
     * Comprueba si ya existe un Administrador Maestro en la base de datos.
     * @return true si existe al menos un admin maestro, false en caso contrario.
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
     * @param a El objeto Administrador a insertar.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
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
     * Actualiza un Administrador existente en la base de datos.
     * @param admin El objeto Administrador con los datos actualizados.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean actualizar(Administrador admin) {
        String sql = "UPDATE administradores SET nombre_usuario = ?, hash_contraseña = ?, nombre_completo = ?, correo_electrónico = ?, es_admin_maestro = ? WHERE id_administrador = ?";

        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, admin.usuario());
            ps.setString(2, admin.hash());
            ps.setString(3, admin.nombreCompleto());
            ps.setString(4, admin.correo());
            ps.setBoolean(5, admin.adminMaestro());
            ps.setInt(6, admin.id());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace(); // Considerar loguear la excepción
            return false;
        }
    }

    /**
     * Elimina un administrador por su ID.
     * @param idAdministrador El ID del administrador a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean eliminar(int idAdministrador) {
        String sql = "DELETE FROM administradores WHERE id_administrador = ?";
        try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql)) {
            ps.setInt(1, idAdministrador);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Considerar loguear la excepción
            return false;
        }
    }

    /**
     * Obtiene el Administrador Maestro, si existe.
     * @return Un Optional con el Administrador Maestro, o vacío.
     */
    public Optional<Administrador> obtenerAdminMaestro() {
        String sql = "SELECT * FROM administradores WHERE es_admin_maestro = 1 LIMIT 1";
        try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return Optional.of(mapear(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Considerar loguear la excepción
        }
        return Optional.empty();
    }

    /**
     * Cuenta el número de Administradores Maestros.
     * @return El número total de administradores maestros.
     */
    public int contarAdministradoresMaestros() {
        String sql = "SELECT COUNT(*) FROM administradores WHERE es_admin_maestro = 1";
        try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Considerar loguear la excepción
        }
        return 0;
    }

    /**
     * Cuenta el número de Administradores activos.
     * @return El número total de administradores activos.
     */
    public int contarAdministradoresActivos() {
        String sql = "SELECT COUNT(*) FROM administradores WHERE activo = 1";
        try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Considerar loguear la excepción
        }
        return 0;
    }

    /**
     * Lista todos los administradores.
     * @return Una lista de todos los administradores.
     */
    public List<Administrador> listarTodos() {
        List<Administrador> administradores = new ArrayList<>();
        String sql = "SELECT * FROM administradores";
        try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                administradores.add(mapear(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Considerar loguear o lanzar excepción personalizada
        }
        return administradores;
    }

    /**
     * Busca administradores por un término de búsqueda (nombre de usuario o nombre completo).
     * @param termino El término a buscar.
     * @return Una lista de administradores que coinciden con el término.
     */
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
            e.printStackTrace(); // Considerar loguear o lanzar excepción personalizada
        }
        return administradores;
    }

    /**
     * Actualiza el estado 'activo' de un administrador.
     * @param idAdministrador El ID del administrador a actualizar.
     * @param nuevoEstado El nuevo estado (true para activo, false para inactivo).
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean actualizarEstadoActivo(int idAdministrador, boolean nuevoEstado) {
        String sql = "UPDATE administradores SET activo = ? WHERE id_administrador = ?";
        try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql)) {
            ps.setBoolean(1, nuevoEstado);
            ps.setInt(2, idAdministrador);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Considerar loguear la excepción
            return false;
        }
    }

    /**
     * Actualiza la contraseña de un administrador si el nombre de usuario y correo coinciden.
     * @param nombreUsuario El nombre de usuario.
     * @param correo El correo electrónico.
     * @param nuevaContrasena La nueva contraseña (texto plano).
     * @return true si la contraseña se actualizó, false en caso contrario.
     */
    public boolean actualizarContrasenaPorUsuarioYCorreo(String nombreUsuario, String correo, String nuevaContrasena) {
        Optional<Administrador> adminOpt = buscarPorUsuario(nombreUsuario);
        if (adminOpt.isPresent()) {
            Administrador admin = adminOpt.get();
            if (admin.correo().equalsIgnoreCase(correo)) {
                String nuevoHash = seguridad.UtilHash.hash(nuevaContrasena);
                String sql = "UPDATE administradores SET hash_contraseña = ? WHERE id_administrador = ?";
                try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql)) {
                    ps.setString(1, nuevoHash);
                    ps.setInt(2, admin.id());
                    int filasAfectadas = ps.executeUpdate();
                    return filasAfectadas > 0;
                } catch (SQLException e) {
                    e.printStackTrace(); // Considerar loguear la excepción
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Mapea un ResultSet a un objeto Administrador.
     * @param r El ResultSet a mapear.
     * @return El objeto Administrador.
     * @throws SQLException Si ocurre un error al acceder a los datos del ResultSet.
     */
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
