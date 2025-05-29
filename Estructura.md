Proyecto Ventas – Estructura y Documentación Técnica
Propósito y Funcionamiento General del Programa
Proyecto Ventas es una aplicación de escritorio desarrollada en Java (usando Swing) para gestionar la venta de productos y la administración de usuarios (administradores del sistema). El sistema permite que un usuario administrador inicie sesión para acceder a un menú principal desde el cual puede realizar operaciones clave: registrar nuevas ventas, administrar el inventario de productos (altas, bajas y modificaciones), gestionar cuentas de otros administradores y consultar un historial de ventas con detalles. Todo esto se apoya en una base de datos MySQL donde se almacena la información de usuarios, productos y transacciones de venta. El programa sigue una estructura cliente-servidor simplificada, donde la interfaz gráfica (cliente) interactúa con la lógica de negocio y estas a su vez con la capa de datos (servidor de base de datos). Al ejecutar la aplicación, la clase principal lanza una ventana de Login que solicita credenciales de administrador. Si es la primera ejecución y no existe ningún administrador maestro registrado, el sistema muestra directamente la opción de crear un administrador inicial en lugar de pedir login
GitHub
. Una vez que un administrador válido inicia sesión, el sistema guarda su ID en una sesión en memoria y presenta el Menú Principal
GitHub
. Desde dicho menú, el usuario puede navegar a los distintos módulos: módulo de ventas, módulo de inventario de productos, módulo de administración de usuarios, y módulo de reportes de ventas, según corresponda a sus permisos (solo el administrador maestro puede acceder a la gestión de usuarios)
GitHub
. Al finalizar, el usuario puede cerrar sesión, lo que retorna a la pantalla de login
GitHub
. A continuación, se detalla la estructura por componentes (capas) y la funcionalidad de cada módulo del sistema.
Arquitectura General y Capas del Sistema
El sistema está organizado en capas de responsabilidad, siguiendo una arquitectura MVC simplificada:
Capa de Presentación (Interfaz de Usuario - UI): Implementada con Java Swing. Consiste en múltiples formularios (frames) para las distintas funciones: ventana de Login, Menú principal, gestión de administradores, gestión de productos, ventana de venta y ventanas de reportes. Estas clases se ubican bajo el paquete ui y sus subpaquetes (ui.login, ui.menu, ui.admin, ui.productos, ui.ventas, ui.reportes). La responsabilidad de esta capa es manejar la interacción con el usuario, mostrar datos en componentes gráficos (tablas, campos de texto, etc.) y enviar las acciones del usuario a la lógica de negocio. Ejemplo: La clase LoginFrame procesa el evento del botón "Iniciar sesión" capturando el usuario y contraseña ingresados, y llama al servicio de autenticación para validarlos
GitHub
. Si la validación es exitosa, la UI muestra un mensaje de bienvenida y abre el menú principal
GitHub
.
Capa de Lógica de Negocio (Servicios): Implementada en el paquete servicios. Contiene clases que encapsulan las reglas de negocio y procesos del sistema, utilizando la capa de datos cuando es necesario. Esta capa actúa como intermediario entre la UI y la base de datos. Por ejemplo, el ServicioLogin maneja la autenticación de usuarios: verifica que el usuario exista, que esté activo y que la contraseña ingresada coincida (aplicando hashing para la comparación)
GitHub
. El ServicioVentas gestiona el proceso completo de registrar una venta: calcula el monto total, verifica y descuenta el stock de cada producto vendido a través del servicio de inventario, registra la venta y detalles en la base de datos, todo controlado en una transacción
GitHub
GitHub
. A su vez, el ServicioInventario encapsula operaciones sobre los productos, como descontar o agregar stock comprobando que no se violen reglas (por ejemplo, no permitir stock negativo ni ventas de productos inactivos o caducados)
GitHub
. Esta capa asegura consistencia y centraliza la lógica compleja fuera de la interfaz de usuario.
Capa de Acceso a Datos (DAO/Conexión BD): Implementada en el paquete datos. Contiene clases cuyos métodos realizan operaciones CRUD (crear, leer, actualizar, borrar) directamente contra la base de datos, generalmente mediante JDBC (sentencias SQL preparadas). Cada entidad principal tiene su clase DAO: por ejemplo, AdministradorDatos maneja la tabla de administradores (métodos para buscar, insertar, actualizar, eliminar administradores)
GitHub
GitHub
, ProductoDatos hace lo propio con la tabla de productos
GitHub
GitHub
, y VentaDatos se encarga de registrar ventas (inserta una venta y sus detalles en conjunto) y consultar ventas para los reportes
GitHub
GitHub
. Estas clases usan la clase de conexión (ubicada en seguridad.ConexionBD) para obtener un objeto Connection compartido y ejecutar las operaciones SQL. La capa de datos no contiene lógica de presentación ni reglas complejas: se limita a interactuar con la BD (por ejemplo, ProductoDatos.listarProductos() devuelve todos los productos activos de la tabla para mostrarlos en el inventario
GitHub
).
Capa de Entidades (Modelo de Datos): Ubicada en el paquete entidades (y subpaquete entidades.dto). Aquí se definen las clases o records que representan los datos del dominio de la aplicación: por ejemplo, la entidad Producto representa un producto con sus campos (id, nombre, descripción, precio, cantidad, fechaCaducidad, activo)
GitHub
 tal como existe en la base de datos, y la entidad Administrador representa a un usuario administrador del sistema con campos como usuario, hash de contraseña, nombre completo, correo, roles y estado
GitHub
. Si bien Java record proporciona automáticamente métodos para acceder a sus componentes, estas entidades son principalmente estructuras de datos. Existen también algunos records especiales en entidades.dto utilizados para los reportes (mostrar ventas con detalles); por ejemplo VentaDisplayDTO combina los datos de una venta con el nombre del administrador que la realizó y una lista de detalles en formato amigable
GitHub
GitHub
. Estas clases modelo se utilizan tanto en la capa de datos (al construir objetos a partir de resultados SQL)
GitHub
 como en la capa de lógica y UI para transferir y mostrar información.
Utilidades de Seguridad: Dentro del paquete seguridad se encuentran utilidades transversales. La clase ConexionBD administra la conexión única a la base de datos MySQL mediante el patrón singleton
GitHub
GitHub
, leyendo los parámetros de conexión desde un archivo de propiedades. La clase UtilHash provee métodos estáticos para generar hashes bcrypt y verificar contraseñas
GitHub
, garantizando que el almacenamiento de credenciales sea seguro (nunca se guarda la contraseña en texto plano, sino un hash). Finalmente, la clase Session actúa como un contenedor simple para almacenar datos de sesión en memoria (por ejemplo, el ID del administrador actualmente autenticado) de forma global mientras la aplicación está en uso
GitHub
.
A continuación, se detalla cada módulo funcional del sistema, explicando sus componentes principales (UI, servicios, datos) y cómo interactúan entre sí.
Módulo de Autenticación y Seguridad (Login)
Este módulo cubre el proceso de inicio de sesión de administradores y la creación del primer usuario administrador (administrador maestro).
Ventana de Login (UI): ui.login.LoginFrame es la pantalla inicial. Su función principal es tomar las credenciales ingresadas por el usuario y autenticarlas. Al construirse, la ventana de login decide si mostrar el formulario de inicio de sesión normal o forzar la creación de un administrador maestro, dependiendo de si existe al menos un administrador maestro en la base de datos
GitHub
. Para ello, en el constructor consulta AdministradorDatos.existeAdminMaestro(); si no hay ninguno, oculta el botón de “Iniciar Sesión” y muestra en su lugar un botón “Crear Admin”
GitHub
, guiando al usuario a registrar el primer administrador del sistema. Cuando el formulario de login está habilitado (ya existe al menos un usuario), al hacer clic en “Iniciar sesión” la clase toma el usuario y contraseña ingresados y llama a servicios.ServicioLogin.autenticar(usuario, contraseña)
GitHub
.
Servicio de Login (Lógica): ServicioLogin encapsula la verificación de credenciales. Internamente utiliza AdministradorDatos.buscarPorUsuario(usuario) para obtener los datos del administrador con el nombre de usuario dado
GitHub
. Si no se encuentra coincidencia, lanza una excepción indicando "Usuario no encontrado". Si el usuario existe pero está marcado como inactivo, se rechaza el login con "Usuario inactivo"
GitHub
. Luego compara la contraseña ingresada con el hash almacenado: para esto emplea UtilHash.verificar(contraseñaPlano, hashAlmacenado) que usa la función bcrypt
GitHub
. Si el hash coincide (es decir, la contraseña es correcta) y el usuario está activo, el servicio devuelve el objeto Administrador autenticado. La ventana de Login entonces registra en la sesión el ID de este admin (Session.setIdAdmin(...) para uso global)
GitHub
, muestra un mensaje de bienvenida con el nombre completo del admin
GitHub
, y abre la ventana Menu_Principal (cerrando la pantalla de login)
GitHub
. Todo este flujo asegura que solo usuarios válidos accedan al sistema y que sus contraseñas sean manejadas de forma segura (hash + salt mediante bcrypt)
GitHub
.
Creación de Administrador Maestro (UI): Si no existe un administrador maestro, desde la pantalla de Login se accede al formulario ui.admin.CrearAdminFrame para registrar el primero. Esta ventana de creación de administradores se utiliza tanto para el caso inicial como posteriormente para añadir nuevos administradores o editar existentes. En el contexto inicial, el formulario marca automáticamente la casilla "Administrador Maestro" y la deshabilita (ya que el primer usuario necesariamente será maestro)
GitHub
. El proceso de creación solicita nombre de usuario, nombre completo, correo y contraseña. Al confirmar (botón “Crear”), la lógica del formulario valida que los campos obligatorios no estén vacíos y que ambas contraseñas coincidan
GitHub
. Luego comprueba mediante AdministradorDatos: (a) que no exista ya un admin maestro en la BD (para no crear dos, aunque en el primer uso esto no aplicaría)
GitHub
, y (b) que el nombre de usuario elegido no esté ya tomado
GitHub
. Tras las validaciones, genera un hash bcrypt de la contraseña ingresada usando UtilHash.hash(...)
GitHub
 y construye un objeto Administrador con los datos ingresados (con ID 0, que la base de datos autogenerará). Finalmente, invoca AdministradorDatos.insertar(nuevoAdmin) para guardar el administrador en la base de datos
