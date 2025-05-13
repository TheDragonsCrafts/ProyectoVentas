package proyectoventas;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import ui.login.LoginFrame;

public class ProyectoVentas {

    public static void main(String[] args) {
        // 1. Fijar Look & Feel Nimbus (o el que prefieras)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException 
                 | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            // Si Nimbus no está disponible, continúa con el L&F por defecto
        }

        // 2. Arrancar GUI en el hilo de Swing
        java.awt.EventQueue.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }

}

