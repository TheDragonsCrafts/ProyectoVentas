package servicios;

import datos.VentaDatos;
import entidades.DetalleVenta;
import entidades.Producto;
import entidades.Venta;
import excepciones.BaseDatosException;
import excepciones.ProductoExpiradoException;
import excepciones.ProductoNoEncontradoException;
import excepciones.StockInsuficienteException;
import excepciones.ValidacionException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import seguridad.ConexionBD;

public class ServicioVentas {

    private static final Logger LOGGER = Logger.getLogger(ServicioVentas.class.getName());
    private final ServicioInventario servicioInventario;

    public ServicioVentas() {
        this.servicioInventario = new ServicioInventario();
    }

    /**
     * Genera una nueva venta, descontando el stock de los productos involucrados
     * y registrando la venta y sus detalles de forma atómica.
     *
     * @param idAdministrador El ID del administrador que registra la venta.
     * @param detalles La lista de detalles de venta (productos y cantidades).
     * @return Un mensaje indicando el resultado de la operación.
     * @throws StockInsuficienteException Si no hay stock suficiente para algún producto.
     * @throws ProductoNoEncontradoException Si algún producto no se encuentra.
     * @throws ProductoExpiradoException Si algún producto está caducado.
     * @throws ValidacionException Si algún dato de la venta es inválido (ej. producto inactivo, cantidad <= 0).
     * @throws BaseDatosException Si ocurre un error de base de datos durante la transacción.
     */
    public String generarVenta(int idAdministrador, List<DetalleVenta> detalles) 
            throws StockInsuficienteException, ProductoNoEncontradoException, ProductoExpiradoException, ValidacionException, BaseDatosException {
        
        if (detalles == null || detalles.isEmpty()) {
            throw new ValidacionException("No se pueden generar ventas sin detalles de productos.");
        }

        double totalVentaCalculado = 0;
        // Preliminary checks for product validity and stock before starting the transaction
        for (DetalleVenta detalle : detalles) {
            if (detalle.getCantidad() <= 0) {
                throw new ValidacionException("La cantidad para el producto ID " + detalle.getIdProducto() + " debe ser positiva.");
            }
            try {
                // Using the non-transactional buscarPorId from ProductoDatos via public ServicioInventario.productoDatos
                Optional<Producto> productoOpt = servicioInventario.productoDatos.buscarPorId(detalle.getIdProducto());
                
                // This check is now vital as buscarPorId(int) from ProductoDatos was modified to throw PNE
                // However, the Optional pattern is still good. Let's assume buscarPorId might return empty if not found.
                // if (productoOpt.isEmpty()) { // This case is now handled by PNE from buscarPorId directly
                //    throw new ProductoNoEncontradoException("Producto con ID " + detalle.getIdProducto() + " no encontrado durante la validación preliminar.");
                // }
                Producto producto = productoOpt.get(); // PNE will be thrown by buscarPorId if not found

                if (!producto.activo()) {
                    throw new ValidacionException("Producto '" + producto.nombre() + "' (ID: " + detalle.getIdProducto() + ") está inactivo.");
                }
                if (producto.fechaCaducidad() != null && producto.fechaCaducidad().isBefore(LocalDate.now())) {
                     throw new ProductoExpiradoException("Producto '" + producto.nombre() + "' (ID: " + detalle.getIdProducto() + ") está caducado.");
                }
                if (producto.cantidad() < detalle.getCantidad()) {
                    throw new StockInsuficienteException("Stock insuficiente (chequeo preliminar) para producto '" + producto.nombre() + 
                                                         "' (ID: " + detalle.getIdProducto() + "). Disponible: " + producto.cantidad() + 
                                                         ", Requerido: " + detalle.getCantidad());
                }
                detalle.setPrecioUnitario(producto.precio());
                detalle.setSubtotal(producto.precio() * detalle.getCantidad());
                totalVentaCalculado += detalle.getSubtotal();
            } catch (ProductoNoEncontradoException e) {
                // Re-throw as it's a valid business exception here
                LOGGER.log(Level.WARNING, "Validación fallida: " + e.getMessage());
                throw e;
            }
        }

        Venta venta = new Venta();
        venta.setIdAdministrador(idAdministrador);
        venta.setFechaVenta(Timestamp.from(Instant.now()));
        venta.setTotalVenta(totalVentaCalculado);

        Connection cx = null;
        try {
            cx = ConexionBD.getConnection();
            cx.setAutoCommit(false);
            LOGGER.log(Level.INFO, "Transacción iniciada para generar venta.");

            for (DetalleVenta detalle : detalles) {
                servicioInventario.descontarStock(cx, detalle.getIdProducto(), detalle.getCantidad());
                LOGGER.log(Level.INFO, "Stock descontado para producto ID {0} en transacción.", detalle.getIdProducto());
            }

            VentaDatos.registrarVenta(cx, venta, detalles); 
            LOGGER.log(Level.INFO, "Venta y detalles registrados en transacción. ID de Venta: {0}", venta.getIdVenta());

            cx.commit();
            LOGGER.log(Level.INFO, "Transacción completada (commit) para venta ID: {0}", venta.getIdVenta());
            return "Venta registrada con éxito. ID de Venta: " + venta.getIdVenta() + ", Total: " + venta.getTotalVenta();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de SQL durante la transacción de venta, iniciando rollback.", e);
            if (cx != null) {
                try {
                    cx.rollback();
                    LOGGER.log(Level.INFO, "Rollback de la transacción de venta completado.");
                } catch (SQLException exRollback) {
                    LOGGER.log(Level.SEVERE, "Error crítico durante el rollback de la transacción de venta.", exRollback);
                }
            }
            throw new BaseDatosException("Error de base de datos al procesar la venta: " + e.getMessage(), e);
        } catch (ProductoNoEncontradoException | StockInsuficienteException | ProductoExpiradoException | ValidacionException e) {
            LOGGER.log(Level.WARNING, "Fallo al generar venta debido a excepción de negocio: " + e.getMessage(), e);
             if (cx != null) {
                try {
                    cx.rollback();
                    LOGGER.log(Level.INFO, "Rollback de la transacción de venta completado debido a: " + e.getClass().getSimpleName());
                } catch (SQLException exRollback) {
                    LOGGER.log(Level.SEVERE, "Error crítico durante el rollback.", exRollback);
                }
            }
            throw e; // Re-throw the specific business exception
        } finally {
            if (cx != null) {
                try {
                    // Aunque HikariCP resetea autoCommit al cerrar la conexión si estaba en false,
                    // es buena práctica hacerlo explícitamente si la conexión pudiera ser usada
                    // para algo más antes de cerrarse (aunque aquí no es el caso).
                    // Sin embargo, el try-with-resources en la anterior version de VentaDatos lo hacia.
                    // Aquí, con la conexión gestionada localmente, el cierre por el pool es suficiente.
                    // cx.setAutoCommit(true); // No estrictamente necesario con HikariCP y cierre inmediato
                    cx.close(); // Devuelve la conexión al pool
                    LOGGER.log(Level.INFO, "Conexión devuelta al pool.");
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Error al cerrar la conexión o devolverla al pool.", e);
                }
            }
        }
    }
}