GitHub
. Si la inserción tiene éxito, se notifica al usuario con un mensaje de éxito
GitHub
 y la ventana se cierra. Nota: En modo creación inicial, al cerrar esta ventana el programa regresa a la pantalla de login (la cual se había ocultado) – el botón “Cancelar” en esta misma ventana también permite volver al login sin crear ningún usuario
GitHub
.
Sesión y Cierre de Sesión: El sistema mantiene el estado del usuario autenticado actual a través de la clase singleton seguridad.Session. Esta simplemente guarda el ID del administrador logueado en una variable estática accesible globalmente
GitHub
. Esto es utilizado, por ejemplo, al registrar una venta para saber qué administrador la realizó, o para verificar permisos (el menú principal comprueba si el admin actual es maestro leyendo el objeto Admin de la BD y viendo su flag correspondiente
GitHub
). Cuando el usuario selecciona “Cerrar sesión” desde el menú principal, la aplicación simplemente descarta la ventana actual y lanza de nuevo el LoginFrame
GitHub
, limpiando así cualquier estado de interfaz; el ID en Session puede mantenerse pero ya no tiene efecto hasta que se vuelva a iniciar sesión.
En resumen, el módulo de autenticación garantiza que solo personal autorizado ingrese al sistema, inicializa la sesión global y controla la creación segura del usuario maestro. El uso de hashing bcrypt para contraseñas y la restricción a un único administrador maestro son aspectos clave de la seguridad de esta capa.
Módulo de Gestión de Usuarios Administradores
Este módulo está disponible únicamente para el administrador maestro y permite crear nuevos administradores, editar sus datos, cambiar su estado activo/inactivo o eliminarlos. Su objetivo es que un admin maestro pueda delegar acceso a otros usuarios o revocar permisos según sea necesario.
Menú Principal – acceso condicional: En la interfaz del menú principal (ui.menu.Menu_Principal), el botón “Administrar Usuarios” (que lleva a la gestión de admins) solo está habilitado si el usuario actual tiene rol de administrador maestro
GitHub
. Esta comprobación se realiza al construir el menú: se obtiene el admin actual desde la BD (AdministradorDatos.buscarPorId(Session.getIdAdmin())) y si admin.adminMaestro() es true, se habilita el botón, de lo contrario se deshabilita
GitHub
. De esta forma, un administrador estándar no podrá siquiera entrar a las funciones de gestión de usuarios.
Ventana de Gestión de Administradores (UI): ui.admin.Gestion_Administradores es un formulario que muestra en una tabla todos los administradores registrados y ofrece botones para las operaciones CRUD sobre ellos. Al inicializarse, invoca cargarAdministradores() que obtiene la lista completa de admins mediante AdministradorDatos.listarTodos() y la muestra en una tabla
GitHub
. La tabla presenta columnas como ID, Usuario, Nombre Completo, Correo, si está Activo, y Rol (Maestro o Estándar)
GitHub
. Encima de la tabla hay un campo de búsqueda y su botón: al usarlo, se toma el texto ingresado y se llama a AdministradorDatos.buscarPorTermino(termino) para filtrar admins cuyo nombre de usuario o nombre completo contengan dicho texto
GitHub
. Si el término de búsqueda está vacío, se vuelve a listar todo
GitHub
.
Crear Nuevo Administrador: Al pulsar “Nuevo Admin” en esta ventana, se abre el mismo formulario CrearAdminFrame utilizado en el login inicial, pero ahora en modo creación estándar
GitHub
. En este contexto, la casilla "Admin Maestro" aparece disponible solo si aún no existe ningún maestro (lo cual normalmente ya existe), evitando duplicar ese rol
GitHub
. El proceso de registro es similar al descrito antes: valida campos, comprueba usuario único, calcula hash y guarda vía insertar
GitHub
GitHub
. Al cerrarse la ventana de creación (sea por éxito o cancelación), un WindowListener notifica a Gestion_Administradores para recargar la tabla de admins
GitHub
, de modo que el nuevo usuario aparezca en la lista sin requerir acción manual.
Editar Administrador Existente: El botón “Editar” permite modificar los datos de un administrador seleccionado en la tabla. La implementación toma la fila seleccionada y extrae su ID
GitHub
. Luego usa AdministradorDatos.buscarPorId(id) para obtener el objeto completo desde la BD
GitHub
. Si el admin sigue existiendo (no fue eliminado en paralelo), abre un CrearAdminFrame pasándole ese objeto en su constructor
GitHub
. El formulario de creación detecta que se pasó un admin a editar, por lo que carga sus datos en los campos y cambia su título y botón a “Guardar Cambios”
GitHub
GitHub
. En modo edición, el proceso al guardar difiere: si se deja la contraseña en blanco significa que no se desea cambiarla (el código conserva el hash antiguo)
GitHub
, se aplican las mismas validaciones de unicidad (no permitir cambiar nombre de usuario a uno ya existente)
GitHub
 y de unicidad de admin maestro (no permitir marcar a otro usuario como maestro si ya hay uno existente)
GitHub
. Finalmente, se construye un nuevo objeto Administrador con los cambios y se llama a AdministradorDatos.actualizar(adminActualizado) para guardar en la BD
GitHub
. Al cerrar la ventana de edición, también se refresca la tabla de la pantalla de gestión
GitHub
. De esta manera, el admin maestro puede corregir nombres, correos o roles de otros admins, e incluso cambiarles la contraseña (ingresando una nueva en el formulario).
Activar/Desactivar (Alta/Baja) Admin: En la tabla, la columna “Activo” indica si una cuenta de admin está habilitada (“Sí”) o deshabilitada (“No”). Para cambiar este estado sin borrar el registro, existe el botón “Activar/Desactivar”. La acción toma el administrador seleccionado y prepara el estado contrario al actual (si está activo, pasará a inactivo y viceversa)
GitHub
. Se muestra un diálogo de confirmación indicando el usuario y el nuevo estado que se aplicará
GitHub
. Si el usuario confirma, se llama a AdministradorDatos.actualizarEstadoActivo(idAdmin, nuevoEstado) que ejecuta un UPDATE en la base de datos para cambiar el campo activo
GitHub
. Si la operación devuelve éxito (al menos una fila afectada), la interfaz muestra un mensaje de confirmación y recarga la lista
GitHub
. Esto permite suspender a un administrador sin eliminarlo completamente (por ejemplo, desactivar temporalmente su acceso).
Eliminar Administrador: El botón “Eliminar” realiza una eliminación permanente del registro de administrador seleccionado. Tras solicitar confirmación al usuario (indicando que la acción no se puede deshacer)
GitHub
, invoca AdministradorDatos.eliminar(id) que ejecuta un DELETE FROM administradores WHERE id_administrador = ?
GitHub
. Si la eliminación fue exitosa en la BD (retorna true), la tabla se actualiza y se informa que el admin fue eliminado
GitHub
. En caso de error, se muestra un mensaje de fallo; por ejemplo, si existieran restricciones de clave foránea (ventas realizadas por ese admin), la operación podría ser rechazada por el motor de BD
GitHub
. En el código se deja comentada la posibilidad de prevenir la eliminación de ciertos usuarios especiales (como evitar borrar al último admin maestro), pero no se implementa lógica adicional
GitHub
. Es responsabilidad del usuario maestro asegurar no eliminar su propia cuenta o la única cuenta disponible.
En conclusión, el módulo de gestión de administradores proporciona al admin maestro las herramientas para manejar los usuarios del sistema. Sus funciones clave incluyen: crearAdministradores (alta de nuevos admins), editarAdministradores (modificar datos y roles), cambiarEstado (activar/desactivar) y eliminarAdministradores. Estas operaciones se reflejan inmediatamente en la base de datos a través de la clase AdministradorDatos y mantienen la integridad mediante validaciones (por ejemplo, unicidad de usuario y unicidad de admin maestro). La interfaz ofrece retroalimentación al usuario maestro en cada acción, asegurando un manejo transparente de las cuentas de administrador.
Módulo de Gestión de Productos (Inventario)
El módulo de Inventario abarca todas las funcionalidades relacionadas con productos: consulta de stock disponible, búsqueda de productos, registro de nuevos productos, modificación de existentes y bajas (marcar como inactivos). Este módulo está disponible para cualquier administrador autenticado (no solo maestro, asumiendo que todos pueden manejar inventario).
Consulta y Búsqueda de Inventario (UI): La pantalla ui.productos.Inventario muestra una lista de todos los productos activos registrados. Al abrirse, configura las columnas de la tabla (ID, Nombre, Descripción, Precio, Cantidad, Fecha de caducidad, Activo)
GitHub
GitHub
 e invoca cargarTodosProductos() para rellenarla. Este método obtiene todos los productos activos a través de ProductoDatos.listarProductos()
GitHub
, que ejecuta un SELECT filtrando activo = true
GitHub
, y por cada producto llena una fila en la tabla con sus datos (convirtiendo la fecha de caducidad a texto legible, u omitiéndola si es nula)
GitHub
. El formulario también ofrece campos de búsqueda por nombre o por ID, con su botón “Buscar”. Al hacer clic en buscar, se lee el texto de nombre y/o ID ingresado
GitHub
. Si ambos están vacíos, se cargan todos nuevamente
GitHub
. Si el ID está especificado, tiene prioridad: se intenta convertir a entero y se usa ProductoDatos.buscarPorId(id)
GitHub
. Este método busca en la BD un producto por su clave primaria; cabe notar que solo retornará un Producto si está activo
GitHub
 (el diseño intencional es no mostrar productos inactivos en esta lista general). Si se encuentra, la interfaz agrega solo ese producto a la tabla. Si no existe o está inactivo, se notifica que no se encontró. Si en lugar del ID se proporcionó un nombre (y no ID), se usa ProductoDatos.buscarPorNombre(patrón) para traer todos cuyos nombres contengan la subcadena ingresada
GitHub
 (también solo activos, según la consulta SQL)
