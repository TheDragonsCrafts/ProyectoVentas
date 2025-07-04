package ui.productos;

import datos.ProductoDatos;
import entidades.Producto;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Optional;
import ui.menu.Menu_Principal;

/**
 * JFrame para visualizar el inventario de productos.
 * Permite filtrar productos por nombre o por ID.
 * Muestra los detalles de los productos en una tabla.
 */
public class Inventario extends javax.swing.JFrame {

    private final ProductoDatos datos = new ProductoDatos();
    private DefaultTableModel model; // Modelo para la JTable

    /**
     * Constructor. Inicializa componentes, configura la tabla y carga todos los productos.
     */
    public Inventario() {
        initComponents();
        setLocationRelativeTo(null); // Centrar ventana
        setTitle("Consulta de Inventario"); // Título de la ventana
        BtnRegresar.addActionListener(this::BtnRegresarActionPerformed);

        // Configurar el modelo de la tabla
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que la tabla no sea editable
            }
        };
        jTable1.setModel(model);
        model.setColumnIdentifiers(new Object[]{
            "ID", "Nombre", "Descripción", "Precio", "Cantidad", "Fecha Caducidad", "Activo"
        });

        cargarTodosProductos(); // Carga inicial de todos los productos activos
    }

    /**
     * Carga todos los productos activos desde la base de datos y los muestra en la tabla.
     */
    private void cargarTodosProductos() {
        model.setRowCount(0); // Limpiar tabla antes de cargar
        try {
            List<Producto> lista = datos.listarProductos(); // Asume que esto devuelve solo activos
            for (Producto p : lista) {
                model.addRow(new Object[]{
                    p.id(),
                    p.nombre(),
                    p.descripcion(),
                    String.format("%.2f", p.precio()), // Formatear precio
                    p.cantidad(),
                    p.fechaCaducidad() != null ? p.fechaCaducidad().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A", // Formatear fecha
                    p.activo() ? "Sí" : "No"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage(), "Error de Carga", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // Los comentarios de NetBeans para initComponents y variables se omiten por brevedad.
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtProducto = new javax.swing.JTextField();
        BtnBuscar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txtProductoID = new javax.swing.JTextField();
        BtnRegresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE); // Cambiado a DISPOSE_ON_CLOSE

        jPanel1.setBackground(new java.awt.Color(0, 102, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Nombre del Producto:");

        txtProducto.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductoActionPerformed(evt);
            }
        });

        BtnBuscar.setBackground(new java.awt.Color(0, 0, 0));
        BtnBuscar.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        BtnBuscar.setForeground(new java.awt.Color(255, 255, 255));
        BtnBuscar.setText("Buscar");
        BtnBuscar.setToolTipText("Realiza la búsqueda según los filtros ingresados.");
        BtnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBuscarActionPerformed(evt);
            }
        });

        jTable1.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.setFillsViewportHeight(true);
        jScrollPane1.setViewportView(jTable1);

        jLabel2.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("ID del Producto:");

        txtProductoID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtProductoID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductoIDActionPerformed(evt);
            }
        });

        BtnRegresar.setBackground(new java.awt.Color(0, 0, 0));
        BtnRegresar.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        BtnRegresar.setForeground(new java.awt.Color(255, 255, 255));
        BtnRegresar.setText("Regresar al Menú");
        BtnRegresar.setToolTipText("Vuelve al menú principal.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(txtProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(100, 100, 100)
                                .addComponent(txtProductoID, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(109, 109, 109)
                                .addComponent(jLabel2))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 753, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(142, 142, 142)
                        .addComponent(BtnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(163, 163, 163)
                        .addComponent(BtnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
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
     * Acción del botón "Buscar". Filtra los productos por nombre o ID.
     * Si ambos campos de filtro están vacíos, carga todos los productos.
     */
    private void BtnBuscarActionPerformed(java.awt.event.ActionEvent evt) {
        String nombreFiltro = txtProducto.getText().trim();
        String idFiltroStr = txtProductoID.getText().trim();
        model.setRowCount(0); // Limpiar tabla antes de nueva búsqueda

        try {
            if (idFiltroStr.isEmpty() && nombreFiltro.isEmpty()) {
                cargarTodosProductos(); // Carga todos si no hay filtros
                return;
            }

            List<Producto> resultados;
            if (!idFiltroStr.isEmpty()) {
                // Búsqueda prioritaria por ID si se proporciona
                try {
                    int id = Integer.parseInt(idFiltroStr);
                    Optional<Producto> opt = datos.buscarPorId(id); // Asume que buscaPorId devuelve activos
                    resultados = opt.map(List::of).orElse(List.of());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "El ID debe ser un número válido.", "ID Inválido", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                // Búsqueda por nombre si el ID está vacío
                resultados = datos.buscarPorNombre(nombreFiltro); // Asume que buscaPorNombre devuelve activos
            }

            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron productos con los criterios especificados.", "Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (Producto p : resultados) {
                    model.addRow(new Object[]{
                        p.id(),
                        p.nombre(),
                        p.descripcion(),
                        String.format("%.2f", p.precio()),
                        p.cantidad(),
                        p.fechaCaducidad() != null ? p.fechaCaducidad().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A",
                        p.activo() ? "Sí" : "No"
                    });
                }
            }
        } catch (Exception e) { // Captura genérica para errores de BD u otros
            JOptionPane.showMessageDialog(this, "Error al realizar la búsqueda: " + e.getMessage(), "Error de Búsqueda", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Los siguientes métodos son generados por NetBeans y usualmente vacíos si no se les añade lógica.
    private void txtProductoActionPerformed(java.awt.event.ActionEvent evt) { /* Vacío */ }
    private void txtProductoIDActionPerformed(java.awt.event.ActionEvent evt) { /* Vacío */ }

    /**
     * Acción para el botón "Regresar". Vuelve al menú principal.
     */
    private void BtnRegresarActionPerformed(java.awt.event.ActionEvent evt) {
        new ui.menu.Menu_Principal().setVisible(true);
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
            java.util.logging.Logger.getLogger(Inventario.class.getName()).log(java.util.logging.Level.INFO, "Nimbus L&F no disponible.", ex);
        }

        java.awt.EventQueue.invokeLater(() -> new Inventario().setVisible(true));
    }

    // Variables generadas por NetBeans.
    private javax.swing.JButton BtnBuscar;
    private javax.swing.JButton BtnRegresar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtProducto;
    private javax.swing.JTextField txtProductoID;
    // End of variables declaration//GEN-END:variables
}
