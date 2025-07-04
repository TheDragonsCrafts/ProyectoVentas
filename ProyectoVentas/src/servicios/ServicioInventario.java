package servicios;

import datos.ProductoDatos;
import entidades.Producto;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;  // <- importa List

public class ServicioInventario {

    private final ProductoDatos datos = new ProductoDatos();

    /**
     * Descuenta una cantidad específica del stock de un producto.
     * Realiza validaciones de existencia, actividad, caducidad y stock disponible.
     * @param idProducto El ID del producto.
     * @param cantidad La cantidad a descontar.
     * @throws Exception Si el producto no se encuentra, está inactivo, caducado, o si no hay stock suficiente.
     * @throws SQLException Si ocurre un error al actualizar la base de datos.
     */
    public void descontarStock(int idProducto, int cantidad) throws Exception {
        Producto p = datos.buscarPorId(idProducto)
                .orElseThrow(() -> new Exception("Producto con ID " + idProducto + " no encontrado."));

        if (!p.activo()) {
            throw new Exception("El producto '" + p.nombre() + "' está inactivo.");
        }
        if (p.fechaCaducidad() != null && p.fechaCaducidad().isBefore(LocalDate.now())) {
            throw new Exception("El producto '" + p.nombre() + "' está caducado (Fecha cad: " + p.fechaCaducidad() + ").");
        }
        if (p.cantidad() < cantidad) {
            throw new Exception("Stock insuficiente para '" + p.nombre() + "'. Solicitado: " + cantidad + ", Disponible: " + p.cantidad());
        }

        Producto actualizado = new Producto(
                p.id(), p.nombre(), p.descripcion(),
                p.precio(), p.cantidad() - cantidad,
                p.fechaCaducidad(), p.activo());
        datos.actualizar(actualizado); // Puede lanzar SQLException
    }

    /**
     * Agrega una cantidad específica al stock de un producto existente.
     * @param idProducto El ID del producto.
     * @param cantidad La cantidad a agregar.
     * @throws SQLException Si ocurre un error al actualizar la base de datos o si el producto no se encuentra.
     */
    public void agregarStock(int idProducto, int cantidad) throws SQLException {
        Producto p = datos.buscarPorId(idProducto)
                .orElseThrow(() -> new SQLException("Producto con ID " + idProducto + " no encontrado para agregar stock."));

        Producto nuevo = new Producto(
                p.id(), p.nombre(), p.descripcion(),
                p.precio(), p.cantidad() + cantidad,
                p.fechaCaducidad(), p.activo());
        datos.actualizar(nuevo);
    }

    /**
     * Obtiene una lista de todos los productos activos en el inventario.
     * @return Una lista de objetos Producto.
     */
    public List<Producto> listarProductos() {
        return datos.listarProductos();
    }

    /**
     * Agrega un nuevo producto al inventario.
     * Verifica que el ID del producto no exista previamente.
     * @param p El producto a agregar.
     * @throws SQLException Si ocurre un error durante la inserción en la base de datos.
     * @throws Exception Si el ID del producto ya existe.
     */
    public void agregarNuevoProducto(Producto p) throws SQLException, Exception {
        if (datos.idExiste(p.id())) {
            throw new Exception("El ID de producto '" + p.id() + "' ya existe. No se puede agregar el producto.");
        }
        datos.insertar(p);
    }
}

