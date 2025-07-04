package ui.admin;

import datos.AdministradorDatos;
import entidades.Administrador;
import java.util.List;
import seguridad.UtilHash;
import ui.login.LoginFrame;

import javax.swing.JOptionPane;

/**
 * JFrame para la creación y edición de administradores.
 * Permite establecer un administrador como maestro y maneja la lógica
 * para asegurar que solo exista un administrador maestro o se transfiera el rol.
 */
public class CrearAdminFrame extends javax.swing.JFrame {

    // Acceso a datos
    private final AdministradorDatos datos = new AdministradorDatos();
    private Administrador adminAEditar;
    private boolean editorPerdioRolMaestro = false;
    private LoginFrame loginFrameInstancia; // Referencia para notificar creación del primer admin maestro.

    /**
     * Constructor para la creación inicial del administrador maestro.
     * @param loginFrame Instancia del LoginFrame que invoca, para notificarle.
     */
    public CrearAdminFrame(LoginFrame loginFrame) {
        this(); // Llama al constructor base para inicializar componentes y lógica común.
        this.loginFrameInstancia = loginFrame;
        // La lógica para jCheckBoxAdminMaestro ya está en el constructor base.
    }

    /**
     * Constructor base para crear o editar administradores.
     * Inicializa componentes y ajusta el estado del checkbox "Admin Maestro"
     * según si ya existe uno.
     */
    public CrearAdminFrame() {
        initComponents();
        setLocationRelativeTo(null); // Centra la ventana.

        // Configura el checkbox "Admin Maestro" basado en la existencia de uno.
        if (!this.datos.existeAdminMaestro()) {
            jCheckBoxAdminMaestro.setSelected(true);
            jCheckBoxAdminMaestro.setEnabled(false); // Si no hay, el primero DEBE ser maestro.
        } else {
            jCheckBoxAdminMaestro.setSelected(false);
            jCheckBoxAdminMaestro.setEnabled(true); // Si ya hay, permitir (des)marcar.
        }
    }

    /**
     * Constructor para editar un administrador existente.
     * @param admin El administrador a editar.
     */
    public CrearAdminFrame(Administrador admin) {
        this(); // Llama al constructor base.
        this.adminAEditar = admin;
        inicializarFormularioParaEdicion();
    }

    /**
     * Pre-llena el formulario con los datos del administrador a editar.
     */
    private void inicializarFormularioParaEdicion() {
        if (this.adminAEditar != null) {
            txtUsuario.setText(adminAEditar.usuario());
            txtNombre.setText(adminAEditar.nombreCompleto());
            txtCorreo.setText(adminAEditar.correo());
            jCheckBoxAdminMaestro.setSelected(adminAEditar.adminMaestro());
            jButtonCrearAdmin.setText("Guardar Cambios");
            this.setTitle("Editar Administrador: " + adminAEditar.usuario());
            // Los campos de contraseña se dejan vacíos intencionalmente por seguridad.
        }
    }

    @SuppressWarnings("unchecked")
    // Los comentarios generados por NetBeans para initComponents y variables se mantienen
    // por compatibilidad con el editor visual, pero se omiten aquí por brevedad.
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

                if (this.loginFrameInstancia != null && !datos.existeAdminMaestro()) { // Doble check, aunque ya se sabe que es el primer admin
                    // Esta condición es un poco redundante si jCheckBoxAdminMaestro estaba deshabilitado y seleccionado,
                    // pero asegura que solo se notifique al LoginFrame original si es el primer admin maestro.
                    // La lógica de !datos.existeAdminMaestro() antes de la inserción era más relevante.
                    // Aquí, después de insertar, es más para confirmar que la instancia de loginFrameInstancia es válida.
                }

                // Si fue llamado desde LoginFrame para crear el primer admin, notificarlo.
                // Y solo si es el primer admin maestro creado (esMaestro = true y antes no había ninguno).
                boolean eraCreacionPrimerMaestro = esMaestro && (this.loginFrameInstancia != null);

