package servicios;

import datos.VentaDatos;
import entidades.DetalleVenta;
import entidades.Venta;
import entidades.dto.VentaDisplayDTO;

import java.sql.SQLException;
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

    public List<VentaDisplayDTO> consultarVentasDetalladas() throws SQLException {
        // VentaDatos instance is already a field: private final VentaDatos ventaDatos = new VentaDatos();
        return ventaDatos.obtenerVentasParaDisplay();
    }
}
