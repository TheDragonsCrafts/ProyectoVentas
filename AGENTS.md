# Guía Técnica del Agente para ProyectoVentas (Versión Profunda)

Este documento proporciona un análisis detallado del sistema `ProyectoVentas` para guiar a un agente de IA en tareas de desarrollo, como la corrección de errores, la refactorización y la implementación de nuevas funcionalidades.

## 1. Resumen del Proyecto

`ProyectoVentas` es una aplicación de escritorio de Punto de Venta (POS) construida en Java Swing. Su propósito es gestionar ventas, inventario y administradores. La aplicación se conecta a una base de datos MySQL y está estructurada siguiendo una arquitectura por capas.

## 2. Pila Tecnológica y Dependencias

-   **Lenguaje:** Java (JDK 21), con uso de características modernas como **Java Records**.
-   **UI:** Java Swing.
-   **Base de Datos:** MySQL 8.x.
-   **Build Tool:** Apache Ant (configurado a través de `build.xml` de NetBeans).
-   **Dependencias (JARs):**
    -   `jBCrypt-0.4.jar`: Para hashing de contraseñas. Ubicado en `ProyectoVentas/lib/`.
    -   `jcalendar-1.4.jar`: Para componentes de calendario en la UI. Ubicado en `ProyectoVentas/lib/`.
    -   `mysql-connector-j-9.2.0.jar`: Driver JDBC para MySQL. La ruta en `project.properties` es absoluta y podría necesitar ajuste.

## 3. Configuración del Entorno de Desarrollo

Para que el agente pueda compilar y ejecutar el proyecto, se deben cumplir los siguientes requisitos:

1.  **Base de Datos MySQL:** Se requiere una instancia de MySQL en ejecución.
2.  **Creación del Esquema:** Ejecutar el script `cafeteria.sql` para crear la base de datos `cafeteria` y todas sus tablas.
3.  **Configuración de la Conexión:** El archivo `ProyectoVentas/src/seguridad/db.properties` contiene la configuración de la conexión a la base de datos. **Es crucial verificar que la URL, el usuario y la contraseña sean correctos para el entorno local.**
    ```properties
    url=jdbc:mysql://localhost:3306/cafeteria?serverTimezone=UTC
    user=root
    password=
    ```
4.  **Compilación:** El comando `ant jar` (ejecutado en el directorio `ProyectoVentas/`) compilará el código y creará un JAR en el directorio `dist/`.

## 4. Arquitectura Detallada y Estructura de Paquetes

El sistema sigue una arquitectura de 3 capas (Presentación, Lógica de Negocio, Acceso a Datos).

-   `proyectoventas`
    -   `ProyectoVentas.java`: Punto de entrada (`main`). Su única responsabilidad es configurar el Look and Feel de Swing e iniciar `LoginFrame` en el Event Dispatch Thread (EDT).

-   `ui` (Capa de Presentación)
    -   Contiene todos los `JFrame` y componentes de Swing.
    -   **Convención:** Los formularios (`.java`) están acoplados a archivos `.form` para el diseño visual de NetBeans. **No modificar el código dentro de los bloques `//GEN-BEGIN:initComponents` y `//GEN-END:initComponents` manualmente.**
    -   La interacción del usuario se maneja a través de listeners de eventos (`actionPerformed`, etc.).
    -   **Flujo de Navegación:** `LoginFrame` -> `Menu_Principal` -> (Otras ventanas).
    -   **Comunicación:** Las clases de la UI **deben** comunicarse con la capa de `servicios`, no directamente con la de `datos`. (Nota: Se ha observado que `AltaYBaja.java` interactúa directamente con `ProductoDatos`. Esto es una inconsistencia; el patrón preferido es a través de un servicio).

-   `servicios` (Capa de Lógica de Negocio)
    -   Orquesta las operaciones. No contiene código de UI ni sentencias SQL.
    -   `ServicioLogin`: Valida credenciales.
    -   `ServicioVentas`: Procesa una venta, interactuando con `ServicioInventario` para la gestión de stock y con `VentaDatos` para la persistencia.
    -   `ServicioInventario`: Gestiona la lógica de stock (agregar, descontar).

