package seguridad; // Ajusta el paquete según tu estructura de proyecto

import java.sql.Timestamp;
import java.util.Objects;

/**
 * Clase modelo que representa a un administrador del sistema.
 * Mapea los datos de la tabla 'administrators' de la base de datos.
 */
public class Administrator {

    private int adminId;            // Corresponde a admin_id (INT, PK)
    private String username;        // Corresponde a username (VARCHAR)
    private String passwordHash;    // Corresponde a password_hash (VARCHAR) - ¡Manejar con cuidado!
    private String fullName;        // Corresponde a full_name (VARCHAR)
    private String email;           // Corresponde a email (VARCHAR, puede ser null)
    private boolean active;         // Corresponde a is_active (BOOLEAN)
    private boolean masterAdmin;    // Corresponde a is_master_admin (BOOLEAN)
    private Timestamp createdAt;    // Corresponde a created_at (TIMESTAMP)
    private Timestamp updatedAt;    // Corresponde a updated_at (TIMESTAMP)

    /**
     * Constructor por defecto.
     */
    public Administrator() {
    }

    /**
     * Constructor para crear un nuevo administrador antes de guardarlo en la BD
     * (sin ID, ya que es autogenerado, y sin timestamps, que se pondrán por defecto).
     *
     * @param username     Nombre de usuario.
     * @param passwordHash Hash de la contraseña (¡ya hasheada!).
     * @param fullName     Nombre completo.
     * @param email        Correo electrónico (puede ser null).
     * @param active       Estado de activación inicial.
     * @param masterAdmin  Si es el administrador maestro (normalmente false para nuevos).
     */
    public Administrator(String username, String passwordHash, String fullName, String email, boolean active, boolean masterAdmin) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.email = email;
        this.active = active;
        this.masterAdmin = masterAdmin;
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
    public Administrator(int adminId, String username, String passwordHash, String fullName, String email, boolean active, boolean masterAdmin, Timestamp createdAt, Timestamp updatedAt) {
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

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtiene el hash de la contraseña almacenado.
     * ¡Usar con precaución! No exponer innecesariamente.
     * @return El hash de la contraseña.
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Establece el hash de la contraseña.
     * Asegúrate de que la contraseña ya esté hasheada antes de llamar a este método.
     * @param passwordHash El hash de la contraseña.
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isMasterAdmin() {
        return masterAdmin;
    }

    public void setMasterAdmin(boolean masterAdmin) {
        this.masterAdmin = masterAdmin;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    // --- Métodos equals, hashCode y toString ---

    /**
     * Compara dos objetos Administrator basándose principalmente en el adminId si está presente,
     * o en el username si el ID es 0 (indicando un objeto aún no persistido).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Administrator that = (Administrator) o;
        if (adminId != 0 && that.adminId != 0) {
            return adminId == that.adminId; // Comparar por ID si ambos lo tienen
        }
        return Objects.equals(username, that.username); // Comparar por username si no hay ID
    }

    /**
     * Genera un hashCode basado en adminId si está presente, o en username si no.
     */
    @Override
    public int hashCode() {
        if (adminId != 0) {
            return Objects.hash(adminId);
        }
        return Objects.hash(username);
    }

    /**
     * Devuelve una representación en String del objeto Administrator,
     * omitiendo deliberadamente el hash de la contraseña por seguridad.
     */
    @Override
    public String toString() {
        return "Administrator{" +
               "adminId=" + adminId +
               ", username='" + username + '\'' +
               ", fullName='" + fullName + '\'' +
               ", email='" + email + '\'' +
               ", active=" + active +
               ", masterAdmin=" + masterAdmin +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               '}';
    }
}
