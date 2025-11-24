# 🏠 Guía de Configuración Local - NetBeans + MySQL

Esta guía te ayudará a clonar y ejecutar el proyecto en tu PC local usando NetBeans y MySQL.

---

## 📋 Requisitos Previos

1. **Java JDK 17** o superior
   - Descargar: https://www.oracle.com/java/technologies/downloads/
   - Verificar: `java -version`

2. **Apache Maven 3.8+**
   - Descargar: https://maven.apache.org/download.cgi
   - Verificar: `mvn -version`

3. **NetBeans IDE 17+**
   - Descargar: https://netbeans.apache.org/download/

4. **MySQL 8.0+**
   - Descargar: https://dev.mysql.com/downloads/mysql/
   - O usar XAMPP/WAMP que incluye MySQL

5. **Git**
   - Descargar: https://git-scm.com/downloads

---

## 🔽 Paso 1: Clonar el Repositorio

Abre una terminal (CMD o PowerShell en Windows) y ejecuta:

```bash
cd C:\Users\TuUsuario\Documents
git clone https://github.com/jcduenas7/InventarioApp.git
cd InventarioApp
```

---

## 🗄️ Paso 2: Configurar MySQL

### 2.1 Crear la Base de Datos

1. Abre **MySQL Workbench** o **phpMyAdmin** (si usas XAMPP)
2. Ejecuta el script SQL ubicado en `database/schema.sql`

**Opción A - MySQL Workbench:**
- File → Open SQL Script → Selecciona `database/schema.sql`
- Ejecuta el script (⚡ icono de rayo)

**Opción B - Línea de comandos:**
```bash
mysql -u root -p < database/schema.sql
```

**Opción C - phpMyAdmin (XAMPP):**
- Abre http://localhost/phpmyadmin
- Crea base de datos `inventariodb`
- Importa el archivo `database/schema.sql`

### 2.2 Verificar Credenciales

Por defecto, el proyecto usa:
- **Usuario**: `root`
- **Contraseña**: `root123`
- **Puerto**: `3306`
- **Base de datos**: `inventariodb`

Si tus credenciales son diferentes, continúa al Paso 3.

---

## ⚙️ Paso 3: Configurar application.yml

Abre el archivo:
```
InventarioSpringBoot/src/main/resources/application.yml
```

Modifica las credenciales de MySQL según tu configuración:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/inventariodb?useSSL=false&serverTimezone=UTC
    username: root          # ← Cambia si es necesario
    password: root123       # ← Cambia por tu contraseña de MySQL
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: update      # ← Mantén en 'update' para producción
    show-sql: true
```

**Importante:**
- Si tu MySQL no tiene contraseña, deja `password:` vacío
- Si usas XAMPP, la contraseña por defecto suele estar vacía
- `ddl-auto: update` actualiza la estructura de tablas automáticamente

---

## 🗑️ Paso 4: Eliminar Archivos de Docker (Opcional)

Si NO vas a usar Docker, puedes eliminar estos archivos:

```
.devcontainer/
  ├── devcontainer.json
  └── Dockerfile
