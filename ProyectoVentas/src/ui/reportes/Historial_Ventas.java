/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui.reportes;

import entidades.dto.VentaDisplayDTO;
import servicios.ServicioVentas;
import ui.menu.Menu_Principal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date; // For JDateChooser
import java.util.List;
import com.toedter.calendar.JDateChooser; // Added JDateChooser

/**
/**
 * JFrame para mostrar el historial de ventas.
 * Permite filtrar las ventas por nombre de vendedor y rango de fechas.
 * Muestra las ventas en una tabla y permite ver los detalles de una venta seleccionada.
 */
public class Historial_Ventas extends javax.swing.JFrame {

    private final ServicioVentas servicioVentas = new ServicioVentas();
    private static final DateTimeFormatter DTF_DISPLAY = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private JDateChooser jdcInicioFecha;
    private JDateChooser jdcTerminoFecha;

    /**
     * Constructor. Inicializa componentes, configura JDateChoosers y carga las ventas iniciales.
     */
    public Historial_Ventas() {
        initComponents(); // Este método será modificado por NetBeans si se edita el diseño.
                          // Los JDateChoosers se inicializan aquí si no están en initComponents.
        setLocationRelativeTo(null); // Centrar ventana
        setTitle("Historial de Ventas"); // Título de la ventana

        // Si jdcInicioFecha y jdcTerminoFecha no son inicializados por NetBeans (ej. si se añaden manualmente al panel)
        // if (jdcInicioFecha == null) jdcInicioFecha = new JDateChooser();
        // if (jdcTerminoFecha == null) jdcTerminoFecha = new JDateChooser();
        // Y luego añadirlos al panel correspondiente. La forma preferida es hacerlo en el diseñador.

        // Asegurar que el formato de fecha sea el esperado para los JDateChooser
        jdcInicioFecha.setDateFormatString("dd/MM/yyyy");
        jdcTerminoFecha.setDateFormatString("dd/MM/yyyy");

        BtnRegresar.addActionListener(this::BtnRegresarActionPerformed);
        cargarVentasPredeterminadas(); // Carga todas las ventas al inicio.
    }

    /**
     * Actualiza la JTable con la lista de ventas (VentaDisplayDTO) proporcionada.
     * @param ventas Lista de DTOs de ventas a mostrar.
     */
    private void actualizarTablaVentas(List<VentaDisplayDTO> ventas) {
        String[] columnNames = {"ID Venta", "Fecha/Hora", "Monto Total", "Vendedor", "Estado"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla no editable
            }
        };

