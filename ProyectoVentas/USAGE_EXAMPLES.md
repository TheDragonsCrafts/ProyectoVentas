# Usage Examples for ConexionBD with HikariCP

This document provides examples of how to use the refactored `ConexionBD.java` which now utilizes HikariCP for connection pooling.

## 1. Getting a Connection

To obtain a database connection, use the static method `ConexionBD.getConnection()`.

```java
import seguridad.ConexionBD;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EjemploUso {

    public void consultarDatos() {
        String sql = "SELECT * FROM algun_tabla WHERE id = ?";
        // Use try-with-resources to ensure the connection is always closed
        try (Connection cx = ConexionBD.getConnection();
             PreparedStatement ps = cx.prepareStatement(sql)) {

            ps.setInt(1, 10); // Example parameter

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Process your results
                    System.out.println("Columna1: " + rs.getString("nombre_columna1"));
                    System.out.println("Columna2: " + rs.getInt("nombre_columna2"));
                }
            }
        } catch (SQLException e) {
            // Handle exceptions related to database access
            e.printStackTrace(); // Or use a proper logger
        }
    }
}
```

## 2. Closing Connections (Crucial with Pooling)

It is **critical** to ensure that connections are always closed after use. The **try-with-resources** statement is the recommended way to achieve this, as it automatically closes the `Connection` object (and any other `AutoCloseable` resources like `PreparedStatement` and `ResultSet`) when the block is exited, whether normally or due to an exception.

**Why is this important with connection pooling?**

When you call `close()` on a pooled connection, it doesn't actually close the physical connection to the database. Instead, it returns the connection to the pool, making it available for other parts of your application. If you forget to close connections, the pool will eventually run out of available connections (a "pool exhaustion" scenario), and your application will hang or fail.

**Correct way (using try-with-resources):**

```java
// ... inside a method ...
try (Connection cx = ConexionBD.getConnection();
     PreparedStatement ps = cx.prepareStatement("SELECT * FROM tu_tabla");
     ResultSet rs = ps.executeQuery()) {

    // Work with your ResultSet (rs)
    while (rs.next()) {
        // ...
    }
    // cx.close(), ps.close(), and rs.close() are called automatically here.
} catch (SQLException e) {
    // Log the error or handle it appropriately
    LOGGER.log(Level.SEVERE, "Error al acceder a la base de datos", e);
}
// Connection is guaranteed to be returned to the pool here.
```

**Incorrect way (manual closing, error-prone):**

```java
// AVOID THIS PATTERN if possible, it's easy to make mistakes.
Connection cx = null;
PreparedStatement ps = null;
ResultSet rs = null;
try {
    cx = ConexionBD.getConnection();
    ps = cx.prepareStatement("SELECT * FROM tu_tabla");
    rs = ps.executeQuery();
    // ... work with rs ...
} catch (SQLException e) {
    // ... handle e ...
} finally {
    // Manual closing is verbose and error-prone.
    // What if rs.close() throws an exception? Then ps and cx might not be closed.
    if (rs != null) {
        try {
            rs.close();
        } catch (SQLException e) { /* log or ignore */ }
    }
    if (ps != null) {
        try {
            ps.close();
        } catch (SQLException e) { /* log or ignore */ }
    }
    if (cx != null) {
        try {
            cx.close(); // Returns the connection to the pool
        } catch (SQLException e) { /* log or ignore */ }
    }
}
```

## 3. Transaction Management

When performing transactions (multiple database operations that must succeed or fail as a single unit), you need to manage `autoCommit`, `commit`, and `rollback` states carefully.

The example in `VentaDatos.java` (`registrarVenta` method) demonstrates this:

