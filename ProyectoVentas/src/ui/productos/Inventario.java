package ui.productos;

import datos.ProductoDatos;
import entidades.Producto;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Optional;

/**
 * Frame de inventario: muestra todos los productos o filtra por nombre/ID.
 * @author Liliana
 */
public class Inventario extends javax.swing.JFrame {

    private final ProductoDatos datos = new ProductoDatos();
    private DefaultTableModel model;

    /**
     * Creates new form Inventario
     */
    public Inventario() {
        initComponents();
        // Inicializar modelo y columnas
        model = (DefaultTableModel) jTable1.getModel();
        model.setColumnIdentifiers(new Object[]{
            "ID", "Nombre", "Descripción", "Precio", "Cantidad", "Fecha caducidad", "Activo"
        });
        cargarTodosProductos();
    }

    /**
     * Carga en la tabla todos los productos activos de la BD.
     */
    private void cargarTodosProductos() {
        model.setRowCount(0);
        List<Producto> lista = datos.listarProductos();
        for (Producto p : lista) {
            model.addRow(new Object[]{
                p.id(),
                p.nombre(),
                p.descripcion(),
                p.precio(),
                p.cantidad(),
                p.fechaCaducidad() != null ? p.fechaCaducidad().toString() : "",
                p.activo()
            });
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtProducto = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtProductoID = new javax.swing.JTextField();
        BtnBuscar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 102, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel1.setText("Nombre del producto:");

        txtProducto.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtProducto.addActionListener(this::txtProductoActionPerformed);

        jLabel2.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel2.setText("ID del producto");

        txtProductoID.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtProductoID.addActionListener(this::txtProductoIDActionPerformed);

        BtnBuscar.setBackground(new java.awt.Color(0, 0, 0));
        BtnBuscar.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18)); // NOI18N
        BtnBuscar.setForeground(new java.awt.Color(255, 255, 255));
        BtnBuscar.setText("Buscar");
        BtnBuscar.addActionListener(this::BtnBuscarActionPerformed);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] { "Title 1", "Title 2", "Title 3", "Title 4" }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(jLabel1)
                .addGap(50, 50, 50)
                .addComponent(txtProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(100, 100, 100)
                .addComponent(jLabel2)
                .addGap(50, 50, 50)
                .addComponent(txtProductoID, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(183, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(383, Short.MAX_VALUE)
                .addComponent(BtnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(353, 353, 353))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 753, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(183, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProductoID, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(BtnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
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

    private void BtnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBuscarActionPerformed
        String name = txtProducto.getText().trim();
        String idText = txtProductoID.getText().trim();
        model.setRowCount(0);

        try {
            if (idText.isEmpty() && name.isEmpty()) {
                // ambos vacíos → todos
                cargarTodosProductos();
                return;
            }
            if (!idText.isEmpty()) {
                // buscar por ID
                int id = Integer.parseInt(idText);
                Optional<Producto> opt = datos.buscarPorId(id);
                opt.ifPresent(p -> model.addRow(new Object[]{
                    p.id(), p.nombre(), p.descripcion(),
                    p.precio(), p.cantidad(),
                    p.fechaCaducidad() != null ? p.fechaCaducidad().toString() : "",
                    p.activo()
                }));
            } else {
                // buscar por nombre (contiene)
                List<Producto> lista = datos.buscarPorNombre(name);
                for (Producto p : lista) {
                    model.addRow(new Object[]{
                        p.id(), p.nombre(), p.descripcion(),
                        p.precio(), p.cantidad(),
                        p.fechaCaducidad() != null ? p.fechaCaducidad().toString() : "",
                        p.activo()
                    });
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "ID inválido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_BtnBuscarActionPerformed

    private void txtProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductoActionPerformed
        // vacío, generado por NetBeans
    }//GEN-LAST:event_txtProductoActionPerformed

    private void txtProductoIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductoIDActionPerformed
        // vacío, generado por NetBeans
    }//GEN-LAST:event_txtProductoIDActionPerformed

    /**
     * @param args the command line arguments
     */
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
            // si Nimbus no está disponible, usa look por defecto
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(() -> new Inventario().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnBuscar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtProducto;
    private javax.swing.JTextField txtProductoID;
    // End of variables declaration//GEN-END:variables
}
