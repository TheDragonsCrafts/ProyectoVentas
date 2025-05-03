package servicios;

import datos.ProductoDatos;
import entidades.Producto;

import java.sql.SQLException;
import java.time.LocalDate;

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
}
