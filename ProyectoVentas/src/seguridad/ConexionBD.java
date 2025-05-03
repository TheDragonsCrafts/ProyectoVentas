package seguridad;

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
            Properties p = new Properties();
            p.load(ConexionBD.class.getResourceAsStream("/seguridad/db.properties"));
            conexion = DriverManager.getConnection(
                    p.getProperty("url"),
                    p.getProperty("user"),
                    p.getProperty("password")
            );
        }
        return conexion;
    }
}

