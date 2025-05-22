package proyectoventas;

import datos.AdministradorDatos;
import excepciones.BaseDatosException;
import seguridad.ConexionBD;
import seguridad.Session;
import ui.login.Login;
import ui.menu.Menu;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ProyectoVentas {
    private static final Logger LOGGER = Logger.getLogger(ProyectoVentas.class.getName());

    public static void main(String[] args) {
        // Configurar el Look and Feel para que la UI se vea más moderna
        try {
            // Intenta establecer Nimbus Look and Feel
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            // Si Nimbus no está disponible, el sistema usará el L&F por defecto.
            // Se puede loguear el error si se desea.
            LOGGER.log(Level.WARNING, "Nimbus Look and Feel no pudo ser establecido.", ex);
        }

        // Añadir shutdown hook para cerrar el pool de conexiones
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.log(Level.INFO, "Iniciando cierre del pool de conexiones BD...");
            ConexionBD.cerrarPool(); // Asegúrate que este método es estático y accesible
            LOGGER.log(Level.INFO, "Pool de conexiones BD cerrado.");
            // Potentially add other cleanup tasks here if needed, like closing log files.
        }));
        
        // Trigger static block of ConexionBD to initialize the pool early
        // and catch any startup errors related to DB connection.
        try {
            Class.forName("seguridad.ConexionBD");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Driver de ConexionBD no encontrado. La aplicación no puede iniciar.", e);
            JOptionPane.showMessageDialog(null, 
                "Error crítico: Driver de base de datos no encontrado.\n" + e.getMessage(), 
                "Error de Inicio", JOptionPane.ERROR_MESSAGE);
            return; // Salir de la aplicación
        }
        // Check if ds is null after static block, which indicates a severe configuration error
        // (This check might be redundant if ConexionBD's static block throws a RuntimeException on failure)
        try {
            if (ConexionBD.getConnection() == null) { // Simple way to check if pool is usable
                 LOGGER.log(Level.SEVERE, "El pool de conexiones no se pudo inicializar. Verifique la configuración de la BD y los logs.");
                 JOptionPane.showMessageDialog(null, 
                    "Error crítico: No se pudo conectar a la base de datos.\nLa aplicación se cerrará.", 
                    "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Close the test connection immediately
            ConexionBD.getConnection().close();
        } catch (Exception e) { // Catch SQLException or any other exception from getConnection()
             LOGGER.log(Level.SEVERE, "Error al intentar obtener una conexión de prueba del pool.", e);
             JOptionPane.showMessageDialog(null, 
                "Error crítico: No se pudo establecer una conexión de prueba con la base de datos.\n" + e.getMessage() + "\nLa aplicación se cerrará.", 
                "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            return;
        }


        // Verificar si existe un administrador maestro
        AdministradorDatos adminDatos = new AdministradorDatos();
        boolean existeAdminMaestro;
        try {
            existeAdminMaestro = adminDatos.existeAdminMaestro();
        } catch (BaseDatosException e) {
            LOGGER.log(Level.SEVERE, "Error al verificar la existencia del administrador maestro. La aplicación no puede continuar.", e);
            JOptionPane.showMessageDialog(null, 
                "Error crítico al acceder a la base de datos para verificar usuarios.\n" + e.getMessage() + "\nLa aplicación se cerrará.", 
                "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            return; // Salir de la aplicación
        }

        if (!existeAdminMaestro) {
            // Aquí se podría abrir un diálogo especial para crear el primer administrador maestro
            // Por ahora, mostramos un mensaje y cerramos.
            LOGGER.log(Level.SEVERE, "No existe un administrador maestro. La aplicación no puede continuar sin un administrador inicial.");
            JOptionPane.showMessageDialog(null,
                    "No existe un administrador maestro en el sistema.\n" +
                    "Por favor, contacte al soporte técnico o ejecute el script de configuración inicial.",
                    "Error Crítico - Falta Administrador Maestro",
                    JOptionPane.ERROR_MESSAGE);
            return; // Salir de la aplicación
        }
        
        // Si todo está bien, proceder con la lógica de login y menú principal
        java.awt.EventQueue.invokeLater(() -> {
            Login loginForm = new Login();
            loginForm.setVisible(true);
            // El flujo de la aplicación continúa desde el login exitoso
            // donde se abre el menú principal.
        });
    }

    public static void mostrarMenuPrincipal() {
        // Cierra todas las ventanas de login si están abiertas
        // (Esto podría ser más robusto buscando instancias de Login)
        for (java.awt.Window window : java.awt.Dialog.getWindows()) {
            if (window instanceof Login) {
                window.dispose();
            }
        }
        
        Menu menuPrincipal = new Menu(Session.getInstance().getAdminLogueado());
        menuPrincipal.setVisible(true);
    }
}
