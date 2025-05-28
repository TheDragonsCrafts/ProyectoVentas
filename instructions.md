Instrucciones para compilar y ejecutar ProyectoVentas
Este documento describe cómo preparar el entorno, compilar el proyecto ProyectoVentas, ejecutar la aplicación y verificar si existen pruebas automatizadas, todo desde la línea de comandos (sin interfaz gráfica).
Prerrequisitos de entorno
Java JDK 17 o superior: El proyecto usa características modernas de Java como records (clases record introducidas en Java 16)
GitHub
, por lo que se requiere JDK 16+ (se recomienda JDK 17 o posterior). Asegúrate de tener el JDK instalado y la variable JAVA_HOME configurada si es necesario.
Apache Ant: Necesario para compilar el proyecto. El archivo de construcción (build.xml) indica que se requiere Ant 1.8.0 o superior
GitHub
 (cualquier versión reciente cumplirá este requisito).
Bibliotecas externas: El proyecto depende de JBCrypt 0.4 (para hashing de contraseñas) y MySQL Connector/J (driver JDBC para MySQL/MariaDB). Estas bibliotecas deben estar disponibles en el classpath durante la compilación y ejecución. En la configuración del proyecto se hacen referencias a jbcrypt-0.4.jar y mysql-connector-j-9.2.0.jar
GitHub
, por lo que debes tener dichos JARs. Si sigues el comando de setup anterior, ya estarán instalados mediante los paquetes libjbcrypt-java y libmariadb-java (Connector/J). De lo contrario, descarga e instala estas bibliotecas manualmente.
Nota: Si el comando de setup anterior se ha ejecutado, ya cuentas con JDK, Ant y las librerías necesarias instaladas en el sistema.
Compilar el proyecto con Ant
Obtener el código fuente: Si aún no lo has hecho, clona el repositorio TheDragonsCrafts/ProyectoVentas o asegúrate de tener el código fuente del proyecto en la máquina. Sitúa tu terminal dentro del directorio del proyecto (donde reside el archivo build.xml).
Compilación: Ejecuta el comando de Ant para compilar. Puedes usar el objetivo por defecto de Ant simplemente ejecutando:
bash
Copiar
Editar
ant
Este comando limpiará y compilará el proyecto, generando los archivos .class. El build de NetBeans está configurado para que el objetivo por defecto compile el código, genere el JAR y (si hubiera) ejecute las pruebas
GitHub
. Dado que el proyecto no incluye pruebas unitarias, el comando ant compilará todo y creará el archivo JAR de distribución. Como resultado de la compilación, Ant colocará el JAR ejecutable del proyecto en la carpeta dist. Por defecto el archivo se llamará ProyectoVentas.jar
GitHub
. Puedes verificar que este archivo exista una vez finalizada la compilación.
Ejecutar la aplicación
Una vez compilado, tienes dos maneras de ejecutar la aplicación desde la terminal:
Usando Ant: Ejecuta ant run desde el directorio del proyecto. Este comando utiliza el target de ejecución definido por Ant, que lanzará la aplicación cargando la clase principal definida (proyectoventas.ProyectoVentas)
GitHub
 con las bibliotecas necesarias en el classpath.
Usando el JAR directamente: Navega a la carpeta dist y ejecuta el JAR generado con java -jar:
bash
Copiar
Editar
cd dist
java -jar ProyectoVentas.jar
Ambas opciones iniciarán la aplicación. Importante: Esta es una aplicación de escritorio con interfaz gráfica (Java Swing)
GitHub
. Si estás en un entorno de solo línea de comandos (sin entorno gráfico disponible), es posible que la aplicación no pueda mostrar la ventana de la interfaz. En entornos servidores o SSH, asegúrate de tener un display X11 configurado o ejecuta la VM con soporte gráfico si deseas ver la interfaz. De lo contrario, la ejecución intentará abrir la ventana de Login pero no será visible en una sesión estrictamente headless. La lógica de la aplicación (conexiones a base de datos, etc.) igualmente se iniciará, pero la interacción requiere la GUI.
Pruebas automatizadas
Actualmente, no se encuentran pruebas unitarias (por ejemplo, casos JUnit) incluidas en el repositorio. No hay un directorio test poblado ni clases de prueba definidas, por lo que no existen pasos específicos para ejecutar pruebas en este proyecto. Si hubiera pruebas, se podrían ejecutar con ant test u ant junit según la configuración de Ant; en este caso, al no existir, puedes omitir este paso. Resumen: Con Java y Ant configurados, compila el proyecto con ant. Luego, puedes ejecutar la aplicación con ant run o con java -jar dist/ProyectoVentas.jar. A falta de pruebas unitarias, la salida del build mostrará únicamente la compilación y creación del JAR, quedando listo el sistema para su uso. Si necesitas interactuar con la aplicación, recuerda que es una aplicación gráfica de escritorio y requiere un entorno con soporte de GUI.
GitHub
GitHub
