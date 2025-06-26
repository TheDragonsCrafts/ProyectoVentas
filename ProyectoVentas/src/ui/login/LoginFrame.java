package ui.login;

import servicios.ServicioLogin;
import entidades.Administrador;
import javax.swing.*;
import seguridad.Session;
import ui.menu.Menu_Principal;
import datos.AdministradorDatos;

public class LoginFrame extends javax.swing.JFrame {

    // Servicio de login (campo manual, fuera de los guarded blocks)
    private final ServicioLogin servicioLogin = new ServicioLogin();
    private int intentosFallidos = 0; // Contador de intentos fallidos
    private boolean primerAdminFueCreadoExitosamente = false; // Bandera

    public LoginFrame() {
        initComponents();

        // Check if a master admin exists to determine button visibility
        AdministradorDatos adminDatos = new AdministradorDatos();
        if (!adminDatos.existeAdminMaestro()) {
            BtnCrearAdmin.setVisible(true);
            btnIniciarSesion.setVisible(false);
            // It's also a good idea to ensure CrearAdminFrame sets the 'Admin Maestro' checkbox
            // if no master admin exists, but that will be handled in CrearAdminFrame.java
        } else {
            BtnCrearAdmin.setVisible(false);
            btnIniciarSesion.setVisible(true);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        UsuarioTextField = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnIniciarSesion = new javax.swing.JButton();
        BtnCrearAdmin = new javax.swing.JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER));
        buttonPanel.add(BtnCrearAdmin);
        buttonPanel.add(btnIniciarSesion);
        // Optional: make buttonPanel transparent if its background clashes with jPanel1
buttonPanel.setOpaque(false);

        jPanel1.setBackground(new java.awt.Color(0, 102, 255));

        UsuarioTextField.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        UsuarioTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UsuarioTextFieldActionPerformed(evt);
            }
        });

        jPasswordField1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPasswordField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel1.setText("Usuario");

        jLabel2.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel2.setText("Contraseña");

        btnIniciarSesion.setBackground(new java.awt.Color(0, 0, 0));
        btnIniciarSesion.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        btnIniciarSesion.setForeground(new java.awt.Color(255, 255, 255));
        btnIniciarSesion.setText("Iniciar sesión");
        btnIniciarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarSesionActionPerformed(evt);
            }
        });

        BtnCrearAdmin.setBackground(new java.awt.Color(0, 0, 0));
        BtnCrearAdmin.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        BtnCrearAdmin.setForeground(new java.awt.Color(255, 255, 255));
        BtnCrearAdmin.setText("Crear administrador");
        BtnCrearAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCrearAdminActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(316, 316, 316)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPasswordField1)
                    .addComponent(UsuarioTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE))
                .addContainerGap(316, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(413, 413, 413)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(394, 394, 394)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(buttonPanel, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(138, 138, 138)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(UsuarioTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(143, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void UsuarioTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UsuarioTextFieldActionPerformed
        // Enter en usuario pasa el foco a contraseña
        jPasswordField1.requestFocus();
    }//GEN-LAST:event_UsuarioTextFieldActionPerformed

    private void jPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField1ActionPerformed
        // Enter en contraseña dispara el login
        btnIniciarSesionActionPerformed(evt);
    }//GEN-LAST:event_jPasswordField1ActionPerformed

    private void btnIniciarSesionActionPerformed(java.awt.event.ActionEvent evt) {
         String usuario    = UsuarioTextField.getText().trim();
        String contraseña = new String(jPasswordField1.getPassword());

        try {
            // 1) Autenticar
            Administrador admin = new ServicioLogin()
                                      .autenticar(usuario, contraseña);

            // 2) Guardar el id del admin en la sesión
            Session.setIdAdmin(admin.id());

            // 3) Mensaje de bienvenida (opcional)
            JOptionPane.showMessageDialog(this,
                "¡Bienvenido, " + admin.nombreCompleto() + "!",
                "Login exitoso",
                JOptionPane.INFORMATION_MESSAGE);

            // 4) Abrir menú principal
            new Menu_Principal().setVisible(true);
            this.dispose();

        } catch (Exception ex) {
            intentosFallidos++;
            if (intentosFallidos >= 5) {
                JOptionPane.showMessageDialog(this,
                    "Ha superado el número máximo de intentos de inicio de sesión.",
                    "Límite de intentos excedido",
                    JOptionPane.ERROR_MESSAGE);
                System.exit(0); // Cierra la aplicación
            } else {
                JOptionPane.showMessageDialog(this,
                    ex.getMessage() + "\nIntentos restantes: " + (5 - intentosFallidos),
                    "Error al iniciar sesión",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }                                                

    private void BtnCrearAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCrearAdminActionPerformed
        // Abre ventana de creación de admin, pasando esta instancia de LoginFrame
        ui.admin.CrearAdminFrame crearAdminFrame = new ui.admin.CrearAdminFrame(this);

        // Escuchar cuando se cierra CrearAdminFrame
        crearAdminFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                if (!primerAdminFueCreadoExitosamente) {
                    // Si CrearAdminFrame se cerró (ej. por cancelación) y el primer admin NO fue creado,
                    // volver a mostrar este LoginFrame.
                    LoginFrame.this.setVisible(true);
                    // Re-evaluar estado de botones por si acaso, aunque no debería haber cambiado
                    AdministradorDatos adminDatosCheck = new AdministradorDatos();
                    if (!adminDatosCheck.existeAdminMaestro()) {
                        BtnCrearAdmin.setVisible(true);
                        btnIniciarSesion.setVisible(false);
                    } else {
                        BtnCrearAdmin.setVisible(false);
                        btnIniciarSesion.setVisible(true);
                    }
                }
                // Si primerAdminFueCreadoExitosamente es true, este LoginFrame ya se habrá cerrado
                // y uno nuevo se habrá abierto desde primerAdminMaestroCreado().
            }
        });

        crearAdminFrame.setVisible(true);
        this.setVisible(false); // Ocultar este LoginFrame en lugar de cerrarlo
    }//GEN-LAST:event_BtnCrearAdminActionPerformed

    /**
     * Este método es llamado por CrearAdminFrame cuando el primer administrador maestro
     * ha sido creado exitosamente.
     */
    public void primerAdminMaestroCreado() {
        this.primerAdminFueCreadoExitosamente = true;
        // Cerrar esta instancia de LoginFrame (que está oculta)
        this.dispose();
        // Abrir una nueva instancia de LoginFrame, que ahora mostrará el botón de Iniciar Sesión
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnCrearAdmin;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton btnIniciarSesion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField UsuarioTextField;
    // End of variables declaration//GEN-END:variables

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            // Si falla, se ignora y se usa el L&F por defecto
        }
        java.awt.EventQueue.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}

