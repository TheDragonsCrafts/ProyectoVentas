package seguridad;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionBD {

    private static HikariDataSource ds;
    private static final Logger LOGGER = Logger.getLogger(ConexionBD.class.getName());

    static {
        LOGGER.log(Level.INFO, "Iniciando configuración de HikariCP...");
        Properties props = new Properties();
        try (InputStream input = ConexionBD.class.getResourceAsStream("/seguridad/db.properties")) {
            if (input == null) {
                LOGGER.log(Level.SEVERE, "No se pudo encontrar db.properties en el classpath.");
                // Consider throwing a custom configuration exception if appropriate for your app's lifecycle
                throw new RuntimeException("No se pudo encontrar db.properties. El pool de conexiones no puede ser inicializado.");
            }
            props.load(input);
            LOGGER.log(Level.INFO, "db.properties cargado exitosamente.");

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("url"));
            config.setUsername(props.getProperty("user"));
            config.setPassword(props.getProperty("password"));
            config.setDataSourceClassName("com.mysql.cj.jdbc.MysqlDataSource"); // For MySQL
            
            LOGGER.log(Level.INFO, "Configurando HikariCP con URL: {0}, User: {1}", new Object[]{props.getProperty("url"), props.getProperty("user")});

            // Pool settings
            config.setMinimumIdle(Integer.parseInt(props.getProperty("minimumIdle", "5")));
            config.setMaximumPoolSize(Integer.parseInt(props.getProperty("maximumPoolSize", "10")));
            config.setConnectionTimeout(Long.parseLong(props.getProperty("connectionTimeout", "30000"))); // 30 seconds
            config.setIdleTimeout(Long.parseLong(props.getProperty("idleTimeout", "600000"))); // 10 minutes
            config.setMaxLifetime(Long.parseLong(props.getProperty("maxLifetime", "1800000"))); // 30 minutes
            
            LOGGER.log(Level.INFO, "Configuración del pool: MinIdle={0}, MaxPoolSize={1}, ConnectionTimeout={2}ms", 
                new Object[]{config.getMinimumIdle(), config.getMaximumPoolSize(), config.getConnectionTimeout()});

            // MySQL specific properties from db.properties or defaults
            config.addDataSourceProperty("cachePrepStmts", props.getProperty("mysql.cachePrepStmts", "true"));
            config.addDataSourceProperty("prepStmtCacheSize", props.getProperty("mysql.prepStmtCacheSize", "250"));
            config.addDataSourceProperty("prepStmtCacheSqlLimit", props.getProperty("mysql.prepStmtCacheSqlLimit", "2048"));
            config.addDataSourceProperty("useServerPrepStmts", props.getProperty("mysql.useServerPrepStmts", "true"));
            config.addDataSourceProperty("useLocalSessionState", props.getProperty("mysql.useLocalSessionState", "true"));
            config.addDataSourceProperty("rewriteBatchedStatements", props.getProperty("mysql.rewriteBatchedStatements", "true"));
            config.addDataSourceProperty("cacheResultSetMetadata", props.getProperty("mysql.cacheResultSetMetadata", "true"));
            config.addDataSourceProperty("cacheServerConfiguration", props.getProperty("mysql.cacheServerConfiguration", "true"));
            config.addDataSourceProperty("elideSetAutoCommits", props.getProperty("mysql.elideSetAutoCommits", "true"));
            config.addDataSourceProperty("maintainTimeStats", props.getProperty("mysql.maintainTimeStats", "false"));
            
            LOGGER.log(Level.INFO, "Propiedades de MySQL para DataSource configuradas.");

            ds = new HikariDataSource(config);
            LOGGER.log(Level.INFO, "HikariCP pool inicializado y DataSource creado exitosamente.");

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error de E/S al cargar db.properties.", e);
            throw new RuntimeException("Error de E/S al cargar db.properties. El pool de conexiones no puede ser inicializado.", e);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Error de formato numérico en las propiedades de configuración del pool.", e);
            throw new RuntimeException("Error de formato numérico en db.properties para la configuración del pool.", e);
        } catch (Exception e) { // Catch-all for other HikariCP or config exceptions
            LOGGER.log(Level.SEVERE, "Error inesperado al inicializar HikariCP pool.", e);
            throw new RuntimeException("Error inesperado al inicializar HikariCP pool.", e);
        }
    }

    private ConexionBD() {
        // Private constructor to prevent instantiation
    }

    public static Connection getConnection() throws SQLException {
        if (ds == null) {
            LOGGER.log(Level.SEVERE, "Intento de obtener conexión pero HikariDataSource (ds) es nulo. La inicialización pudo haber fallado.");
            throw new SQLException("HikariDataSource no ha sido inicializado. Revise los logs para errores de configuración.");
        }
        try {
            // HikariCP's getConnection() can throw SQLException if timeout occurs, etc.
            Connection conn = ds.getConnection();
            // LOGGER.log(Level.FINEST, "Conexión obtenida del pool. {0}", conn); // Could be too verbose
            return conn;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener conexión del pool HikariCP.", e);
            throw e; // Re-throw la SQLException original
        }
    }

    public static void cerrarPool() {
        if (ds != null && !ds.isClosed()) {
            LOGGER.log(Level.INFO, "Cerrando HikariCP pool...");
            ds.close();
            LOGGER.log(Level.INFO, "HikariCP pool cerrado exitosamente.");
        } else {
            LOGGER.log(Level.INFO, "Intento de cerrar pool, pero ya estaba cerrado o no inicializado.");
        }
    }
}
