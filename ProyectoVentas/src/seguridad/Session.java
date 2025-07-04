package seguridad;

/**
 * Clase de utilidad para gestionar la sesión del usuario (Administrador).
 * Almacena el ID del administrador que ha iniciado sesión.
 * Esta clase utiliza campos estáticos, lo que implica una única sesión global para la aplicación.
 */
public final class Session {
    private static int idAdminLogueado;

    // Constructor privado para evitar instanciación.
    private Session() {}

    /**
     * Establece el ID del administrador que ha iniciado sesión.
     * @param id El ID del administrador.
     */
    public static void setIdAdmin(int id) {
        idAdminLogueado = id;
    }

    /**
     * Obtiene el ID del administrador que ha iniciado sesión.
     * @return El ID del administrador, o 0 si ningún administrador ha iniciado sesión
     *         o si el ID no fue establecido correctamente.
     */
    public static int getIdAdmin() {
        return idAdminLogueado;
    }
}

