package seguridad; // Ajusta el paquete según tu estructura de proyecto

// Consider using java.time classes if possible (Java 8+)
// import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Clase modelo que representa a un administrador del sistema.
 * Mapea los datos de la tabla 'administrators' de la base de datos.
 * Esta clase se utiliza para transportar datos entre capas (e.g., DAO y Servicio).
 */
public class Administrador {

    private int adminId;            // Corresponde a admin_id (INT, PK). 0 si no está persistido.
    private String username;        // Corresponde a username (VARCHAR, UNIQUE NOT NULL) - Asumiendo restricciones típicas
    private String passwordHash;    // Corresponde a password_hash (VARCHAR) - ¡NUNCA almacenar contraseña en texto plano!
    private String fullName;        // Corresponde a full_name (VARCHAR)
    private String email;           // Corresponde a email (VARCHAR, UNIQUE, puede ser null dependiendo de la BD)
    private boolean active;         // Corresponde a is_active (BOOLEAN / TINYINT(1))
    private boolean masterAdmin;    // Corresponde a is_master_admin (BOOLEAN / TINYINT(1))

    // Consider using java.time.Instant or java.time.LocalDateTime if using Java 8+ and compatible persistence layer
    private Timestamp createdAt;    // Corresponde a created_at (TIMESTAMP DEFAULT CURRENT_TIMESTAMP)
    private Timestamp updatedAt;    // Corresponde a updated_at (TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)

    /**
     * Constructor por defecto.
     * Necesario para algunos frameworks (JPA, Jackson, etc.).
     */
    public Administrador() {
    }

    /**
     * Constructor para crear un nuevo administrador antes de guardarlo en la BD
     * (sin ID, ya que es autogenerado, y sin timestamps, que usualmente los maneja la BD).
     * Valida que los campos obligatorios no sean nulos o vacíos.
     *
     * @param username     Nombre de usuario (obligatorio).
     * @param passwordHash Hash de la contraseña (¡ya hasheada!, obligatorio).
     * @param fullName     Nombre completo (obligatorio).
     * @param email        Correo electrónico (puede ser null si la BD lo permite).
     * @param active       Estado de activación inicial.
     * @param masterAdmin  Si es el administrador maestro (normalmente false para nuevos).
     * @throws IllegalArgumentException si username, passwordHash o fullName son nulos o vacíos.
     */
    public Administrador(String username, String passwordHash, String fullName, String email, boolean active, boolean masterAdmin) {
        // Basic validation for required fields
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        if (passwordHash == null || passwordHash.trim().isEmpty()) {
            // Consider more robust hash validation if possible (e.g., format check)
            throw new IllegalArgumentException("Password hash cannot be null or empty.");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be null or empty.");
        }

        this.username = username.trim(); // Trim whitespace
        this.passwordHash = passwordHash; // Don't trim hash, whitespace might be significant depending on algorithm
        this.fullName = fullName.trim(); // Trim whitespace
        this.email = (email == null) ? null : email.trim(); // Trim if not null
        this.active = active;
        this.masterAdmin = masterAdmin;
        // ID remains 0, createdAt/updatedAt remain null until persisted/retrieved
    }


    /**
     * Constructor completo, útil para mapear datos recuperados de la base de datos.
     *
     * @param adminId       ID del administrador.
     * @param username      Nombre de usuario.
     * @param passwordHash  Hash de la contraseña recuperado de la BD.
     * @param fullName      Nombre completo.
     * @param email         Correo electrónico.
     * @param active        Estado de activación.
     * @param masterAdmin   Si es el administrador maestro.
     * @param createdAt     Fecha de creación.
     * @param updatedAt     Fecha de última actualización.
     */
    public Administrador(int adminId, String username, String passwordHash, String fullName, String email, boolean active, boolean masterAdmin, Timestamp createdAt, Timestamp updatedAt) {
        // Could add validation here too, but often data from DB is assumed to be valid
        this.adminId = adminId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.email = email;
        this.active = active;
        this.masterAdmin = masterAdmin;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // --- Getters y Setters ---
    // Standard Getters/Setters are generally fine.

    public int getAdminId() {
        return adminId;
    }

    // Usually, ID is set by the persistence layer or only once, consider if setter is needed publicly
    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
         if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        this.username = username.trim();
    }

