package servicios;

import datos.AdministradorDatos;
import entidades.Administrador;
import excepciones.AutenticacionException;
import excepciones.BaseDatosException;
import seguridad.Session;
import seguridad.UtilHash;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServicioLogin {
    private static final Logger LOGGER = Logger.getLogger(ServicioLogin.class.getName());
    private final AdministradorDatos adminDatos = new AdministradorDatos();

    /**
     * Autentica a un administrador.
     *
     * @param usuario El nombre de usuario.
     * @param clave La contraseña en texto plano.
     * @throws AutenticacionException Si la autenticación falla (usuario no encontrado, inactivo, contraseña incorrecta).
     * @throws BaseDatosException Si ocurre un error al consultar la base de datos.
     */
    public void autenticar(String usuario, String clave) throws AutenticacionException, BaseDatosException {
        if (usuario == null || usuario.trim().isEmpty()) {
            throw new AutenticacionException("El nombre de usuario no puede estar vacío.");
        }
        if (clave == null || clave.isEmpty()) {
            throw new AutenticacionException("La contraseña no puede estar vacía.");
        }

        try {
            Optional<Administrador> adminOpt = adminDatos.buscarPorUsuario(usuario);

            if (adminOpt.isEmpty()) {
                LOGGER.log(Level.WARNING, "Intento de login fallido: Usuario ''{0}'' no encontrado.", usuario);
                throw new AutenticacionException("Usuario '" + usuario + "' no encontrado.");
            }

            Administrador admin = adminOpt.get();

            if (!admin.isActivo()) { // Assuming Administrador has an isActivo() method or similar
                LOGGER.log(Level.WARNING, "Intento de login fallido: Usuario ''{0}'' está inactivo.", usuario);
                throw new AutenticacionException("El usuario '" + usuario + "' está inactivo.");
            }

            if (!UtilHash.verificarClave(clave, admin.getClaveHash())) {
                LOGGER.log(Level.WARNING, "Intento de login fallido: Contraseña incorrecta para el usuario ''{0}''.", usuario);
                throw new AutenticacionException("La contraseña es incorrecta para el usuario '" + usuario + "'.");
            }

            // Autenticación exitosa
            Session.getInstance().setAdminLogueado(admin);
            LOGGER.log(Level.INFO, "Usuario ''{0}'' autenticado exitosamente.", usuario);

        } catch (BaseDatosException e) {
            // Log already done in AdministradorDatos, but adding context here
            LOGGER.log(Level.SEVERE, "Error de base de datos durante la autenticación del usuario: " + usuario, e);
            throw e; // Re-throw BaseDatosException
        }
        // No catch-all for Exception, let specific ones propagate or be handled by BaseDatosException
    }

    public void cerrarSesion() {
        Administrador admin = Session.getInstance().getAdminLogueado();
        if (admin != null) {
            LOGGER.log(Level.INFO, "Cerrando sesión para el usuario: {0}", admin.getUsuario());
        } else {
            LOGGER.log(Level.INFO, "Cerrando sesión, ningún usuario estaba logueado.");
        }
        Session.getInstance().setAdminLogueado(null);
    }
}