-   `datos` (Capa de Acceso a Datos - DAO)
    -   Contiene la lógica de persistencia. Es el único lugar donde se deben encontrar sentencias SQL.
    -   **Conexión:** Todas las clases obtienen la conexión a través de `seguridad.ConexionBD.obtener()`.
    -   **Seguridad:** Utiliza `PreparedStatement` para prevenir inyección SQL.
    -   **Mapeo:** Las clases aquí convierten `ResultSet` a `records` de la capa `entidades`.
    -   **Convención:** Los métodos que buscan un único registro (ej. `buscarPorId`) devuelven un `Optional<T>`.

-   `entidades` (Modelos de Dominio)
    -   Definidos como **Java Records**. Son inmutables y se utilizan para transferir datos entre capas.
    -   `entidades.dto`: Contiene Data Transfer Objects (DTOs) como `VentaDisplayDTO`, que son estructuras de datos aplanadas y específicas para las vistas de la UI, desacoplando el modelo de dominio de la presentación.

-   `seguridad`
    -   `ConexionBD`: Gestiona la conexión Singleton a la base de datos.
    -   `UtilHash`: Wrapper para `jBCrypt` para hashear y verificar contraseñas.
    -   `Session`: Una clase estática simple para mantener el `idAdmin` del usuario que ha iniciado sesión. **Este es un estado global y debe manejarse con cuidado.**

## 5. Flujos de Trabajo Principales (Guía para Tareas)

### Flujo 1: Inicio de Sesión
1.  **UI:** `ui.login.LoginFrame` captura usuario y contraseña.
2.  **Evento:** `btnIniciarSesionActionPerformed` se dispara.
3.  **Servicio:** Llama a `servicios.ServicioLogin.autenticar(usuario, contraseña)`.
4.  **Lógica:**
    -   `ServicioLogin` usa `datos.AdministradorDatos.buscarPorUsuario(usuario)` para obtener el admin.
    -   Si existe, `ServicioLogin` usa `seguridad.UtilHash.verificar(contraseña, admin.hash())` para comparar las contraseñas.
5.  **Sesión:** Si la autenticación es exitosa, `seguridad.Session.setIdAdmin(admin.id())` almacena el ID del usuario.
6.  **Navegación:** Se cierra `LoginFrame` y se abre `ui.menu.Menu_Principal`.

### Flujo 2: Registrar una Venta
1.  **UI:** `ui.ventas.Venta` permite al usuario seleccionar productos de un `JComboBox` y agregarlos a una `JTable`. Los productos agregados se almacenan en una `List<DetalleVenta>`.
2.  **Evento:** `BtnPagarActionPerformed` se dispara.
3.  **Servicio:** Llama a `servicios.ServicioVentas.generarVenta(idAdmin, detallesList)`.
4.  **Lógica:**
    -   `ServicioVentas` itera sobre la lista de detalles y llama a `servicios.ServicioInventario.descontarStock()` para cada producto.
    -   `ServicioInventario` a su vez llama a `datos.ProductoDatos.actualizar()` para reducir la cantidad en la tabla `productos`.
    -   Si el stock se descuenta correctamente para todos los productos, `ServicioVentas` llama a `datos.VentaDatos.registrarVenta()`.
5.  **Transacción:** `VentaDatos.registrarVenta()` es transaccional. Inserta un registro en la tabla `ventas` y luego inserta todos los detalles en `detalles_venta`. Si algo falla, hace un `rollback`.

## 6. Guía para el Agente (Cómo modificar el código)

### Compilación y Pruebas (Línea de Comandos)

Dado que el agente opera en un entorno sin GUI, las pruebas y la verificación deben realizarse a través de la línea de comandos.

