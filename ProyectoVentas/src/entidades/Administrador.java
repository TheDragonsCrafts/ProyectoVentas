package entidades;

public record Administrador(
        int id,
        String usuario,
        String hash,
        String nombreCompleto,
        String correo,
        boolean activo,
        boolean adminMaestro
) {}

