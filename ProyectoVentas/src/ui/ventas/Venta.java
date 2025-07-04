package ui.ventas;

import servicios.ServicioVentas;
import servicios.ServicioInventario;
import entidades.Producto;
import entidades.DetalleVenta;
import seguridad.Session;
import ui.login.LoginFrame;
import ui.menu.Menu_Principal; // Importación añadida

import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * JFrame para el procesamiento de ventas.
 * Permite al usuario seleccionar productos, especificar cantidades,
 * agregarlos a una lista (tabla), editar o eliminar ítems de la lista,
 * y finalmente procesar el pago para registrar la venta.
 * Utiliza el ID del administrador almacenado en {@link seguridad.Session}.
 */
public class Venta extends javax.swing.JFrame {

    private final ServicioVentas servicioVentas = new ServicioVentas();
    private final ServicioInventario servicioInv = new ServicioInventario();

    private DefaultTableModel model; // Modelo para la JTable de detalles de venta.
    private List<Producto> productosDisponiblesList; // Lista de productos cargados en el ComboBox.
    private List<DetalleVenta> detallesVentaList;    // Lista de ítems actualmente en la venta.

    private final int idAdminLogueado; // ID del administrador que realiza la venta.

    /**
     * Constructor. Inicializa componentes, obtiene el ID del administrador,
     * configura la tabla y carga los productos disponibles.
     */
    public Venta() {
        this.idAdminLogueado = Session.getIdAdmin(); // Obtener ID del admin actual.
        this.detallesVentaList = new ArrayList<>();

        initComponents();
        setLocationRelativeTo(null); // Centrar ventana.
        setTitle("Procesar Nueva Venta"); // Título de la ventana.

        // Configurar JTable
        model = (DefaultTableModel) jTable1.getModel();
        inicializarTabla();

        cargarProductosAlComboBox(); // Cargar productos al ComboBox y configurar Spinner.

        // Configurar listener para el ComboBox de productos.
        CBProducto.addActionListener(this::CBProductoActionPerformed);
        // Asegurar que el SpinnerCantidad se actualice al seleccionar un producto.
        if (CBProducto.getItemCount() > 0) {
            CBProducto.setSelectedIndex(0); // Dispara el ActionListener.
        }
    }

