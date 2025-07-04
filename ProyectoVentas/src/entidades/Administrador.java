package entidades;

/**
 * Representa un administrador del sistema.
 * Almacena información de identificación, credenciales y roles.
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
    // El método getUsuario() se elimina ya que los records generan accesors automáticamente (admin.usuario()).
    // Si se necesita compatibilidad con JavaBeans (getUsuario), se puede añadir explícitamente,
    // pero se prefiere el acceso directo al campo en records.

    /**
     * Representación en cadena del administrador, útil para UI (e.g., JComboBox).
     * @return El nombre completo del administrador.
     */
    @Override
    public String toString() {
        return this.nombreCompleto;
    }
}

