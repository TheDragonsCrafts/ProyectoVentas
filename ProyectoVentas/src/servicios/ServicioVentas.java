package servicios;

import datos.VentaDatos;
import entidades.DetalleVenta;
import entidades.Venta;
import entidades.dto.VentaDisplayDTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ServicioVentas {

    private final ServicioInventario invSrv = new ServicioInventario();
    private final VentaDatos ventaDatos = new VentaDatos();

    /**
     * Procesa y registra una nueva venta.
     * Calcula el monto total, descuenta los productos del inventario y registra la venta en la base de datos.
     * Esta operación debería ser transaccional a nivel de base de datos si `registrarVenta` y `descontarStock`
     * no operan bajo una transacción gestionada externamente o si `registrarVenta` no maneja el rollback
     * de los descuentos de stock en caso de fallo. Actualmente, `registrarVenta` es transaccional para
     * la inserción de venta y detalles, pero el descuento de stock ocurre antes y por separado.
     * @param idAdmin El ID del administrador que realiza la venta.
     * @param detalles La lista de detalles de la venta (productos, cantidades, precios).
     * @return El ID de la venta generada.
     * @throws Exception Si ocurre un error durante el descuento de stock (e.g., producto no encontrado, stock insuficiente)
     *                   o durante el registro de la venta en la base de datos.
     */
    public int generarVenta(int idAdmin, List<DetalleVenta> detalles) throws Exception {

        double total = detalles.stream()
                               .mapToDouble(d -> d.precioUnitario() * d.cantidad())
                               .sum();

        // 1. Descontar stock para cada producto en la venta.
        // Si algún descuento falla, se lanza una excepción y la venta no se registra.
        // Considerar: Si un descuento falla después de que otros ya se aplicaron,
        // esos descuentos no se revierten automáticamente aquí. La reversión
        // necesitaría lógica adicional o una transacción que englobe esta operación y los descuentos.
        for (DetalleVenta d : detalles) {
            invSrv.descontarStock(d.idProducto(), d.cantidad()); // Puede lanzar Exception o SQLException
        }

        // 2. Si todos los descuentos de stock son exitosos, registrar la venta.
        Venta v = new Venta(
                0, // El ID se autogenerará en la base de datos.
                LocalDateTime.now(),
                total,
                idAdmin,
                detalles
        );
        return ventaDatos.registrarVenta(v); // Puede lanzar SQLException
    }

    /**
     * Consulta ventas detalladas aplicando filtros opcionales por nombre de vendedor y rango de fechas.
     * @param nombreVendedor Nombre del vendedor (o parte del nombre) para filtrar. Null o vacío para no filtrar.
     * @param fechaInicio Fecha de inicio del rango para filtrar. Null para no filtrar.
     * @param fechaFin Fecha de fin del rango para filtrar. Null para no filtrar.
     * @return Una lista de DTOs {@link VentaDisplayDTO} que representan las ventas encontradas.
     * @throws SQLException Si ocurre un error durante la consulta a la base de datos.
     */
    public List<VentaDisplayDTO> consultarVentasDetalladas(String nombreVendedor, LocalDate fechaInicio, LocalDate fechaFin) throws SQLException {
        return ventaDatos.obtenerVentasParaDisplay(nombreVendedor, fechaInicio, fechaFin);
    }

    /**
     * Consulta todas las ventas detalladas sin aplicar ningún filtro.
     * @return Lista de {@link VentaDisplayDTO} con todas las ventas y sus detalles.
     * @throws SQLException Si ocurre un error durante la consulta a la base de datos.
     */
    public List<VentaDisplayDTO> consultarVentasDetalladas() throws SQLException {
        return ventaDatos.obtenerVentasParaDisplay(null, null, null);
    }
}
