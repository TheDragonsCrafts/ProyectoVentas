package servicios;

import datos.AdministradorDatos;
import entidades.Administrador;
import seguridad.UtilHash;

public class ServicioLogin {

    private final AdministradorDatos datos = new AdministradorDatos();

    /**
     * Autentica a un administrador basándose en su nombre de usuario y contraseña.
     * Verifica que el usuario exista, esté activo y que la contraseña sea correcta.
     * @param usuario El nombre de usuario del administrador.
     * @param contraseña La contraseña en texto plano.
     * @return El objeto Administrador si la autenticación es exitosa.
     * @throws Exception Si el usuario no se encuentra, está inactivo o la contraseña es incorrecta.
     */
    public Administrador autenticar(String usuario, String contraseña) throws Exception {
        Administrador admin = datos.buscarPorUsuario(usuario)
                .orElseThrow(() -> new Exception("Usuario '" + usuario + "' no encontrado."));

        if (!admin.activo()) {
            throw new Exception("El usuario '" + usuario + "' se encuentra inactivo.");
        }

        if (!UtilHash.verificar(contraseña, admin.hash())) {
            throw new Exception("Contraseña incorrecta para el usuario '" + usuario + "'.");
        }

        return admin;
    }
}
