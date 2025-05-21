package servicios;

import datos.ProductoDatos;
import entidades.Producto;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;  // <- importa List

public class ServicioInventario {

    private final ProductoDatos datos = new ProductoDatos();

    public void descontarStock(int idProducto, int cantidad) throws Exception {
        var prodOpt = datos.buscarPorId(idProducto);
        if (prodOpt.isEmpty()) throw new Exception("Producto no encontrado");

        Producto p = prodOpt.get();
        if (!p.activo()) throw new Exception("Producto inactivo");
        if (p.fechaCaducidad() != null && p.fechaCaducidad().isBefore(LocalDate.now()))
            throw new Exception("Producto caducado");
        if (p.cantidad() < cantidad)
            throw new Exception("Stock insuficiente");

        Producto actualizado = new Producto(
                p.id(), p.nombre(), p.descripcion(),
                p.precio(), p.cantidad() - cantidad,
                p.fechaCaducidad(), p.activo());
        datos.actualizar(actualizado);
    }

    public void agregarStock(int idProducto, int cantidad) throws SQLException {
        var p = datos.buscarPorId(idProducto).orElseThrow();
        Producto nuevo = new Producto(
                p.id(), p.nombre(), p.descripcion(),
                p.precio(), p.cantidad() + cantidad,
                p.fechaCaducidad(), p.activo());
        datos.actualizar(nuevo);
    }

    /**
     * Devuelve la lista de productos activos para poblar el ComboBox.
     */
    public List<Producto> listarProductos() {
        return datos.listarProductos();
    }

    /**
     * Agrega un nuevo producto al inventario.
     * Verifica primero si el ID del producto ya existe.
     *
     * @param p El producto a agregar.
     * @throws SQLException Si ocurre un error de base de datos durante la inserción.
     * @throws Exception Si el ID del producto ya existe.
     */
    public void agregarNuevoProducto(Producto p) throws SQLException, Exception {
        if (datos.idExiste(p.id())) {
            throw new Exception("El ID de producto '" + p.id() + "' ya existe.");
        }
        // Si ocurre un error durante la inserción, ProductoDatos.insertar(p) lanzará SQLException
        datos.insertar(p);
    }
}

