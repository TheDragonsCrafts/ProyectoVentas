package seguridad;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Provee una conexión única (singleton) a la base de datos.
 */
public final class ConexionBD {

    private static Connection conexion;

    private ConexionBD() { }

    public static Connection obtener() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            // 1) Asegurarnos de que el driver de MySQL esté cargado
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver MySQL no encontrado. " +
                                       "¿Añadiste mysql-connector-java al classpath?", e);
            }

            // 2) Cargar propiedades desde /seguridad/db.properties
            Properties p = new Properties();
            try (InputStream is = ConexionBD.class
                     .getResourceAsStream("/seguridad/db.properties")) {
                if (is == null) {
                    throw new SQLException(
                        "No se encontró db.properties en /seguridad");
                }
                p.load(is);
            } catch (IOException ex) {
                throw new SQLException("Error al leer db.properties", ex);
            }

            // 3) Abrir la conexión
            conexion = DriverManager.getConnection(
                p.getProperty("url"),
                p.getProperty("user"),
                p.getProperty("password")
            );
        }
        return conexion;
    }
}


