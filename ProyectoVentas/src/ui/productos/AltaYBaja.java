package ui.productos;

import datos.ProductoDatos;
import entidades.Producto;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Formulario para alta, baja y consulta de productos.
 * Si “Activo” está marcado, crea o actualiza;
 * si no, elimina.
 * Permite buscar por ID y cargar los datos en los campos.
 * NetBeans guarded blocks conservados para edición visual.
 * @author Liliana
 */
public class AltaYBaja extends javax.swing.JFrame {

    private final ProductoDatos datos = new ProductoDatos();

    public AltaYBaja() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1           = new javax.swing.JPanel();
        jPanel2           = new javax.swing.JPanel();
        jLabel1           = new javax.swing.JLabel();
        jLabel2           = new javax.swing.JLabel();
        jLabel3           = new javax.swing.JLabel();
        jLabel4           = new javax.swing.JLabel();
        txtPrecio         = new javax.swing.JTextField();
        txtNombreProducto = new javax.swing.JTextField();
        txtFechaCaducidad = new javax.swing.JTextField();
        txtCantidad       = new javax.swing.JTextField();
        txtID             = new javax.swing.JTextField();
        jLabel5           = new javax.swing.JLabel();
        jLabel6           = new javax.swing.JLabel();
        jScrollPane1      = new javax.swing.JScrollPane();
        txtDescripcion    = new javax.swing.JTextArea();
        jCheckBoxEsActivo = new javax.swing.JCheckBox();
        BtnBuscarPorID    = new javax.swing.JButton();
        BtnCancelar       = new javax.swing.JButton();
        BtnGuardar1       = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(51, 102, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel1.setText("Nombre");

        jLabel2.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel2.setText("Precio");

        jLabel3.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel3.setText("Descripcion");

        jLabel4.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel4.setText("ID");

        txtPrecio.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        txtNombreProducto.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        txtFechaCaducidad.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        txtCantidad.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        txtID.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel5.setText("Fecha de caducidad");

        jLabel6.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel6.setText("Cantidad");

        txtDescripcion.setColumns(20);
        txtDescripcion.setRows(5);
        jScrollPane1.setViewportView(txtDescripcion);

        jCheckBoxEsActivo.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18)); // NOI18N
        jCheckBoxEsActivo.setText("Activo");

        BtnBuscarPorID.setBackground(new java.awt.Color(0, 0, 0));
        BtnBuscarPorID.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18)); // NOI18N
        BtnBuscarPorID.setForeground(new java.awt.Color(255, 255, 255));
        BtnBuscarPorID.setText("Buscar por ID");
        BtnBuscarPorID.addActionListener(this::BtnBuscarPorIDActionPerformed);

        BtnCancelar.setBackground(new java.awt.Color(0, 0, 0));
        BtnCancelar.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18)); // NOI18N
        BtnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        BtnCancelar.setText("Cancelar");
        BtnCancelar.addActionListener(this::BtnCancelarActionPerformed);

        BtnGuardar1.setBackground(new java.awt.Color(0, 0, 0));
        BtnGuardar1.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18)); // NOI18N
        BtnGuardar1.setForeground(new java.awt.Color(255, 255, 255));
        BtnGuardar1.setText("Guardar");
        BtnGuardar1.addActionListener(this::BtnGuardar1ActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                jPanel2Layout.createSequentialGroup()
                    .addGap(23, 23, 23)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(81, 81, 81)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                     javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5,       javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFechaCaducidad, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(78, 78, 78)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                  javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6,       javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCantidad,    javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPrecio,       javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4,         javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtID,           javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Integer.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                jPanel2Layout.createSequentialGroup()
                    .addGap(50, 50, 50)
                    .addComponent(BtnCancelar,  javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(127, 127, 127)
                    .addComponent(BtnGuardar1,  javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                     javax.swing.GroupLayout.DEFAULT_SIZE, Integer.MAX_VALUE)
                    .addComponent(jCheckBoxEsActivo)
                    .addGap(56, 56, 56)
                    .addComponent(BtnBuscarPorID, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(30, Integer.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrecio,          javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1,       javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFechaCaducidad, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtID,             javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCantidad,       javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(88, 88, 88)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxEsActivo)
                    .addComponent(BtnBuscarPorID, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnCancelar,    javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnGuardar1,    javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(96, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private void BtnBuscarPorIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBuscarPorIDActionPerformed
        try {
            int id = Integer.parseInt(txtID.getText().trim());
            Optional<Producto> opt = datos.buscarPorId(id);
            if (opt.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Producto no encontrado",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Producto p = opt.get();
            // Rellenar campos básicos
            txtNombreProducto.setText(p.nombre());
            txtDescripcion.setText(p.descripcion());
            txtPrecio.setText(String.valueOf(p.precio()));
            txtCantidad.setText(String.valueOf(p.cantidad()));
            jCheckBoxEsActivo.setSelected(p.activo());
    
            // ───formateo correcto ─────────────
            if (p.fechaCaducidad() != null) {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yy");
                txtFechaCaducidad.setText(p.fechaCaducidad().format(fmt));
            } else {
                txtFechaCaducidad.setText("");
            }
            // ────────────────────────────────────────────
    
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "ID inválido",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_BtnBuscarPorIDActionPerformed

    private void BtnGuardar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGuardar1ActionPerformed
        try {
        // 1) Leer y validar campos básicos
        int id = Integer.parseInt(txtID.getText().trim());
        String nombre    = txtNombreProducto.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        double precio    = Double.parseDouble(txtPrecio.getText().trim());
        int cantidad     = Integer.parseInt(txtCantidad.getText().trim());
        boolean activo   = jCheckBoxEsActivo.isSelected();

        if (nombre.isEmpty() || descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Nombre y descripción no pueden estar vacíos",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2) Parsear fecha con el formato dd/MM/yy
        LocalDate fechaCaducidad = null;
        String fc = txtFechaCaducidad.getText().trim();
        if (!fc.isEmpty()) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yy");
            fechaCaducidad = LocalDate.parse(fc, fmt);
        }

        // 3) Construir objeto Producto
        Producto p = new Producto(
            id,
            nombre,
            descripcion,
            precio,
            cantidad,
            fechaCaducidad,
            activo
        );

        // 4) Insertar o actualizar (o eliminar si no está activo)
        if (activo) {
            Optional<Producto> existente = datos.buscarPorId(id);
            if (existente.isPresent()) {
                datos.actualizar(p);
            } else {
                // insertar y obtener el ID generado
                int nuevoId = datos.insertar(p);
                txtID.setText(String.valueOf(nuevoId));
            }
            JOptionPane.showMessageDialog(this,
                "Producto guardado correctamente",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // baja del producto
            if (datos.buscarPorId(id).isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No existe producto con ID " + id,
                    "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                datos.eliminar(id);
                JOptionPane.showMessageDialog(this,
                    "Producto eliminado correctamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        }

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this,
            "Formato numérico inválido en ID, precio o cantidad",
            "Error", JOptionPane.ERROR_MESSAGE);
    } catch (DateTimeParseException ex) {
        JOptionPane.showMessageDialog(this,
            "Formato de fecha inválido (debe ser dd/MM/yy)",
            "Error", JOptionPane.ERROR_MESSAGE);
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this,
            "Error en la base de datos: " + ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_BtnGuardar1ActionPerformed

    private void BtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_BtnCancelarActionPerformed

    // NetBeans-generated empty handlers
    private void txtNombreProductoActionPerformed(java.awt.event.ActionEvent evt) {}
    private void txtFechaCaducidadActionPerformed(java.awt.event.ActionEvent evt) {}
    private void txtCantidadActionPerformed(java.awt.event.ActionEvent evt) {}
    private void txtIDActionPerformed(java.awt.event.ActionEvent evt) {}
    private void txtPrecioActionPerformed(java.awt.event.ActionEvent evt) {}
    private void jCheckBoxEsActivoActionPerformed(java.awt.event.ActionEvent evt) {}
    // End of empty handlers

    public static void main(String args[]) {
        /* Nimbus look & feel */
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
            // si falla, usar L&F por defecto
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(() -> new AltaYBaja().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnBuscarPorID;
    private javax.swing.JButton BtnCancelar;
    private javax.swing.JButton BtnGuardar1;
    private javax.swing.JCheckBox jCheckBoxEsActivo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextArea txtDescripcion;
    private javax.swing.JTextField txtFechaCaducidad;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtNombreProducto;
    private javax.swing.JTextField txtPrecio;
    // End of variables declaration//GEN-END:variables
}