GitHub
. Esos resultados poblarán la tabla. En caso de error de formato (e.g. ID no numérico), se muestra un mensaje de "ID inválido"
GitHub
. El botón “Regresar” cierra esta ventana y retorna al menú principal
GitHub
.
Alta, Baja y Edición de Productos (UI): La pantalla principal para crear o modificar productos es ui.productos.AltaYBaja. En un mismo formulario permite: dar de alta un producto nuevo, editar uno existente, o dar de baja (eliminar) un producto, dependiendo de las acciones del usuario. Los campos incluyen ID de producto, nombre, descripción, precio, cantidad, fecha de caducidad (texto con formato) y un checkbox "Activo". La lógica es la siguiente:
Al abrir AltaYBaja, el usuario puede ingresar un ID específico en el campo ID y pulsar “Buscar por ID” para cargar un producto existente en el formulario. Esta acción toma el ID ingresado, busca el producto correspondiente vía ProductoDatos.buscarPorId(id)
GitHub
 y si lo encuentra, rellena todos los campos del formulario con los datos del producto
GitHub
. Además, formatea la fecha de caducidad al formato dd/MM/yy antes de mostrarla
GitHub
. Si no se encuentra producto (por ejemplo, se ingresó un ID inexistente o de un producto inactivo), muestra "Producto no encontrado"
GitHub
.
Crear o Actualizar (Guardar): El botón principal “Guardar” ejecuta la operación de alta o modificación. El comportamiento depende del estado del checkbox "Activo" y de si el producto ya existía:
Si Activo está marcado, significa que queremos un producto en stock (nuevo o actualizar uno existente). El código recoge todos los campos del formulario, validando que nombre y descripción no estén vacíos y que los valores numéricos (ID, precio, cantidad) sean válidos
GitHub
GitHub
. La fecha de caducidad, si se ingresó, se intenta parsear en formato dd/MM/yy
GitHub
 (si falla, se lanza excepción que es capturada mostrando error de formato
GitHub
). Con los datos, se instancia un nuevo objeto Producto con ese ID y atributos
GitHub
. Luego, el flujo comprueba si ese producto ya existe en la base de datos: utiliza ProductoDatos.buscarPorId(id) para ver si hay un producto activo con ese ID
GitHub
.
Si sí existe (es un update), simplemente llama a ProductoDatos.actualizar(p) para aplicar los cambios en la base de datos
GitHub
. Esto ejecuta un UPDATE productos SET ... WHERE id_producto = ? con los nuevos valores
GitHub
. Al terminar, informa al usuario "Producto actualizado correctamente"
GitHub
.
Si no existe un producto activo con ese ID, podría significar dos cosas: o nunca ha habido un producto con ese ID, o hubo uno pero está inactivo. Para estar seguros de no duplicar claves, el programa invoca ProductoDatos.idExiste(id)
GitHub
. Este método hace un COUNT en la tabla sin filtrar activos
GitHub
. Si idExiste devuelve true, significa que ese ID ya fue utilizado (probablemente corresponde a un producto dado de baja). En tal caso, se muestra un error al usuario indicando que el ID ya existe y debe elegir otro
GitHub
. Si idExiste devuelve false, entonces ese ID está totalmente libre y se procede a insertar un nuevo registro: se llama a ProductoDatos.insertar(p)
GitHub
 para crear el producto en la BD. La inserción incluye explícitamente el ID proporcionado y los demás campos
GitHub
 (en este sistema el ID de producto es decidido por el usuario, no autogenerado, por lo que debe ser único y válido). Tras insertar, se confirma con el mensaje "Producto guardado correctamente con ID X"
GitHub
.
Si Activo no está marcado (checkbox desmarcado), la intención es dar de baja/eliminar el producto indicado. En este caso, el programa verifica primero si existe un producto activo con ese ID (buscarPorId retornará vacío si está inactivo o no existe)
GitHub
. Si no se encuentra ninguno activo, muestra "No existe producto con ID X" como error
GitHub
. Si sí existe (activo actualmente), entonces invoca ProductoDatos.eliminar(id)
GitHub
. En la implementación, este método realiza un UPDATE productos SET activo = false WHERE id_producto = ? en la base de datos
GitHub
, marcando el producto como inactivo en lugar de borrarlo físicamente (esto para conservar referencias en ventas pasadas, etc.). Al completar, se notifica "Producto eliminado correctamente"
GitHub
. Desde ese momento, buscarPorId ya no lo retornará y no aparecerá en listados normales.
El formulario de Alta/Baja incluye también un botón “Cancelar” que cierra la ventana sin guardar cambios (descartando cualquier entrada)
GitHub
, y un botón “Regresar” que además de cerrar la ventana, retorna al menú principal
GitHub
.
Lógica de Inventario (Servicio de Inventario): Aunque muchas operaciones con productos las realiza directamente la UI mediante ProductoDatos (listados, búsqueda, creación, etc.), existe una clase servicios.ServicioInventario enfocada en manejo de stock. Este servicio es utilizado sobre todo durante el proceso de ventas para ajustar existencias. Sus funciones principales son:
descontarStock(int idProducto, int cantidad): reduce la cantidad en stock de un producto dado. Primero obtiene el producto de la BD (buscarPorId en ProductoDatos)
GitHub
. Verifica que el producto esté activo y que no esté caducado (si tiene fecha de caducidad anterior a hoy)
GitHub
. También comprueba que la cantidad solicitada a descontar no supere al stock disponible actual
GitHub
. Si alguna de estas condiciones falla, lanza una excepción con el mensaje de error correspondiente (por ejemplo "Stock insuficiente"). Si todo está ok, crea un nuevo objeto Producto con la misma información pero con la cantidad reducida (cantidad = cantidad actual - cantidad vendida)
GitHub
, y llama a ProductoDatos.actualizar(actualizado) para persistir el cambio
GitHub
. De este modo se asegura la atomicidad de disminuir stock y evita valores negativos.
agregarStock(int idProducto, int cantidad): operación complementaria para aumentar existencias (por ejemplo, entrada de mercancía). Similar al anterior, busca el producto, suma la cantidad indicada al stock y actualiza el registro
GitHub
. Esta función no parece ser invocada en la UI actual, pero está preparada para su posible uso.
listarProductos(): simplemente delega en ProductoDatos.listarProductos() para obtener la lista de productos activos
GitHub
. Se utiliza en la pantalla de Venta (para llenar el combobox de productos disponibles).
agregarNuevoProducto(Producto p): encapsula la lógica de inserción de un nuevo producto verificando la unicidad del ID. Internamente llama a ProductoDatos.idExiste(p.id()) y lanza una excepción si devuelve true (evitando duplicados)
GitHub
, de lo contrario inserta el producto con ProductoDatos.insertar(p)
GitHub
. (En la práctica, la UI de Alta/Baja realiza esta verificación manualmente y usaría directamente el DAO, pero el servicio ofrece esa protección adicional).
Resumiendo, el módulo de gestión de productos permite mantener actualizado el catálogo de artículos que se pueden vender, controlando su disponibilidad. Las funciones clave incluyen la creación y actualización de productos (insertar y actualizar vía DAO, invocados desde UI), la búsqueda por nombre o ID para consulta rápida, y la eliminación lógica (baja, marcándolos inactivos). Adicionalmente, las operaciones de ajuste de stock antes y después de ventas son críticas para la integridad del inventario – estas se realizan mediante el servicio de inventario, asegurando que no se vendan productos sin stock o caducados
GitHub
GitHub
 y reflejando cada venta en la cantidad disponible.
Módulo de Ventas (Registro de Ventas)
El módulo de ventas se encarga de registrar transacciones de venta de productos, generando un registro tanto de la venta global como del detalle de productos vendidos, y actualizando el inventario. Consta de la interfaz para realizar la venta (selección de productos y cantidades) y la lógica para almacenar la venta en la base de datos de manera consistente.
Interfaz de Venta (UI): La ventana ui.ventas.Venta representa el punto de venta donde un administrador puede agregar productos a una lista de compra, indicar cantidades y finalizar la venta. Al abrirse, el constructor de Venta toma el ID del administrador en sesión (Session.getIdAdmin()) para asociarlo a cualquier venta que se registre
GitHub
. Luego inicializa los componentes gráficos incluyendo un combo desplegable de productos disponibles (CBProducto), un spinner para la cantidad a vender, una tabla que irá listando los ítems añadidos, y botones para Agregar producto, Editar cantidad, Pagar (finalizar) y Cancelar la venta
GitHub
GitHub
. Inmediatamente se cargan los productos en el combo: la función cargarProductos() llama a servicioInv.listarProductos() (servicio de inventario) para obtener la lista de productos activos
GitHub
, rellena el JComboBox con cada producto mostrando nombre y stock disponible
GitHub
, y configura el spinner de cantidad para que su valor máximo sea el stock actual del producto seleccionado
GitHub
 (deshabilitando el spinner si no hay stock para evitar seleccionar cantidades no disponibles). De esta forma la UI siempre refleja en tiempo real el inventario disponible mientras se arma la venta.
Agregar Producto a la Venta: Al seleccionar un producto en el combo y pulsar “Agregar”, se ejecuta BtnAgregarActionPerformed. Este manejador obtiene el índice seleccionado del combo y con eso recupera el objeto Producto correspondiente de la lista cargada
GitHub
. Luego:
Verifica si el producto tiene stock > 0; si no, muestra una alerta "Sin Stock" y no lo añade
GitHub
.
Verifica si la cantidad deseada (tomada del spinner) es menor o igual al stock disponible; si el usuario ha pedido más de lo que hay, muestra error "Stock insuficiente"
GitHub
.
Luego verifica si el producto ya estaba en la lista de venta (por su ID) recorriendo detallesList (la lista interna de detalles de venta)
GitHub
. Si ya existe:
Suma la nueva cantidad a la previamente agregada y comprueba de nuevo que la suma no exceda el stock; de exceder, alerta y cancela la operación
GitHub
.
Si la suma es válida, actualiza el objeto DetalleVenta correspondiente en la lista con la nueva cantidad y recalcula el subtotal
GitHub
. También actualiza la fila correspondiente en la tabla (columna cantidad y subtotal)
GitHub
 para reflejar el cambio. Finalmente informa "Cantidad actualizada..." al usuario
