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

/**
 * JFrame para mostrar los detalles de una venta específica.
 * Presenta información general de la venta y una tabla con los productos,
 * cantidades, precios unitarios y subtotales.
 * Esta ventana es modal y se cierra al presionar el botón "Cerrar".
 */
public class DetalleVentaFrame extends JFrame {

    private final VentaDisplayDTO venta; // DTO con la información de la venta y sus detalles.
    // Formateadores para fecha/hora y moneda.
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(new Locale("es", "CL")); // CLP

    // Componentes de la UI (declarados como campos para acceso en métodos)
    private JLabel lblIdVenta;
    private JLabel lblFecha;
    private JLabel lblVendedor;
    private JLabel lblMontoTotal;
    private JLabel lblEstado;
    private JTable jTableDetalles;
    // private JButton btnCerrar; // No es necesario como campo si la acción es simple.

    /**
     * Constructor.
     * @param ventaDisplayDTO El DTO de la venta cuyos detalles se mostrarán.
     */
    public DetalleVentaFrame(VentaDisplayDTO ventaDisplayDTO) {
        this.venta = ventaDisplayDTO;
        initComponents(); // Inicializa y configura los componentes de la UI.
        populateData();   // Llena los componentes con los datos de la venta.

        setTitle("Detalles de Venta - ID: " + venta.idVenta());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo esta ventana, no la aplicación.
        setResizable(false); // Evita redimensionar para mantener el diseño.
        pack(); // Ajusta el tamaño de la ventana al contenido.
        setLocationRelativeTo(null); // Centra la ventana en la pantalla.
        // La visibilidad (setVisible(true)) debe ser controlada por quien llama a este constructor.
    }

    /**
     * Inicializa y organiza los componentes de la interfaz gráfica.
     * Se utilizan paneles con diferentes LayoutManagers para la disposición.
     */
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10)); // Panel principal con borde.
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Márgenes.
        setContentPane(mainPanel);

        // Panel superior para información general de la venta (GridLayout).
        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 5, 5)); // Múltiples filas, 1 columna.
        lblIdVenta = new JLabel();
        lblFecha = new JLabel();
        lblVendedor = new JLabel();
        lblMontoTotal = new JLabel();
        lblEstado = new JLabel();

        infoPanel.add(new JLabel("<html><b>Información de la Venta:</b></html>")); // Título de sección.
        infoPanel.add(lblIdVenta);
        infoPanel.add(lblFecha);
        infoPanel.add(lblVendedor);
        infoPanel.add(lblMontoTotal);
        infoPanel.add(lblEstado);
        mainPanel.add(infoPanel, BorderLayout.NORTH); // Añadir panel de info al norte.

        // Panel central para la tabla de detalles de venta.
        jTableDetalles = new JTable();
        JScrollPane jScrollPaneDetalles = new JScrollPane(jTableDetalles);
        jTableDetalles.setFillsViewportHeight(true); // La tabla ocupa toda la altura del scroll pane.
        jTableDetalles.setEnabled(false); // Hacer la tabla no editable por el usuario.
        mainPanel.add(jScrollPaneDetalles, BorderLayout.CENTER); // Añadir tabla al centro.

        // Panel inferior para el botón de cerrar.
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Botón alineado a la derecha.
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setToolTipText("Cierra esta ventana de detalles.");
        btnCerrar.addActionListener(e -> dispose()); // Acción lambda para cerrar la ventana.
        buttonPanel.add(btnCerrar);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH); // Añadir panel de botón al sur.
    }

    /**
     * Llena los componentes de la UI con los datos de la venta.
     * Formatea fechas y montos según corresponda.
     */
    private void populateData() {
        lblIdVenta.setText("ID Venta: " + venta.idVenta());
        lblFecha.setText("Fecha: " + (venta.fechaVenta() != null ? venta.fechaVenta().format(DTF) : "N/A"));
        lblVendedor.setText("Vendedor: " + (venta.nombreAdministrador() != null ? venta.nombreAdministrador() : "N/A"));
        lblMontoTotal.setText("Monto Total: " + CURRENCY_FORMAT.format(venta.montoTotal()));
        lblEstado.setText("Estado: " + (venta.estado() != null ? venta.estado() : "N/A"));

        // Configurar modelo para la tabla de detalles.
        String[] columnNames = {"Producto", "Cantidad", "Precio Unitario", "Subtotal"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        if (venta.detalles() != null) {
            for (DetalleVentaDisplayDTO detalle : venta.detalles()) {
                double subtotal = detalle.cantidad() * detalle.precioEnVenta();
                model.addRow(new Object[]{
                        detalle.nombreProducto(),
                        detalle.cantidad(),
                        CURRENCY_FORMAT.format(detalle.precioEnVenta()), // Formatear moneda
                        CURRENCY_FORMAT.format(subtotal)                // Formatear moneda
                });
            }
        }
        jTableDetalles.setModel(model);
        
        // Ajustar anchos de columna preferidos para mejor visualización.
        if (jTableDetalles.getColumnCount() > 0) {
            jTableDetalles.getColumnModel().getColumn(0).setPreferredWidth(200); // Producto
            jTableDetalles.getColumnModel().getColumn(1).setPreferredWidth(60);  // Cantidad
            jTableDetalles.getColumnModel().getColumn(2).setPreferredWidth(120); // Precio Unitario
            jTableDetalles.getColumnModel().getColumn(3).setPreferredWidth(120); // Subtotal
        }
    }

    // El método main para pruebas puede ser útil durante el desarrollo,
    // pero usualmente se elimina o comenta en la versión final.
    /*
    public static void main(String[] args) {
        // Crear datos de prueba (dummy data) para visualizar el frame.
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
