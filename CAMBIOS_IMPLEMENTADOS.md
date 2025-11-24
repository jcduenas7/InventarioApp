# Cambios Implementados - Sistema de Inventario

## 📋 Resumen de Mejoras

Se implementaron 4 mejoras principales al sistema de inventario:
1. **Sistema de Autenticación y Roles**
2. **Dashboard con Estadísticas y Gráficos**
3. **Búsqueda y Filtros Avanzados**
4. **Validaciones Mejoradas**

---

## 🔐 1. Sistema de Autenticación y Roles

### Archivos Creados/Modificados:

#### `pom.xml`
- **Dependencias agregadas:**
  - `spring-boot-starter-security`: Framework de seguridad de Spring
  - `thymeleaf-extras-springsecurity6`: Integración de Thymeleaf con Spring Security
  - `spring-boot-starter-validation`: Para validaciones Bean Validation

#### `SecurityConfig.java` (NUEVO)
**Ubicación:** `src/main/java/com/inventario/config/SecurityConfig.java`

**Qué hace:**
- Configura Spring Security para proteger la aplicación
- Define usuarios en memoria (admin y user)
- Establece reglas de acceso por roles

**Componentes clave:**

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```
- Crea un codificador de contraseñas usando BCrypt (algoritmo seguro de hash)
- Las contraseñas se almacenan encriptadas, no en texto plano

```java
@Bean
public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    UserDetails admin = User.builder()
            .username("admin")
            .password(passwordEncoder.encode("admin123"))
            .roles("ADMIN")
            .build();
    // ...
}
```
- Define usuarios en memoria (no requiere base de datos para usuarios)
- `admin` tiene rol ADMIN (puede crear, editar, eliminar)
- `user` tiene rol USER (solo puede ver)

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) {
    http.authorizeHttpRequests(auth -> auth
        .requestMatchers("/productos/nuevo", "/productos/*/editar", "/productos/*/eliminar")
            .hasRole("ADMIN")
        .requestMatchers("/productos/**").hasAnyRole("USER", "ADMIN")
        // ...
    )
}
```
- Define qué URLs requieren qué roles
- `/productos/nuevo`, editar y eliminar: solo ADMIN
- `/productos/**`: USER y ADMIN pueden ver

#### `Usuario.java` (NUEVO)
**Ubicación:** `src/main/java/com/inventario/model/Usuario.java`

**Qué hace:**
- Entidad JPA para almacenar usuarios en base de datos (preparado para futuro)
- Campos: id, username, password, rol, activo, fechaCreacion

#### `UsuarioRepository.java` (NUEVO)
**Ubicación:** `src/main/java/com/inventario/repository/UsuarioRepository.java`

**Qué hace:**
- Repositorio JPA para operaciones CRUD de usuarios
- Método `findByUsername()` para buscar usuarios por nombre

#### `AuthController.java` (NUEVO)
**Ubicación:** `src/main/java/com/inventario/controller/AuthController.java`

**Qué hace:**
- Maneja la página de login
- Muestra mensajes de error si las credenciales son incorrectas
- Muestra mensaje de éxito al cerrar sesión

#### `login.html` (NUEVO)
**Ubicación:** `src/main/resources/templates/login.html`

**Qué hace:**
- Página de inicio de sesión con diseño moderno
- Muestra las credenciales de prueba
- Formulario que envía a `/login` (manejado por Spring Security)

#### `ProductoController.java` (MODIFICADO)
**Cambios:**
- Agregado `@PreAuthorize("hasRole('ADMIN')")` en métodos de crear, editar y eliminar
- Solo usuarios con rol ADMIN pueden ejecutar estas acciones
- Si un USER intenta acceder, recibe error 403 (Forbidden)

---

## 📊 2. Dashboard con Estadísticas y Gráficos

### Archivos Creados:

#### `EstadisticasService.java` (NUEVO)
**Ubicación:** `src/main/java/com/inventario/service/EstadisticasService.java`

**Qué hace:**
- Calcula estadísticas del inventario usando Java Streams
- Métodos principales:

