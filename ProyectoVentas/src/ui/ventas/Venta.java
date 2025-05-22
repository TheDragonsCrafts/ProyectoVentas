package ui.ventas;

import entidades.DetalleVenta;
import entidades.Producto;
import java.awt.event.KeyEvent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import seguridad.Session;
import servicios.ServicioVentas;
// Importar las nuevas excepciones si se van a manejar aquí directamente
import excepciones.StockInsuficienteException;
import excepciones.ProductoNoEncontradoException;
import excepciones.ProductoExpiradoException;
import excepciones.ValidacionException;
import excepciones.BaseDatosException;


public class Venta extends javax.swing.JDialog {
    private static final Logger LOGGER = Logger.getLogger(Venta.class.getName());

    private final DefaultTableModel model;
    private final List<Producto> productosList; // Lista de productos disponibles (para el ComboBox)
    private final List<DetalleVenta> detallesVentaList; // Lista de detalles de la venta actual

    public Venta(java.awt.Frame parent, boolean modal, List<Producto> productos) {
        super(parent, modal);
        initComponents();
        this.productosList = productos; // Asigna la lista de productos pasada
        this.detallesVentaList = new ArrayList<>();

        // Configurar modelo de tabla
        model = (DefaultTableModel) tablaDetalles.getModel();
        actualizarTotalGeneral();

        // Llenar ComboBox de productos
        productos.forEach(p -> CbxProductos.addItem(p.nombre()));
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        CbxProductos = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        SpnCantidad = new javax.swing.JSpinner();
        BtnAgregar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaDetalles = new javax.swing.JTable();
        BtnEliminar = new javax.swing.JButton();
        BtnEditar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        LblTotal = new javax.swing.JLabel();
        BtnConfirmarVenta = new javax.swing.JButton();
        BtnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Registrar Venta");

        jLabel1.setText("Producto:");

        jLabel2.setText("Cantidad:");

        SpnCantidad.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        SpnCantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SpnCantidadKeyPressed(evt);
            }
        });

        BtnAgregar.setText("Agregar");
        BtnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAgregarActionPerformed(evt);
            }
        });

        tablaDetalles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Producto", "Nombre", "Cantidad", "Precio Unit.", "Subtotal"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaDetalles);

        BtnEliminar.setText("Eliminar Seleccionado");
        BtnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEliminarActionPerformed(evt);
            }
        });

        BtnEditar.setText("Editar Seleccionado");
        BtnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEditarActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("TOTAL:");

        LblTotal.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        LblTotal.setText("S/ 0.00");

        BtnConfirmarVenta.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        BtnConfirmarVenta.setText("Confirmar Venta");
        BtnConfirmarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnConfirmarVentaActionPerformed(evt);
            }
        });

        BtnCancelar.setText("Cancelar Venta");
        BtnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CbxProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SpnCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(BtnAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(BtnEliminar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BtnEditar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(BtnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BtnConfirmarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(CbxProductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(SpnCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnAgregar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtnEliminar)
                    .addComponent(BtnEditar)
                    .addComponent(jLabel3)
                    .addComponent(LblTotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtnConfirmarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnCancelar))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>                        

    private void BtnAgregarActionPerformed(java.awt.event.ActionEvent evt) {                                           
        agregarProductoATabla();
    }                                          

    private void agregarProductoATabla() {
        int selectedIndex = CbxProductos.getSelectedIndex();
        if (selectedIndex < 0) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un producto.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Producto productoSeleccionado = productosList.get(selectedIndex);
        int cantidad = (Integer) SpnCantidad.getValue();

        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor que cero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Verificar si el producto ya está en la tabla de detalles
        Optional<DetalleVenta> detalleExistente = detallesVentaList.stream()
                .filter(d -> d.getIdProducto() == productoSeleccionado.id())
                .findFirst();

        if (detalleExistente.isPresent()) {
            // Si existe, actualizar la cantidad
            DetalleVenta detalle = detalleExistente.get();
            detalle.setCantidad(detalle.getCantidad() + cantidad);
            detalle.setSubtotal(detalle.getPrecioUnitario() * detalle.getCantidad());
        } else {
            // Si no existe, añadir nuevo detalle
            DetalleVenta nuevoDetalle = new DetalleVenta();
            nuevoDetalle.setIdProducto(productoSeleccionado.id());
            // Nombre se tomará de la tabla al renderizar, o se puede almacenar aquí
            // Para este ejemplo, no se almacena el nombre en DetalleVenta, se toma de la tabla.
            nuevoDetalle.setCantidad(cantidad);
            nuevoDetalle.setPrecioUnitario(productoSeleccionado.precio());
            nuevoDetalle.setSubtotal(productoSeleccionado.precio() * cantidad);
            detallesVentaList.add(nuevoDetalle);
        }

        actualizarTablaDetalles();
        actualizarTotalGeneral();
        SpnCantidad.setValue(1); // Resetear spinner
    }

    private void actualizarTablaDetalles() {
        model.setRowCount(0); // Limpiar tabla
        for (DetalleVenta detalle : detallesVentaList) {
            Producto p = productosList.stream()
                                      .filter(prod -> prod.id() == detalle.getIdProducto())
                                      .findFirst()
                                      .orElse(null); // Debería existir si se agregó correctamente
            String nombreProducto = (p != null) ? p.nombre() : "Desconocido";
            model.addRow(new Object[]{
                detalle.getIdProducto(),
                nombreProducto,
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                detalle.getSubtotal()
            });
        }
    }

    private void actualizarTotalGeneral() {
        double total = detallesVentaList.stream().mapToDouble(DetalleVenta::getSubtotal).sum();
        LblTotal.setText(String.format("S/ %.2f", total));
    }

    private void BtnEliminarActionPerformed(java.awt.event.ActionEvent evt) {                                            
        int selectedRow = tablaDetalles.getSelectedRow();
        if (selectedRow >= 0) {
            // Obtener el ID del producto de la fila seleccionada en la tabla
            int idProductoEnTabla = (Integer) model.getValueAt(selectedRow, 0);
            
            // Eliminar el detalle de la lista detallesVentaList
            detallesVentaList.removeIf(d -> d.getIdProducto() == idProductoEnTabla);
            
            actualizarTablaDetalles();
            actualizarTotalGeneral();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }                                           

    private void BtnEditarActionPerformed(java.awt.event.ActionEvent evt) {                                          
        int selectedRow = tablaDetalles.getSelectedRow();
        if (selectedRow >= 0) {
            int idProductoEnTabla = (Integer) model.getValueAt(selectedRow, 0);
            int cantidadActualEnTabla = (Integer) model.getValueAt(selectedRow, 2);

            DetalleVenta detalleParaEditar = detallesVentaList.stream()
                    .filter(d -> d.getIdProducto() == idProductoEnTabla)
                    .findFirst()
                    .orElse(null);

            if (detalleParaEditar == null) {
                JOptionPane.showMessageDialog(this, "Error interno: Detalle no encontrado en la lista.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Encontrar el producto original para poner su nombre en el ComboBox
            Producto productoOriginal = productosList.stream()
                                      .filter(prod -> prod.id() == detalleParaEditar.getIdProducto())
                                      .findFirst()
                                      .orElse(null);
             if (productoOriginal == null) {
                JOptionPane.showMessageDialog(this, 
                    "Error interno: Producto con ID " + detalleParaEditar.getIdProducto() + " no encontrado en la lista local de productos disponibles.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            // Llenar el ComboBox y el Spinner con los datos del producto a editar
            CbxProductos.setSelectedItem(productoOriginal.nombre());
            SpnCantidad.setValue(cantidadActualEnTabla); // O usar detalleParaEditar.getCantidad()

            // Eliminar el producto de la lista de detalles (se volverá a agregar con la nueva cantidad)
            detallesVentaList.remove(detalleParaEditar);
            actualizarTablaDetalles();
            actualizarTotalGeneral();

            // El usuario modificará la cantidad y presionará "Agregar"
            // o seleccionará otro producto.
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }                                         

    private void BtnConfirmarVentaActionPerformed(java.awt.event.ActionEvent evt) {                                                  
        if (detallesVentaList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay productos en la venta.", "Venta Vacía", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirmación
        int confirm = JOptionPane.showConfirmDialog(this, 
                String.format("¿Confirmar la venta por un total de %s?", LblTotal.getText()), 
                "Confirmar Venta", 
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            ServicioVentas servicioVentas = new ServicioVentas();
            try {
                // Asumiendo que Session.getInstance().getAdminLogueado().getId() te da el ID del admin.
                // Si getAdminLogueado() puede ser null, manejarlo.
                if (Session.getInstance().getAdminLogueado() == null) {
                    JOptionPane.showMessageDialog(this, "No hay un administrador logueado. Por favor, inicie sesión.", "Error de Sesión", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int idAdmin = Session.getInstance().getAdminLogueado().getIdAdmin();
                
                // Crear los objetos Venta y DetalleVenta a partir de la UI
                // El objeto 'venta' se crea dentro de generarVenta
                
                String resultado = servicioVentas.generarVenta(idAdmin, new ArrayList<>(detallesVentaList)); // Pasar una copia
                
                JOptionPane.showMessageDialog(this, resultado, "Venta Confirmada", JOptionPane.INFORMATION_MESSAGE);
                LOGGER.log(Level.INFO, "Venta confirmada: {0}", resultado);
                this.dispose(); // Cerrar ventana de venta

            } catch (StockInsuficienteException | ProductoNoEncontradoException | ProductoExpiradoException | ValidacionException | BaseDatosException e) {
                LOGGER.log(Level.SEVERE, "Error al generar la venta: " + e.getMessage(), e);
                JOptionPane.showMessageDialog(this, "Error al registrar la venta:\n" + e.getMessage(), "Error de Venta", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) { // Catch-all para errores inesperados
                LOGGER.log(Level.SEVERE, "Error inesperado al generar la venta.", e);
                JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado: " + e.getMessage(), "Error Crítico", JOptionPane.ERROR_MESSAGE);
            }
        }
    }                                                 

    private void BtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {                                            
        if (!detallesVentaList.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "¿Está seguro de que desea cancelar la venta actual?\nSe perderán los detalles agregados.", 
                    "Cancelar Venta", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
            }
        } else {
            this.dispose();
        }
    }                                           

    private void SpnCantidadKeyPressed(java.awt.event.KeyEvent evt) {                                       
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            agregarProductoATabla();
        }
    }                                      


    // Variables declaration - do not modify                     
    private javax.swing.JButton BtnAgregar;
    private javax.swing.JButton BtnCancelar;
    private javax.swing.JButton BtnConfirmarVenta;
    private javax.swing.JButton BtnEditar;
    private javax.swing.JButton BtnEliminar;
    private javax.swing.JComboBox<String> CbxProductos;
    private javax.swing.JLabel LblTotal;
    private javax.swing.JSpinner SpnCantidad;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaDetalles;
    // End of variables declaration                   
}
