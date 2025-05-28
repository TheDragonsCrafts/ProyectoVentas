package ui.admin;

import datos.AdministradorDatos;
import entidades.Administrador;
import seguridad.UtilHash;
import ui.login.LoginFrame;
import ui.menu.Menu_Principal;

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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jPasswordFieldConfirmar = new javax.swing.JPasswordField();
        txtUsuario = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtCorreo = new javax.swing.JTextField();
        jCheckBoxAdminMaestro = new javax.swing.JCheckBox();
        jButtonCrearAdmin = new javax.swing.JButton();
        jButtonCancelar = new javax.swing.JButton();
        BtnRegresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 102, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel1.setText("Nombre de usuario");

        jLabel2.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel2.setText("Confirme contraseña");

        jLabel3.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel3.setText("Contraseña");

        jPasswordField1.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jPasswordField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField1ActionPerformed(evt);
            }
        });

        jPasswordFieldConfirmar.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jPasswordFieldConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordFieldConfirmarActionPerformed(evt);
            }
        });

        txtUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsuarioActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel4.setText("Correo");

        jLabel5.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel5.setText("Nombre completo");

        txtNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreActionPerformed(evt);
            }
        });

        txtCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCorreoActionPerformed(evt);
            }
        });

        jCheckBoxAdminMaestro.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18)); // NOI18N
        jCheckBoxAdminMaestro.setText("Administrador Maestro");
        jCheckBoxAdminMaestro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxAdminMaestroActionPerformed(evt);
            }
        });

        jButtonCrearAdmin.setBackground(new java.awt.Color(0, 0, 0));
        jButtonCrearAdmin.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jButtonCrearAdmin.setForeground(new java.awt.Color(255, 255, 255));
        jButtonCrearAdmin.setText("Crear Admin");
        jButtonCrearAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCrearAdminActionPerformed(evt);
            }
        });

        jButtonCancelar.setBackground(new java.awt.Color(0, 0, 0));
        jButtonCancelar.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jButtonCancelar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonCancelar.setText("Cancelar");
        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarActionPerformed(evt);
            }
        });

        BtnRegresar.setBackground(new java.awt.Color(0, 0, 0));
        BtnRegresar.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18)); // NOI18N
        BtnRegresar.setForeground(new java.awt.Color(255, 255, 255));
        BtnRegresar.setText("Regresar");
        BtnRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnRegresarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPasswordFieldConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(192, 192, 192)
                        .addComponent(jButtonCrearAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49)
                        .addComponent(jButtonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(311, 311, 311)
                        .addComponent(jCheckBoxAdminMaestro))
                    .addComponent(jLabel5)
                    .addComponent(jLabel3)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(335, 335, 335)
                        .addComponent(BtnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(566, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jPasswordFieldConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonCrearAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jCheckBoxAdminMaestro)))
                .addGap(27, 27, 27)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnRegresarActionPerformed
        new ui.menu.Menu_Principal().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BtnRegresarActionPerformed

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

            // new LoginFrame().setVisible(true); // Not needed here, handled by window listener
            this.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error al crear administrador",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        // new LoginFrame().setVisible(true); // Not needed here, handled by window listener
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
    private javax.swing.JButton BtnRegresar;
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