```

**Estos archivos NO afectan la ejecución local**, solo son para entornos Gitpod/Docker.

---

## 🚀 Paso 5: Abrir en NetBeans

1. Abre **NetBeans IDE**
2. **File → Open Project**
3. Navega a la carpeta `InventarioApp/InventarioSpringBoot`
4. Selecciona el proyecto y haz clic en **Open Project**
5. NetBeans detectará automáticamente que es un proyecto Maven

### 5.1 Resolver Dependencias

NetBeans descargará automáticamente las dependencias de Maven. Si no lo hace:

1. Click derecho en el proyecto
2. **Build with Dependencies** o **Clean and Build**

---

## ▶️ Paso 6: Ejecutar el Proyecto

### Opción A - Desde NetBeans (Recomendado)

1. Click derecho en el proyecto `InventarioSpringBoot`
2. **Run** o presiona `F6`
3. Espera a que aparezca en la consola:
   ```
   Started InventarioSpringBootApplication in X.XXX seconds
   ```
4. Abre tu navegador en: **http://localhost:8080**

### Opción B - Desde Terminal

```bash
cd InventarioSpringBoot
mvn clean package
java -jar target/InventarioSpringBoot.jar
```

---

## 🔐 Paso 7: Iniciar Sesión

Abre tu navegador en: **http://localhost:8080**

Serás redirigido a `/login`. Usa estas credenciales:

**Administrador:**
- Usuario: `admin`
- Contraseña: `admin123`

**Usuario:**
- Usuario: `user`
- Contraseña: `user123`

---

## 🛠️ Solución de Problemas

### Error: "Access denied for user 'root'@'localhost'"

**Causa:** Credenciales incorrectas de MySQL

**Solución:**
1. Verifica tu usuario y contraseña de MySQL
2. Actualiza `application.yml` con las credenciales correctas
3. Reinicia la aplicación

### Error: "Unknown database 'inventariodb'"

**Causa:** La base de datos no existe

**Solución:**
1. Ejecuta el script `database/schema.sql` en MySQL
2. O crea manualmente la base de datos:
   ```sql
   CREATE DATABASE inventariodb;
   ```

### Error: "Port 8080 already in use"

**Causa:** Otro proceso está usando el puerto 8080

**Solución A - Cambiar puerto:**
Edita `application.yml`:
```yaml
server:
  port: 8081  # Cambia a otro puerto
```

**Solución B - Liberar puerto:**
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:8080 | xargs kill -9
```

### Error: "Could not find or load main class"

**Causa:** Proyecto no compilado correctamente

**Solución:**
1. En NetBeans: **Clean and Build**
2. O desde terminal:
   ```bash
   mvn clean install
   ```

### Error: "Failed to configure a DataSource"

**Causa:** MySQL no está corriendo o credenciales incorrectas

**Solución:**
1. Inicia MySQL (XAMPP → Start MySQL)
2. Verifica que MySQL esté corriendo:
   ```bash
   mysql -u root -p
   ```
3. Verifica credenciales en `application.yml`

---

## 📊 Verificar Datos en MySQL

Para ver los productos insertados:

```sql
USE inventariodb;
SELECT * FROM productos;
SELECT COUNT(*) FROM productos;
```

---

## 🔄 Actualizar Código desde GitHub

Si haces cambios en GitHub y quieres actualizarlos en local:

```bash
cd InventarioApp
git pull origin main
```

Luego en NetBeans:
1. Click derecho en el proyecto
2. **Clean and Build**
3. **Run**

---

## 👥 Agregar Más Usuarios

### Opción 1: En Memoria (Temporal)

Edita `SecurityConfig.java`:

```java
@Bean
public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    UserDetails admin = User.builder()
            .username("admin")
            .password(passwordEncoder.encode("admin123"))
            .roles("ADMIN")
            .build();

    UserDetails user = User.builder()
            .username("user")
            .password(passwordEncoder.encode("user123"))
            .roles("USER")
            .build();

    // NUEVO USUARIO
    UserDetails nuevoUsuario = User.builder()
            .username("juan")
            .password(passwordEncoder.encode("juan123"))
            .roles("USER")  // o "ADMIN"
            .build();

    return new InMemoryUserDetailsManager(admin, user, nuevoUsuario);
}
```

### Opción 2: En Base de Datos (Permanente)

**Paso 1:** Inserta usuario en MySQL:

```sql
USE inventariodb;

-- Generar contraseña encriptada (usa BCrypt online: https://bcrypt-generator.com/)
-- Ejemplo: "juan123" → $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhLu

INSERT INTO usuarios (username, password, rol, activo) VALUES
('juan', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhLu', 'USER', TRUE);
```

**Paso 2:** Modifica `SecurityConfig.java` para usar base de datos:

