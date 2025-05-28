package ui.reportes;

import entidades.dto.DetalleVentaDisplayDTO;
import entidades.dto.VentaDisplayDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DetalleVentaFrame extends JFrame {

    private final VentaDisplayDTO venta;
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(new Locale("es", "CL")); // For CLP

    private JLabel lblIdVenta;
    private JLabel lblFecha;
    private JLabel lblVendedor;
    private JLabel lblMontoTotal;
    private JLabel lblEstado;
    private JTable jTableDetalles;
    private JButton btnCerrar;

    public DetalleVentaFrame(VentaDisplayDTO ventaDisplayDTO) {
        this.venta = ventaDisplayDTO;
        initComponents();
        populateData();

        setTitle("Detalles de Venta - ID: " + venta.idVenta());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null); // Center the frame
        // setVisible(true); // Visibility should be controlled by the caller
    }

    private void initComponents() {
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // Top panel for general sale information (GridLayout)
        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 5, 5)); // 0 rows means as many as needed, 1 column
        lblIdVenta = new JLabel();
        lblFecha = new JLabel();
        lblVendedor = new JLabel();
        lblMontoTotal = new JLabel();
        lblEstado = new JLabel();

        infoPanel.add(new JLabel("<html><b>Información de la Venta:</b></html>")); // Title for section
        infoPanel.add(lblIdVenta);
        infoPanel.add(lblFecha);
        infoPanel.add(lblVendedor);
        infoPanel.add(lblMontoTotal);
        infoPanel.add(lblEstado);
        mainPanel.add(infoPanel, BorderLayout.NORTH);

        // Center panel for details table
        jTableDetalles = new JTable();
        JScrollPane jScrollPaneDetalles = new JScrollPane(jTableDetalles);
        jTableDetalles.setFillsViewportHeight(true);
        mainPanel.add(jScrollPaneDetalles, BorderLayout.CENTER);

        // Bottom panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        buttonPanel.add(btnCerrar);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void populateData() {
        lblIdVenta.setText("ID Venta: " + venta.idVenta());
        lblFecha.setText("Fecha: " + (venta.fechaVenta() != null ? venta.fechaVenta().format(DTF) : "N/A"));
        lblVendedor.setText("Vendedor: " + venta.nombreAdministrador());
        lblMontoTotal.setText("Monto Total: " + CURRENCY_FORMAT.format(venta.montoTotal()));
        lblEstado.setText("Estado: " + venta.estado());

        String[] columnNames = {"Producto", "Cantidad", "Precio Unitario", "Subtotal"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        if (venta.detalles() != null) {
            for (DetalleVentaDisplayDTO detalle : venta.detalles()) {
                double subtotal = detalle.cantidad() * detalle.precioEnVenta();
                model.addRow(new Object[]{
                        detalle.nombreProducto(),
                        detalle.cantidad(),
                        CURRENCY_FORMAT.format(detalle.precioEnVenta()),
                        CURRENCY_FORMAT.format(subtotal)
                });
            }
        }
        jTableDetalles.setModel(model);
        
        // Adjust column widths (optional, but often useful)
        if (jTableDetalles.getColumnCount() > 0) {
            jTableDetalles.getColumnModel().getColumn(0).setPreferredWidth(200); // Producto
            jTableDetalles.getColumnModel().getColumn(1).setPreferredWidth(60);  // Cantidad
            jTableDetalles.getColumnModel().getColumn(2).setPreferredWidth(100); // Precio Unitario
            jTableDetalles.getColumnModel().getColumn(3).setPreferredWidth(100); // Subtotal
        }
    }

    // Main method for testing (optional)
    /*
    public static void main(String[] args) {
        // Create dummy data for testing
        List<DetalleVentaDisplayDTO> detalles = List.of(
                new DetalleVentaDisplayDTO("Producto A", 2, 1500),
                new DetalleVentaDisplayDTO("Producto B", 1, 5000)
        );
        VentaDisplayDTO testVenta = new VentaDisplayDTO(
                101,
                java.time.LocalDateTime.now(),
                8000,
                "Admin Test",
                detalles,
                "COMPLETADA"
        );
        SwingUtilities.invokeLater(() -> new DetalleVentaFrame(testVenta).setVisible(true));
    }
    */
}
