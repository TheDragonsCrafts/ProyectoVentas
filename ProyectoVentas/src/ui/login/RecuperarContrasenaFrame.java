/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui.login;

/**
/**
 * JFrame para la recuperación de contraseñas.
 * Permite al usuario ingresar su nombre de usuario, correo y nueva contraseña.
 */
public class RecuperarContrasenaFrame extends javax.swing.JFrame {

    /**
     * Constructor. Inicializa los componentes y centra la ventana.
     */
    public RecuperarContrasenaFrame() {
        initComponents();
        setLocationRelativeTo(null); // Centrar la ventana
    }

    /**
     * Código generado por NetBeans para inicializar el formulario.
     * Los comentarios de NetBeans se omiten por brevedad.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        CorreoTextField = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnCambiarContrasena = new javax.swing.JButton();
        UsuarioTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btnIniciarSesion = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE); // Cambiado a DISPOSE_ON_CLOSE
        setTitle("Recuperar Contraseña"); // Título de la ventana

        jPanel1.setBackground(new java.awt.Color(0, 102, 255));

        CorreoTextField.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        CorreoTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CorreoTextFieldActionPerformed(evt);
            }
        });

        jPasswordField1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPasswordField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel1.setText("Correo Electrónico"); // Etiqueta más descriptiva

        jLabel2.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel2.setText("Nueva Contraseña"); // Etiqueta más descriptiva

        btnCambiarContrasena.setBackground(new java.awt.Color(0, 0, 0));
        btnCambiarContrasena.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        btnCambiarContrasena.setForeground(new java.awt.Color(255, 255, 255));
        btnCambiarContrasena.setText("Cambiar Contraseña");
        btnCambiarContrasena.setToolTipText("Intentar cambiar la contraseña con los datos ingresados."); // Tooltip
        btnCambiarContrasena.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCambiarContrasenaActionPerformed(evt);
            }
        });

        UsuarioTextField1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        UsuarioTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UsuarioTextField1ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel3.setText("Nombre de Usuario"); // Etiqueta más descriptiva

        btnIniciarSesion.setBackground(new java.awt.Color(0, 0, 0));
        btnIniciarSesion.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        btnIniciarSesion.setForeground(new java.awt.Color(255, 255, 255));
        btnIniciarSesion.setText("Volver al Login"); // Texto del botón cambiado
        btnIniciarSesion.setToolTipText("Regresar a la pantalla de inicio de sesión."); // Tooltip
        btnIniciarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarSesionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(CorreoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(312, 312, 312))
                        .addComponent(UsuarioTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(367, 367, 367))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(376, 376, 376)
                        .addComponent(jLabel2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(275, 275, 275)
                        .addComponent(btnCambiarContrasena)
                        .addGap(18, 18, 18)
                        .addComponent(btnIniciarSesion)))
                .addGap(0, 227, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(370, 370, 370)
                    .addComponent(jLabel3)
                    .addContainerGap(374, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(141, Short.MAX_VALUE)
                .addComponent(UsuarioTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CorreoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(29, 29, 29)
                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCambiarContrasena, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnIniciarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(72, 72, 72))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(103, 103, 103)
                    .addComponent(jLabel3)
                    .addContainerGap(474, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CorreoTextFieldActionPerformed(java.awt.event.ActionEvent evt) {
        jPasswordField1.requestFocus(); // Mover foco al campo de nueva contraseña
    }

    private void jPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) {
        btnCambiarContrasenaActionPerformed(evt); // Intentar cambiar contraseña al presionar Enter
    }

    private void btnCambiarContrasenaActionPerformed(java.awt.event.ActionEvent evt) {
        String usuario = UsuarioTextField1.getText().trim();
        String correo = CorreoTextField.getText().trim();
        String nuevaContrasena = new String(jPasswordField1.getPassword());

        if (usuario.isEmpty() || correo.isEmpty() || nuevaContrasena.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Campos Incompletos", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        datos.AdministradorDatos adminDatos = new datos.AdministradorDatos();
        boolean actualizado = adminDatos.actualizarContrasenaPorUsuarioYCorreo(usuario, correo, nuevaContrasena);

        if (actualizado) {
            javax.swing.JOptionPane.showMessageDialog(this, "Contraseña actualizada exitosamente. Ahora puede iniciar sesión.", "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            new LoginFrame().setVisible(true);
            this.dispose();
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "No se pudo actualizar la contraseña. Verifique que el nombre de usuario y el correo electrónico sean correctos y que pertenezcan a una cuenta existente.", "Error de Actualización", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void UsuarioTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
        CorreoTextField.requestFocus(); // Mover foco al campo de correo
    }

    private void btnIniciarSesionActionPerformed(java.awt.event.ActionEvent evt) {
        new LoginFrame().setVisible(true);
        this.dispose(); // Cierra la ventana actual de recuperación
    }

    /**
     * Método main para pruebas (opcional).
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(RecuperarContrasenaFrame.class.getName()).log(java.util.logging.Level.INFO, "Nimbus L&F no disponible, usando L&F por defecto.", ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new RecuperarContrasenaFrame().setVisible(true);
        });
    }

    // Declaración de variables generada por NetBeans.
    private javax.swing.JTextField CorreoTextField;
    private javax.swing.JTextField UsuarioTextField1;
    private javax.swing.JButton btnCambiarContrasena;
    private javax.swing.JButton btnIniciarSesion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordField1;
    // End of variables declaration//GEN-END:variables
}