        if (ventas != null) {
            for (VentaDisplayDTO venta : ventas) {
                String fechaFormateada = venta.fechaVenta() != null ? venta.fechaVenta().format(DTF_DISPLAY) : "N/A";
                Object[] row = {
                    venta.idVenta(),
                    fechaFormateada,
                    String.format("%.2f", venta.montoTotal()), // Formatear monto
                    venta.nombreAdministrador() != null ? venta.nombreAdministrador() : "N/A",
                    venta.estado() != null ? venta.estado() : "N/A"
                };
                model.addRow(row);
            }
        }
        jTable1.setModel(model);
    }

    /**
     * Carga todas las ventas (sin filtros) y actualiza la tabla.
     * Maneja excepciones durante la carga.
     */
    private void cargarVentasPredeterminadas() {
        try {
            List<VentaDisplayDTO> ventas = servicioVentas.consultarVentasDetalladas();
            actualizarTablaVentas(ventas);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el historial de ventas: " + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado al cargar ventas: " + e.getMessage(), "Error Inesperado", JOptionPane.ERROR_MESSAGE);
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
        jdcInicioFecha = new JDateChooser();
        jdcTerminoFecha = new JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtAdminVendedor = new javax.swing.JTextField();
        BtnBuscar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        BtnDetalles = new javax.swing.JButton();
        BtnCancelar = new javax.swing.JButton();
        BtnRegresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE); // Cambiado a DISPOSE_ON_CLOSE

        jPanel1.setBackground(new java.awt.Color(0, 102, 255));

        jdcInicioFecha.setDateFormatString("dd/MM/yyyy");
        jdcTerminoFecha.setDateFormatString("dd/MM/yyyy");

        jLabel1.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Fecha Inicio:");

        jLabel2.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Fecha Fin:");

        jLabel3.setFont(new java.awt.Font("Segoe UI Emoji", 0, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Vendedor:");

        txtAdminVendedor.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtAdminVendedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAdminVendedorActionPerformed(evt);
            }
        });

        BtnBuscar.setBackground(new java.awt.Color(0, 0, 0));
        BtnBuscar.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        BtnBuscar.setForeground(new java.awt.Color(255, 255, 255));
        BtnBuscar.setText("Buscar");
        BtnBuscar.setToolTipText("Aplicar filtros y buscar ventas.");
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
                "ID Venta", "Fecha/Hora", "Monto Total", "Vendedor", "Estado"
            }
        ));
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jTable1);

        BtnDetalles.setBackground(new java.awt.Color(0, 0, 0));
        BtnDetalles.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        BtnDetalles.setForeground(new java.awt.Color(255, 255, 255));
        BtnDetalles.setText("Ver Detalles");
        BtnDetalles.setToolTipText("Muestra los detalles de la venta seleccionada.");
        BtnDetalles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDetallesActionPerformed(evt);
            }
        });

        BtnCancelar.setBackground(new java.awt.Color(204, 0, 51));
        BtnCancelar.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        BtnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        BtnCancelar.setText("Limpiar Filtros");
        BtnCancelar.setToolTipText("Limpia los filtros y muestra todas las ventas.");
        BtnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCancelarActionPerformed(evt);
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
                        .addGap(87, 87, 87)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(txtAdminVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel3))
                        .addGap(125, 125, 125)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jdcInicioFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(27, 27, 27)
                        .addComponent(jLabel2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(610, 610, 610)
                        .addComponent(jdcTerminoFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(123, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 461, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(BtnDetalles, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(BtnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(BtnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(119, 119, 119))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(175, 175, 175)
                .addComponent(BtnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtAdminVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jdcInicioFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jdcTerminoFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(BtnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(148, 148, 148)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(BtnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BtnDetalles, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(51, 51, 51)
                        .addComponent(BtnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
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
     * Acción para el botón "Ver Detalles". Muestra el frame DetalleVentaFrame
     * para la venta seleccionada en la tabla.
     */
    private void BtnDetallesActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una venta de la tabla.", "Ninguna Venta Seleccionada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int idVenta = (int) jTable1.getValueAt(selectedRow, 0);
            
            // Para obtener el DTO completo, es mejor volver a consultar la lista de ventas filtradas actualmente
            // o, idealmente, tener la lista de DTOs como un campo de clase que se actualiza con cada búsqueda.
            // Aquí se asume que se puede reconstruir la lista filtrada para encontrar el DTO.
            String nombreVendedor = txtAdminVendedor.getText().trim();
            Date utilFechaInicio = jdcInicioFecha.getDate();
            Date utilFechaFin = jdcTerminoFecha.getDate();
            LocalDate fechaInicio = (utilFechaInicio != null) ? utilFechaInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
            LocalDate fechaFin = (utilFechaFin != null) ? utilFechaFin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
            
            List<VentaDisplayDTO> ventasActuales = servicioVentas.consultarVentasDetalladas(nombreVendedor, fechaInicio, fechaFin);
            
            VentaDisplayDTO ventaSeleccionada = ventasActuales.stream()
                .filter(v -> v.idVenta() == idVenta)
                .findFirst()
                .orElse(null);

            if (ventaSeleccionada != null) {
                new DetalleVentaFrame(ventaSeleccionada).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo encontrar la información detallada de la venta seleccionada.", "Error de Detalles", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar detalles de la venta: " + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado al mostrar detalles: " + e.getMessage(), "Error Inesperado", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Acción para el botón "Buscar". Filtra las ventas según los criterios ingresados.
     */
    private void BtnBuscarActionPerformed(java.awt.event.ActionEvent evt) {
        String nombreVendedor = txtAdminVendedor.getText().trim();
        Date utilFechaInicio = jdcInicioFecha.getDate();
        Date utilFechaFin = jdcTerminoFecha.getDate();

        LocalDate fechaInicio = (utilFechaInicio != null) ? utilFechaInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
        LocalDate fechaFin = (utilFechaFin != null) ? utilFechaFin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;

        if (fechaInicio != null && fechaFin != null && fechaInicio.isAfter(fechaFin)) {
            JOptionPane.showMessageDialog(this, "La fecha de inicio no puede ser posterior a la fecha de fin.", "Error de Fechas", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            List<VentaDisplayDTO> ventasFiltradas = servicioVentas.consultarVentasDetalladas(nombreVendedor, fechaInicio, fechaFin);
            actualizarTablaVentas(ventasFiltradas);
            if (ventasFiltradas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron ventas con los criterios especificados.", "Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al buscar ventas: " + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado al buscar: " + e.getMessage(), "Error Inesperado", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método txtinicioFechaActionPerformed eliminado ya que el componente fue reemplazado.

    private void txtAdminVendedorActionPerformed(java.awt.event.ActionEvent evt) {
        BtnBuscarActionPerformed(evt); // Ejecutar búsqueda al presionar Enter en el campo de vendedor
    }

    /**
     * Acción para el botón "Actualizar" (ahora "Limpiar Filtros").
     * Limpia los campos de filtro y recarga todas las ventas.
     */
    private void BtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        txtAdminVendedor.setText("");
        jdcInicioFecha.setDate(null);
        jdcTerminoFecha.setDate(null);
        cargarVentasPredeterminadas(); // Recarga todas las ventas
        JOptionPane.showMessageDialog(this, "Filtros limpiados. Mostrando todas las ventas.", "Filtros Limpiados", JOptionPane.INFORMATION_MESSAGE);
    }

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
            java.util.logging.Logger.getLogger(Historial_Ventas.class.getName()).log(java.util.logging.Level.INFO, "Nimbus L&F no disponible.", ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new Historial_Ventas().setVisible(true);
        });
    }

    // Variables generadas por NetBeans
    private javax.swing.JButton BtnBuscar;
    private javax.swing.JButton BtnCancelar;
    private javax.swing.JButton BtnDetalles;
    private javax.swing.JButton BtnRegresar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtAdminVendedor;
    // private javax.swing.JTextField txtTerminoFecha; // Replaced by jdcTerminoFecha
    // private javax.swing.JTextField txtinicioFecha; // Replaced by jdcInicioFecha
    // JDateChooser components are already declared as class fields: jdcInicioFecha, jdcTerminoFecha
    // End of variables declaration//GEN-END:variables
}
