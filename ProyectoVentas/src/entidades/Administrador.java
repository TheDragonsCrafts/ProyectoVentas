package entidades;

/**
 * Entidad Administrador con sus componentes y un getter adicional
 * para compatibilidad con llamadas a getUsuario().
 */
public record Administrador(
        int id,
        String usuario,
        String hash,
        String nombreCompleto,
        String correo,
        boolean activo,
        boolean adminMaestro
) {
    /**
     * Getter adicional para el nombre de usuario,
     * necesario si el código invoca admin.getUsuario().
     */
    public String getUsuario() {
        return this.usuario;
    }

    /**
     * Devuelve el nombre completo del administrador para ser usado en
     * componentes de UI como JComboBox o JList.
     * @return El nombre completo del administrador.
     */
    @Override
    public String toString() {
        return this.nombreCompleto;
    }
}