    /**
     * Código generado por NetBeans para inicializar el formulario.
     * Los comentarios de NetBeans se omiten por brevedad.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE); // Cambiado a DISPOSE_ON_CLOSE

        jPanel1.setBackground(new java.awt.Color(0, 102, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Producto a Vender:");

        CBProducto.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        CBProducto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cargando..." }));
        CBProducto.setToolTipText("Seleccione el producto a agregar a la venta.");

        jLabel2.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Cantidad:");

        SpinnerCantidad.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        SpinnerCantidad.setModel(new javax.swing.SpinnerNumberModel(1, 1, 100, 1)); // Modelo inicial
        SpinnerCantidad.setToolTipText("Especifique la cantidad del producto seleccionado.");

        BtnAgregar.setBackground(new java.awt.Color(0, 153, 51));
        BtnAgregar.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        BtnAgregar.setForeground(new java.awt.Color(255, 255, 255));
        BtnAgregar.setText("Agregar");
        BtnAgregar.setToolTipText("Agrega el producto seleccionado con la cantidad especificada a la venta.");
        BtnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAgregarActionPerformed(evt);
            }
        });

        jTable1.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] { "Producto", "Cantidad", "Precio unit.", "Subtotal" }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jTable1);

        BtnEditar.setBackground(new java.awt.Color(255, 153, 0));
        BtnEditar.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        BtnEditar.setForeground(new java.awt.Color(255, 255, 255));
        BtnEditar.setText("Editar Cantidad");
        BtnEditar.setToolTipText("Edita la cantidad del producto seleccionado en la tabla usando el valor del spinner.");
        BtnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEditarActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI Emoji", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Total a Pagar:");

        BtnPagar.setBackground(new java.awt.Color(0, 0, 0));
        BtnPagar.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        BtnPagar.setForeground(new java.awt.Color(255, 255, 255));
        BtnPagar.setText("PAGAR");
        BtnPagar.setToolTipText("Procesa la venta con los productos listados.");
        BtnPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPagarActionPerformed(evt);
            }
        });

        BtnCancelar.setBackground(new java.awt.Color(204, 0, 51));
        BtnCancelar.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        BtnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        BtnCancelar.setText("Cancelar Venta");
        BtnCancelar.setToolTipText("Cancela la venta actual y regresa al menú principal.");
        BtnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCancelarActionPerformed(evt);
            }
        });

        txtPagoTotal.setEditable(false);
        txtPagoTotal.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        txtPagoTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPagoTotal.setText("0.00");
        txtPagoTotal.setToolTipText("Monto total de la venta actual.");
        txtPagoTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPagoTotalActionPerformed(evt);
            }
        });

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
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Acción para el botón "Agregar". Agrega el producto seleccionado a la tabla de venta
     * o actualiza su cantidad si ya existe. Valida stock disponible.
     */
    private void BtnAgregarActionPerformed(java.awt.event.ActionEvent evt) {
        int indiceSeleccionado = CBProducto.getSelectedIndex();
        if (indiceSeleccionado < 0 || productosDisponiblesList == null || productosDisponiblesList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay productos disponibles o no se ha seleccionado uno.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Producto productoSeleccionado = productosDisponiblesList.get(indiceSeleccionado);
        int cantidadSeleccionada = (int) SpinnerCantidad.getValue();
        
        if (cantidadSeleccionada <= 0) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor que cero.", "Cantidad Inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (productoSeleccionado.cantidad() == 0) { // Verifica el stock del producto original
            JOptionPane.showMessageDialog(this, "El producto '" + productoSeleccionado.nombre() + "' no tiene stock disponible.", "Sin Stock", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Buscar si el producto ya está en la lista de detalles
        int indiceExistente = -1;
        for (int i = 0; i < detallesVentaList.size(); i++) {
            if (detallesVentaList.get(i).idProducto() == productoSeleccionado.id()) {
                indiceExistente = i;
                break;
            }
        }
        
        if (indiceExistente >= 0) { // Producto ya en la lista, actualizar cantidad
            DetalleVenta detalleExistente = detallesVentaList.get(indiceExistente);
            int nuevaCantidad = detalleExistente.cantidad() + cantidadSeleccionada;
            
            if (nuevaCantidad > productoSeleccionado.cantidad()) {
                JOptionPane.showMessageDialog(this, "La cantidad total (" + nuevaCantidad + ") para '" + productoSeleccionado.nombre() + "' excedería el stock disponible: " + productoSeleccionado.cantidad(), "Stock Insuficiente", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            DetalleVenta detalleActualizado = new DetalleVenta(
                detalleExistente.id(), detalleExistente.idVenta(), detalleExistente.idProducto(),
                nuevaCantidad, detalleExistente.precioUnitario()
            );
            detallesVentaList.set(indiceExistente, detalleActualizado);
            
            model.setValueAt(nuevaCantidad, indiceExistente, 1); // Actualizar cantidad en tabla
            model.setValueAt(String.format("%.2f", detalleActualizado.precioUnitario() * nuevaCantidad), indiceExistente, 3); // Actualizar subtotal
            
        } else { // Producto nuevo en la lista
            if (cantidadSeleccionada > productoSeleccionado.cantidad()) {
                JOptionPane.showMessageDialog(this, "Stock insuficiente para '" + productoSeleccionado.nombre() + "'. Disponible: " + productoSeleccionado.cantidad() + ", Solicitado: " + cantidadSeleccionada, "Stock Insuficiente", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double precioUnitario = productoSeleccionado.precio();
            DetalleVenta nuevoDetalle = new DetalleVenta(0, 0, productoSeleccionado.id(), cantidadSeleccionada, precioUnitario);
            detallesVentaList.add(nuevoDetalle);
            model.addRow(new Object[]{
                productoSeleccionado.nombre(),
                cantidadSeleccionada,
                String.format("%.2f", precioUnitario),
                String.format("%.2f", precioUnitario * cantidadSeleccionada)
            });
        }
        
        actualizarTotalVenta();
        SpinnerCantidad.setValue(1); // Resetear spinner a 1
    }

    /**
     * Acción para el botón "Editar". Permite modificar la cantidad de un producto
     * ya agregado a la tabla de venta. Valida stock.
     */
    private void BtnEditarActionPerformed(java.awt.event.ActionEvent evt) {
        int filaSeleccionada = jTable1.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la lista para editar su cantidad.", "Ningún Producto Seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DetalleVenta detalleAntiguo = detallesVentaList.get(filaSeleccionada);
        int nuevaCantidad = (int) SpinnerCantidad.getValue(); // Nueva cantidad desde el spinner
        
        if (nuevaCantidad <= 0) {
            // Si la nueva cantidad es 0 o negativa, eliminar el ítem de la lista
            detallesVentaList.remove(filaSeleccionada);
            model.removeRow(filaSeleccionada);
            JOptionPane.showMessageDialog(this, "Producto eliminado de la venta.", "Producto Eliminado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            Producto productoOriginal = productosDisponiblesList.stream()
                .filter(p -> p.id() == detalleAntiguo.idProducto())
                .findFirst()
                .orElse(null); // No debería ser null si el producto está en la lista

            if (productoOriginal == null) {
                 JOptionPane.showMessageDialog(this, "Error: No se encontró el producto original en la lista de disponibles.", "Error Interno", JOptionPane.ERROR_MESSAGE);
                 return;
            }

            if (nuevaCantidad > productoOriginal.cantidad()) {
                JOptionPane.showMessageDialog(this, "Stock insuficiente para '" + productoOriginal.nombre() + "'. Stock disponible: " + productoOriginal.cantidad(), "Stock Insuficiente", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DetalleVenta detalleActualizado = new DetalleVenta(
                detalleAntiguo.id(), detalleAntiguo.idVenta(), detalleAntiguo.idProducto(),
                nuevaCantidad, detalleAntiguo.precioUnitario()
            );
            detallesVentaList.set(filaSeleccionada, detalleActualizado);

            // Actualizar la tabla
            model.setValueAt(nuevaCantidad, filaSeleccionada, 1);
            model.setValueAt(String.format("%.2f", detalleActualizado.precioUnitario() * nuevaCantidad), filaSeleccionada, 3);
        }
        actualizarTotalVenta();
        SpinnerCantidad.setValue(1); // Resetear spinner
    }

    /**
     * Acción para el botón "Pagar". Procesa la venta registrándola y actualizando el stock.
     * Muestra mensajes de éxito o error.
     */
    private void BtnPagarActionPerformed(java.awt.event.ActionEvent evt) {
        if (detallesVentaList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe agregar al menos un producto a la venta.", "Venta Vacía", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int idVentaGenerada = servicioVentas.generarVenta(idAdminLogueado, detallesVentaList);
            JOptionPane.showMessageDialog(this, "Venta registrada exitosamente con ID: " + idVentaGenerada, "Venta Exitosa", JOptionPane.INFORMATION_MESSAGE);

            // Resetear para una nueva venta
            detallesVentaList.clear();
            model.setRowCount(0);
            cargarProductosAlComboBox(); // Recargar productos para actualizar stock en ComboBox y Spinner
            actualizarTotalVenta();
            SpinnerCantidad.setValue(1);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al procesar la venta: " + ex.getMessage(), "Error de Venta", JOptionPane.ERROR_MESSAGE);
            // Considerar recargar stock en ComboBox si la venta falla para reflejar el estado correcto.
            cargarProductosAlComboBox();
        }
    }

    /**
     * Acción para el botón "Cancelar". Cierra la ventana de venta y vuelve al menú principal.
     */
    private void BtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        new ui.menu.Menu_Principal().setVisible(true);
        this.dispose();
    }

    // Método txtPagoTotalActionPerformed usualmente no necesita lógica si el campo es solo de display.
    private void txtPagoTotalActionPerformed(java.awt.event.ActionEvent evt) { /* Vacío */ }

    /**
     * Configura las columnas de la tabla de detalles de venta.
     */
    private void inicializarTabla() {
        model.setColumnIdentifiers(new Object[]{"Producto", "Cantidad", "Precio Unitario", "Subtotal"});
        model.setRowCount(0); // Limpiar cualquier fila existente
    }

    /**
     * Carga los productos activos desde el inventario al ComboBox.
     * Muestra el nombre del producto y su stock actual.
     */
    private void cargarProductosAlComboBox() {
        productosDisponiblesList = servicioInv.listarProductos(); // Asume que lista solo productos activos.
        CBProducto.removeAllItems(); // Limpiar ComboBox
        if (productosDisponiblesList != null && !productosDisponiblesList.isEmpty()) {
            for (Producto p : productosDisponiblesList) {
                String itemTexto = p.nombre() + " (Stock: " + p.cantidad() + ")";
                CBProducto.addItem(itemTexto);
            }
            // Seleccionar el primer producto y actualizar el spinner si hay productos.
            if (CBProducto.getItemCount() > 0) {
                CBProducto.setSelectedIndex(0);
                // El ActionListener de CBProducto se encargará de actualizar el Spinner.
            }
        } else {
            CBProducto.addItem("No hay productos disponibles");
            SpinnerCantidad.setEnabled(false); // Deshabilitar spinner si no hay productos.
        }
    }

    /**
     * ActionListener para el ComboBox de productos.
     * Actualiza el valor máximo del SpinnerCantidad según el stock del producto seleccionado.
     */
    private void CBProductoActionPerformed(java.awt.event.ActionEvent evt) {
        int indiceSeleccionado = CBProducto.getSelectedIndex();
        if (indiceSeleccionado >= 0 && productosDisponiblesList != null && !productosDisponiblesList.isEmpty()) {
            Producto productoSeleccionado = productosDisponiblesList.get(indiceSeleccionado);
            int stockActual = productoSeleccionado.cantidad();
            
            // El Spinner debe permitir seleccionar de 1 hasta el stock actual.
            // Si el stock es 0, el spinner debe estar deshabilitado o su máximo ser 0 (o 1 si se permite agregar y luego falla).
            // Para simplificar, si stock es 0, se podría deshabilitar el botón "Agregar".
            if (stockActual > 0) {
                SpinnerCantidad.setModel(new SpinnerNumberModel(1, 1, stockActual, 1));
                SpinnerCantidad.setEnabled(true);
                BtnAgregar.setEnabled(true); // Habilitar botón de agregar
            } else {
                SpinnerCantidad.setModel(new SpinnerNumberModel(0, 0, 0, 1)); // No se puede agregar cantidad
                SpinnerCantidad.setEnabled(false);
                BtnAgregar.setEnabled(false); // Deshabilitar botón de agregar si no hay stock
            }
        } else {
             SpinnerCantidad.setEnabled(false); // No hay producto seleccionado o lista vacía
             BtnAgregar.setEnabled(false);
        }
    }

    /**
     * Calcula y actualiza el monto total de la venta mostrándolo en txtPagoTotal.
     */
    private void actualizarTotalVenta() {
        double total = detallesVentaList.stream()
            .mapToDouble(d -> d.precioUnitario() * d.cantidad())
            .sum();
        txtPagoTotal.setText(String.format("%.2f", total)); // Formatear a dos decimales
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
            java.util.logging.Logger.getLogger(Venta.class.getName()).log(java.util.logging.Level.INFO, "Nimbus L&F no disponible.", ex);
        }

        java.awt.EventQueue.invokeLater(() -> new Venta().setVisible(true));
    }

    // Variables generadas por NetBeans.
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
    // End of variables declaration//GEN-END:variables
}