```java
public Map<String, Object> obtenerEstadisticas() {
    // Total de productos
    stats.put("totalProductos", productos.size());
    
    // Valor total del inventario (precio × stock)
    double valorTotal = productos.stream()
            .mapToDouble(p -> p.getPrecio() * p.getStock())
            .sum();
    
    // Productos por categoría (agrupación)
    Map<String, Long> productosPorCategoria = productos.stream()
            .collect(Collectors.groupingBy(
                    p -> p.getCategoria(),
                    Collectors.counting()
            ));
    
    // Top 5 productos más caros
    List<Producto> productosMasCaros = productos.stream()
            .sorted(Comparator.comparing(Producto::getPrecio).reversed())
            .limit(5)
            .collect(Collectors.toList());
}
```

**Conceptos importantes:**
- **Stream API**: Permite procesar colecciones de forma funcional
- **Collectors.groupingBy()**: Agrupa elementos por una característica (categoría)
- **Comparator**: Ordena elementos por un campo específico
- **mapToDouble().sum()**: Transforma a números y suma

#### `DashboardController.java` (NUEVO)
**Ubicación:** `src/main/java/com/inventario/controller/DashboardController.java`

**Qué hace:**
- Controlador para la página principal (dashboard)
- Obtiene estadísticas del servicio y las pasa a la vista
- Mapea las rutas `/` y `/dashboard`

#### `dashboard.html` (NUEVO)
**Ubicación:** `src/main/resources/templates/dashboard.html`

**Qué hace:**
- Muestra 4 tarjetas con estadísticas principales
- 2 gráficos usando Chart.js:
  - **Pie Chart**: Productos por categoría
  - **Bar Chart**: Stock por categoría
- Tablas de productos con stock crítico y más caros

**Tecnologías usadas:**
- **Chart.js**: Librería JavaScript para gráficos
- **Thymeleaf inline JavaScript**: `/*[[${variable}]]*/` para pasar datos de Java a JavaScript

```javascript
const productosPorCategoria = /*[[${productosPorCategoria}]]*/ {};
const categorias = Object.keys(productosPorCategoria);
const cantidades = Object.values(productosPorCategoria);

new Chart(ctx, {
    type: 'pie',
    data: {
        labels: categorias,
        datasets: [{
            data: cantidades,
            backgroundColor: ['#667eea', '#764ba2', ...]
        }]
    }
});
```

---

## 🔍 3. Búsqueda y Filtros Avanzados

### Archivos Modificados:

#### `ProductoService.java` (MODIFICADO)
**Método agregado:**

```java
public List<Producto> buscarYFiltrar(String buscar, String categoria, String ordenar) {
    List<Producto> productos = repository.findAll();
    
    // Filtrar por búsqueda (código o nombre)
    if (buscar != null && !buscar.trim().isEmpty()) {
        String buscarLower = buscar.toLowerCase();
        productos = productos.stream()
                .filter(p -> p.getCodigo().toLowerCase().contains(buscarLower) ||
                           p.getNombre().toLowerCase().contains(buscarLower))
                .collect(Collectors.toList());
    }
    
    // Filtrar por categoría
    if (categoria != null && !categoria.equals("Todas")) {
        productos = productos.stream()
                .filter(p -> categoria.equals(p.getCategoria()))
                .collect(Collectors.toList());
    }
    
    // Ordenar
    switch (ordenar) {
        case "nombre":
            productos.sort(Comparator.comparing(Producto::getNombre));
            break;
        case "precio_asc":
            productos.sort(Comparator.comparing(Producto::getPrecio));
            break;
        // ...
    }
    
    return productos;
}
```

**Conceptos:**
- **filter()**: Filtra elementos que cumplen una condición
- **contains()**: Busca subcadenas (búsqueda parcial)
- **toLowerCase()**: Búsqueda insensible a mayúsculas/minúsculas
- **sort()**: Ordena la lista según un criterio

#### `ProductoController.java` (MODIFICADO)
**Método listar actualizado:**