```java
// public static String registrarVenta(Venta venta, List<DetalleVenta> detallesVenta) {
//     // ...
//     try (Connection cx = ConexionBD.getConnection()) { // Connection from pool
//         try {
//             cx.setAutoCommit(false); // Start transaction
//
//             // ... Perform database operations (inserts, updates) ...
//
//             cx.commit(); // Commit transaction if all operations succeed
//             mensaje = "Venta registrada con éxito...";
//
//         } catch (SQLException e) {
//             if (cx != null) {
//                 try {
//                     cx.rollback(); // Rollback transaction on error
//                 } catch (SQLException ex) {
//                     // Log rollback error
//                 }
//             }
//             mensaje = "Error al registrar la venta: " + e.getMessage();
//         } finally {
//             if (cx != null) {
//                 try {
//                     cx.setAutoCommit(true); // CRITICAL: Reset autoCommit to true before returning to pool
//                 } catch (SQLException e) {
//                     // Log error setting autoCommit back
//                 }
//             }
//         }
//     } catch (SQLException e) {
//         // Error getting connection from pool
//         mensaje = "Error crítico al obtener la conexión: " + e.getMessage();
//     }
//     return mensaje;
// }
```
**Key point for transactions with pooling:** Always ensure `cx.setAutoCommit(true);` is called in a `finally` block within the try-with-resources for the connection. This returns the connection to the pool in its default state.

## 4. Shutting Down the Connection Pool

When your application is shutting down, you should close the HikariCP connection pool to release all database resources.

This can be done by calling the static method `ConexionBD.cerrarPool()`.

**Example: Using a ServletContextListener for a web application**

```java
// import javax.servlet.ServletContextEvent;
// import javax.servlet.ServletContextListener;
// import javax.servlet.annotation.WebListener;
// import seguridad.ConexionBD;
//
// @WebListener
// public class AppLifecycleListener implements ServletContextListener {
//
//     @Override
//     public void contextInitialized(ServletContextEvent sce) {
//         // Pool is initialized statically when ConexionBD class is loaded.
//         // You could explicitly trigger class loading here if needed:
//         // try {
//         //     Class.forName("seguridad.ConexionBD");
//         // } catch (ClassNotFoundException e) {
//         //     sce.getServletContext().log("Failed to initialize ConexionBD", e);
//         // }
//         sce.getServletContext().log("Aplicación iniciada, pool de conexiones (HikariCP) debería estar listo.");
//     }
//
//     @Override
//     public void contextDestroyed(ServletContextEvent sce) {
//         ConexionBD.cerrarPool();
//         sce.getServletContext().log("Aplicación detenida, pool de conexiones (HikariCP) cerrado.");
//     }
// }
```

**Example: Using a Shutdown Hook for a standalone application**

```java
// public class MainApp {
//     public static void main(String[] args) {
//         // Register a shutdown hook
//         Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//             System.out.println("Cerrando pool de conexiones...");
//             ConexionBD.cerrarPool();
//             System.out.println("Pool de conexiones cerrado.");
//         }));
//
//         // Your application logic starts here
//         System.out.println("Aplicación iniciada. Usa Ctrl+C para salir.");
//
//         // Example: Perform some operations that use the database
//         try {
//             // Simulate application work
//             Thread.sleep(5000); // Keep alive for 5 seconds
//             // new EjemploUso().consultarDatos();
//         } catch (InterruptedException e) {
//             Thread.currentThread().interrupt();
//         }
//
//         System.out.println("Terminando aplicación...");
//     }
// }
```

By following these examples, you can effectively use the `ConexionBD` class with HikariCP for robust and efficient database connection management in your application.
Remember to have the `HikariCP.jar` (e.g., `hikaricp-5.0.1.jar`) and its dependencies (like `slf4j-api.jar`) in your classpath. For this project, place them in the `lib` folder.
You will also need the MySQL connector JAR (e.g., `mysql-connector-j-8.2.0.jar`) in the `lib` folder.
The `jbcrypt-0.4.jar` should also be in the `lib` folder if it's a runtime dependency not managed by the build system directly for compilation.
The `build.xml` has been updated to look for JARs in the `lib` directory.
```