1.  **Compilar el Proyecto:**
    -   Después de realizar cualquier cambio en los archivos `.java`, es **obligatorio** recompilar el proyecto para asegurar que no se hayan introducido errores de sintaxis o compilación.
    -   Navega al directorio `ProyectoVentas/` y ejecuta el siguiente comando:
        ```bash
        ant jar
        ```
    -   Este comando limpiará las compilaciones anteriores, compilará todo el código fuente y empaquetará la aplicación en un archivo JAR ejecutable ubicado en `ProyectoVentas/dist/ProyectoVentas.jar`.

2.  **Verificar Cambios:**
    -   Una compilación exitosa es la principal forma de verificar que los cambios son sintácticamente correctos.
    -   Si el comando `ant jar` falla, el agente debe analizar la salida del error para identificar y corregir el problema en el código fuente o usar otro comando de compilación según sea conveniente.

### Tarea: Añadir una nueva funcionalidad (Ej: "Cancelar Venta")

1.  **Análisis de UI:** Decide dónde encaja la nueva función. Podría ser un nuevo botón en `ui.reportes.Historial_Ventas`.
2.  **Modificar UI:**
    -   Abre `Historial_Ventas.form` en el diseñador de NetBeans y añade un `JButton` (ej. `BtnCancelarVenta`).
    -   En `Historial_Ventas.java`, implementa el listener `BtnCancelarVentaActionPerformed`.
    -   En el listener, obtén el `idVenta` de la fila seleccionada en la `JTable`. Muestra un `JOptionPane.showConfirmDialog` para confirmar la acción.
3.  **Crear Lógica de Servicio:**
    -   Crea un nuevo método en `servicios.ServicioVentas`, por ejemplo, `public void cancelarVenta(int idVenta)`.
    -   Este método contendrá la lógica: cambiar el estado de la venta y reponer el stock de los productos.
4.  **Implementar Acceso a Datos:**
    -   En `datos.VentaDatos`, crea un método `actualizarEstado(int idVenta, String nuevoEstado)`.
    -   En `datos.ProductoDatos`, necesitarás un método para obtener los detalles de una venta (`List<DetalleVenta> listarPorVenta(int idVenta)`), que actualmente está vacío en `DetalleVentaDatos`. Deberás implementarlo.
    -   En `servicios.ServicioVentas.cancelarVenta`, usa los métodos de datos para:
        a.  Obtener los detalles de la venta.
        b.  Iterar sobre ellos y llamar a `ServicioInventario.agregarStock()` para cada producto.
        c.  Llamar a `VentaDatos.actualizarEstado()` para marcar la venta como 'CANCELADA'.
        d.  Envuelve estas operaciones en un `try-catch` para manejar errores de base de datos.

### Tarea: Corregir un Bug

1.  **Localizar el Bug:** Determina la capa del error.
    -   **Visual/Interacción:** El problema está en la capa `ui`. Revisa los listeners de eventos y el código que actualiza los componentes Swing en el `JFrame` correspondiente.
    -   **Lógica Incorrecta:** El problema está en la capa `servicios`. Revisa el servicio relevante para ver si la lógica de negocio es correcta.
    -   **Datos Incorrectos/Fallo de BD:** El problema está en la capa `datos`. Revisa las sentencias SQL y la lógica de mapeo de `ResultSet` a `record`.
2.  **Depurar:** Añade logs o utiliza un depurador para seguir el flujo de ejecución desde la UI hasta la base de datos.
3.  **Aplicar Corrección:** Modifica el código en la capa apropiada, respetando la arquitectura. No añadas lógica de negocio en la UI ni SQL en los servicios.

### Convenciones a Seguir

-   **Inmutabilidad:** Usa Java Records para las entidades. Si necesitas modificar una, crea una nueva instancia con los valores cambiados.
-   **Manejo de Nulos:** Prefiere `Optional<T>` en la capa de datos para búsquedas que pueden no devolver resultados.
-   **Feedback al Usuario:** Usa `JOptionPane` para mostrar mensajes de error, advertencia o éxito.
-   **Nomenclatura:** Sigue las convenciones existentes (ej. `Btn` para `JButton`, `txt` para `JTextField`, `CB` para `JComboBox`).
