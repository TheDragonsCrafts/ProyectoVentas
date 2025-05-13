package ui.ventas;

import servicios.ServicioVentas;
import servicios.ServicioInventario;
import entidades.Producto;
import entidades.DetalleVenta;
import seguridad.Session;
import ui.login.LoginFrame;

import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Frame para procesar ventas.
 * Lee idAdmin desde Session, permite agregar/editar líneas,
 * muestra total y al “Pagar” invoca ServicioVentas.generarVenta.
 */
public class Venta extends javax.swing.JFrame {

    // Servicios
    private final ServicioVentas servicioVentas = new ServicioVentas();
    private final ServicioInventario servicioInv = new ServicioInventario();

    // Modelo de la tabla y listas de trabajo
    private DefaultTableModel model;
    private List<Producto> productosList;
    private List<DetalleVenta> detallesList;

    // ID del administrador autenticado
    private final int idAdmin;

    /** Constructor: toma idAdmin de Session y prepara GUI */
    public Venta() {
        this.idAdmin = Session.getIdAdmin();
        this.detallesList = new ArrayList<>();
        initComponents();
        model = (DefaultTableModel) jTable1.getModel();
        inicializarTabla();
        cargarProductos();
        // Mover listener aquí para evitar NPE
        CBProducto.addActionListener(this::CBProductoActionPerformed);
    }

