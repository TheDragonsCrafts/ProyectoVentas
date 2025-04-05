/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package seguridad;

import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author IanDa
 */
public final class PasswordUtil { // 'final' para evitar herencia

    // Work factor (log rounds). Valor recomendado entre 10 y 12.
    // Un valor más alto es más seguro pero más lento. 12 es un buen balance.
    private static final int WORK_FACTOR = 12;

    /**
     * Constructor privado para prevenir la instanciación de la clase utilitaria.
     */
    private PasswordUtil() {
        throw new AssertionError("Esta clase no debe ser instanciada.");
    }

    /**
     * Genera un hash seguro de una contraseña utilizando bcrypt.
     * Incluye una sal generada automáticamente.
     *
     * @param plainPassword La contraseña en texto plano a hashear. No debe ser null ni vacía.
     * @return El hash de la contraseña (incluye la sal y el work factor).
     * @throws IllegalArgumentException Si plainPassword es null o vacía.
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede ser nula ni vacía.");
        }
        // BCrypt.gensalt() genera una sal con el work factor especificado.
        // BCrypt.hashpw() combina la contraseña y la sal para generar el hash.
        String salt = BCrypt.gensalt(WORK_FACTOR);
        return BCrypt.hashpw(plainPassword, salt);
    }

    /**
     * Verifica si una contraseña en texto plano coincide con un hash almacenado.
     * Extrae automáticamente la sal y el work factor del hash proporcionado.
     *
     * @param plainPassword  La contraseña en texto plano ingresada por el usuario.
     * @param hashedPassword El hash de la contraseña almacenado en la base de datos.
     * @return true si la contraseña coincide con el hash, false en caso contrario
     *         (o si alguna entrada es inválida/null).
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || plainPassword.isEmpty() || hashedPassword == null || hashedPassword.isEmpty()) {
            // No se puede verificar una contraseña vacía o contra un hash vacío.
            return false;
        }

        try {
            // BCrypt.checkpw compara la contraseña plana contra el hash almacenado.
            // Detecta automáticamente la sal y el work factor usados en el hash.
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            // Esto puede ocurrir si el formato del hashedPassword es inválido
            // o si la contraseña plana es excesivamente larga (poco común).
            System.err.println("Error al verificar la contraseña (formato de hash inválido?): " + e.getMessage());
            return false; // Considerar el chequeo como fallido en caso de error
        }
    }

    // --- Ejemplo de uso (Opcional, para probar la clase) ---
    /*
    public static void main(String[] args) {
        String miPassword = "contraseñaSegura123";

        // 1. Hashear la contraseña
        String hashGenerado = hashPassword(miPassword);
        System.out.println("Contraseña original: " + miPassword);
        System.out.println("Hash generado     : " + hashGenerado); // Este hash se guardaría en la BD

        // 2. Verificar la contraseña (simulando un login)
        String intentoLoginCorrecto = "contraseñaSegura123";
        String intentoLoginIncorrecto = "contraseñaincorrecta";

        System.out.println("\nVerificando intento correcto...");
        boolean esCorrecta = checkPassword(intentoLoginCorrecto, hashGenerado);
        System.out.println("¿Coincide '" + intentoLoginCorrecto + "'?: " + esCorrecta); // Debería ser true

        System.out.println("\nVerificando intento incorrecto...");
        esCorrecta = checkPassword(intentoLoginIncorrecto, hashGenerado);
        System.out.println("¿Coincide '" + intentoLoginIncorrecto + "'?: " + esCorrecta); // Debería ser false

        // Prueba con contraseña nula o hash inválido
        System.out.println("\nVerificando contraseña nula...");
        System.out.println("¿Coincide null?: " + checkPassword(null, hashGenerado)); // false

        System.out.println("\nVerificando hash inválido...");
        System.out.println("¿Coincide contra 'hashinvalido'?: " + checkPassword(miPassword, "hashinvalido")); // false
    }
    */
}
