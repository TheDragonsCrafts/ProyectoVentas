package excepciones;

public class AutenticacionException extends Exception {
    public AutenticacionException(String message) {
        super(message);
    }

    public AutenticacionException(String message, Throwable cause) {
        super(message, cause);
    }
}
