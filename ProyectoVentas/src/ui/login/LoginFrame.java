package ui.login;

import servicios.ServicioLogin;
import entidades.Administrador;
import javax.swing.*;
import seguridad.Session;
import ui.menu.Menu_Principal;
import datos.AdministradorDatos;

// Material UI Imports
import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.MaterialOceanicTheme; // Or any other theme
import mdlaf.utils.MaterialColors;
import mdlaf.utils.MaterialFontFactory;
import material.components.MaterialButton;
import material.components.MaterialPasswordField;
import material.components.MaterialTextField;
// Keep JOptionPane for messages, Material L&F will style it
// import material.components.MaterialLabel; // If we decide to use MaterialLabel

public class LoginFrame extends javax.swing.JFrame {

    private final AdministradorDatos adminDatos;
    private final ServicioLogin servicioLogin;

    // Public constructor for production
    public LoginFrame() {
        this(new AdministradorDatos(), new ServicioLogin());
    }

    // Constructor for testing (allows injecting mocks)
    public LoginFrame(AdministradorDatos adminDatos, ServicioLogin servicioLogin) {
        this.adminDatos = adminDatos;
        this.servicioLogin = servicioLogin;

        // It's good practice to set L&F before initComponents if components depend on it
        // However, main is static, so we'll do it there or ensure this frame is created after L&F set.
        // For now, assuming main sets it.
        initComponents();

        // Check if a master admin exists to determine button visibility
        // Use the injected adminDatos instance
        if (!this.adminDatos.existeAdminMaestro()) {
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
        // UsuarioTextField will be MaterialTextField
        // jPasswordField1 will be MaterialPasswordField
        // jLabel1 and jLabel2 will be removed and their text used as labels/hints
        // btnIniciarSesion will be MaterialButton
        // BtnCrearAdmin will be MaterialButton

        UsuarioTextField = new MaterialTextField();
        jPasswordField1 = new MaterialPasswordField();
        btnIniciarSesion = new MaterialButton();
        BtnCrearAdmin = new MaterialButton();
        // jLabel1 and jLabel2 are effectively replaced by setLabel on the fields

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER));
        buttonPanel.add(BtnCrearAdmin);
        buttonPanel.add(btnIniciarSesion);
        // buttonPanel.setOpaque(false); // Theme should handle panel opacity/styling
        // jPanel1.setBackground(new java.awt.Color(0, 102, 255)); // Theme will handle background

        UsuarioTextField.setLabel("Usuario");
        // UsuarioTextField.setFont(new java.awt.Font("Segoe UI", 0, 18)); // Material L&F handles fonts
        UsuarioTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UsuarioTextFieldActionPerformed(evt);
            }
        });

        jPasswordField1.setLabel("Contraseña");
        // jPasswordField1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // Material L&F handles fonts
        jPasswordField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField1ActionPerformed(evt);
            }
        });

        // jLabel1 and jLabel2 are removed. Their text is now set as labels above.

        // btnIniciarSesion.setBackground(new java.awt.Color(0, 0, 0)); // Material L&F handles button colors
        // btnIniciarSesion.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // Material L&F handles fonts
        // btnIniciarSesion.setForeground(new java.awt.Color(255, 255, 255)); // Material L&F handles text colors
        btnIniciarSesion.setText("Iniciar sesión"); // Text is kept
        btnIniciarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarSesionActionPerformed(evt);
            }
        });

        // BtnCrearAdmin.setBackground(new java.awt.Color(0, 0, 0)); // Material L&F handles button colors
        // BtnCrearAdmin.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // Material L&F handles fonts
        // BtnCrearAdmin.setForeground(new java.awt.Color(255, 255, 255)); // Material L&F handles text colors
        BtnCrearAdmin.setText("Crear administrador"); // Text is kept
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
                .addGap(316, 316, 316) // Adjust this gap as Material components might have different preferred sizes
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE) // Keep size for now
                    .addComponent(UsuarioTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)) // Keep size for now
                .addContainerGap(316, Short.MAX_VALUE))
            // jLabel1 and jLabel2 and their specific layout groups are removed.
            // The UsuarioTextField and jPasswordField1 are centered by the .addGap and .addContainerGap in their group.
            .addComponent(buttonPanel, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(138, 138, 138) // Initial gap from top
                .addComponent(UsuarioTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE) // Keep size for now
                .addGap(18, 18, 18) // Adjusted gap between UsuarioTextField and jPasswordField1
                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE) // Keep size for now
                .addGap(45, 45, 45) // Gap before button panel
                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(143, Short.MAX_VALUE)) // Gap after button panel
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
            // 1) Autenticar using the injected servicioLogin instance
            Administrador admin = this.servicioLogin.autenticar(usuario, contraseña);

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
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error al iniciar sesión",
                JOptionPane.ERROR_MESSAGE);
        }
    }                                                

    private void BtnCrearAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCrearAdminActionPerformed
        // Abre ventana de creación de admin
        new ui.admin.CrearAdminFrame().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BtnCrearAdminActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private MaterialButton BtnCrearAdmin;
    private javax.swing.JPanel buttonPanel;
    private MaterialButton btnIniciarSesion;
    private javax.swing.JPanel jPanel1;
    private MaterialPasswordField jPasswordField1;
    private MaterialTextField UsuarioTextField;
    // End of variables declaration//GEN-END:variables

    // Flag for testing dispose behavior
    public boolean isDisposedForTest = false;

    @Override
    public void dispose() {
        super.dispose();
        isDisposedForTest = true;
    }

    public static void main(String args[]) {
        try {
            // Set Material Look and Feel
            UIManager.setLookAndFeel(new mdlaf.MaterialLookAndFeel(new mdlaf.themes.MaterialOceanicTheme()));
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
            // Fallback or error handling if Material L&F fails
            // For example, try Nimbus or system L&F as a fallback:
            /*
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ex) {
                // Handle fallback failure
            }
            */
        }
        java.awt.EventQueue.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}