GitHub
.
Si el producto no estaba aún en la lista:
Crea un nuevo objeto DetalleVenta con id 0 (se asignará luego en BD), idVenta 0 (aún no hay venta registrada), el id del producto, la cantidad seleccionada y el precio unitario actual del producto
GitHub
. Lo agrega a la lista detallesList y añade una fila a la tabla con el nombre del producto, cantidad, precio unitario y subtotal calculado (precio * cantidad)
GitHub
.
Al finalizar la adición o actualización, llama a actualizarTotal() para recalcular el total de la venta sumando los subtotales de todos los DetalleVenta en la lista
GitHub
, mostrando el monto total en un campo no editable (formateado a dos decimales)
GitHub
.
Editar/Remover Productos de la Venta: La interfaz también permite seleccionar una fila en la tabla y pulsar “Editar” para cambiar la cantidad de ese producto. Esto toma la fila seleccionada, extrae el objeto DetalleVenta correspondiente de detallesList y utiliza el spinner (cantidad actualmente seleccionada en la UI) como nueva cantidad
GitHub
. Se realiza de nuevo la verificación de stock (obteniendo el producto actual de la lista de productos) para asegurarse de que la nueva cantidad no exceda lo disponible
GitHub
. Si hay stock suficiente, actualiza el objeto DetalleVenta en la lista con la nueva cantidad y recalcula subtotal, y refleja estos cambios en la tabla. Si la cantidad nueva es 0, una posible mejora sería remover el producto de la lista, aunque el código no lo especifica (no obstante, la UI Spinner probablemente no permite seleccionar 0 como cantidad mínima). Para remover un producto de la lista, el usuario podría usar Editar y poner 0, o más simplemente reiniciar la venta. (No hay un botón "Quitar" explícito en la interfaz, por lo que se asume la edición manual o la cancelación completa).
Cancelar Venta: Si el usuario presiona “Cancelar” durante el armado de la venta, la acción definida cierra la ventana de venta actual y retorna al menú (es decir, abre LoginFrame de nuevo, lo cual en este flujo funciona como menú principal o pantalla de login)
GitHub
. Esto efectivamente aborta la venta en curso descartando los detalles agregados.
Finalizar Venta (Pagar): Al terminar de agregar productos, el usuario hace clic en “Pagar”. El evento BtnPagarActionPerformed primero comprueba que la lista de detalles no esté vacía (no se puede pagar una venta sin items)
GitHub
. Luego procede a registrar la venta mediante la lógica de negocio: llama a servicioVentas.generarVenta(idAdmin, detallesList)
GitHub
. Aquí idAdmin es el ID del administrador en sesión (guardado en la instancia, obtenido al inicio) y detallesList es la lista de objetos DetalleVenta con los productos y cantidades. Esta llamada está envuelta en un bloque try-catch; si lanza una excepción, se muestra al usuario en un diálogo de error
GitHub
. Si no hay error, el servicio devuelve el ID de la venta recién creada (autogenerado por la base de datos), el cual se muestra en un mensaje informativo "Venta registrada con ID X"
GitHub
. Seguidamente, la interfaz de venta se cierra (dispose()) y, como acción final, se abre de nuevo la ventana de Login
GitHub
. Esta última acción indica quizás volver al menú principal; en la implementación actual, se instancia un nuevo LoginFrame, lo que efectivamente termina la sesión actual. El resultado es que tras completar una venta, el sistema retorna a la pantalla inicial (posiblemente por diseño de seguridad para que cada venta requiera reautenticación, o simplemente porque no se implementó una vuelta más lógica al menú principal directamente).
Servicio de Ventas (Lógica): La clase servicios.ServicioVentas coordina el proceso de registrar una venta completa. Su método principal, generarVenta(int idAdmin, List<DetalleVenta> detalles), realiza los siguientes pasos:
Calcular el monto total de la venta sumando para cada detalle cantidad * precioUnitario
GitHub
.
Por cada detalle en la lista, invocar ServicioInventario.descontarStock(idProducto, cantidad)
GitHub
. Esto intenta reducir el stock de ese producto en la cantidad vendida, aplicando las validaciones de activo, no caducado y stock suficiente
GitHub
. Si alguna de esas condiciones falla, descontarStock lanza una excepción; generarVenta no captura dentro del bucle, por lo que la excepción propagará causando que la venta no se registre y salte al catch en la UI, informando el error. Es decir, si un producto ya no tiene stock suficiente en el momento de pagar (por ejemplo, si cambió concurrentemente o un error de lógica permitió agregar más de lo disponible), la operación se aborta antes de tocar la base de datos para la venta. Importante: en la implementación actual, cada descuento de stock se realiza inmediatamente en la base de datos
GitHub
; si uno falla a mitad de la lista, los productos descontados previamente permanecen con stock reducido. Idealmente se podría manejar todo en una transacción única, pero aquí se hace secuencialmente.
Si todos los productos se descontaron correctamente, el siguiente paso es registrar la venta. Se construye un objeto Venta (entidad) con ID 0 (autonumérico), fecha actual (LocalDateTime.now()), el total calculado, el id del administrador que realiza la venta, y la lista de detalles
GitHub
. Luego se llama a VentaDatos.registrarVenta(v)
GitHub
.
VentaDatos.registrarVenta(Venta v) inserta la venta y sus detalles en la base de datos dentro de una transacción JDBC
GitHub
GitHub
. Específicamente, abre una conexión (o toma la existente) y ejecuta: un INSERT en la tabla ventas para crear el registro de la venta (fecha, monto total, id_administrador)
GitHub
, obteniendo la clave generada (id de venta)
GitHub
; luego, prepara un INSERT en la tabla detalles_venta para cada detalle de la lista, estableciendo el id de venta recién obtenido, el id de producto, cantidad y precio unitario en ese momento
GitHub
. Todos los detalles se ejecutan en batch (por eficiencia) dentro de la misma transacción
GitHub
. Finalmente, hace commit() para confirmar ambas inserciones
GitHub
. Si alguna operación falla (p.ej., error de base de datos), se hace rollback() para deshacer cualquier cambio parcial
GitHub
, y se lanza la excepción hacia arriba. Esto asegura que la venta y sus detalles se registren todo o nada en la BD.
registrarVenta retorna el ID autogenerado de la venta (si tuvo éxito)
GitHub
. Ese valor es el que generarVenta propaga de vuelta a la UI (para mostrar al usuario el número de referencia de su venta)
GitHub
.
En caso de éxito completo, la base de datos habrá sido actualizada de la siguiente forma: la tabla ventas tiene un nuevo registro con el total, fecha y admin de la venta; la tabla detalles_venta tiene una o varias filas nuevas, cada una ligada al id de esa venta y referenciando un producto con la cantidad vendida y el precio unitario en ese momento; y adicionalmente, la tabla productos refleja la reducción de stock para esos productos. Todo esto permite luego consultar el historial de ventas y conocer tanto quién la realizó como qué se vendió exactamente. Funciones clave del módulo de ventas: La interfaz Venta maneja la recolección de items y cuantías, garantizando en la UI que no se vendan cantidades mayores al stock disponible (con mensajes inmediatos al usuario)
GitHub
GitHub
. El ServicioVentas.generarVenta orquesta la validación final de stock y la persistencia atómica de la venta con sus detalles
GitHub
GitHub
. La actualización de inventario es fundamental en este flujo, delegada al ServicioInventario para mantener la consistencia del stock. En conjunto, este módulo asegura que cada venta quede registrada con integridad referencial (usa transacciones y claves foráneas en BD) y que el inventario se descuente acorde a lo vendido.
Módulo de Reportes (Historial de Ventas)
El módulo de reportes proporciona una vista del historial de ventas registradas, permitiendo filtrar por fecha y por nombre de vendedor (administrador), y ver el detalle de cada venta. Esto ayuda a la supervisión y auditoría de las transacciones realizadas en el sistema.
Pantalla de Historial de Ventas (UI): ui.reportes.Historial_Ventas es un JFrame que muestra una tabla con las ventas realizadas. Al abrirse, en su constructor invoca cargarVentasPredeterminadas()
GitHub
, que a su vez llama al servicio de ventas consultarVentasDetalladas() sin filtros para obtener todas las ventas
GitHub
. El resultado es una lista de objetos VentaDisplayDTO (DTO de ventas) que incluyen ya el nombre del administrador y la lista de detalles pre-agregados. La UI pasa esa lista a actualizarTablaVentas(ventas)
GitHub
, la cual limpia el modelo de la tabla y agrega una fila por cada venta, mostrando: ID de venta, fecha/hora, monto total, nombre del vendedor y estado
GitHub
GitHub
. La fecha se formatea a "dd/MM/yyyy HH:mm:ss" para mejor legibilidad
GitHub
. La columna "Estado" proviene del campo estado en la base de datos para esa venta (por ejemplo, podría ser "COMPLETADA" en condiciones normales)
GitHub
. Si ocurre algún error SQL al cargar el historial completo, se notifica por diálogo de error
GitHub
. La interfaz ofrece controles de filtro en la parte superior: un campo de texto para el nombre del vendedor, y dos controles de fecha (calendarios de JDateChooser) para fecha de inicio y fin del rango. El usuario puede ingresar ninguno, uno o varios criterios y pulsar “Buscar”. Al hacerlo, se captura el texto del nombre y las fechas seleccionadas
GitHub
. Se validan las fechas: si ambas están seleccionadas y la fecha inicio es posterior a la de fin, se avisa del error de rango
GitHub
. Luego se llama a servicioVentas.consultarVentasDetalladas(nombreVendedor, fechaInicio, fechaFin) pasando null en cualquier parámetro no especificado
GitHub
. El servicio devolverá la lista de ventas que cumplen los filtros. Se vuelve a llenar la tabla con actualizarTablaVentas usando esa lista
GitHub
. Si la lista viene vacía (no hay ventas que coincidan con los criterios), se informa con un mensaje "Búsqueda sin resultados"
GitHub
. Existe también un botón “Cancelar” en la interfaz de filtros que permite limpiar todos los campos (borra el texto de vendedor y resetea las fechas) y recargar la tabla con todos los registros llamando otra vez a cargarVentasPredeterminadas()
GitHub
, confirmando al usuario que los filtros fueron limpiados.
Consulta de Ventas Detalladas (Servicio): La funcionalidad de obtención de ventas con detalles está en servicios.ServicioVentas.consultarVentasDetalladas(...). Hay dos sobrecargas: una sin parámetros (que simplemente llama a la otra pasando nulls, para obtener todo)
GitHub
GitHub
, y otra que acepta opcionalmente un nombre de vendedor y un rango de fechas. Internamente, estas llamadas delegan en VentaDatos.obtenerVentasParaDisplay(nombre, inicio, fin)
GitHub
. Este método en el DAO realiza una consulta SQL diseñada para devolver los datos listos para la vista:
Primero obtiene todos los detalles de todas las ventas en un período amplio mediante un JOIN entre detalles_venta y productos para traer el nombre del producto
GitHub
GitHub
. Esto llena una estructura en memoria (Map<Integer, List<DetalleVentaDisplayDTO>>) que agrupa los detalles por ID de venta
GitHub
GitHub
. Aunque puede no ser la forma más eficiente (trae todos los detalles sin filtrar primero), simplifica luego armar las ventas.
Luego construye dinámicamente una consulta para la tabla ventas uniendo con administradores para obtener también el nombre completo del administrador
GitHub
. Agrega condiciones WHERE según los parámetros recibidos: si se proporcionó un nombre de vendedor, incluye nombre_completo LIKE ?
GitHub
; si se dio fechaInicio, incluye fecha_venta >= ?
GitHub
; si se dio fechaFin, incluye fecha_venta <= ?
GitHub
. Los placeholders se rellenan en un PreparedStatement con los valores correspondientes
GitHub
. Finalmente ordena los resultados por fecha de venta descendente (ventas más recientes primero)
GitHub
.
Ejecutando esa consulta, por cada venta resultante crea un VentaDisplayDTO poblando id, fecha (conversión de Timestamp a LocalDateTime), monto total, estado, y el nombre del administrador
GitHub
. Inmediatamente obtiene del mapa de detalles la lista de DetalleVentaDisplayDTO correspondiente a ese id de venta (si no hay, pone lista vacía)
GitHub
, y la adjunta al DTO de venta
GitHub
. Ese DTO se añade a la lista final.
La lista de VentaDisplayDTO se retorna al servicio, y de ahí a la UI.
Gracias a esta lógica, la interfaz de historial recibe objetos ya listos para mostrar, con los detalles anidados. Así, al hacer doble clic en una venta o pulsar un botón de “Ver Detalle” (en la UI se muestra un botón Detalles por cada fila, o un botón general tras seleccionar una fila, según implementación), se puede desplegar la información completa de la venta.
Detalle de una Venta: El formulario ui.reportes.DetalleVentaFrame brinda una vista de solo lectura de los detalles de una venta específica. Esta ventana se invoca pasando el VentaDisplayDTO de la venta seleccionada. En Historial_Ventas se maneja, por ejemplo, al hacer clic en el botón “Detalles”: se toma la fila seleccionada, se busca en la lista filtrada actual el DTO de esa venta por ID (para asegurar tener la información actualizada)
GitHub
, y luego se instancia un DetalleVentaFrame(ventaSeleccionada)
GitHub
. Este frame no usa los mecanismos de NetBeans GUI builder, sino que programa dinámicamente una interfaz sencilla: en el constructor, guarda el VentaDisplayDTO recibido, luego crea labels para información general (ID de venta, fecha, vendedor, monto total, estado) y una tabla para listar los productos
GitHub
GitHub
. En populateData() llena esos labels con los datos de la venta formateando fecha y monto a moneda local
GitHub
. También configura el modelo de la tabla con columnas Producto, Cantidad, Precio Unitario y Subtotal
GitHub
GitHub
. Recorre la lista de detalles (venta.detalles()) y para cada DetalleVentaDisplayDTO agrega una fila con nombre de producto, cantidad, precio unitario y subtotal calculado
GitHub
. Los precios y subtotales los formatea a moneda usando NumberFormat.getCurrencyInstance(new Locale("es","CL")) (en el ejemplo formatean como peso chileno)
GitHub
GitHub
. Al final, añade un botón “Cerrar” que simplemente cierra la ventana
GitHub
. Esta ventana es modal informativa (aunque en la implementación dada, la llamada no la hace modal explícitamente, simplemente la lanza y permite múltiples ventanas abiertas). Sirve como reporte detallado impreso en pantalla.
El módulo de reportes no modifica datos, solo consulta. Sus funciones clave son: la generación de listados de ventas con filtros a través de consultarVentasDetalladas (combinando información de varias tablas para comodidad de la UI)
GitHub
GitHub
, y la presentación detallada de cada venta. Permite responder preguntas como “¿Qué vendió el usuario X en tal periodo?” o “¿Cuáles fueron los productos de la venta #123 y cuánto sumaron?”. Gracias a la estructura de datos preparada (DTOs), la interfaz puede mostrar esta información eficientemente al usuario final.
Estructura de Carpetas y Archivos del Proyecto
El repositorio está organizado de manera clara según las capas descritas. A continuación, se presenta la estructura de directorios y archivos más relevante bajo el directorio ProyectoVentas/src/:
proyectoventas/
ProyectoVentas.java – Clase principal que inicia la aplicación (configura el Look & Feel Nimbus y lanza el LoginFrame)
GitHub
.
seguridad/
ConexionBD.java – Clase singleton para obtener la conexión JDBC a MySQL (lee credenciales desde archivo de propiedades y usa DriverManager)
GitHub
.
Session.java – Clase estática para almacenar el estado de sesión actual (ID del admin logueado)
GitHub
.
UtilHash.java – Utilidad para hash de contraseñas con BCrypt (ofrece métodos hash(textoPlano) y verificar(textoPlano, hash) usando la librería jBCrypt)
GitHub
.
db.properties – Archivo de configuración (no versionado en el repositorio) con las propiedades de conexión a la BD MySQL (URL, usuario, contraseña). Es leído por ConexionBD.
entidades/ (Modelo de datos – registros inmutables que representan tablas de la BD)
Administrador.java – Record que representa a un administrador (campos: id, usuario, hash contraseña, nombreCompleto, correo, activo, adminMaestro)
GitHub
. Incluye un método getUsuario() adicional por compatibilidad con ciertas llamadas UI
GitHub
.
Producto.java – Record para producto (campos: id, nombre, descripción, precio, cantidad, fechaCaducidad, activo)
GitHub
.
Venta.java – Record para una venta (campos: id, fecha (LocalDateTime), montoTotal, idAdministrador, lista de DetalleVenta)
GitHub
. Usado principalmente al crear una venta nueva antes de insertarla.
DetalleVenta.java – Record para detalle de venta individual (campos: id, idVenta, idProducto, cantidad, precioUnitario)
GitHub
. Representa una línea de producto en una venta.
entidades/dto/ (Objetos de transferencia de datos para reportes)
VentaDisplayDTO.java – Record que combina datos de una venta para mostrar en reportes (campos: idVenta, fechaVenta, montoTotal, nombreAdministrador, lista de DetalleVentaDisplayDTO, estado)
GitHub
.
DetalleVentaDisplayDTO.java – Record para detallar productos en reportes de venta (campos: nombreProducto, cantidad, precioEnVenta)
GitHub
.
datos/ (Capa de acceso a datos – métodos CRUD y consultas SQL directas)
AdministradorDatos.java – Métodos para operar sobre la tabla administradores:
buscarPorUsuario(String usuario) – Busca admin por nombre de usuario (SELECT * ... WHERE nombre_usuario = ?)
GitHub
.
buscarPorId(int idAdmin) – Busca admin por ID (SELECT * ... WHERE id_administrador = ?)
GitHub
.
existeAdminMaestro() – Verifica si hay al menos un admin maestro en BD (COUNT(*) con es_admin_maestro = 1)
GitHub
.
insertar(Administrador a) – Inserta un nuevo admin (INSERT con campos usuario, hash_contraseña, etc.)
GitHub
GitHub
.
actualizar(Administrador a) – Actualiza todos los datos de un admin existente (UPDATE ... WHERE id_administrador = ?)
GitHub
GitHub
.
eliminar(int idAdministrador) – Elimina físicamente un admin (DELETE ... WHERE id_administrador = ?)
GitHub
.
listarTodos() – Devuelve lista de todos los admins (activos e inactivos)
GitHub
.
buscarPorTermino(String term) – Busca admins cuyo usuario o nombre completo coincida parcialmente con el término dado (usado para búsquedas en la UI)
GitHub
.
actualizarEstadoActivo(int id, boolean nuevoEstado) – Cambia solo el campo activo de un admin (UPDATE ... SET activo = ? WHERE id = ?)
GitHub
.
(Incluye métodos privados como mapear(ResultSet) para construir un Administrador desde un ResultSet)
GitHub
.
ProductoDatos.java – Métodos para la tabla productos:
insertar(Producto p) – Inserta un nuevo producto con un ID específico (INSERT ... VALUES ...). El ID es proporcionado por el usuario; si se reutiliza uno existente se violaría la PK
GitHub
.
actualizar(Producto p) – Actualiza un producto por ID (UPDATE ... SET nombre=?, descripción=?, ... WHERE id_producto=?)
GitHub
.
eliminar(int id) – Baja lógica de producto: marca el campo activo = false para el ID dado
GitHub
.
buscarPorId(int id) – Busca un producto activo por su ID (si está inactivo no lo retorna)
GitHub
.
buscarPorNombre(String patron) – Busca productos activos cuyo nombre contenga la cadena dada (usa LIKE '%patron%')
GitHub
.
listarProductos() – Lista todos los productos activos
GitHub
.
idExiste(int id) – Comprueba si un ID de producto ya existe en la tabla (activo o no)
GitHub
.
(Incluye métodos privados cargar(PreparedStatement, Producto, isUpdate) para setear parámetros en SQL, y mapear(ResultSet) para construir un Producto)
GitHub
.
VentaDatos.java – Métodos para tabla ventas (y sus detalles):
registrarVenta(Venta v) – Registra una venta y sus detalles en una sola transacción
GitHub
GitHub
. Inserta en ventas, obtiene el ID generado
GitHub
, luego inserta en detalles_venta cada detalle con ese ID de venta
GitHub
, haciendo commit al final
GitHub
. Devuelve el ID de venta insertado
GitHub
.
obtenerVentasParaDisplay(nombreVendedor, fechaInicio, fechaFin) – Consulta combinada para reportes
GitHub
GitHub
. Obtiene detalles de ventas (JOIN con productos) y luego ventas (JOIN con administradores) aplicando filtros dinámicos según parámetros
GitHub
GitHub
. Arma y retorna una lista de VentaDisplayDTO con sub-listas de DetalleVentaDisplayDTO.
DetalleVentaDatos.java – (Actualmente minimal) Incluye solo un método listarPorVenta(int idVenta) aún no implementado (devuelve lista vacía), ya que la lógica de detalles se maneja en VentaDatos
GitHub
.
servicios/ (Lógica de negocio):
ServicioLogin.java – Lógica de autenticación de administradores: método autenticar(usuario, contraseña) que valida existencia, activo y verifica hash de contraseña usando UtilHash
GitHub
. Retorna el Administrador autenticado o lanza excepción con mensaje de error.
ServicioInventario.java – Lógica de stock e inventario:
descontarStock(idProducto, cantidad) – Valida y descuenta stock (verifica activo, no caducado, stock suficiente) antes de actualizar
GitHub
GitHub
.
agregarStock(idProducto, cantidad) – Aumenta stock de un producto dado (usado para posibles devoluciones o correcciones de inventario)
GitHub
.
listarProductos() – Devuelve lista de productos activos (envuelve a DAO)
GitHub
.
agregarNuevoProducto(Producto p) – Inserta un nuevo producto verificando que el ID no exista ya (evita duplicados)
GitHub
.
ServicioVentas.java – Lógica para proceso de ventas y consultas:
generarVenta(idAdmin, detallesList) – Proceso completo de registrar venta: calcula total
GitHub
, descuenta stock de cada detalle usando ServicioInventario.descontarStock
GitHub
, luego crea objeto Venta y llama a DAO VentaDatos.registrarVenta para insertar venta + detalles en BD atómicamente
GitHub
. Devuelve el ID de venta nuevo o lanza excepción si algo falla en el camino.
consultarVentasDetalladas(nombreVendedor, fechaInicio, fechaFin) – Obtiene lista de ventas con detalles aplicando filtros opcionales (delegando en DAO obtenerVentasParaDisplay)
GitHub
GitHub
. Hay también consultarVentasDetalladas() sin parámetros que obtiene todo el historial
GitHub
.
ui/login/
LoginFrame.java – Ventana de inicio de sesión. Contiene campos para usuario y contraseña, botón "Iniciar sesión" y (condicionalmente) botón "Crear Admin". Implementa la lógica de mostrar/ocultar estos botones según exista administrador maestro
GitHub
. Al hacer login, usa ServicioLogin y maneja la navegación al menú principal o muestra mensajes de error
GitHub
GitHub
.
ui/menu/
Menu_Principal.java – Menú principal de la aplicación. Muestra botones para las funciones: "Nueva venta", "Administrar Usuarios" (admins), "Agregar/Eliminar Productos", "Inventario", "Historial de Ventas" y "Cerrar sesión". Habilita o deshabilita el botón de admins según el rol maestro
GitHub
. Cada botón al ser pulsado instancia la ventana correspondiente y cierra el menú actual
GitHub
GitHub
GitHub
GitHub
GitHub
. Por ejemplo, Nueva venta abre ui.ventas.Venta
GitHub
, Administrar Usuarios abre ui.admin.Gestion_Administradores
GitHub
, etc., y Cerrar sesión retorna al LoginFrame
GitHub
.
ui/admin/
Gestion_Administradores.java – Pantalla principal para gestión de usuarios admins. Muestra todos los admins en una tabla
GitHub
. Ofrece campo de búsqueda
GitHub
. Botones: "Nuevo Admin" (abre CrearAdminFrame para alta)
GitHub
, "Editar" (abre CrearAdminFrame con admin seleccionado)
GitHub
, "Activar/Desactivar" (toggle estado activo del admin seleccionado con confirmación)
GitHub
, "Eliminar" (borra admin seleccionado con confirmación)
GitHub
GitHub
, "Regresar" (vuelve al menú principal)
GitHub
.
CrearAdminFrame.java – Formulario para crear o editar un administrador. Reutilizado en contexto de primera ejecución (crear maestro) y en gestión continua. Campos: usuario, nombre completo, correo, contraseña y confirmar contraseña, checkbox "Admin Maestro". En modo creación (por defecto), obliga a llenar todos los campos obligatorios, verificar coincidencia de contraseñas, unicidad de usuario, y si se marca como maestro asegura que no haya ya otro
GitHub
GitHub
. Luego genera hash y guarda vía AdministradorDatos.insertar
GitHub
. En modo edición (cuando se construye con un objeto Admin existente), precarga datos y deja contraseñas en blanco
GitHub
. Al guardar, valida campos básicos, y solo si se ingresó algo en contraseña verifica coincidencia (permitiendo dejarla vacía para no cambiarla)
GitHub
GitHub
. También controla que no se pueda asignar rol maestro si ya hay otro
GitHub
, ni duplicar nombre de usuario si se cambió
GitHub
. Arma un objeto actualizado (con el mismo ID) asignando el hash viejo o nuevo según corresponda
GitHub
, y persiste con AdministradorDatos.actualizar
GitHub
. En ambos casos (creación/edición) muestra mensaje de éxito y cierra. El botón "Cancelar" simplemente cierra la ventana sin acciones
GitHub
, mientras que al cerrarse vía "Guardar" o "Cancelar", la ventana de gestión de admins refrescará su tabla al haber registrado un listener de ventana
GitHub
GitHub
.
ui/productos/
Inventario.java – Ventana de consulta de productos. Muestra tabla con inventario activo (llena usando ProductoDatos.listarProductos)
GitHub
GitHub
. Ofrece filtro por nombre/ID con botón buscar (usa buscarPorId o buscarPorNombre según lo ingresado)
GitHub
GitHub
. Botón "Regresar" vuelve al menú
GitHub
.
AltaYBaja.java – Formulario para alta, edición o baja de productos. Campos: ID, nombre, descripción, precio, cantidad, fecha caducidad, checkbox activo. Botón "Buscar por ID" carga datos de un producto en el formulario (vía DAO)
GitHub
GitHub
. Botón "Guardar" ejecuta creación/actualización o baja según estado del checkbox activo (la lógica detallada en sección anterior, invocando métodos de ProductoDatos correspondientes: insertar, actualizar o eliminar)
GitHub
GitHub
GitHub
. Botón "Cancelar" cierra sin guardar
GitHub
, "Regresar" vuelve al menú principal
GitHub
.
ui/ventas/
Venta.java – Pantalla de proceso de venta (punto de venta). Presenta selección de producto y cantidad, tabla de items agregados y botones Agregar, Editar, Pagar, Cancelar. Controla en la UI la disponibilidad de stock (limita el spinner al stock actual, muestra alertas si se intenta agregar más de lo disponible)
GitHub
GitHub
. Mantiene una lista interna de DetalleVenta mientras se arma la venta. Al pagar, invoca ServicioVentas.generarVenta y maneja la finalización (mensaje de éxito y cierre)
GitHub
GitHub
. Cancelar cierra la venta sin registrar
GitHub
.
ui/reportes/
Historial_Ventas.java – Ventana de historial de ventas. Muestra tabla de ventas con columnas (ID, Fecha/Hora, Monto, Vendedor, Estado)
GitHub
. Permite filtrar por vendedor (subcadena en nombre) y por rango de fechas usando componentes de calendario
GitHub
GitHub
. El botón "Buscar" aplica los filtros llamando al servicio
GitHub
, y "Cancelar" limpia filtros y muestra todo de nuevo
GitHub
. Al seleccionar una venta y pedir detalles (en código, manejan esto en BtnDetalles u evento de tabla), se abre un DetalleVentaFrame para esa venta
GitHub
GitHub
. Botón "Regresar" vuelve al menú principal
GitHub
.
DetalleVentaFrame.java – Ventana de detalle de venta individual. Muestra información de la venta seleccionada: ID, fecha, vendedor, total y estado
GitHub
, y en una tabla lista los productos incluidos con su cantidad, precio unitario y subtotal
GitHub
. Es únicamente informativa con un botón "Cerrar"
GitHub
. Se construye pasando un VentaDisplayDTO ya listo con todos los datos
GitHub
.
(Además de estos, existen archivos de configuración de proyecto (Ant build files, etc.) en el repositorio, pero a nivel de código ejecutable la estructura anterior recoge las partes fundamentales.)
Dependencias Externas y Tecnologías Utilizadas
El proyecto utiliza las siguientes librerías y recursos externos integrados para su funcionamiento:
JDBC MySQL Connector: Se requiere el driver JDBC de MySQL (por ejemplo MySQL Connector/J versión 9.2.0 mencionada en la documentación) para que la clase ConexionBD pueda establecer la conexión con la base de datos MySQL
GitHub
. Este driver debe estar en el classpath al ejecutar la aplicación, ya que Class.forName("com.mysql.cj.jdbc.Driver") es invocado para cargarlo
GitHub
. Las credenciales y URL de conexión se proveen en el archivo db.properties bajo el paquete seguridad. En entornos productivos, normalmente este archivo contendría algo como:
properties
Copiar
url=jdbc:mysql://<host>/<nombre_base>?useSSL=false&serverTimezone=UTC  
user=<usuario_bd>  
password=<password_bd>
La aplicación asume que la base de datos y tablas ya existen conforme al esquema esperado (ver sección de Base de Datos). El uso de JDBC estándar permite compatibilidad con MariaDB/MySQL u otros dialectos si se cambia el conector y la URL.
Biblioteca jBCrypt (v0.4): Para la seguridad de contraseñas, el proyecto incluye la librería BCrypt (implementación de OpenBSD BCrypt para Java, paquete org.mindrot.jbcrypt). La clase UtilHash utiliza métodos de esta librería para generar hashes con sal aleatoria (BCrypt.hashpw) y verificar contraseñas (BCrypt.checkpw)
GitHub
. Gracias a esto, las contraseñas de administradores nunca se guardan en claro en la base de datos, sino como hashes robustos con sal de 12 rondas, lo que protege contra accesos no autorizados en caso de fuga de la BD.
JCalendar (Toedter): En la interfaz de reportes se usa el componente JDateChooser de la librería JCalendar (com.toedter.calendar) para facilitar la selección de fechas por parte del usuario
GitHub
. Estos calendarios gráficos evitan errores de formato al ingresar fechas para filtrar el historial de ventas. La librería JCalendar debe estar disponible para que la clase Historial_Ventas funcione correctamente, ya que se instancia un par de JDateChooser en lugar de campos de texto para fechas. En caso de ejecutar la aplicación sin entorno gráfico, estos componentes no tendrían efecto.
Java Swing y AWT: La aplicación está construida sobre Swing (parte del JDK estándar) para todos los elementos de GUI. Se aprovechan componentes como JFrame, JPanel, JTable, JButton, etc., y en algunos casos se ha utilizado el editor gráfico de NetBeans para diseñarlos (lo cual genera bloques de código guardados que no se modifican manualmente, indicados por comentarios //GEN-BEGIN:initComponents en varios frames). También se configura el Look and Feel Nimbus para darle una apariencia moderna a la interfaz
GitHub
. Dado que es una app de escritorio, requiere un entorno con capacidad gráfica; la ejecución en modo consola (headless) iniciaría la lógica pero no podría mostrar ventanas
GitHub
.
Java SE 17+: Se utiliza Java 17 (como mínimo) para compilar y ejecutar el proyecto. Esto se debe a que se emplean características modernas del lenguaje incorporadas en Java 16, como las clases record para definir entidades inmutables de forma concisa
GitHub
GitHub
. Además, Java 17 es LTS, asegurando compatibilidad a largo plazo. Es importante tener instalado JDK 17+ y configurar correctamente JAVA_HOME al compilar con Ant, según las instrucciones del proyecto.
Apache Ant: El proyecto incluye un script de compilación build.xml propio de NetBeans/Ant
GitHub
. Aunque Ant no es una dependencia de tiempo de ejecución de la aplicación, sí es la herramienta propuesta para compilar y generar el JAR (ant para compilar, ant run para ejecutar según el archivo instructions.md). Este script se apoya en los archivos en nbproject/ con la configuración del proyecto (no detallados aquí), que indican las dependencias de librerías (por ejemplo, referencias a los JAR de MySQL Connector y jBCrypt). La salida de la compilación es un ProyectoVentas.jar autocontenido en el directorio dist/, el cual se puede ejecutar con java -jar.
En suma, las dependencias externas se reducen al driver de base de datos y la librería de hashing (y JCalendar para componentes UI), mientras que todo lo demás se basa en las APIs estándar de Java. La combinación de estas bibliotecas permite que el sistema maneje de forma segura las contraseñas, interactúe con MySQL de manera confiable y ofrezca una interfaz de usuario amigable.
Manejo de la Base de Datos
El sistema persiste sus datos en una base de datos MySQL (o MariaDB equivalente) y se comunica con ella mediante JDBC. A continuación se describe el esquema de base de datos esperado y cómo el código lo utiliza:
Conexión a la BD: La clase seguridad.ConexionBD se encarga de establecer una única conexión reutilizable con la base de datos
GitHub
. Cuando la aplicación necesita acceder a la BD por primera vez, ConexionBD.obtener() carga el driver MySQL y lee el archivo de propiedades db.properties embebido en el JAR
GitHub
. A partir de ahí, crea la conexión usando DriverManager.getConnection con la URL, usuario y contraseña proporcionados
GitHub
. Esta conexión se mantiene abierta y compartida por todas las operaciones (a menos que se cierre o se detecte cerrada, en cuyo caso se reabrirá). Esto simplifica el manejo ya que no se abren y cierran conexiones repetidamente para cada consulta, aunque el código procura usar try-with-resources para PreparedStatement y ResultSet. Se debe asegurar que la BD esté levantada y accesible con las credenciales correctas antes de iniciar la aplicación, o de lo contrario ConexionBD.obtener() lanzará excepciones que, si no son capturadas, podrían detener la aplicación (por ejemplo, si el driver no está en el classpath se lanza SQLException con mensaje apropiado
GitHub
).
Esquema de Tablas: De acuerdo al código, la base de datos contiene al menos cuatro tablas principales: administradores, productos, ventas y detalles_venta. A continuación una descripción de cada una inferida del código SQL:
Tabla administradores: almacena los usuarios administradores del sistema. Sus campos (columnas) son:
id_administrador – entero, clave primaria (auto-incremental).
nombre_usuario – texto/varchar, nombre de login único de cada admin.
hash_contraseña – texto, guarda el hash bcrypt de la contraseña.
nombre_completo – texto, nombre real del administrador.
correo_electrónico – texto, correo de contacto.
activo – booleano (o entero 0/1), indica si la cuenta está habilitada.
es_admin_maestro – booleano, indica si tiene rol de superusuario.
Restricciones lógicas manejadas por la aplicación: solo puede haber un administrador maestro (el código verifica esto antes de crear o ascender a maestro a alguien
GitHub
GitHub
). También se maneja unicidad de nombre_usuario (antes de insertar o cambiar se comprueba que no esté repetido)
GitHub
GitHub
. Es de esperar que en la base de datos nombre_usuario tenga una restricción UNIQUE para integridad adicional.
Tabla productos: almacena el catálogo de productos vendibles. Campos:
id_producto – entero, clave primaria. En este caso, no necesariamente autoincremental, ya que el programa permite elegir el ID manualmente al crear productos (ej. podría usarse un código interno SKU). La aplicación verifica que no se duplique un ID antes de insertar
GitHub
.
nombre – texto, nombre del producto.
descripción – texto, descripción del producto (en código aparece con tilde, probablemente la columna en BD se llama exactamente "descripción" con acento, lo cual es posible en MySQL si se utilizan charset UTF-8).
precio – numérico (p. ej. DECIMAL o DOUBLE), precio unitario del producto.
cantidad – entero, stock disponible actualmente.
fecha_caducidad – date/datetime, fecha de expiración si aplica (puede ser NULL para productos sin caducidad).
activo – booleano (0/1), indica si el producto está activo en catálogo. Una baja lógica se marca poniendo este campo a 0 en lugar de eliminar el registro
GitHub
.
Restricciones: El sistema espera que dos productos no compartan el mismo ID (lo garantiza la PK). Además, la aplicación no permite (vía lógica) vender productos inactivos o con fecha de caducidad pasada: se comprueba en ServicioInventario.descontarStock antes de confirmar una venta
GitHub
. También en las consultas de inventario y ventas siempre se filtran productos activos (las consultas SQL añaden WHERE activo = true). Se asume que la base de datos puede tener registros con activo=false para conservar historial de productos discontinuados sin ofrecerlos a la venta.
Tabla ventas: registra cada transacción de venta realizada. Campos deducidos:
id_venta – entero autoincremental, clave primaria de la venta.
fecha_venta – datetime, marca el momento en que se registró la venta. El código guarda la fecha actual al generar la venta
GitHub
.
monto_total – numérico (ej. DECIMAL), total de la venta sumando precios * cantidades de todos los detalles.
id_administrador – entero, clave foránea que referencia a administradores.id_administrador. Indica qué admin realizó la venta. En la consulta de reportes se hace JOIN usando este campo para obtener el nombre del administrador
GitHub
GitHub
.
estado – texto (o ENUM) que indica el estado de la venta. Por defecto, dado que el sistema no contempla cancelación de ventas después de registradas, este campo posiblemente siempre tiene el valor "COMPLETADA" (u otro indicador similar). En el código de registro de venta no se setea manualmente este campo, por lo que es probable que tenga un valor DEFAULT en la base de datos (ej. 'COMPLETADA') o que la columna admita NULL y no se use activamente. Sin embargo, sí se lee el campo estado al consultar ventas para los reportes
GitHub
 y se muestra en la tabla de historial
GitHub
, así que existe. Podría ser pensado para extensiones futuras (por ejemplo, para marcar ventas canceladas, devueltas, etc., cambiando su estado).
Claves foráneas: es esperable que id_administrador en ventas tenga una FOREIGN KEY hacia administradores para mantener integridad (lo cual impediría borrar un administrador que tiene ventas registradas, a menos que se borren en cascada o se reatribuya). De hecho, en AdministradorDatos.eliminar se hace mención a manejar la violación de integridad referencial (código SQLState 23000) en un comentario
GitHub
, sugiriendo que efectivamente existe la restricción en la BD.
Tabla detalles_venta: almacena las líneas de cada venta, es decir, qué productos y cuántos se vendieron en una transacción. Campos:
id_detalle – (posiblemente) entero autoincremental clave primaria de cada detalle. Aunque el record DetalleVenta define un campo id, en el código nunca se usa explícitamente ni se inserta, por lo que podría existir esta PK o podrían haber optado por una clave compuesta. Lo más seguro es que haya un id_detalle PK solo para identificar cada registro.
id_venta – entero, clave foránea a ventas.id_venta. Cada detalle está ligado a la venta a la que pertenece.
id_producto – entero, clave foránea a productos.id_producto. Identifica qué producto se vendió en ese detalle.
cantidad – entero, la cantidad vendida de ese producto.
precio_en_venta – numérico (DECIMAL), el precio unitario del producto en el momento de la venta. Este campo es importante para mantener un historial consistente: si posteriormente se cambia el precio del producto en el catálogo, las ventas antiguas conservan el precio al que se vendió originalmente cada item.
Claves foráneas: se espera que detalles_venta(id_venta) haga referencia a ventas(id_venta) y detalles_venta(id_producto) a productos(id_producto), con posiblemente ON DELETE CASCADE en el primero (para borrar detalles si se borra una venta, aunque en este sistema no se contempla borrar ventas) y al menos ON DELETE RESTRICT en el segundo (no permitir borrar un producto que tenga detalles asociados; por eso la aplicación hace baja lógica de productos en lugar de borrarlos de la BD).
Transacciones y consistencia: La aplicación maneja explícitamente una transacción al registrar ventas (venta + detalles) para asegurar consistencia
GitHub
GitHub
. Fuera de ese caso, la mayoría de operaciones CRUD se realizan en modo auto-commit (cada sentencia SQL confirma inmediata). Esto es razonable dado que, por ejemplo, al crear un admin no hay acciones múltiples que deban ser atómicas (solo un INSERT), lo mismo para productos. En el caso de la venta, sí era crítico agrupar inserción de venta cabecera y sus múltiples detalles, logrando una escritura atómica. El ConexionBD.obtener() devuelve siempre la misma conexión; en registrarVenta se hace setAutoCommit(false) sobre ella
GitHub
 y luego commit/rollback según corresponda
GitHub
, volviendo a dejar setAutoCommit(true) después
GitHub
. Esto implica que mientras dura ese proceso, otras operaciones sobre la misma conexión podrían verse afectadas. Sin embargo, dado que la app es monohilo (todas las acciones se desencadenan desde la UI en el Event Dispatch Thread), no habrá concurrencia de queries durante esa transacción. Aun así, cabe mencionar que la secuencia de descontar stock producto por producto ocurre fuera de la transacción de venta, por lo que una falla en mitad del bucle puede dejar stocks ya modificados sin haberse registrado la venta – es un detalle de implementación que el sistema tal cual no resuelve completamente (no revierte los stocks descontados si la venta falla luego). En entornos reales, se podría mejorar incluyendo las actualizaciones de stock dentro de la misma transacción de venta o usando bloqueos optimistas/pesimistas. No obstante, la aplicación mitiga la mayoría de casos no permitiendo llegar a pagar si algo está inconsistente en stock (lo verifica antes)
GitHub
GitHub
.
Consultas y rendimiento: Las consultas de búsqueda y filtrado usan índices apropiados (por ejemplo, buscarPorUsuario usa el campo nombre_usuario que debería estar indexado con UNIQUE
GitHub
, buscarPorId utiliza la PK, etc., lo cual es muy eficiente). La consulta de reporte carga todos los detalles de todas las ventas sin filtro
GitHub
, lo cual podría ser potencialmente costoso si la base crece mucho; una optimización mencionada en el código sería primero filtrar ventas por fecha/vendedor y luego obtener solo los detalles de esas ventas
GitHub
. Aun así, funcionalmente el enfoque actual asegura simplicidad.
Inicialización de datos: Al ser un sistema que inicia vacío, la primera vez se debe crear un admin maestro (como se hace vía la UI). También es de suponer que inicialmente la tabla de productos esté vacía, y que los administradores registren los productos mediante el módulo de Inventario antes de poder realizar ventas. No hay un script SQL en el repositorio, pero para que el sistema funcione se debe crear la base de datos con sus tablas. Deduciendo del esquema, un script podría ser similar a:
sql
Copiar
CREATE TABLE administradores (
    id_administrador INT PRIMARY KEY AUTO_INCREMENT,
    nombre_usuario VARCHAR(50) UNIQUE NOT NULL,
    hash_contraseña VARCHAR(60) NOT NULL,
    nombre_completo VARCHAR(100) NOT NULL,
    correo_electrónico VARCHAR(100),
    activo BOOLEAN NOT NULL DEFAULT 1,
    es_admin_maestro BOOLEAN NOT NULL DEFAULT 0
);
CREATE TABLE productos (
    id_producto INT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripción TEXT,
    precio DECIMAL(10,2) NOT NULL,
    cantidad INT NOT NULL,
    fecha_caducidad DATE,
    activo BOOLEAN NOT NULL DEFAULT 1
);
CREATE TABLE ventas (
    id_venta INT PRIMARY KEY AUTO_INCREMENT,
    fecha_venta DATETIME NOT NULL,
    monto_total DECIMAL(10,2) NOT NULL,
    id_administrador INT NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'COMPLETADA',
    FOREIGN KEY (id_administrador) REFERENCES administradores(id_administrador)
);
CREATE TABLE detalles_venta (
    id_detalle INT PRIMARY KEY AUTO_INCREMENT,
    id_venta INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_en_venta DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_venta) REFERENCES ventas(id_venta) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);
