# Gestor de Tareas REST API

Este proyecto es una aplicación web backend construida con **Spring Boot**, **MongoDB**, **Spring Security** y **JSON Web Token (JWT)** para la gestión de tareas y usuarios. Proporciona una interfaz RESTful segura para realizar operaciones CRUD sobre tareas y administrar el registro y login de usuarios.

---

## 🛠️ Tecnologías Utilizadas

- **Java**: Versión 17
- **Spring Boot**: Versión 3.5.14
- **Spring Data MongoDB**: Almacenamiento y persistencia NoSQL.
- **Spring Security**: Marco para la seguridad, autenticación y control de accesos.
- **io.jsonwebtoken (JJWT 0.11.0)**: Generación, firma y validación de tokens JWT.
- **Lombok**: Reducción de código boilerplate (Getters, Setters, Constructores, etc.).
- **Jakarta Validation**: Reglas de validación para entradas de API (ej. `@NotBlank`, `@NotNull`).
- **Maven**: Motor de construcción y gestión de dependencias.

---

## 📁 Estructura del Proyecto

El código fuente se organiza dentro del paquete base `cl.sarayar.gestorTareasRest` de la siguiente forma:

- [GestorTareasRestApplication.java](file:///C:/Users/alvaro/Desktop/hdd_taller2/src/main/java/cl/sarayar/gestorTareasRest/GestorTareasRestApplication.java): Clase principal de entrada. Al iniciar la aplicación, implementa `CommandLineRunner` para sembrar automáticamente un usuario Administrador por defecto (`sarayar@skynux.cl`) si la base de datos está vacía.
- **`config/auth`**:
  - [UserDetailsImpl.java](file:///C:/Users/alvaro/Desktop/hdd_taller2/src/main/java/cl/sarayar/gestorTareasRest/config/auth/UserDetailsImpl.java): Implementación personalizada de la interfaz `UserDetails` de Spring Security para encapsular al usuario autenticado.
  - **`dto`**: Data Transfer Objects como `JwtResponse` y `MessageResponse` para estructurar los retornos de las solicitudes de autenticación.
- **`controllers`**:
  - [UsuariosController.java](file:///C:/Users/alvaro/Desktop/hdd_taller2/src/main/java/cl/sarayar/gestorTareasRest/controllers/UsuariosController.java): Expone endpoints relacionados con el ciclo de vida del usuario (Login, Registro, Actualización, Listado).
  - [TareasController.java](file:///C:/Users/alvaro/Desktop/hdd_taller2/src/main/java/cl/sarayar/gestorTareasRest/controllers/TareasController.java): Expone endpoints CRUD para la administración de tareas.
- **`entities`**:
  - [Usuario.java](file:///C:/Users/alvaro/Desktop/hdd_taller2/src/main/java/cl/sarayar/gestorTareasRest/entities/Usuario.java): Representa el documento `Usuario` en MongoDB.
  - [Tarea.java](file:///C:/Users/alvaro/Desktop/hdd_taller2/src/main/java/cl/sarayar/gestorTareasRest/entities/Tarea.java): Representa el documento `Tarea` en MongoDB. Utiliza un campo secuencial autoincrementado.
  - [Secuencia.java](file:///C:/Users/alvaro/Desktop/hdd_taller2/src/main/java/cl/sarayar/gestorTareasRest/entities/Secuencia.java): Colección auxiliar para implementar secuencias autoincrementales similares a secuencias SQL.
- **`listeners`**:
  - [TareasModelListener.java](file:///C:/Users/alvaro/Desktop/hdd_taller2/src/main/java/cl/sarayar/gestorTareasRest/listeners/TareasModelListener.java): Escucha eventos antes de persistir documentos de tipo `Tarea` (`BeforeConvertEvent`), llamando al generador de secuencias para autoincrementar el campo `identificador`.
- **`repositories`**:
  - [UsuariosRepository.java](file:///C:/Users/alvaro/Desktop/hdd_taller2/src/main/java/cl/sarayar/gestorTareasRest/repositories/UsuariosRepository.java): Interfaz para operaciones CRUD sobre la colección de usuarios en MongoDB.
  - [TareasRepository.java](file:///C:/Users/alvaro/Desktop/hdd_taller2/src/main/java/cl/sarayar/gestorTareasRest/repositories/TareasRepository.java): Interfaz para operaciones CRUD sobre la colección de tareas en MongoDB.
- **`services`**:
  - Contiene las interfaces y sus respectivas implementaciones (`*ServiceImpl`) para resolver la lógica de negocio de usuarios, tareas y generación de secuencias.
- **`utils`**:
  - [JwtUtils.java](file:///C:/Users/alvaro/Desktop/hdd_taller2/src/main/java/cl/sarayar/gestorTareasRest/utils/JwtUtils.java): Utilidades para la generación, lectura de reclamos (claims) y validación de vigencia de tokens JWT.

---

## 🗄️ Modelos de Datos (MongoDB)

### Usuario
* Colección: `usuarios`
* Estructura:
  - `id` (String - auto-generado por MongoDB)
  - `nombre` (String)
  - `correo` (String - Único e indexado)
  - `clave` (String)
  - `estado` (int - por defecto `1` (Activo))

### Tarea
* Colección: `tareas`
* Estructura:
  - `id` (String - auto-generado por MongoDB)
  - `identificador` (long - ID numérico secuencial incremental, indexado y único)
  - `descripcion` (String - No en blanco)
  - `fechaCreacion` (LocalDateTime - Generado al guardar)
  - `vigente` (boolean - Requerido)

---

## ⚙️ Configuración y Perfiles

Los archivos de configuración se encuentran en [src/main/resources](file:///C:/Users/alvaro/Desktop/hdd_taller2/src/main/resources):

- **[application.properties](file:///C:/Users/alvaro/Desktop/hdd_taller2/src/main/resources/application.properties)**: 
  - Define el perfil activo (`spring.profiles.active=dev`).
  - Deshabilita la seguridad básica por defecto (`security.basic.enabled=false`).
  - Configura el Secreto JWT (`gestor.app.jwtSecret=$gestor1234#`) y tiempo de expiración (24 horas).
  - Establece límites para carga de archivos (10MB).
- **[application-dev.properties](file:///C:/Users/alvaro/Desktop/hdd_taller2/src/main/resources/application-dev.properties)**:
  - URI de base de datos local: `mongodb://localhost:27017/gestortareasbd`.
- **[application-prod.properties](file:///C:/Users/alvaro/Desktop/hdd_taller2/src/main/resources/application-prod.properties)**:
  - URI de base de datos en Docker: `mongodb://bd_gestor_tareas:27017/gestortareasbd`.

---

## 🌐 Endpoints de la API REST

### Usuarios (`/usuarios`)

| Método | Endpoint | Descripción | Payload de Entrada |
| :--- | :--- | :--- | :--- |
| **POST** | `/usuarios/login` | Autentica al usuario. | `Usuario` (correo, clave) |
| **POST** | `/usuarios/registrar` | Registra un nuevo usuario (evita correos duplicados). | `Usuario` (nombre, correo, clave) |
| **POST** | `/usuarios/actualizar` | Actualiza un usuario existente (valida unicidad del correo). | `Usuario` (id, nombre, correo, estado) |
| **GET** | `/usuarios/get` | Lista todos los usuarios registrados. | *Ninguno* |

### Tareas (`/tareas`)

| Método | Endpoint | Descripción | Payload de Entrada |
| :--- | :--- | :--- | :--- |
| **GET** | `/tareas/get` | Lista todas las tareas en el sistema. | *Ninguno* |
| **POST** | `/tareas/post` | Crea una nueva tarea (genera secuencialmente el identificador). | `Tarea` (descripcion, vigente) |
| **POST** | `/tareas/update` | Actualiza la descripción o vigencia de una tarea existente. | `Tarea` (id, descripcion, vigente) |
| **DELETE** | `/tareas/delete/{id}` | Elimina físicamente la tarea por su ID hexadecimal de MongoDB. | *Variable de ruta (`id`)* |

---

## 🚀 Instrucciones de Ejecución

### Requisitos Previos
1. Tener instalado **Java 17 (JDK)**.
2. Tener una base de datos **MongoDB** en ejecución en `localhost:27017` con el nombre `gestortareasbd`.

### Comandos Maven
- **Compilar y empaquetar el JAR**:
  ```bash
  ./mvnw clean package
  ```
- **Ejecutar en modo Desarrollo (dev)**:
  ```bash
  ./mvnw spring-boot:run
  ```
- **Ejecutar Tests y Generar Reporte de Cobertura (JaCoCo)**:
  ```bash
  ./mvnw test
  ```
  Esto ejecutará la suite completa de pruebas unitarias (51 pruebas) y generará el reporte de cobertura en formato HTML en `target/site/jacoco/index.html`.

---

## 🧪 Pruebas Unitarias y Cobertura (100% Coverage)

Se ha diseñado e implementado una suite completa de pruebas unitarias utilizando **JUnit 5** y **Mockito** puro (bajo la extensión `@ExtendWith(MockitoExtension.class)` y usando técnicas de mocks e inyecciones de dependencias manuales para garantizar aislamiento absoluto y tiempos de ejecución muy rápidos).

### Cobertura de Código por Paquete (JaCoCo):

| Paquete / Elemento | Instrucciones Cubiertas | Cobertura de Instrucciones | Cobertura de Ramas (Branches) | Tipo de Pruebas |
| :--- | :---: | :---: | :---: | :--- |
| **`controllers`** | `137 / 137` | **100%** | **100%** | Mockito puro (llamadas directas a métodos) |
| **`services`** | `114 / 114` | **100%** | **100%** | Mockito (con mocks de repositorio/operaciones) |
| **`utils`** | `108 / 108` | **100%** | **100%** | Mockito (se cubren excepciones y firmas de JWT) |
| **`config.auth`** | `60 / 60` | **100%** | **100%** | JUnit 5 estándar (UserDetails y DTOs) |
| **`listeners`** | `19 / 19` | **100%** | **100%** | Mockito (BeforeConvertEvent MongoDB) |
| **`entities`** | `3 / 3` | **100%** | **100%** | JUnit 5 estándar (Lombok y constructores) |
| **`cl.sarayar.gestorTareasRest`** | `42 / 42` | **100%** | **100%** | MockedStatic para SpringApplication y CommandLineRunner |
| **Total Proyecto** | **483 / 483** | **100%** | **100%** | **51 tests exitosos** |
