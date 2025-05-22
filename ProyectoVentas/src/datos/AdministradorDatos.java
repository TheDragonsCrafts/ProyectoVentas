package datos;

import entidades.Administrador;
import excepciones.BaseDatosException;
import seguridad.ConexionBD;
import seguridad.UtilHash;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdministradorDatos {
    private static final Logger LOGGER = Logger.getLogger(AdministradorDatos.class.getName());

    /** Verifica si el usuario y contraseña son válidos */
    public Optional<Administrador> validar(String usuario, String clave) {
        Administrador admin = buscarPorUsuario(usuario).orElse(null);
        if (admin == null || !UtilHash.verificarClave(clave, admin.getClaveHash())) {
            return Optional.empty();
        }
        return Optional.of(admin);
    }

    /** Busca un administrador por su nombre de usuario */
    public Optional<Administrador> buscarPorUsuario(String usuario) throws BaseDatosException {
        String sql = "SELECT id_admin, usuario, nombre_completo, clave_hash, es_admin_maestro FROM Administrador WHERE usuario = ?";
        try (Connection cx = ConexionBD.getConnection(); // Assuming ConexionBD.getConnection() is available
             PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setString(1, usuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Administrador(
                            rs.getInt("id_admin"),
                            rs.getString("usuario"),
                            rs.getString("nombre_completo"),
                            rs.getString("clave_hash"),
                            rs.getBoolean("es_admin_maestro")
                    ));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de base de datos al buscar administrador por usuario: " + usuario, e);
            throw new BaseDatosException("Error al buscar administrador por usuario: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    /**
     * Verifica si existe al menos un administrador maestro.
     * @return true si existe al menos un administrador maestro, false en caso contrario.
     * @throws BaseDatosException Si ocurre un error al consultar la base de datos.
     */
    public boolean existeAdminMaestro() throws BaseDatosException {
        String sql = "SELECT COUNT(*) FROM Administrador WHERE es_admin_maestro = TRUE";
        try (Connection cx = ConexionBD.getConnection();
             PreparedStatement ps = cx.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de base de datos al verificar existencia de administrador maestro.", e);
            throw new BaseDatosException("Error al verificar administrador maestro: " + e.getMessage(), e);
        }
        return false; // Debería ser alcanzado solo si la tabla está vacía y COUNT(*) es 0.
    }
    
    /**
     * Registra un nuevo administrador.
     * @param admin El administrador a registrar.
     * @throws BaseDatosException Si ocurre un error durante la inserción.
     */
    public void registrarAdmin(Administrador admin) throws BaseDatosException {
        String sql = "INSERT INTO Administrador (usuario, nombre_completo, clave_hash, es_admin_maestro) VALUES (?, ?, ?, ?)";
        try (Connection cx = ConexionBD.getConnection();
             PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setString(1, admin.getUsuario());
            ps.setString(2, admin.getNombreCompleto());
            ps.setString(3, admin.getClaveHash()); // Asume que la clave ya está hasheada
            ps.setBoolean(4, admin.isEsAdminMaestro());
            ps.executeUpdate();
            LOGGER.log(Level.INFO, "Administrador registrado exitosamente: {0}", admin.getUsuario());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de base de datos al registrar administrador: " + admin.getUsuario(), e);
            throw new BaseDatosException("Error al registrar administrador: " + e.getMessage(), e);
        }
    }
}