```java
@Autowired
private UsuarioRepository usuarioRepository;

@Bean
public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    return username -> {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        
        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(usuario.getRol())
                .disabled(!usuario.getActivo())
                .build();
    };
}
```

---

## 📝 Explicación: Usuarios en Memoria vs Base de Datos

### ¿Por qué los usuarios están en memoria?

**Ventajas:**
- ✅ Más simple para proyectos académicos
- ✅ No requiere tabla de usuarios en BD
- ✅ Credenciales definidas en código (fácil de ver)
- ✅ No hay riesgo de olvidar contraseñas

**Desventajas:**
- ❌ No se pueden agregar usuarios sin recompilar
- ❌ Usuarios se pierden al reiniciar (no aplica, están en código)
- ❌ No escalable para producción

### ¿Cómo funciona?

```java
@Bean
public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    // Crea usuarios en memoria al iniciar la aplicación
    UserDetails admin = User.builder()
            .username("admin")
            .password(passwordEncoder.encode("admin123"))  // Encripta con BCrypt
            .roles("ADMIN")  // Rol del usuario
            .build();
    
    // InMemoryUserDetailsManager almacena usuarios en RAM
    return new InMemoryUserDetailsManager(admin, user);
}
```

**Flujo de autenticación:**
1. Usuario ingresa credenciales en `/login`
2. Spring Security busca el usuario en `InMemoryUserDetailsManager`
3. Compara la contraseña ingresada con la encriptada (BCrypt)
4. Si coincide, crea sesión y asigna rol
5. En cada petición, verifica rol para autorización

### ¿Cuándo usar Base de Datos?

Para producción o si necesitas:
- Registro de usuarios dinámico
- Cambio de contraseñas
- Gestión de usuarios desde la aplicación
- Auditoría de accesos

---

## 🎓 Para Explicar en Clase

### "¿Por qué los usuarios no están en la base de datos?"

**Respuesta:**
> "Para este proyecto académico, los usuarios están definidos en memoria mediante `InMemoryUserDetailsManager` de Spring Security. Esto simplifica la configuración inicial y permite enfocarnos en las funcionalidades principales del inventario. Los usuarios se definen en la clase `SecurityConfig.java` y se cargan al iniciar la aplicación. Aunque existe la tabla `usuarios` en la base de datos, actualmente no se utiliza, pero está preparada para una futura implementación donde los usuarios puedan registrarse y gestionarse dinámicamente."

### "¿Cómo están protegidas las contraseñas?"

**Respuesta:**
> "Las contraseñas se encriptan usando BCrypt, un algoritmo de hash unidireccional. Esto significa que aunque veamos `admin123` en el código, Spring Security la convierte a algo como `$2a$10$N9qo8uLO...` antes de almacenarla. BCrypt es resistente a ataques de fuerza bruta porque es computacionalmente costoso y usa 'salt' aleatorio para cada contraseña."

---

## 📞 Soporte

Si tienes problemas:

1. Verifica que MySQL esté corriendo
2. Revisa los logs en la consola de NetBeans
3. Verifica credenciales en `application.yml`
4. Asegúrate de que el puerto 8080 esté libre

---

## ✅ Checklist de Configuración

- [ ] Java 17 instalado
- [ ] Maven instalado
- [ ] NetBeans instalado
- [ ] MySQL instalado y corriendo
- [ ] Base de datos `inventariodb` creada
- [ ] Script SQL ejecutado
- [ ] `application.yml` configurado con credenciales correctas
- [ ] Proyecto abierto en NetBeans
- [ ] Dependencias descargadas
- [ ] Aplicación ejecutándose
- [ ] Login funcionando en http://localhost:8080

---

## 🚀 ¡Listo!

Tu proyecto está configurado y corriendo en local. Ahora puedes:
- Hacer cambios en el código
- Probar nuevas funcionalidades
- Presentar en clase sin depender de internet
- Trabajar con tu propia base de datos MySQL
