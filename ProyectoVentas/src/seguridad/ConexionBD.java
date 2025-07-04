package seguridad;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestiona la conexión a la base de datos.
 * Utiliza un patrón Singleton para mantener una única instancia de conexión.
 * Lee la configuración de la base de datos desde `db.properties`.
 */
public final class ConexionBD {

    private static Connection conexion;

    // Constructor privado para evitar instanciación externa.
    private ConexionBD() { }

    /**
     * Obtiene la instancia de la conexión a la base de datos.
     * Si la conexión no existe o está cerrada, la crea.
     * @return La conexión activa a la base de datos.
     * @throws SQLException Si ocurre un error al conectar o configurar.
     */
    public static Connection obtener() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            // Cargar driver MySQL
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver MySQL no encontrado. Asegúrate de que mysql-connector-java esté en el classpath.", e);
            }

            // Cargar propiedades de conexión desde db.properties
            Properties p = new Properties();
            try (InputStream is = ConexionBD.class.getResourceAsStream("/seguridad/db.properties")) {
                if (is == null) {
                    throw new SQLException("Archivo db.properties no encontrado en el classpath /seguridad.");
                }
                p.load(is);
            } catch (IOException ex) {
                throw new SQLException("Error al leer db.properties.", ex);
            }

            // Establecer la conexión
            conexion = DriverManager.getConnection(
                p.getProperty("url"),
                p.getProperty("user"),
                p.getProperty("password")
            );
        }
        return conexion;
    }
}