    /**
     * Obtiene el hash de la contraseña almacenado.
     * ¡Usar con precaución! No exponer innecesariamente, evitar loggear.
     * @return El hash de la contraseña.
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Establece el hash de la contraseña.
     * ¡¡ADVERTENCIA!! Asegúrate de que la contraseña ya esté hasheada de forma segura
     * (e.g., usando BCrypt, SCrypt, Argon2) ANTES de llamar a este método.
     * ¡NUNCA pases una contraseña en texto plano aquí!
     * @param passwordHash El hash de la contraseña (resultado de una función de hashing segura).
     */
    public void setPasswordHash(String passwordHash) {
         if (passwordHash == null || passwordHash.trim().isEmpty()) {
            // Consider more robust hash validation if possible (e.g., format check)
            throw new IllegalArgumentException("Password hash cannot be null or empty.");
        }
        // Consider adding a check here or elsewhere to ensure it *looks* like a hash
        // e.g., if using BCrypt, it starts with "$2a$", "$2b$", or "$2y$"
        this.passwordHash = passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be null or empty.");
        }
        this.fullName = fullName.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        // Allow null email if DB schema permits, but trim if not null
         this.email = (email == null) ? null : email.trim();
         // Could add email format validation here if desired, though often done in service/controller layer
    }

    // Getter for boolean follows Java convention (isXXX)
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // Getter for boolean follows Java convention (isXXX)
    public boolean isMasterAdmin() {
        return masterAdmin;
    }

    public void setMasterAdmin(boolean masterAdmin) {
        this.masterAdmin = masterAdmin;
    }

    public Timestamp getCreatedAt() {
        // Defensive copy for mutable objects like Timestamp (or Date)
        return (this.createdAt != null) ? new Timestamp(this.createdAt.getTime()) : null;
    }

    public void setCreatedAt(Timestamp createdAt) {
        // Defensive copy for mutable objects
        this.createdAt = (createdAt != null) ? new Timestamp(createdAt.getTime()) : null;
    }

    public Timestamp getUpdatedAt() {
        // Defensive copy
        return (this.updatedAt != null) ? new Timestamp(this.updatedAt.getTime()) : null;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        // Defensive copy
        this.updatedAt = (updatedAt != null) ? new Timestamp(updatedAt.getTime()) : null;
    }

    // --- Métodos equals, hashCode y toString ---

    /**
     * Compara dos objetos Administrator basándose principalmente en el adminId si está presente y es diferente de 0
     * (indicando un objeto persistido). Si los IDs son 0 o iguales a 0, compara por username (asumiendo que username
     * es un identificador único natural antes de la persistencia).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Administrador that = (Administrador) o;

        // If both have a non-zero ID, compare by ID
        if (this.adminId != 0 && that.adminId != 0) {
            return this.adminId == that.adminId;
        }
        // If IDs are zero or only one has ID, compare by username (case-sensitive)
        // Consider case-insensitive comparison if username uniqueness ignores case in DB:
        // return Objects.equals(username.toLowerCase(), that.username.toLowerCase());
        return Objects.equals(username, that.username);
    }

    /**
     * Genera un hashCode basado en adminId si es diferente de 0, o en username si no.
     * Coherente con la lógica de equals.
     */
    @Override
    public int hashCode() {
        if (adminId != 0) {
            return Objects.hash(adminId);
        }
        // Consider using username.toLowerCase() if equals uses case-insensitive comparison
        return Objects.hash(username);
    }

    /**
     * Devuelve una representación en String del objeto Administrator,
     * omitiendo deliberadamente el hash de la contraseña por seguridad.
     */
    @Override
    public String toString() {
        return "Administrador{" + // Changed class name to match actual class
               "adminId=" + adminId +
               ", username='" + username + '\'' +
               ", fullName='" + fullName + '\'' +
               ", email='" + (email != null ? email : "N/A") + '\'' + // Handle null email gracefully
               ", active=" + active +
               ", masterAdmin=" + masterAdmin +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               '}';
    }
}