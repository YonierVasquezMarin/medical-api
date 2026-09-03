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
| Desarrollo | `local` (por defecto) | `java:comp/env/jdbc/MedicalDS` | `LocalJndiDataSourceInitializer` |
| WebLogic | `weblogic` | `jdbc/MedicalDS` | Datasource del dominio |

Las credenciales **no** van en Git. En local solo sirven para **publicar** el DataSource en JNDI. En WebLogic viven cifradas en el dominio.

### Desarrollo local

Copia `src/main/resources/application-local.properties.example` a `application-local.properties` (está en `.gitignore`):

```properties
local.datasource.url=jdbc:oracle:thin:@localhost:1521:XE
local.datasource.username=TU_USUARIO
local.datasource.password=TU_CONTRASEÑA
```

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

## 🔐 Variables de entorno para JWT

Para el manejo y cifrado de tokens JWT, es necesario definir las siguientes variables de entorno (ámbito **Usuario** en Windows):

| Variable               | Descripción                               | Ejemplo                        |
|------------------------|-------------------------------------------|--------------------------------|
| `JWT_SECRET`           | Clave secreta para firmar el token JWT (32 caracteres)    | `TuSecretoSuperSeguro123!`     |
| `JWT_EXPIRATION_MS`    | Tiempo de expiración del token en milisegundos | `3600000` (1 hora)         |

Asegúrate de registrar estas variables en el sistema antes de ejecutar la API para garantizar la correcta generación y validación de tokens.

---

# ▶️ Ejecución

## Desde VS Code (Spring Boot Extension Pack)

Con la extensión **Spring Boot Extension Pack** instalada, también puedes levantar el servidor desde el editor:

1. Abrir `MedicalApiApplication.java`
2. Pulsar el botón **Play** (▶) que aparece sobre el método `main` o en la barra de **Run and Debug**

Spring Boot iniciará la aplicación con la misma configuración del proyecto Maven.

## Desde la terminal

```bash
./mvnw spring-boot:run
```

En Windows:

```powershell
mvnw.cmd spring-boot:run
```
