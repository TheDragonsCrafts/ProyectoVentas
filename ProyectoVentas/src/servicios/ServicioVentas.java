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
     * Registra la venta y descuenta inventario producto por producto.
     * Lanza excepción y revierte si algo falla.
     */
    public int generarVenta(int idAdmin, List<DetalleVenta> detalles) throws Exception {

        double total = detalles.stream()
                               .mapToDouble(d -> d.precioUnitario() * d.cantidad())
                               .sum();

        // 1. Verifica y descuenta stock uno a uno
        for (DetalleVenta d : detalles) {
            invSrv.descontarStock(d.idProducto(), d.cantidad());
        }

        // 2. Registra venta en BD
        Venta v = new Venta(
                0,
                LocalDateTime.now(),
                total,
                idAdmin,
                detalles
        );
        return ventaDatos.registrarVenta(v);
    }

    public List<VentaDisplayDTO> consultarVentasDetalladas(String nombreVendedor, LocalDate fechaInicio, LocalDate fechaFin) throws SQLException {
        return ventaDatos.obtenerVentasParaDisplay(nombreVendedor, fechaInicio, fechaFin);
    }

    /**
     * Consulta todas las ventas detalladas sin aplicar filtros.
     * @return Lista de todas las ventas con sus detalles.
     * @throws SQLException Si ocurre un error durante la consulta a la base de datos.
     */
    public List<VentaDisplayDTO> consultarVentasDetalladas() throws SQLException {
        // Llama al método modificado con parámetros nulos para obtener todos los resultados
        return ventaDatos.obtenerVentasParaDisplay(null, null, null);
    }
}