```java
@GetMapping
public String listar(
        @RequestParam(value = "buscar", required = false) String buscar,
        @RequestParam(value = "categoria", required = false) String categoria,
        @RequestParam(value = "ordenar", required = false) String ordenar,
        Model model) {
    
    List<Producto> productos = service.buscarYFiltrar(buscar, categoria, ordenar);
    model.addAttribute("productos", productos);
    // Mantener valores en el formulario
    model.addAttribute("buscar", buscar);
    model.addAttribute("categoria", categoria);
    model.addAttribute("ordenar", ordenar);
    
    return "productos/listado";
}
```

**Conceptos:**
- **@RequestParam**: Captura parámetros de la URL (?buscar=laptop&categoria=Electronicos)
- **required = false**: Parámetros opcionales
- **Model**: Objeto para pasar datos a la vista

#### `listado.html` (MODIFICADO)
**Formulario de filtros agregado:**

```html
<form th:action="@{/productos}" method="get">
    <div class="row g-3">
        <div class="col-md-4">
            <input type="text" name="buscar" th:value="${buscar}" 
                   placeholder="Código o nombre...">
        </div>
        <div class="col-md-3">
            <select name="categoria">
                <option value="">Todas</option>
                <option value="Electronicos" 
                        th:selected="${categoria == 'Electronicos'}">
                    Electrónicos
                </option>
            </select>
        </div>
        <button type="submit">Filtrar</button>
    </div>
</form>
```

**Conceptos:**
- **method="get"**: Envía datos por URL (permite compartir enlaces filtrados)
- **th:value="${buscar}"**: Mantiene el valor ingresado después de filtrar
- **th:selected**: Marca la opción seleccionada en el select

---

## ✅ 4. Validaciones Mejoradas

### Archivos Modificados:

#### `Producto.java` (MODIFICADO)
**Anotaciones de validación agregadas:**

```java
@NotBlank(message = "El código es obligatorio")
@Size(min = 3, max = 50, message = "El código debe tener entre 3 y 50 caracteres")
@Pattern(regexp = "^[A-Z0-9-]+$", message = "Solo letras mayúsculas, números y guiones")
private String codigo;

@NotBlank(message = "El nombre es obligatorio")
@Size(min = 5, max = 120, message = "El nombre debe tener entre 5 y 120 caracteres")
private String nombre;

@NotNull(message = "El precio es obligatorio")
@DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
@DecimalMax(value = "999999.99", message = "El precio no puede exceder 999,999.99")
private Double precio;

@NotNull(message = "El stock es obligatorio")
@Min(value = 0, message = "El stock no puede ser negativo")
@Max(value = 999999, message = "El stock no puede exceder 999,999")
private Integer stock;
```

**Anotaciones explicadas:**
- **@NotBlank**: Campo no puede estar vacío (String)
- **@NotNull**: Campo no puede ser null (números, objetos)
- **@Size**: Longitud mínima y máxima
- **@Pattern**: Expresión regular (regex) para formato específico
- **@DecimalMin/@DecimalMax**: Rango de valores decimales
- **@Min/@Max**: Rango de valores enteros

#### `ProductoController.java` (MODIFICADO)
**Validación en métodos crear y actualizar:**

```java
@PostMapping
public String crear(@Valid @ModelAttribute Producto producto,
                   BindingResult result,
                   RedirectAttributes redirect,
                   Model model) {
    
    if (result.hasErrors()) {
        return "productos/formulario";  // Vuelve al formulario con errores
    }
    
    // Si no hay errores, guardar
    service.crear(producto);
    return "redirect:/productos";
}
```

**Conceptos:**
- **@Valid**: Activa la validación de Bean Validation
- **BindingResult**: Contiene los errores de validación
- **result.hasErrors()**: Verifica si hay errores
- Si hay errores, vuelve al formulario (no guarda)

#### `formulario.html` (MODIFICADO)
**Mostrar errores de validación:**

```html
<input type="text" 
       class="form-control" 
       th:classappend="${#fields.hasErrors('codigo')} ? 'is-invalid' : ''"
       th:field="*{codigo}">
<div class="invalid-feedback" 
     th:if="${#fields.hasErrors('codigo')}" 
     th:errors="*{codigo}">
</div>
```

**Conceptos:**
- **th:classappend**: Agrega clase CSS si hay error (borde rojo)
- **#fields.hasErrors('campo')**: Verifica si un campo tiene errores
- **th:errors**: Muestra el mensaje de error del campo