                if (eraCreacionPrimerMaestro) {
                    this.loginFrameInstancia.primerAdminMaestroCreado();
                } else {
                    // Si es una creación normal (no el primer admin desde LoginFrame, o no es maestro),
                    // o si fue llamado desde Gestion_Administradores, simplemente se cierra.
                    // La lógica anterior de `new LoginFrame().setVisible(true)` se elimina para estos casos.
                    // Si se crea un admin estándar desde Gestion_Administradores, simplemente se cierra y Gestion_Administradores refresca.
                    // Si se crea un admin maestro desde Gestion_Administradores (y ya existe otro, lo cual está prevenido),
                    // esa lógica de prevención ya actuó.
                }
                this.dispose(); // Siempre cerrar este frame
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error al crear administrador",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // ----- MODO EDICIÓN -----
            if (usuario.isEmpty() || nombre.isEmpty() || correo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Los campos Usuario, Nombre completo y Correo no pueden estar vacíos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!pass1.isEmpty() && !pass1.equals(pass2)) {
                JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden. Si no desea cambiar la contraseña, deje ambos campos vacíos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int idEditor = seguridad.Session.getIdAdmin();
                java.util.Optional<Administrador> optEditor = datos.buscarPorId(idEditor);
                if (optEditor.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Error crítico: No se pudo encontrar al administrador que realiza la edición.", "Error Interno", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Administrador editor = optEditor.get();

                // Caso 1: El editor es Admin Maestro y está intentando cambiar el rol de Admin Maestro.
                if (editor.adminMaestro()) {
                    // 1a: El editor (Admin Maestro) está editando a OTRO usuario y lo marca como Admin Maestro (Transferencia)
                    if (esMaestro && adminAEditar.id() != editor.id() && !adminAEditar.adminMaestro()) {
                        int confirm = JOptionPane.showConfirmDialog(this,
                                "Está a punto de transferir su rol de Administrador Maestro a '" + adminAEditar.usuario() + "'.\n" +
                                "Usted perderá su rol de Administrador Maestro y deberá iniciar sesión nuevamente.\n" +
                                "¿Desea continuar?",
                                "Confirmar Transferencia de Rol Maestro", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                        if (confirm == JOptionPane.YES_OPTION) {
                            // Degradar al editor actual
                            Administrador editorDegradado = new Administrador(
                                    editor.id(), editor.usuario(), editor.hash(), editor.nombreCompleto(),
                                    editor.correo(), editor.activo(), false // Quitar rol maestro al editor
                            );
                            if (!datos.actualizar(editorDegradado)) {
                                JOptionPane.showMessageDialog(this, "Error al intentar degradar al actual Administrador Maestro.", "Error de Transferencia", JOptionPane.ERROR_MESSAGE);
                                jCheckBoxAdminMaestro.setSelected(adminAEditar.adminMaestro()); // Revertir UI
                                return;
                            }
                            this.editorPerdioRolMaestro = true; // Marcar que el editor perdió el rol
                            // Promover al admin editado (adminAEditar ya tiene 'esMaestro = true' por el checkbox)
                            // La actualización de adminAEditar se hará al final del método.
                        } else {
                            jCheckBoxAdminMaestro.setSelected(adminAEditar.adminMaestro()); // Revertir UI porque el usuario canceló
                            return; // No continuar con el guardado
                        }
                    }
                    // 1b: El editor (Admin Maestro) se está editando a SÍ MISMO e intenta DESMARCARSE como Admin Maestro
                    else if (!esMaestro && adminAEditar.id() == editor.id()) {
                        int numAdminsMaestros = datos.contarAdministradoresMaestros();
                        if (numAdminsMaestros <= 1) { // Es el único admin maestro
                            List<Administrador> otrosAdminsDisponibles = datos.listarTodos().stream()
                                    .filter(a -> a.id() != editor.id() && a.activo()) // No necesita ser !adminMaestro, ya que se promoverá
                                    .toList();

                            if (otrosAdminsDisponibles.isEmpty()) {
                                JOptionPane.showMessageDialog(this,
                                        "No se puede quitar el rol de Administrador Maestro porque no hay otros administradores activos a quien transferirlo.",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                                jCheckBoxAdminMaestro.setSelected(true); // Revertir el cambio en la UI
                                return; // No continuar con el guardado
                            }

                            Administrador[] posiblesNuevosMaestros = otrosAdminsDisponibles.toArray(new Administrador[0]);
                            Administrador nuevoMaestroSeleccionado = (Administrador) JOptionPane.showInputDialog(
                                    this,
                                    "Debe transferir el rol de Administrador Maestro.\nSeleccione un nuevo Administrador Maestro:",
                                    "Transferir Rol Maestro",
                                    JOptionPane.QUESTION_MESSAGE, null,
                                    posiblesNuevosMaestros, posiblesNuevosMaestros[0]);

                            if (nuevoMaestroSeleccionado == null) {
                                JOptionPane.showMessageDialog(this, "Debe seleccionar un nuevo Administrador Maestro para poder quitar el rol al actual.", "Transferencia cancelada", JOptionPane.WARNING_MESSAGE);
                                jCheckBoxAdminMaestro.setSelected(true); // Revertir
                                return; // Usuario canceló
                            }

                            // Promover al nuevo maestro seleccionado
                            Administrador adminPromovido = new Administrador(
                                    nuevoMaestroSeleccionado.id(), nuevoMaestroSeleccionado.usuario(), nuevoMaestroSeleccionado.hash(),
                                    nuevoMaestroSeleccionado.nombreCompleto(), nuevoMaestroSeleccionado.correo(),
                                    nuevoMaestroSeleccionado.activo(), true);
                            if (!datos.actualizar(adminPromovido)) {
                                JOptionPane.showMessageDialog(this, "Error al promover al nuevo Administrador Maestro.", "Error de Transferencia", JOptionPane.ERROR_MESSAGE);
                                jCheckBoxAdminMaestro.setSelected(true); // Revertir
                                return;
                            }
                            // El admin actual (editor) se degradará porque 'esMaestro' (del checkbox) es false.
                        }
                        // Si hay otros admins maestros (numAdminsMaestros > 1), se permite quitar el rol sin transferir.
                    }
                }
                // Caso 2: Se intenta marcar a un usuario como Admin Maestro cuando ya existe uno y no es una transferencia por el Admin Maestro actual.
                else if (esMaestro && !adminAEditar.adminMaestro() && datos.existeAdminMaestro()) {
                     // Esta condición previene que un admin estándar intente crear un segundo admin maestro,
                     // o que un admin maestro intente crear un segundo admin maestro sin pasar por el flujo de transferencia.
                     // La lógica de transferencia (1a) ya maneja el caso de un admin maestro transfiriendo su rol.
                     // Si el editor no es maestro, no puede designar a otro como maestro si ya existe uno.
                    if (!editor.adminMaestro() || (editor.adminMaestro() && adminAEditar.id() == editor.id() && !esMaestro /*se está desmarcando a sí mismo y ya fue manejado en 1b*/)) {
                         JOptionPane.showMessageDialog(this,
                            "Ya existe un Administrador Maestro. No se puede asignar este rol a otro administrador a menos que el actual Administrador Maestro transfiera el rol.",
                            "Error de Unicidad", JOptionPane.ERROR_MESSAGE);
                        jCheckBoxAdminMaestro.setSelected(adminAEditar.adminMaestro()); // Revertir UI
                        return;
                    }
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

                boolean operacionExitosa = datos.actualizar(adminActualizado);

                if (operacionExitosa) {
                    JOptionPane.showMessageDialog(this,
                            "Administrador actualizado con éxito",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose(); // Se cierra el frame. La redirección al login si es necesaria se manejará en Gestion_Administradores o Menu_Principal
                } else {
                     JOptionPane.showMessageDialog(this,
                            "No se pudo actualizar el administrador.",
                            "Error de Actualización", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error al actualizar administrador",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        // Simplemente cierra este frame. La ventana anterior (Gestion_Administradores o LoginFrame) permanecerá.
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

    public boolean editorPerdioRolMaestro() {
        return this.editorPerdioRolMaestro;
    }
}

