package ui.menu;

import datos.AdministradorDatos;
import entidades.Administrador; // Ensure this is imported
import java.util.Optional;     // Required for handling the result of buscarPorId
import seguridad.Session;
import ui.login.LoginFrame;
import ui.productos.AltaYBaja;
import ui.ventas.Venta;
import ui.productos.Inventario;
import ui.admin.Gestion_Administradores;
import ui.reportes.Historial_Ventas;

/**
 * JFrame que representa el menú principal de la aplicación.
 * Proporciona acceso a las diferentes funcionalidades del sistema,
 * como ventas, inventario, gestión de usuarios y reportes.
 * La visibilidad de ciertas opciones (ej. Administrar Usuarios)
 * puede depender del rol del usuario actual.
 */
public class Menu_Principal extends javax.swing.JFrame {

    /**
     * Constructor. Inicializa componentes, centra la ventana y ajusta
     * la habilitación del botón de administrar usuarios según el rol del admin actual.
     */
    public Menu_Principal() {
        initComponents();
        setLocationRelativeTo(null); // Centrar la ventana

        // Habilitar/deshabilitar botón de gestión de administradores
        // basado en si el usuario actual es un administrador maestro.
        AdministradorDatos adminDatos = new AdministradorDatos();
        int currentAdminId = Session.getIdAdmin(); // Asume que Session.getIdAdmin() devuelve el ID del admin logueado.
        Optional<Administrador> currentAdminOpt = adminDatos.buscarPorId(currentAdminId);

        if (currentAdminOpt.isPresent() && currentAdminOpt.get().adminMaestro()) {
            BtnAdministrarUsuariosAdmins.setEnabled(true);
        } else {
            BtnAdministrarUsuariosAdmins.setEnabled(false);
            BtnAdministrarUsuariosAdmins.setToolTipText("Solo administradores maestros pueden gestionar usuarios.");
        }
    }

    @SuppressWarnings("unchecked")
    // Los comentarios de NetBeans para initComponents y variables se omiten por brevedad.
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        BtnVenta = new javax.swing.JButton();
        BtnAdministrarUsuariosAdmins = new javax.swing.JButton();
        BtnAltaYBaja = new javax.swing.JButton();
        BtnCerrarSesion = new javax.swing.JButton();
        BtnHistorial = new javax.swing.JButton();
        BtnInventario = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menú Principal - Sistema de Ventas"); // Título de la ventana