---

## 🎨 Mejoras Adicionales

### Navbar Unificado
- Agregado en `dashboard.html` y `listado.html`
- Muestra usuario actual y rol
- Botón de cerrar sesión
- Navegación entre Dashboard y Productos

### Inicialización de Datos
**`DataInitializer.java` (NUEVO)**
- Carga 12 productos de ejemplo al iniciar la aplicación
- Solo si la base de datos está vacía
- Implementa `CommandLineRunner` (se ejecuta al inicio)

---

## 🚀 Cómo Funciona el Sistema

### Flujo de Autenticación:

1. Usuario accede a cualquier URL protegida
2. Spring Security redirige a `/login`
3. Usuario ingresa credenciales (admin/admin123 o user/user123)
4. Spring Security valida contra `UserDetailsService`
5. Si es correcto, crea sesión y redirige a `/dashboard`
6. En cada petición, Spring Security verifica:
   - ¿Está autenticado?
   - ¿Tiene el rol necesario?

### Flujo de Validación:

1. Usuario llena formulario y envía
2. Spring recibe datos y ejecuta validaciones (@Valid)
3. Si hay errores:
   - `BindingResult` contiene los errores
   - Vuelve al formulario con mensajes
4. Si no hay errores:
   - Guarda en base de datos
   - Redirige a listado con mensaje de éxito

### Flujo de Búsqueda:

1. Usuario ingresa criterios en formulario
2. Formulario envía GET a `/productos?buscar=laptop&categoria=Electronicos`
3. Controller captura parámetros con `@RequestParam`
4. Service filtra productos usando Streams
5. Controller devuelve lista filtrada a la vista
6. Vista mantiene valores en formulario (th:value)

---

## 📚 Conceptos para Explicar en Clase

### 1. Spring Security
- **Autenticación**: Verificar quién eres (login)
- **Autorización**: Verificar qué puedes hacer (roles)
- **BCrypt**: Algoritmo de hash para contraseñas (irreversible)
- **SecurityFilterChain**: Cadena de filtros que interceptan peticiones

### 2. Bean Validation
- **JSR-303**: Especificación Java para validaciones
- **Anotaciones declarativas**: Validaciones en el modelo, no en el controller
- **BindingResult**: Objeto que contiene errores de validación
- **Mensajes personalizados**: Mejora experiencia de usuario

### 3. Java Streams
- **Programación funcional**: Operaciones sobre colecciones
- **filter()**: Selecciona elementos que cumplen condición
- **map()**: Transforma elementos
- **collect()**: Convierte Stream a colección
- **Collectors**: Operaciones de agregación (groupingBy, counting, summing)

### 4. Thymeleaf
- **th:field**: Binding bidireccional con modelo
- **th:errors**: Muestra errores de validación
- **th:if**: Renderizado condicional
- **sec:authorize**: Muestra/oculta según rol
- **Inline JavaScript**: Pasar datos de Java a JavaScript

### 5. Patrón MVC
- **Model**: Entidades (Producto, Usuario)
- **View**: Templates HTML (Thymeleaf)
- **Controller**: Maneja peticiones HTTP
- **Service**: Lógica de negocio
- **Repository**: Acceso a datos (JPA)

---

## 🎯 Puntos Clave para el Parcial

1. **Seguridad implementada**: Autenticación y autorización con Spring Security
2. **Roles diferenciados**: ADMIN puede todo, USER solo lectura
3. **Validaciones robustas**: Bean Validation con mensajes personalizados
4. **Experiencia de usuario**: Dashboard visual con gráficos
5. **Funcionalidad práctica**: Búsqueda y filtros avanzados
6. **Código limpio**: Separación de responsabilidades (MVC)
7. **Tecnologías modernas**: Spring Boot 3.2, Java 17, Chart.js

---

## 📝 Credenciales de Acceso

- **Administrador**: admin / admin123
- **Usuario**: user / user123

## 🌐 URL de Acceso

La aplicación está corriendo en:
https://8080--019ab6ed-daf9-7a81-ab1f-f2414310c4f4.us-east-1-01.gitpod.dev
