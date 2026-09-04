# 📦 Medical API

Este proyecto es una API REST desarrollada con Spring Boot y JPA para el registro y gestión de información médica.

---

# 🚀 Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate (ORM)
- Maven
- VS Code
- Chocolatey (gestión de paquetes en Windows)

---

# 🧰 Configuración del entorno

## 🍫 Instalación de Chocolatey

[Chocolatey](https://chocolatey.org/) es el gestor de paquetes usado en Windows para instalar Java, Git, Maven y otras herramientas de este proyecto.

Ejecutar **PowerShell como administrador** e instalar con:

```powershell
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
```

### Verificación

```powershell
choco -v
```

## ☕ Instalación de Java

Java fue instalado mediante Chocolatey:

```powershell
choco install temurin21 -y
```

### Verificación

```powershell
java -version
javac -version
```

## ⚙️ Configuración de JAVA_HOME

Ruta de instalación:

```
C:\Program Files\Eclipse Adoptium\jdk-21.0.9.10-hotspot
```

Variable de entorno:

```
JAVA_HOME = C:\Program Files\Eclipse Adoptium\jdk-21.0.9.10-hotspot
```

Agregar al `PATH`:

```
%JAVA_HOME%\bin
```

## 🧰 Herramientas instaladas

```powershell
choco install git maven vscode spring-boot-cli -y
```

## 🧩 Extensiones en VS Code

- Java Extension Pack
- Spring Boot Extension Pack

## 🏗️ Creación del proyecto

```powershell
spring init --name=medical-api --build=maven --java-version=21 --dependencies=web,data-jpa,lombok medical-api
```

---

# 🗄️ Base de datos (JNDI unificado)

La API **siempre** obtiene el pool JDBC con un lookup JNDI (`spring.datasource.jndi-name`). No usa `url` / `username` / `password` en el `DataSource` de Spring.

| Entorno | Perfil | Nombre JNDI | Quién publica el recurso |
|---------|--------|-------------|--------------------------|
| Desarrollo | `local` (por defecto) | `jdbc/MedicalDS` | `LocalJndiDataSourceInitializer` |
| WebLogic | `weblogic` | `jdbc/MedicalDS` | Datasource del dominio |

Las credenciales **no** van en Git. En local solo sirven para **publicar** el DataSource en JNDI. En WebLogic viven cifradas en el dominio.

### Desarrollo local

Copia `src/main/resources/application-local.properties.example` a `application-local.properties` (está en `.gitignore`):

```properties
local.datasource.url=jdbc:oracle:thin:@localhost:1521:XE
local.datasource.username=TU_USUARIO
local.datasource.password=TU_CONTRASEÑA
jwt.secret=TU_SECRETO_JWT_DE_32_CARACTERES
jwt.expiration-ms=3600000
server.port=8081
```

Oracle XE suele ocupar el **8080** (listener / Apex). En local la API usa **8081**.

Equivalente con variables de usuario de Windows:

```powershell
[System.Environment]::SetEnvironmentVariable("DATABASE_URL", "jdbc:oracle:thin:@localhost:1521:XE", "User")
[System.Environment]::SetEnvironmentVariable("DATABASE_USERNAME", "TU_USUARIO", "User")
[System.Environment]::SetEnvironmentVariable("DATABASE_PASSWORD", "TU_CONTRASEÑA", "User")
```

Arranque: `mvnw.cmd spring-boot:run` (perfil `local`).

### WebLogic

1. Crear un JDBC Data Source con JNDI `jdbc/MedicalDS`.
2. Desplegar el artefacto con `SPRING_PROFILES_ACTIVE=weblogic`.
3. No activar el perfil `local` en el servidor (evitaría el JNDI en memoria).

---

# 👤 Usuarios y autenticación JWT

## Tabla `USERS`

Ejecutar a mano el script [`src/main/resources/db/users.sql`](src/main/resources/db/users.sql) (`ddl-auto=none`). Está pensado para **Oracle 11g XE**: secuencia `USERS_SEQ` + tabla (no usa `IDENTITY`, que es 12c+).

Si la tabla ya existía con otra definición:

```sql
DROP TRIGGER USERS_BI;
DROP TABLE USERS;
DROP SEQUENCE USERS_SEQ;
```

Luego vuelve a ejecutar `users.sql`.

El administrador se inserta en base de datos (rol `ADMIN` y `PASSWORD_HASHED` con BCrypt). `POST /api/users` es público.

## JWT en local

Igual que la base de datos: valores en `application-local.properties` (fuera de Git). Copia desde el `.example`:

```properties
jwt.secret=clave-de-al-menos-32-caracteres!!
jwt.expiration-ms=3600000
```

`jwt.secret` debe tener **mínimo 32 caracteres**. En WebLogic usar `JWT_SECRET` y `JWT_EXPIRATION_MS` en el servidor, no un secreto en el repositorio.

## Endpoints

| Método | Ruta | Auth |
|--------|------|------|
| POST | `/api/auth/login` | Público |
| POST | `/api/users` | Público |
| GET | `/api/users` | JWT + rol `ADMIN` |
| DELETE | `/api/users/{id}` | JWT + rol `ADMIN` |

Tras el login, enviar `Authorization: Bearer <token>` en listado y eliminación.

Códigos: 400 validación, 401 sin token o login inválido, 403 autenticado sin rol ADMIN, 404 al eliminar un id inexistente, 409 email duplicado.

---

# ▶️ Ejecución

## Desde VS Code (Spring Boot Extension Pack)

Con la extensión **Spring Boot Extension Pack** instalada, también puedes levantar el servidor desde el editor:

1. Abrir `MedicalApiApplication.java`
2. Pulsar el botón **Play** (▶) que aparece sobre el método `main` o en la barra de **Run and Debug**

Spring Boot iniciará la aplicación con la misma configuración del proyecto Maven. En local queda en `http://localhost:8081`.

## Desde la terminal

```bash
./mvnw spring-boot:run
```

En Windows:

```powershell
mvnw.cmd spring-boot:run
```
