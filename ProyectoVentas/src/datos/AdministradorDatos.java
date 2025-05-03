package datos;

import entidades.Administrador;
import seguridad.ConexionBD;

import java.sql.*;
import java.util.Optional;

public class AdministradorDatos {

    public Optional<Administrador> buscarPorUsuario(String usuario) {
        String sql = "SELECT * FROM administradores WHERE nombre_usuario = ?";
        try (PreparedStatement ps = ConexionBD.obtener().prepareStatement(sql)) {
            ps.setString(1, usuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapear(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /* ==== Métodos CRUD básicos (puedes ampliar) ==== */

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

