package seguridad;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Proporciona métodos de utilidad para el hashing y verificación de contraseñas
 * utilizando el algoritmo BCrypt.
 */
public final class UtilHash {

    // Constructor privado para evitar instanciación.
    private UtilHash() { }

    /**
     * Genera un hash BCrypt para una contraseña de texto plano.
     * @param textoPlano La contraseña a hashear.
     * @return El hash BCrypt de la contraseña.
     */
    public static String hash(String textoPlano) {
        // El segundo argumento de gensalt es el log_rounds, 12 es un buen valor por defecto.
        return BCrypt.hashpw(textoPlano, BCrypt.gensalt(12));
    }

    /**
     * Verifica una contraseña de texto plano contra un hash BCrypt existente.
     * @param textoPlano La contraseña de texto plano a verificar.
     * @param hash El hash BCrypt almacenado previamente.
     * @return true si la contraseña coincide con el hash, false en caso contrario.
     */
    public static boolean verificar(String textoPlano, String hash) {
        return BCrypt.checkpw(textoPlano, hash);
    }
}

