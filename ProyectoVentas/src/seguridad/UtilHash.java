package seguridad;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utilidad para hashear y verificar contraseñas.
 */
public final class UtilHash {

    private UtilHash() { }

    public static String hash(String textoPlano) {
        return BCrypt.hashpw(textoPlano, BCrypt.gensalt(12));
    }

    public static boolean verificar(String textoPlano, String hash) {
        return BCrypt.checkpw(textoPlano, hash);
    }
}

