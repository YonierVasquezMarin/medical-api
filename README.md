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

# 🗄️ Base de datos

La API lee la cadena de conexión desde variables de entorno del sistema (ámbito **Usuario** en Windows):

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| `DATABASE_URL` | JDBC URL completa | `jdbc:postgresql://ep-xxxx-xxx-apatdgea-pooler.c-7.us-east-1.aws.neon.tech` |
| `DATABASE_USERNAME` | Usuario de la base de datos | `postgres` |
| `DATABASE_PASSWORD` | Contraseña | *(tu contraseña)* |

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