Lo anterior es una inferencia; podría variar en tipos exactos y restricciones según el autor del proyecto.
Uso de la BD en la aplicación: Durante la ejecución típica:
Al iniciar sesión, se hace una consulta SELECT a administradores por nombre_usuario
GitHub
.
Al crear admins, operaciones de INSERT/UPDATE en administradores
GitHub
GitHub
.
Al listar admins en gestión, SELECT * de administradores
GitHub
 (posiblemente sin filtrar).
En inventario: SELECT de productos (filtrando activos)
GitHub
, búsqueda por ID (SELECT por PK)
GitHub
, búsqueda por nombre (SELECT with LIKE)
GitHub
, INSERT/UPDATE/DELETE (baja lógica) en productos según corresponda
GitHub
GitHub
GitHub
.
En proceso de venta: antes de finalizar, por cada ítem vendido un SELECT y UPDATE a productos vía descontarStock
GitHub
GitHub
. Luego un INSERT en ventas y múltiples INSERT en detalles_venta dentro de una transacción
GitHub
GitHub
.
En reportes: una SELECT combinada que involucra ventas, administradores, detalles_venta y productos con JOINS
GitHub
.
Estas operaciones están bien distribuidas y utilizan parámetros preparados (evitando SQL injection), aunque la aplicación confía en que los datos enviados son correctos (p.ej., conversión de tipos y validaciones se hacen del lado Java antes de ejecutar las consultas).
En resumen, la base de datos es el núcleo persistente donde residen usuarios, productos y ventas. El diseño sigue una normalización adecuada (ventas y detalles separadas en relación 1 a N, referencias a admins y productos por ID). La aplicación asegura la integridad a nivel de lógica (ej.: no vender más de lo disponible, no duplicar usuarios) complementando las restricciones en la BD. Gracias al uso de transacciones en los puntos críticos y el control de excepciones SQL, el sistema mantiene consistencia entre la lógica de negocio y el estado de la base de datos. Cada capa del sistema (DAO, servicios, UI) colabora para que las operaciones con la base de datos ocurran de forma segura y eficiente, brindando mensajes claros al usuario en caso de algún error (como violaciones de integridad o formatos incorrectos). En conclusión, el manejo de la base de datos en Proyecto Ventas está estructurado de forma robusta para una aplicación de este tipo, permitiendo futuras extensiones (por ejemplo, más estados de venta, reportes adicionales o manejo de transacciones más complejo) sin reñir con el diseño existente.

