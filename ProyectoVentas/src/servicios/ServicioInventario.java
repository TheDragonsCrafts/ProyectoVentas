package servicios;

import datos.ProductoDatos;
import entidades.Producto;
import excepciones.ProductoExpiradoException;
import excepciones.ProductoNoEncontradoException;
import excepciones.StockInsuficienteException;
import excepciones.ValidacionException; // For producto inactivo

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServicioInventario {

    // Made productoDatos public for ServicioVentas to use for preliminary checks
    // This is a design choice; an alternative would be a dedicated method in ServicioInventario
    // for fetching product details for validation.
    public final ProductoDatos productoDatos = new ProductoDatos(); 
    private static final Logger LOGGER = Logger.getLogger(ServicioInventario.class.getName());

    /**
     * Descuenta el stock de un producto. Este método gestiona su propia transacción (o usa conexiones auto-commit).
     * Mantenido para usos que no requieran una transacción externa.
     * @param idProducto El ID del producto.
     * @param cantidad La cantidad a descontar.
     * @throws ValidacionException Si el producto está inactivo.
     * @throws ProductoExpiradoException Si el producto está caducado.
     * @throws StockInsuficienteException Si no hay stock suficiente.
     * @throws ProductoNoEncontradoException Si el producto no se encuentra.
     * @throws SQLException Si ocurre un error de base de datos.
     */
    public void descontarStock(int idProducto, int cantidad) 
            throws ValidacionException, ProductoExpiradoException, StockInsuficienteException, ProductoNoEncontradoException, SQLException {
        
        // Note: This non-transactional version now also uses the refined exceptions.
        // It gets its own connection via productoDatos methods.
        Optional<Producto> prodOpt = productoDatos.buscarPorId(idProducto); // Throws ProductoNoEncontradoException
        // buscarPorId already throws ProductoNoEncontradoException if not found.
        // So, prodOpt will not be empty here, or an exception would have been thrown.
        // However, to be absolutely safe and adhere to Optional pattern:
        if (prodOpt.isEmpty()) { 
             // This line should ideally not be reached if buscarPorId works as specified
            throw new ProductoNoEncontradoException("Producto con ID " + idProducto + " no encontrado (inesperado).");
        }

        Producto p = prodOpt.get();
        if (!p.activo()) {
            throw new ValidacionException("Producto '" + p.nombre() + "' (ID: " + idProducto + ") está inactivo.");
        }
        if (p.fechaCaducidad() != null && p.fechaCaducidad().isBefore(LocalDate.now())) {
            throw new ProductoExpiradoException("Producto '" + p.nombre() + "' (ID: " + idProducto + ") está caducado desde " + p.fechaCaducidad() + ".");
        }
        if (p.cantidad() < cantidad) {
            // This is a pre-check. The atomic check is in actualizarStock.
            throw new StockInsuficienteException("Stock insuficiente (pre-chequeo) para el producto '" + p.nombre() + "' (ID: " + idProducto + "). Disponible: " + p.cantidad() + ", Requerido: " + cantidad);
        }
        
        // For the non-transactional version, we simulate the stock update logic.
        // Ideally, we'd call a version of actualizarStock that takes no connection, 
        // but that wasn't part of the previous refactor for ProductoDatos.
        // So, we update the product object and call the general 'actualizar'.
        // This part is NOT ATOMIC in the same way as the transactional version.
        Producto actualizado = new Producto(
                p.id(), p.nombre(), p.descripcion(),
                p.precio(), p.cantidad() - cantidad,
                p.fechaCaducidad(), p.activo());
        try {
            productoDatos.actualizar(actualizado); // Uses its own connection from ConexionBD
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de BD al actualizar producto en descontarStock no transaccional", e);
            throw e; // Re-throw SQLException
        }
        LOGGER.log(Level.INFO, "Stock descontado (método no transaccional) para producto ID {0}, cantidad: {1}", new Object[]{idProducto, cantidad});
    }

    /**
     * Descuenta el stock de un producto utilizando una conexión existente (para transacciones).
     *
     * @param cx La conexión de base de datos activa.
     * @param idProducto El ID del producto.
     * @param cantidad La cantidad a descontar.
     * @throws SQLException Si ocurre un error de base de datos.
     * @throws ProductoNoEncontradoException Si el producto no se encuentra.
     * @throws StockInsuficienteException Si el stock es insuficiente (detectado por la BD o pre-chequeo).
     * @throws ValidacionException Si el producto está inactivo.
     * @throws ProductoExpiradoException Si el producto está caducado.
     */
    public void descontarStock(Connection cx, int idProducto, int cantidad) 
            throws SQLException, ProductoNoEncontradoException, StockInsuficienteException, ValidacionException, ProductoExpiradoException {
        
        // buscarPorId now throws ProductoNoEncontradoException if not found.
        Optional<Producto> prodOpt = productoDatos.buscarPorId(cx, idProducto); 
        
        // This check is technically redundant if buscarPorId guarantees exception on not found.
        // However, it's good practice with Optional if its contract isn't strictly throwing an exception.
        // Given previous changes, buscarPorId(cx, id) *will* throw ProductoNoEncontradoException.
        // So, prodOpt.get() should be safe.
        Producto p = prodOpt.get(); // Will throw NoSuchElementException if empty, but PNE should precede.

        if (!p.activo()) {
            throw new ValidacionException("Producto '" + p.nombre() + "' (ID: " + idProducto + ") está inactivo (dentro de transacción).");
        }
        if (p.fechaCaducidad() != null && p.fechaCaducidad().isBefore(LocalDate.now())) {
            throw new ProductoExpiradoException("Producto '" + p.nombre() + "' (ID: " + idProducto + ") está caducado desde " + p.fechaCaducidad() + " (dentro de transacción).");
        }
        // Pre-check for stock. The final atomic check is in productoDatos.actualizarStock.
        if (p.cantidad() < cantidad) {
            throw new StockInsuficienteException("Stock insuficiente (pre-chequeo transaccional) para producto '" + p.nombre() + "' (ID: " + idProducto + "). Disponible: " + p.cantidad() + ", Requerido: " + cantidad);
        }

        // productoDatos.actualizarStock throws ProductoNoEncontradoException or StockInsuficienteException
        productoDatos.actualizarStock(cx, idProducto, cantidad);
        LOGGER.log(Level.INFO, "Stock descontado (método transaccional) para producto ID {0}, cantidad: {1}", new Object[]{idProducto, cantidad});
    }


    public void agregarStock(int idProducto, int cantidad) throws SQLException, ProductoNoEncontradoException {
        // This method would also need a transactional version if used in a larger transaction.
        // For now, it uses its own connection via productoDatos.
        Optional<Producto> pOpt = productoDatos.buscarPorId(idProducto); // Throws PNE
        Producto p = pOpt.get(); // Safe due to PNE
        Producto nuevo = new Producto(
                p.id(), p.nombre(), p.descripcion(),
                p.precio(), p.cantidad() + cantidad,
                p.fechaCaducidad(), p.activo());
        productoDatos.actualizar(nuevo); 
    }

    public List<Producto> listarProductos() {
        return productoDatos.listarProductos(); 
    }

    public void agregarNuevoProducto(Producto p) throws SQLException, ValidacionException {
        if (productoDatos.idExiste(p.id())) { 
            throw new ValidacionException("El ID de producto '" + p.id() + "' ya existe.");
        }
        productoDatos.insertar(p); 
    }
}
