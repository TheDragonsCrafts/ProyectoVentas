package ui.productos;

import datos.ProductoDatos;
import entidades.Producto;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import ui.menu.Menu_Principal;

/**
 * JFrame para dar de alta, baja (lógica) y modificar productos del inventario.
 * Permite buscar productos por ID para cargar sus datos y editarlos.
 * La creación de un nuevo producto requiere un ID único.
 * La "baja" se realiza marcando el producto como inactivo.
 */
public class AltaYBaja extends javax.swing.JFrame {

    private final ProductoDatos datos = new ProductoDatos();

    /**
     * Constructor. Inicializa componentes y configura listeners.
     */
    public AltaYBaja() {
        initComponents();
        setLocationRelativeTo(null); // Centrar ventana
        setTitle("Gestión de Productos"); // Título de la ventana
        BtnRegresar.addActionListener(this::BtnRegresarActionPerformed);
        // Configurar el checkbox "Activo" por defecto y tooltips
        jCheckBoxEsActivo.setSelected(true);
        jCheckBoxEsActivo.setToolTipText("Marcar para crear/actualizar. Desmarcar para dar de baja (eliminar lógicamente).");
        txtFechaCaducidad.setToolTipText("Formato: dd/MM/yy (ej: 31/12/24)");
    }

    @SuppressWarnings("unchecked")
    // Los comentarios de NetBeans para initComponents y variables se omiten por brevedad.
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();
        txtNombreProducto = new javax.swing.JTextField();
        txtFechaCaducidad = new javax.swing.JTextField();
        txtCantidad = new javax.swing.JTextField();
        txtID = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();
        jCheckBoxEsActivo = new javax.swing.JCheckBox();
        BtnBuscarPorID = new javax.swing.JButton();
        BtnCancelar = new javax.swing.JButton();
        BtnGuardar1 = new javax.swing.JButton();
        BtnRegresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE); // Cambiado a DISPOSE_ON_CLOSE

        jPanel2.setBackground(new java.awt.Color(51, 102, 255));
        jPanel2.setForeground(new java.awt.Color(0, 102, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Nombre");

        jLabel2.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Precio");

        jLabel3.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Descripción");

        jLabel4.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("ID Producto");

        txtPrecio.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtPrecio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrecioActionPerformed(evt);
            }
        });

        txtNombreProducto.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtNombreProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreProductoActionPerformed(evt);
            }
        });

        txtFechaCaducidad.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtFechaCaducidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaCaducidadActionPerformed(evt);
            }
        });

        txtCantidad.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtCantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantidadActionPerformed(evt);
            }
        });

        txtID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIDActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Fecha de Caducidad");

        jLabel6.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Cantidad");

        txtDescripcion.setColumns(20);
        txtDescripcion.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtDescripcion.setRows(5);
        jScrollPane1.setViewportView(txtDescripcion);

        jCheckBoxEsActivo.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18)); // NOI18N
        jCheckBoxEsActivo.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBoxEsActivo.setText("Activo");
        jCheckBoxEsActivo.setOpaque(false);
        jCheckBoxEsActivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxEsActivoActionPerformed(evt);
            }
        });

        BtnBuscarPorID.setBackground(new java.awt.Color(0, 0, 0));
        BtnBuscarPorID.setFont(new java.awt.Font("Segoe UI Emoji", 1, 18)); // NOI18N
        BtnBuscarPorID.setForeground(new java.awt.Color(255, 255, 255));
        BtnBuscarPorID.setText("Buscar por ID");
        BtnBuscarPorID.setToolTipText("Busca un producto existente por su ID y carga sus datos.");
        BtnBuscarPorID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBuscarPorIDActionPerformed(evt);
            }
        });

        BtnCancelar.setBackground(new java.awt.Color(204, 0, 51));
        BtnCancelar.setFont(new java.awt.Font("Segoe UI Emoji", 1, 18)); // NOI18N
        BtnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        BtnCancelar.setText("Limpiar Campos");
        BtnCancelar.setToolTipText("Limpia todos los campos del formulario.");
        BtnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCancelarActionPerformed(evt);
            }
        });

        BtnGuardar1.setBackground(new java.awt.Color(0, 153, 51));
        BtnGuardar1.setFont(new java.awt.Font("Segoe UI Emoji", 1, 18)); // NOI18N
        BtnGuardar1.setForeground(new java.awt.Color(255, 255, 255));
        BtnGuardar1.setText("Guardar");
        BtnGuardar1.setToolTipText("Guarda el producto nuevo o los cambios al producto existente.");
        BtnGuardar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGuardar1ActionPerformed(evt);
            }
        });

        BtnRegresar.setBackground(new java.awt.Color(0, 0, 0));
        BtnRegresar.setFont(new java.awt.Font("Segoe UI Emoji", 1, 18)); // NOI18N
        BtnRegresar.setForeground(new java.awt.Color(255, 255, 255));
        BtnRegresar.setText("Regresar al Menú");
        BtnRegresar.setToolTipText("Vuelve al menú principal sin guardar cambios.");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(81, 81, 81)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(78, 78, 78)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 141, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtFechaCaducidad, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(129, 129, 129)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(50, 50, 50))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(BtnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BtnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(BtnGuardar1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addComponent(jCheckBoxEsActivo)
                .addGap(56, 56, 56)
                .addComponent(BtnBuscarPorID, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(24, 24, 24)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(713, Short.MAX_VALUE)))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(43, 43, 43)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(703, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFechaCaducidad, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(88, 88, 88)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxEsActivo)
                    .addComponent(BtnBuscarPorID, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnGuardar1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(95, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addContainerGap(253, Short.MAX_VALUE)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(238, 238, 238)))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(189, 189, 189)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(305, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Busca un producto por su ID y carga sus datos en los campos del formulario.
     */
    private void BtnBuscarPorIDActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            int id = Integer.parseInt(txtID.getText().trim());
            Optional<Producto> opt = datos.buscarPorId(id); // Asume que buscarPorId ahora solo busca activos.
                                                          // Si también debe buscar inactivos para editarlos, se necesitaría otro método en ProductoDatos.
            if (opt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Producto no encontrado o está inactivo.", "No Encontrado", JOptionPane.WARNING_MESSAGE);
                limpiarCampos(false); // Limpiar campos excepto ID
                return;
            }
            Producto p = opt.get();
            txtNombreProducto.setText(p.nombre());
            txtDescripcion.setText(p.descripcion());
            txtPrecio.setText(String.valueOf(p.precio()));
            txtCantidad.setText(String.valueOf(p.cantidad()));
            jCheckBoxEsActivo.setSelected(p.activo());
    
            if (p.fechaCaducidad() != null) {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yy");
                txtFechaCaducidad.setText(p.fechaCaducidad().format(fmt));
            } else {
                txtFechaCaducidad.setText("");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número válido.", "ID Inválido", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Guarda un producto nuevo o actualiza uno existente.
     * Si el checkbox "Activo" no está marcado, intenta dar de baja (eliminar lógicamente) el producto.
     */
    private void BtnGuardar1ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            int id;
            try {
                id = Integer.parseInt(txtID.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El ID del producto debe ser un número.", "ID Inválido", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String nombre = txtNombreProducto.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            double precio;
            int cantidad;

            try {
                precio = Double.parseDouble(txtPrecio.getText().trim());
                cantidad = Integer.parseInt(txtCantidad.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Precio y Cantidad deben ser números válidos.", "Datos Inválidos", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean activo = jCheckBoxEsActivo.isSelected();

            if (nombre.isEmpty()) { // Descripción puede ser opcional según reglas de negocio
                JOptionPane.showMessageDialog(this, "El nombre del producto no puede estar vacío.", "Campo Obligatorio", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (precio <= 0 || cantidad < 0) {
                JOptionPane.showMessageDialog(this, "Precio debe ser mayor a cero y cantidad no puede ser negativa.", "Valores Inválidos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            LocalDate fechaCaducidad = null;
            String fcStr = txtFechaCaducidad.getText().trim();
            if (!fcStr.isEmpty()) {
                try {
                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yy");
                    fechaCaducidad = LocalDate.parse(fcStr, fmt);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use dd/MM/yy (ej: 31/12/24).", "Fecha Inválida", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            Producto p = new Producto(id, nombre, descripcion, precio, cantidad, fechaCaducidad, activo);

            Optional<Producto> existenteOpt = datos.buscarPorId(id); // Busca si el producto ya existe (activo o inactivo)

            if (activo) { // Intención de crear o actualizar un producto activo
                if (existenteOpt.isPresent()) { // El ID ya existe, se intenta actualizar
                    datos.actualizar(p);
                    JOptionPane.showMessageDialog(this, "Producto con ID " + id + " actualizado correctamente.", "Actualización Exitosa", JOptionPane.INFORMATION_MESSAGE);
                } else { // El ID no existe, se intenta crear uno nuevo
                    datos.insertar(p);
                    JOptionPane.showMessageDialog(this, "Producto nuevo con ID " + id + " guardado correctamente.", "Creación Exitosa", JOptionPane.INFORMATION_MESSAGE);
                }
            } else { // Intención de dar de baja (marcar como inactivo)
                if (existenteOpt.isPresent()) {
                    if (!existenteOpt.get().activo()) {
                         JOptionPane.showMessageDialog(this, "El producto con ID " + id + " ya se encuentra inactivo.", "Información", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        datos.eliminar(id); // 'eliminar' en ProductoDatos realmente marca como inactivo
                        JOptionPane.showMessageDialog(this, "Producto con ID " + id + " marcado como inactivo (dado de baja).", "Baja Exitosa", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No se puede dar de baja un producto con ID " + id + " porque no existe.", "Error de Baja", JOptionPane.ERROR_MESSAGE);
                }
            }
            limpiarCampos(true);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error en la base de datos: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) { // Captura general para otros errores inesperados
             JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado: " + ex.getMessage(), "Error Inesperado", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Limpia los campos del formulario.
     * @param limpiarTodo Si es true, limpia todos los campos incluido el ID. Si es false, preserva el ID.
     */
    private void limpiarCampos(boolean limpiarTodo) {
        if (limpiarTodo) {
            txtID.setText("");
        }
        txtNombreProducto.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtCantidad.setText("");
        txtFechaCaducidad.setText("");
        jCheckBoxEsActivo.setSelected(true); // Por defecto, para nueva alta
        txtNombreProducto.requestFocus(); // Poner foco en el primer campo útil
    }

    /**
     * Acción para el botón "Cancelar" (ahora "Limpiar Campos"). Limpia el formulario.
     */
    private void BtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        limpiarCampos(true); // Limpia todos los campos, incluyendo el ID.
    }

    // Los siguientes métodos son generados por NetBeans y usualmente vacíos si no se les añade lógica.
    // Se pueden dejar o eliminar si no tienen funcionalidad específica.
    private void txtNombreProductoActionPerformed(java.awt.event.ActionEvent evt) { /* Vacío */ }
    private void txtFechaCaducidadActionPerformed(java.awt.event.ActionEvent evt) { /* Vacío */ }
    private void txtCantidadActionPerformed(java.awt.event.ActionEvent evt) { /* Vacío */ }
    private void txtIDActionPerformed(java.awt.event.ActionEvent evt) { /* Vacío */ }
    private void txtPrecioActionPerformed(java.awt.event.ActionEvent evt) { /* Vacío */ }
    private void jCheckBoxEsActivoActionPerformed(java.awt.event.ActionEvent evt) { /* Vacío */ }

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
            java.util.logging.Logger.getLogger(AltaYBaja.class.getName()).log(java.util.logging.Level.INFO, "Nimbus L&F no disponible.", ex);
        }

        java.awt.EventQueue.invokeLater(() -> new AltaYBaja().setVisible(true));
    }

    // Variables generadas por NetBeans.
    private javax.swing.JButton BtnBuscarPorID;
    private javax.swing.JButton BtnCancelar;
    private javax.swing.JButton BtnGuardar1;
    private javax.swing.JButton BtnRegresar;
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