base de datos cafeteria.sql usada:

-- phpMyAdmin SQL Dump
-- versión 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3306
-- Tiempo de generación: 04-04-2025 a las 20:40:22
-- Versión del servidor: 8.2.0
-- Versión de PHP: 8.2.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4;

--
-- Base de datos: `cafeteria`
--

-- --------------------------------------------------------
-- Estructura de tabla para la tabla `administradores`
-- --------------------------------------------------------
DROP TABLE IF EXISTS `administradores`;
CREATE TABLE IF NOT EXISTS `administradores` (
  `id_administrador` int NOT NULL AUTO_INCREMENT,
  `nombre_usuario` varchar(50) NOT NULL,
  `hash_contraseña` varchar(255) NOT NULL,
  `nombre_completo` varchar(100) NOT NULL,
  `correo_electrónico` varchar(100) DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `es_admin_maestro` tinyint(1) NOT NULL DEFAULT '0',
  `indicador_admin_maestro` int GENERATED ALWAYS AS (IF(`es_admin_maestro`,1,NULL)) STORED COMMENT 'Columna auxiliar para restricción UNIQUE',
  `creado_en` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha y hora de creación',
  `actualizado_en` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha y hora de última actualización',
  PRIMARY KEY (`id_administrador`),
  UNIQUE KEY `uk_nombre_usuario` (`nombre_usuario`),
  UNIQUE KEY `uk_correo_electrónico` (`correo_electrónico`),
  UNIQUE KEY `uk_admin_maestro` (`indicador_admin_maestro`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------
-- Estructura de tabla para la tabla `productos`
-- --------------------------------------------------------
DROP TABLE IF EXISTS `productos`;
CREATE TABLE IF NOT EXISTS `productos` (
  `id_producto` int NOT NULL COMMENT 'Identificador único del producto',
  `nombre` varchar(150) NOT NULL COMMENT 'Nombre del producto',
  `descripción` text COMMENT 'Descripción detallada (opcional)',
  `precio` decimal(10,2) NOT NULL COMMENT 'Precio de venta unitario',
  `cantidad` int NOT NULL DEFAULT '0' COMMENT 'Cantidad actual en inventario',
  `fecha_caducidad` date DEFAULT NULL COMMENT 'Fecha de caducidad (NULL si no aplica)',
  `activo` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'Disponibilidad (1=Activo, 0=Descontinuado)',
  `creado_en` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha y hora de creación',
  `actualizado_en` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha y hora de última actualización',
  PRIMARY KEY (`id_producto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------
-- Estructura de tabla para la tabla `ventas`
-- --------------------------------------------------------
DROP TABLE IF EXISTS `ventas`;
CREATE TABLE IF NOT EXISTS `ventas` (
  `id_venta` int NOT NULL AUTO_INCREMENT COMMENT 'Identificador único de la venta',
  `fecha_venta` datetime NOT NULL COMMENT 'Fecha y hora de la venta',
  `monto_total` decimal(12,2) NOT NULL COMMENT 'Monto total de la venta',
  `estado` enum('COMPLETADA','CANCELADA') NOT NULL DEFAULT 'COMPLETADA' COMMENT 'Estado de la venta',
  `id_administrador` int NOT NULL COMMENT 'ID del administrador que realizó la venta (FK)',
  `creado_en` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha y hora de creación del registro',
  `actualizado_en` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha y hora de última actualización',
  PRIMARY KEY (`id_venta`),
  KEY `fk_ventas_administradores` (`id_administrador`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------
-- Estructura de tabla para la tabla `detalles_venta`
-- (id_producto ahora admite NULL y FK con ON DELETE SET NULL)
-- --------------------------------------------------------
DROP TABLE IF EXISTS `detalles_venta`;
CREATE TABLE IF NOT EXISTS `detalles_venta` (
  `id_detalle_venta` int NOT NULL AUTO_INCREMENT COMMENT 'ID único de la línea de detalle',
  `id_venta` int NOT NULL COMMENT 'ID de la venta a la que pertenece (FK)',
  `id_producto` int NULL COMMENT 'ID del producto vendido (FK) — ahora admite NULL',
  `cantidad` int NOT NULL COMMENT 'Cantidad de este producto vendido',
  `precio_en_venta` decimal(10,2) NOT NULL COMMENT 'Precio unitario al momento de la venta',
  PRIMARY KEY (`id_detalle_venta`),
  UNIQUE KEY `uk_venta_producto` (`id_venta`,`id_producto`),
  KEY `fk_detallesventa_productos` (`id_producto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------
-- Restricciones para la tabla `ventas`
-- --------------------------------------------------------
ALTER TABLE `ventas`
  ADD CONSTRAINT `fk_ventas_administradores`
    FOREIGN KEY (`id_administrador`)
    REFERENCES `administradores` (`id_administrador`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

-- --------------------------------------------------------
-- Restricciones para la tabla `detalles_venta`
-- - FK a ventas con ON DELETE CASCADE
-- - FK a productos con ON DELETE SET NULL
-- --------------------------------------------------------
ALTER TABLE `detalles_venta`
  ADD CONSTRAINT `fk_detallesventa_ventas`
    FOREIGN KEY (`id_venta`)
    REFERENCES `ventas` (`id_venta`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_detallesventa_productos`
    FOREIGN KEY (`id_producto`)
    REFERENCES `productos` (`id_producto`)
    ON DELETE SET NULL
    ON UPDATE CASCADE;

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
