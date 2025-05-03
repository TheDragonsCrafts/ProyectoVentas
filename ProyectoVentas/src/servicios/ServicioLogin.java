package servicios;

import datos.AdministradorDatos;
import entidades.Administrador;
import seguridad.UtilHash;

public class ServicioLogin {

    private final AdministradorDatos datos = new AdministradorDatos();

    public Administrador autenticar(String usuario, String contraseña) throws Exception {
        var opt = datos.buscarPorUsuario(usuario);
        if (opt.isEmpty()) throw new Exception("Usuario no encontrado");

        var admin = opt.get();
        if (!admin.activo()) throw new Exception("Usuario inactivo");
        if (!UtilHash.verificar(contraseña, admin.hash()))
            throw new Exception("Contraseña incorrecta");

        return admin;
    }
}