        jPanel1.setBackground(new java.awt.Color(51, 102, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI Emoji", 0, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255)); // Color de texto para contraste
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER); // Centrar texto
        jLabel1.setText("Bienvenido");

        BtnVenta.setBackground(new java.awt.Color(0, 0, 0));
        BtnVenta.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // Fuente ajustada
        BtnVenta.setForeground(new java.awt.Color(255, 255, 255));
        BtnVenta.setText("Nueva Venta"); // Texto más descriptivo
        BtnVenta.setToolTipText("Iniciar una nueva transacción de venta.");
        BtnVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnVentaActionPerformed(evt);
            }
        });

        BtnAdministrarUsuariosAdmins.setBackground(new java.awt.Color(0, 0, 0));
        BtnAdministrarUsuariosAdmins.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // Fuente ajustada
        BtnAdministrarUsuariosAdmins.setForeground(new java.awt.Color(255, 255, 255));
        BtnAdministrarUsuariosAdmins.setText("Administrar Usuarios");
        BtnAdministrarUsuariosAdmins.setToolTipText("Gestionar cuentas de administradores (solo para Admin Maestro).");
        BtnAdministrarUsuariosAdmins.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAdministrarUsuariosAdminsActionPerformed(evt);
            }
        });

        BtnAltaYBaja.setBackground(new java.awt.Color(0, 0, 0));
        BtnAltaYBaja.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // Fuente ajustada
        BtnAltaYBaja.setForeground(new java.awt.Color(255, 255, 255));
        BtnAltaYBaja.setText("Gestión de Productos"); // Texto más descriptivo
        BtnAltaYBaja.setToolTipText("Agregar, eliminar o modificar productos del inventario.");
        BtnAltaYBaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAltaYBajaActionPerformed(evt);
            }
        });

        BtnCerrarSesion.setBackground(new java.awt.Color(204, 0, 51)); // Color distintivo para cerrar sesión
        BtnCerrarSesion.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // Fuente ajustada
        BtnCerrarSesion.setForeground(new java.awt.Color(255, 255, 255));
        BtnCerrarSesion.setText("Cerrar Sesión"); // Texto más descriptivo
        BtnCerrarSesion.setToolTipText("Finalizar la sesión actual y volver a la pantalla de login.");
        BtnCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCerrarSesionActionPerformed(evt);
            }
        });

        BtnHistorial.setBackground(new java.awt.Color(0, 0, 0));
        BtnHistorial.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // Fuente ajustada
        BtnHistorial.setForeground(new java.awt.Color(255, 255, 255));
        BtnHistorial.setText("Historial de Ventas");
        BtnHistorial.setToolTipText("Consultar el registro de todas las ventas realizadas.");
        BtnHistorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnHistorialActionPerformed(evt);
            }
        });

        BtnInventario.setBackground(new java.awt.Color(0, 0, 0));
        BtnInventario.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // Fuente ajustada
        BtnInventario.setForeground(new java.awt.Color(255, 255, 255));
        BtnInventario.setText("Ver Inventario"); // Texto más descriptivo
        BtnInventario.setToolTipText("Consultar el estado actual del inventario de productos.");
        BtnInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnInventarioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(BtnCerrarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(75, 75, 75)
                        .addComponent(BtnAdministrarUsuariosAdmins, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(297, 297, 297)
                        .addComponent(BtnHistorial, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(92, 92, 92)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BtnAltaYBaja, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BtnInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 920, Short.MAX_VALUE)))
                .addGap(46, 46, 46))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(33, 33, 33)
                    .addComponent(BtnVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(733, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel1)
                .addGap(123, 123, 123)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtnHistorial, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(64, 64, 64)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtnCerrarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnAdministrarUsuariosAdmins, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnAltaYBaja, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(120, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(199, 199, 199)
                    .addComponent(BtnVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(267, Short.MAX_VALUE)))
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

    /**
     * Acción para el botón "Nueva Venta". Abre el frame de Venta.
     */
    private void BtnVentaActionPerformed(java.awt.event.ActionEvent evt) {
        new Venta().setVisible(true);
        this.dispose();
    }

    /**
     * Acción para el botón "Inventario". Abre el frame de Inventario.
     */
    private void BtnInventarioActionPerformed(java.awt.event.ActionEvent evt) {
        new Inventario().setVisible(true);
        this.dispose();
    }

    /**
     * Acción para el botón "Cerrar Sesión". Vuelve al LoginFrame.
     */
    private void BtnCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {
        new LoginFrame().setVisible(true);
        this.dispose();
    }

    /**
     * Acción para el botón "Alta y Baja de Productos". Abre el frame correspondiente.
     */
    private void BtnAltaYBajaActionPerformed(java.awt.event.ActionEvent evt) {
        new AltaYBaja().setVisible(true);
        this.dispose();
    }

    /**
     * Acción para el botón "Administrar Usuarios". Abre el frame de gestión de administradores.
     */
    private void BtnAdministrarUsuariosAdminsActionPerformed(java.awt.event.ActionEvent evt) {
        new Gestion_Administradores().setVisible(true);
        this.dispose();
    }

    /**
     * Acción para el botón "Historial de Ventas". Abre el frame correspondiente.
     */
    private void BtnHistorialActionPerformed(java.awt.event.ActionEvent evt) {
        new Historial_Ventas().setVisible(true);
        this.dispose();
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
            java.util.logging.Logger.getLogger(Menu_Principal.class.getName()).log(java.util.logging.Level.INFO, "Nimbus L&F no disponible.", ex);
        }

        java.awt.EventQueue.invokeLater(() -> new Menu_Principal().setVisible(true));
    }

    // Variables generadas por NetBeans.
    private javax.swing.JButton BtnAdministrarUsuariosAdmins;
    private javax.swing.JButton BtnAltaYBaja;
    private javax.swing.JButton BtnCerrarSesion;
    private javax.swing.JButton BtnHistorial;
    private javax.swing.JButton BtnInventario;
    private javax.swing.JButton BtnVenta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