    /** Código generado por NetBeans para crear los componentes */
    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        CBProducto = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        SpinnerCantidad = new javax.swing.JSpinner();
        BtnAgregar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        BtnEditar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        BtnPagar = new javax.swing.JButton();
        BtnCancelar = new javax.swing.JButton();
        txtPagoTotal = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 102, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24));
        jLabel1.setText("Producto a vender");

        CBProducto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel2.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24));
        jLabel2.setText("Cantidad");

        SpinnerCantidad.setFont(new java.awt.Font("Segoe UI", 0, 18));

        BtnAgregar.setBackground(new java.awt.Color(0, 0, 0));
        BtnAgregar.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18));
        BtnAgregar.setForeground(new java.awt.Color(255, 255, 255));
        BtnAgregar.setText("Agregar");
        BtnAgregar.addActionListener(this::BtnAgregarActionPerformed);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] { "Producto", "Cantidad", "Precio unit.", "Subtotal" }
        ));
        jScrollPane1.setViewportView(jTable1);

        BtnEditar.setBackground(new java.awt.Color(0, 0, 0));
        BtnEditar.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18));
        BtnEditar.setForeground(new java.awt.Color(255, 255, 255));
        BtnEditar.setText("Editar");
        BtnEditar.addActionListener(this::BtnEditarActionPerformed);

        jLabel3.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24));
        jLabel3.setText("Total a Pagar");

        BtnPagar.setBackground(new java.awt.Color(0, 0, 0));
        BtnPagar.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18));
        BtnPagar.setForeground(new java.awt.Color(255, 255, 255));
        BtnPagar.setText("Pagar");
        BtnPagar.addActionListener(this::BtnPagarActionPerformed);

        BtnCancelar.setBackground(new java.awt.Color(0, 0, 0));
        BtnCancelar.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18));
        BtnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        BtnCancelar.setText("Cancelar");
        BtnCancelar.addActionListener(this::BtnCancelarActionPerformed);

        txtPagoTotal.setEditable(false);
        txtPagoTotal.setFont(new java.awt.Font("Segoe UI", 0, 18));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34,34,34)
                .addComponent(BtnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE,163,javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(179,179,179)
                .addComponent(txtPagoTotal, javax.swing.GroupLayout.PREFERRED_SIZE,205,javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,180,Short.MAX_VALUE)
                .addComponent(BtnPagar, javax.swing.GroupLayout.PREFERRED_SIZE,180,javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(164,164,164)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE,626,javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,jPanel1Layout.createSequentialGroup()
                .addGap(410,410,410)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(54,54,54)
                .addComponent(CBProducto,javax.swing.GroupLayout.PREFERRED_SIZE,223,javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(160,160,160)
                .addComponent(SpinnerCantidad,javax.swing.GroupLayout.PREFERRED_SIZE,169,javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,116,Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,false)
                    .addComponent(BtnAgregar,javax.swing.GroupLayout.DEFAULT_SIZE,139,Short.MAX_VALUE)
                    .addComponent(BtnEditar,javax.swing.GroupLayout.DEFAULT_SIZE,javax.swing.GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE))
                .addGap(76,76,76))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(65,65,65)
                .addComponent(jLabel1)
                .addGap(203,203,203)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24,24,24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(18,18,18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CBProducto,javax.swing.GroupLayout.PREFERRED_SIZE,45,javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SpinnerCantidad,javax.swing.GroupLayout.PREFERRED_SIZE,45,javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(BtnAgregar,javax.swing.GroupLayout.PREFERRED_SIZE,50,javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18,18,18)
                        .addComponent(BtnEditar,javax.swing.GroupLayout.PREFERRED_SIZE,48,javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(29,29,29)
                .addComponent(jScrollPane1,javax.swing.GroupLayout.PREFERRED_SIZE,197,javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35,35,35)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtnCancelar,javax.swing.GroupLayout.PREFERRED_SIZE,56,javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnPagar,javax.swing.GroupLayout.PREFERRED_SIZE,56,javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPagoTotal,javax.swing.GroupLayout.PREFERRED_SIZE,49,javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(59,Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1,javax.swing.GroupLayout.DEFAULT_SIZE,javax.swing.GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1,javax.swing.GroupLayout.DEFAULT_SIZE,javax.swing.GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE)
        );

        pack();
    }

    private void BtnAgregarActionPerformed(java.awt.event.ActionEvent evt) {
        int idx = CBProducto.getSelectedIndex();
        if (idx < 0) return;
        Producto p = productosList.get(idx);
        int cantidad = (int) SpinnerCantidad.getValue();
        if (cantidad > p.cantidad()) {
            JOptionPane.showMessageDialog(this,
                "Stock insuficiente",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double precio = p.precio();
        double subtotal = precio * cantidad;
        DetalleVenta d = new DetalleVenta(0, 0, p.id(), cantidad, precio);
        detallesList.add(d);
        model.addRow(new Object[]{p.nombre(), cantidad, precio, subtotal});
        actualizarTotal();
    }

    private void BtnEditarActionPerformed(java.awt.event.ActionEvent evt) {
        int row = jTable1.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Selecciona una fila",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DetalleVenta old = detallesList.get(row);
        int cantidad = (int) SpinnerCantidad.getValue();
        Producto p = productosList.stream()
            .filter(prod -> prod.id() == old.idProducto())
            .findFirst().orElseThrow();
        if (cantidad > p.cantidad()) {
            JOptionPane.showMessageDialog(this,
                "Stock insuficiente",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DetalleVenta updated = new DetalleVenta(
            old.id(), old.idVenta(), old.idProducto(),
            cantidad, old.precioUnitario()
        );
        detallesList.set(row, updated);
        model.setValueAt(cantidad, row, 1);
        model.setValueAt(updated.precioUnitario() * cantidad, row, 3);
        actualizarTotal();
    }

    private void BtnPagarActionPerformed(java.awt.event.ActionEvent evt) {
        if (detallesList.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Agrega al menos un producto",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int ventaId = servicioVentas.generarVenta(idAdmin, detallesList);
            JOptionPane.showMessageDialog(this,
                "Venta registrada con ID " + ventaId,
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            new LoginFrame().setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error al procesar venta", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void BtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
        new LoginFrame().setVisible(true);
    }

    private void inicializarTabla() {
        model.setColumnIdentifiers(new Object[]{"Producto", "Cantidad", "Precio unit.", "Subtotal"});
        model.setRowCount(0);
    }

    private void cargarProductos() {
        productosList = servicioInv.listarProductos();
        CBProducto.removeAllItems();
        for (Producto p : productosList) {
            CBProducto.addItem(p.nombre());
        }
        if (!productosList.isEmpty()) {
            CBProducto.setSelectedIndex(0);
            CBProductoActionPerformed(null);
        }
    }

    private void CBProductoActionPerformed(java.awt.event.ActionEvent evt) {
        int idx = CBProducto.getSelectedIndex();
        if (idx >= 0) {
            int stock = productosList.get(idx).cantidad();
            SpinnerCantidad.setModel(new SpinnerNumberModel(1, 1, stock, 1));
        }
    }

    private void actualizarTotal() {
        double total = detallesList.stream()
            .mapToDouble(d -> d.precioUnitario() * d.cantidad())
            .sum();
        txtPagoTotal.setText(String.format("%.2f", total));
    }

    public static void main(String args[]) {
        /* Look & Feel Nimbus */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info :
                 javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}
        java.awt.EventQueue.invokeLater(() -> new Venta().setVisible(true));
    }

    // Variables declaration - do not modify
    private javax.swing.JButton BtnAgregar;
    private javax.swing.JButton BtnCancelar;
    private javax.swing.JButton BtnEditar;
    private javax.swing.JButton BtnPagar;
    private javax.swing.JComboBox<String> CBProducto;
    private javax.swing.JSpinner SpinnerCantidad;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtPagoTotal;
    // End of variables declaration
}
