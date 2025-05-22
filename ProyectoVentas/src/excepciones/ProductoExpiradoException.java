package excepciones;

public class ProductoExpiradoException extends Exception {
    public ProductoExpiradoException(String message) {
        super(message);
    }

    public ProductoExpiradoException(String message, Throwable cause) {
        super(message, cause);
    }
}
