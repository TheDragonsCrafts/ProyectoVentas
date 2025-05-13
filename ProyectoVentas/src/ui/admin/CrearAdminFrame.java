package ui.admin;

import datos.AdministradorDatos;
import entidades.Administrador;
import seguridad.UtilHash;
import ui.login.LoginFrame;

import javax.swing.JOptionPane;

/**
 * Formulario para crear nuevos administradores.
 * Conserva los bloques generados por NetBeans para edición visual.
 * @author Liliana
 */
public class CrearAdminFrame extends javax.swing.JFrame {

    // Acceso a datos
    private final AdministradorDatos datos = new AdministradorDatos();

    /**
     * Creates new form CrearAdminFrame
     */
    public CrearAdminFrame() {
        initComponents();
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1                  = new javax.swing.JPanel();
        jLabel1                  = new javax.swing.JLabel();
        txtUsuario               = new javax.swing.JTextField();
        jLabel3                  = new javax.swing.JLabel();
        jPasswordField1          = new javax.swing.JPasswordField();
        jLabel2                  = new javax.swing.JLabel();
        jPasswordFieldConfirmar  = new javax.swing.JPasswordField();
        jLabel5                  = new javax.swing.JLabel();
        txtNombre                = new javax.swing.JTextField();
        jLabel4                  = new javax.swing.JLabel();
        txtCorreo                = new javax.swing.JTextField();
        jCheckBoxAdminMaestro    = new javax.swing.JCheckBox();
        jButtonCrearAdmin        = new javax.swing.JButton();
        jButtonCancelar          = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 102, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel1.setText("Nombre de usuario");

        txtUsuario.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtUsuario.addActionListener(this::txtUsuarioActionPerformed);

        jLabel3.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel3.setText("Contraseña");

        jPasswordField1.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jPasswordField1.addActionListener(this::jPasswordField1ActionPerformed);

        jLabel2.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel2.setText("Confirme contraseña");

        jPasswordFieldConfirmar.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jPasswordFieldConfirmar.addActionListener(this::jPasswordFieldConfirmarActionPerformed);

        jLabel5.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel5.setText("Nombre completo");

        txtNombre.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtNombre.addActionListener(this::txtNombreActionPerformed);

        jLabel4.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel4.setText("Correo");

        txtCorreo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtCorreo.addActionListener(this::txtCorreoActionPerformed);

        jCheckBoxAdminMaestro.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18)); // NOI18N
        jCheckBoxAdminMaestro.setText("Administrador Maestro");
        jCheckBoxAdminMaestro.addActionListener(this::jCheckBoxAdminMaestroActionPerformed);

        jButtonCrearAdmin.setBackground(new java.awt.Color(0, 0, 0));
        jButtonCrearAdmin.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jButtonCrearAdmin.setForeground(new java.awt.Color(255, 255, 255));
        jButtonCrearAdmin.setText("Crear Admin");
        jButtonCrearAdmin.addActionListener(this::jButtonCrearAdminActionPerformed);

        jButtonCancelar.setBackground(new java.awt.Color(0, 0, 0));
        jButtonCancelar.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jButtonCancelar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonCancelar.setText("Cancelar");
        jButtonCancelar.addActionListener(this::jButtonCancelarActionPerformed);

        // Diseño de jPanel1
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(78, 78, 78)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordFieldConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(78, 78, 78)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxAdminMaestro))
                .addGap(78, 78, 78)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonCrearAdmin)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonCancelar)))
                .addContainerGap(75, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPasswordFieldConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxAdminMaestro))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCrearAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        // Diseño de content pane
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

    private void txtUsuarioActionPerformed(java.awt.event.ActionEvent evt) {
        jPasswordField1.requestFocus();
    }

    private void jPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) {
        jPasswordFieldConfirmar.requestFocus();
    }

    private void jPasswordFieldConfirmarActionPerformed(java.awt.event.ActionEvent evt) {
        txtNombre.requestFocus();
    }

    private void txtNombreActionPerformed(java.awt.event.ActionEvent evt) {
        jCheckBoxAdminMaestro.requestFocus();
    }

    private void txtCorreoActionPerformed(java.awt.event.ActionEvent evt) {
        jButtonCrearAdmin.requestFocus();
    }

    private void jCheckBoxAdminMaestroActionPerformed(java.awt.event.ActionEvent evt) {
        // no necesita lógica aquí
    }

    private void jButtonCrearAdminActionPerformed(java.awt.event.ActionEvent evt) {
        String usuario = txtUsuario.getText().trim();
        String pass1   = new String(jPasswordField1.getPassword());
        String pass2   = new String(jPasswordFieldConfirmar.getPassword());
        String nombre  = txtNombre.getText().trim();
        String correo  = txtCorreo.getText().trim();
        boolean esMaestro = jCheckBoxAdminMaestro.isSelected();

        // Validaciones
        if (usuario.isEmpty() || pass1.isEmpty() || pass2.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Completa todos los campos obligatorios",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!pass1.equals(pass2)) {
            JOptionPane.showMessageDialog(this,
                "Las contraseñas no coinciden",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            // Un sólo Admin Maestro
            if (esMaestro && datos.existeAdminMaestro()) {
                JOptionPane.showMessageDialog(this,
                    "Ya existe un Administrador Maestro",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Usuario único
            if (datos.buscarPorUsuario(usuario).isPresent()) {
                JOptionPane.showMessageDialog(this,
                    "El nombre de usuario ya existe",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Crear y persistir
            String hash = UtilHash.hash(pass1);
            Administrador nuevo = new Administrador(
                0, usuario, hash, nombre, correo, true, esMaestro
            );
            datos.insertar(nuevo);
            JOptionPane.showMessageDialog(this,
                "Administrador creado con éxito",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);

            new LoginFrame().setVisible(true);
            this.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error al crear administrador",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        new LoginFrame().setVisible(true);
        this.dispose();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Look & Feel Nimbus */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code ">
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info :
                 javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            // ignora y continúa con L&F por defecto
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(() -> new CrearAdminFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonCrearAdmin;
    private javax.swing.JCheckBox jCheckBoxAdminMaestro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPasswordField jPasswordFieldConfirmar;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}

