/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package seguridad;

/**
 *
 * @author IanDa
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Clase Upsilon para operaciones de autenticación relacionadas con la tabla 'administrators'.
 * Anteriormente AuthDAO.
 */
public class AuthUpsilon { // <<-- Nombre cambiado aquí

    /**
     * Verifica las credenciales de un administrador contra la base de datos.
     *
     * @param username      El nombre de usuario ingresado.
     * @param plainPassword La contraseña en texto plano ingresada.
     * @return Un objeto Administrator si las credenciales son válidas y el usuario está activo,
     *         null en caso contrario (usuario no encontrado, contraseña incorrecta, usuario inactivo o error de BD).
     */
    public Administrador verifyCredentials(String username, String plainPassword) {
        // Definir la consulta SQL para buscar al usuario por username
        String sql = "SELECT admin_id, password_hash, full_name, email, is_active, is_master_admin, created_at, updated_at " +
                     "FROM administrators WHERE username = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Administrador authenticatedAdmin = null;

        try {
            // 1. Obtener conexión a la BD
            conn = DatabaseConnection.getConnection();

            // 2. Preparar el statement SQL
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);

            // 3. Ejecutar la consulta
            rs = pstmt.executeQuery();

            // 4. Procesar el resultado
            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                boolean isActive = rs.getBoolean("is_active");

                // 5. Verificar si está activo Y la contraseña coincide
                if (isActive && PasswordUtil.checkPassword(plainPassword, storedHash)) {
                    // ¡Credenciales válidas y usuario activo! Construir el objeto.
                    int adminId = rs.getInt("admin_id");
                    String fullName = rs.getString("full_name");
                    String email = rs.getString("email");
                    boolean isMaster = rs.getBoolean("is_master_admin");
                    Timestamp createdAt = rs.getTimestamp("created_at");
                    Timestamp updatedAt = rs.getTimestamp("updated_at");

                    authenticatedAdmin = new Administrador(
                        adminId, username, storedHash, fullName, email, isActive, isMaster, createdAt, updatedAt
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error de SQL al verificar credenciales (AuthUpsilon) para '" + username + "': " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 6. Cerrar recursos
            DatabaseConnection.close(rs, pstmt, conn);
        }

        // 7. Devolver resultado
        return authenticatedAdmin;
    }

    // --- Otros métodos relacionados podrían ir aquí ---

} // <<-- Fin de la clase AuthUpsilon
