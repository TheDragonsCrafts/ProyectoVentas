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
    private Administrador adminAEditar;

    /**
     * Creates new form CrearAdminFrame
     */
    public CrearAdminFrame() {
        initComponents();
        setLocationRelativeTo(null);

        // Logic to handle 'Admin Maestro' checkbox based on existing master admin
        // This code should be in the constructor CrearAdminFrame()
        // It seems 'datos' is already a field: private final AdministradorDatos datos = new AdministradorDatos();
        // So we can use that.
        if (!this.datos.existeAdminMaestro()) {
            jCheckBoxAdminMaestro.setSelected(true);
            jCheckBoxAdminMaestro.setEnabled(false);
        } else {
            // Optional: Ensure it's not selected and is enabled if a master admin already exists
            // and this frame is somehow opened directly (though current logic in LoginFrame should prevent this specific scenario for initial creation)
            jCheckBoxAdminMaestro.setSelected(false);
            jCheckBoxAdminMaestro.setEnabled(true);
        }
    }

    public CrearAdminFrame(Administrador admin) {
        this.adminAEditar = admin;
        initComponents();
        setLocationRelativeTo(null);
        inicializarFormularioParaEdicion();
    }

    private void inicializarFormularioParaEdicion() {
        if (this.adminAEditar != null) {
            txtUsuario.setText(adminAEditar.usuario());
            txtNombre.setText(adminAEditar.nombreCompleto());
            txtCorreo.setText(adminAEditar.correo());
            jCheckBoxAdminMaestro.setSelected(adminAEditar.adminMaestro());
            jButtonCrearAdmin.setText("Guardar Cambios");
            this.setTitle("Editar Administrador");
            // Los campos de contraseña se dejan vacíos intencionalmente
        }
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)))
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
                .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
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
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        boolean esMaestro = jCheckBoxAdminMaestro.isSelected();
        String pass1 = new String(jPasswordField1.getPassword());
        String pass2 = new String(jPasswordFieldConfirmar.getPassword());

        if (this.adminAEditar == null) {
            // ----- MODO CREACIÓN -----
            // Validaciones para Creación
            if (usuario.isEmpty() || pass1.isEmpty() || pass2.isEmpty() || nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Completa todos los campos obligatorios (usuario, contraseñas, nombre)",
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
                // Un sólo Admin Maestro (en creación)
                if (esMaestro && datos.existeAdminMaestro()) {
                    JOptionPane.showMessageDialog(this,
                            "Ya existe un Administrador Maestro. Solo puede haber uno.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Usuario único (en creación)
                if (datos.buscarPorUsuario(usuario).isPresent()) {
                    JOptionPane.showMessageDialog(this,
                            "El nombre de usuario ya existe",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Crear y persistir
                String hash = UtilHash.hash(pass1);
                Administrador nuevo = new Administrador(
                        0, usuario, hash, nombre, correo, true, esMaestro);
                datos.insertar(nuevo);
                JOptionPane.showMessageDialog(this,
                        "Administrador creado con éxito",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                // Navegar a Login después de crear el admin (especialmente el maestro)
                new LoginFrame().setVisible(true);
                this.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error al crear administrador",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // ----- MODO EDICIÓN -----
            // Validaciones para Edición
            if (usuario.isEmpty() || nombre.isEmpty() || correo.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Los campos Usuario, Nombre completo y Correo no pueden estar vacíos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validación de contraseña (solo si se intenta cambiar)
            if (!pass1.isEmpty() && !pass1.equals(pass2)) {
                JOptionPane.showMessageDialog(this,
                        "Las contraseñas no coinciden. Si no desea cambiar la contraseña, deje ambos campos vacíos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Admin Maestro Unicidad (en edición)
                if (esMaestro && !this.adminAEditar.adminMaestro() && datos.existeAdminMaestro()) { // Intenta asignar rol Maestro a OTRO admin cuando ya existe un Maestro
                    JOptionPane.showMessageDialog(this,
                            "Ya existe un Administrador Maestro. No se puede asignar este rol a otro administrador.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (!esMaestro && this.adminAEditar.adminMaestro()) { // Intenta QUITAR rol Maestro al admin actual
                    int numAdminsMaestros = datos.contarAdministradoresMaestros();
                    int numAdminsActivos = datos.contarAdministradoresActivos(); // Podríamos querer solo activos que no sean el actual

                    if (numAdminsMaestros <= 1) { // Es el único admin maestro
                        List<Administrador> otrosAdminsDisponibles = datos.listarTodos().stream()
                                .filter(a -> a.id() != this.adminAEditar.id() && a.activo() && !a.adminMaestro())
                                .toList();

                        if (otrosAdminsDisponibles.isEmpty()) {
                            JOptionPane.showMessageDialog(this,
                                    "No se puede quitar el rol de Administrador Maestro porque no hay otros administradores activos a quien transferirlo.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            jCheckBoxAdminMaestro.setSelected(true); // Revertir el cambio en la UI
                            return;
                        }

                        // Solicitar la selección de un nuevo admin maestro
                        Administrador[] posiblesNuevosMaestros = otrosAdminsDisponibles.toArray(new Administrador[0]);
                        Administrador nuevoMaestroSeleccionado = (Administrador) JOptionPane.showInputDialog(
                                this,
                                "Debe transferir el rol de Administrador Maestro.\nSeleccione un nuevo Administrador Maestro:",
                                "Transferir Rol Maestro",
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                posiblesNuevosMaestros,
                                posiblesNuevosMaestros[0]);

                        if (nuevoMaestroSeleccionado == null) {
                            JOptionPane.showMessageDialog(this, "Debe seleccionar un nuevo Administrador Maestro para poder quitar el rol al actual.", "Transferencia cancelada", JOptionPane.WARNING_MESSAGE);
                            jCheckBoxAdminMaestro.setSelected(true); // Revertir
                            return; // El usuario canceló la selección
                        }
                        // Actualizar el admin seleccionado para que sea el nuevo maestro
                        Administrador adminActualizadoParaNuevoMaestro = new Administrador(
                            nuevoMaestroSeleccionado.id(), nuevoMaestroSeleccionado.usuario(), nuevoMaestroSeleccionado.hash(),
                            nuevoMaestroSeleccionado.nombreCompleto(), nuevoMaestroSeleccionado.correo(),
                            nuevoMaestroSeleccionado.activo(), true // esAdminMaestro = true
                        );
                        datos.actualizar(adminActualizadoParaNuevoMaestro);
                        // El admin actual (adminAEditar) perderá su rol de maestro más adelante en este mismo método.
                    }
                    // Si hay otros admins maestros (numAdminsMaestros > 1), se permite quitar el rol sin transferir.
                }


                // Usuario único (en edición, si cambió el usuario)
                if (!usuario.equals(this.adminAEditar.usuario()) && datos.buscarPorUsuario(usuario).isPresent()) {
                    JOptionPane.showMessageDialog(this,
                            "El nuevo nombre de usuario ya existe",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Preparar datos para actualizar
                String passwordHashParaActualizar;
                if (!pass1.isEmpty()) { // Si se ingresó nueva contraseña
                    passwordHashParaActualizar = UtilHash.hash(pass1);
                } else { // Mantener contraseña anterior
                    passwordHashParaActualizar = this.adminAEditar.hash(); // Reverted to .hash()
                }

                Administrador adminActualizado = new Administrador(
                        this.adminAEditar.id(),
                        usuario,
                        passwordHashParaActualizar,
                        nombre,
                        correo,
                        this.adminAEditar.activo(), // Mantener estado de actividad
                        esMaestro);

                datos.actualizar(adminActualizado);
                JOptionPane.showMessageDialog(this,
                        "Administrador actualizado con éxito",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error al actualizar administrador",
                        JOptionPane.ERROR_MESSAGE);
            }
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

