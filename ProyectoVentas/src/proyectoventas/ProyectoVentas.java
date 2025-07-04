package proyectoventas;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import ui.login.LoginFrame;

public class ProyectoVentas {

    /**
     * Punto de entrada principal de la aplicación.
     * Configura el Look and Feel de la interfaz gráfica e inicia la ventana de login.
     * @param args Argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        // Intenta establecer el Look and Feel Nimbus para una apariencia moderna.
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException 
                 | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            // Si Nimbus no está disponible o falla, se usará el L&F por defecto de Swing.
            // Se podría loguear este error si fuera necesario.
            System.err.println("Nimbus L&F no encontrado o no se pudo aplicar: " + ex.getMessage());
        }

        // Inicia la interfaz gráfica en el Event Dispatch Thread (EDT) de Swing.
        java.awt.EventQueue.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }

}

