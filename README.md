salvaPeru - Videojuego Espacial (Java Swing & MySQL)
Videojuego de disparos espacial (shoot 'em up) desarrollado en Java, implementando interfaces gráficas con Swing, animaciones personalizadas mediante bucles de tiempo (Timer) y persistencia de datos conectada a MySQL (DAO).

Características principales
Sistema de Autenticación: Ventanas de inicio de sesión (Login) y registro de nuevos jugadores con validaciones de seguridad y generación automática de contraseñas.

Gameplay Dinámico (Game Loop): Motor gráfico a 50 FPS basado en temporizadores, control de nave por teclado y detección de colisiones por distancia euclidiana.

Progresión de Niveles: Sistema de dificultad escalonada (3 niveles de enemigos con distintas velocidades) condicionado por la puntuación acumulada.

Base de Datos (MySQL): Almacenamiento y actualización en tiempo real de los puntajes máximos (scores) y gestión de usuarios activos.

Sección de Instrucciones: Animación narrativa con scroll vertical (scroll text), elementos flotantes y gráficos vectoriales personalizados.

Tecnologías utilizadas
Lenguaje: Java (JDK)

Interfaz Gráfica: Java Swing / AWT

Base de Datos: MySQL / XAMPP (con JDBC)

Control de Versiones: Git & GitHub

Estructura del Repositorio
/src: Contiene todo el código fuente (Controladores, Modelos, Vistas, DAO).

/docs: Contiene el Manual de Usuario y el Manual Técnico en PDF.

database.sql: Script de exportación de la base de datos.

.gitignore: Exclusión de archivos innecesarios de compilación.

LICENSE: Licencia MIT para el proyecto.

Instalación y Ejecución
Clona este repositorio o descarga el ZIP.

Importa el proyecto en tu IDE (NetBeans, IntelliJ, etc.).

Asegúrate de tener XAMPP corriendo con los servicios de Apache y MySQL.

Importa el archivo database.sql en tu servidor MySQL local.

¡Ejecuta la clase principal FrMenu.java!

Desarrollado por: Christian Torres Ibarra

Recuerda guardar este contenido en un archivo llamado README.md dentro de la raíz de tu proyecto, y luego ejecutar git add README.md, git commit -m "docs: agrega presentación profesional al README" y git push origin main para completar la actualización en GitHub.
