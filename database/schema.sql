-- ============================================
-- Script de Base de Datos - Sistema de Inventario
-- Base de Datos: MySQL 8.0+
-- ============================================

-- Crear base de datos
CREATE DATABASE IF NOT EXISTS inventariodb 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE inventariodb;

-- ============================================
-- Tabla: productos
-- ============================================
DROP TABLE IF EXISTS productos;

CREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    nombre VARCHAR(120) NOT NULL,
    categoria VARCHAR(50),
    precio DOUBLE NOT NULL,
    stock INT NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    INDEX idx_codigo (codigo),
    INDEX idx_categoria (categoria),
    INDEX idx_activo (activo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Tabla: usuarios (opcional - para futuro)
-- ============================================
DROP TABLE IF EXISTS usuarios;

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Datos de ejemplo
-- ============================================

-- Productos de Electrónicos
INSERT INTO productos (codigo, nombre, categoria, precio, stock, activo) VALUES
('LAPTOP-001', 'Laptop Dell Inspiron 15', 'Electronicos', 3500000, 15, TRUE),
('LAPTOP-002', 'Laptop HP Pavilion 14', 'Electronicos', 2800000, 8, TRUE),
('LAPTOP-003', 'MacBook Air M2', 'Electronicos', 5200000, 5, TRUE),
('MONITOR-001', 'Monitor LG 27 pulgadas 4K', 'Electronicos', 1200000, 8, TRUE),
('MONITOR-002', 'Monitor Samsung 24 pulgadas', 'Electronicos', 650000, 12, TRUE),
('TABLET-001', 'Tablet Samsung Galaxy Tab S8', 'Electronicos', 1800000, 2, TRUE),
('TABLET-002', 'iPad 10.2 pulgadas', 'Electronicos', 1950000, 6, TRUE),
('IMPRESORA-001', 'Impresora HP LaserJet Pro', 'Electronicos', 950000, 20, TRUE),
('IMPRESORA-002', 'Impresora Epson EcoTank', 'Electronicos', 1100000, 4, TRUE),
('CAMARA-001', 'Cámara Web Logitech C920', 'Electronicos', 380000, 18, TRUE),
('PROYECTOR-001', 'Proyector Epson PowerLite', 'Electronicos', 2200000, 3, TRUE);

-- Productos de Accesorios
INSERT INTO productos (codigo, nombre, categoria, precio, stock, activo) VALUES
('MOUSE-001', 'Mouse Logitech MX Master 3', 'Accesorios', 320000, 45, TRUE),
('MOUSE-002', 'Mouse Gamer Razer DeathAdder', 'Accesorios', 280000, 30, TRUE),
('MOUSE-003', 'Mouse Inalámbrico Microsoft', 'Accesorios', 95000, 60, TRUE),
('TECLADO-001', 'Teclado Mecánico RGB Corsair', 'Accesorios', 450000, 30, TRUE),
('TECLADO-002', 'Teclado Logitech K380', 'Accesorios', 180000, 25, TRUE),
('AURICULAR-001', 'Auriculares Bluetooth Sony WH-1000XM5', 'Accesorios', 1200000, 3, TRUE),
('AURICULAR-002', 'Auriculares Gaming HyperX Cloud', 'Accesorios', 380000, 15, TRUE),
('CABLE-001', 'Cable HDMI 2.1 de 2 metros', 'Accesorios', 45000, 100, TRUE),
('CABLE-002', 'Cable USB-C a USB-C', 'Accesorios', 35000, 80, TRUE),
('ADAPTADOR-001', 'Adaptador USB-C Hub 7 en 1', 'Accesorios', 120000, 22, TRUE),
('MOUSEPAD-001', 'Mousepad Gaming XXL', 'Accesorios', 65000, 40, TRUE);

-- Productos de Muebles
INSERT INTO productos (codigo, nombre, categoria, precio, stock, activo) VALUES
('SILLA-001', 'Silla Ergonómica de Oficina', 'Muebles', 850000, 12, TRUE),
('SILLA-002', 'Silla Gaming DXRacer', 'Muebles', 1200000, 7, TRUE),
('SILLA-003', 'Silla Ejecutiva de Cuero', 'Muebles', 950000, 5, TRUE),
('ESCRITORIO-001', 'Escritorio Ejecutivo L-Shape', 'Muebles', 1500000, 5, TRUE),
('ESCRITORIO-002', 'Escritorio Ajustable en Altura', 'Muebles', 1800000, 3, TRUE),
('ESCRITORIO-003', 'Escritorio Compacto para PC', 'Muebles', 450000, 10, TRUE),
('LAMPARA-001', 'Lámpara LED de Escritorio', 'Muebles', 85000, 25, TRUE),
('LAMPARA-002', 'Lámpara de Pie Moderna', 'Muebles', 320000, 8, TRUE),
('ESTANTE-001', 'Estantería Modular 5 Niveles', 'Muebles', 380000, 6, TRUE),
('ARCHIVADOR-001', 'Archivador Metálico 4 Gavetas', 'Muebles', 650000, 4, TRUE);

-- Productos de Ropa
INSERT INTO productos (codigo, nombre, categoria, precio, stock, activo) VALUES
('CAMISA-001', 'Camisa Formal Blanca Slim Fit', 'Ropa', 120000, 50, TRUE),
('CAMISA-002', 'Camisa Casual a Cuadros', 'Ropa', 95000, 35, TRUE),
('CAMISA-003', 'Camisa Polo Lacoste', 'Ropa', 180000, 28, TRUE),
('PANTALON-001', 'Pantalón de Vestir Negro', 'Ropa', 180000, 35, TRUE),
('PANTALON-002', 'Jeans Levi\'s 501 Original', 'Ropa', 220000, 40, TRUE),
('PANTALON-003', 'Pantalón Chino Beige', 'Ropa', 150000, 30, TRUE),
('CHAQUETA-001', 'Chaqueta de Cuero', 'Ropa', 450000, 8, TRUE),
('CHAQUETA-002', 'Blazer Formal Azul Marino', 'Ropa', 380000, 12, TRUE),
('ZAPATOS-001', 'Zapatos Formales de Cuero', 'Ropa', 280000, 20, TRUE),
('ZAPATOS-002', 'Tenis Nike Air Max', 'Ropa', 350000, 15, TRUE),
('CORBATA-001', 'Corbata de Seda Italiana', 'Ropa', 85000, 45, TRUE);

-- Productos INACTIVOS (descontinuados o fuera de stock)
INSERT INTO productos (codigo, nombre, categoria, precio, stock, activo) VALUES
('LAPTOP-OLD-001', 'Laptop Dell Latitude E6430 (Descontinuado)', 'Electronicos', 800000, 0, FALSE),
('MOUSE-OLD-001', 'Mouse PS/2 Genérico (Descontinuado)', 'Accesorios', 15000, 0, FALSE),
('SILLA-OLD-001', 'Silla de Oficina Básica (Descontinuado)', 'Muebles', 180000, 0, FALSE),
('MONITOR-OLD-001', 'Monitor CRT 17 pulgadas (Obsoleto)', 'Electronicos', 50000, 0, FALSE),
('TECLADO-OLD-001', 'Teclado PS/2 IBM (Vintage)', 'Accesorios', 120000, 1, FALSE);

-- ============================================
-- Verificación de datos
-- ============================================
SELECT 'Productos insertados correctamente' AS mensaje;
SELECT COUNT(*) AS total_productos FROM productos;
SELECT COUNT(*) AS productos_activos FROM productos WHERE activo = TRUE;
SELECT COUNT(*) AS productos_inactivos FROM productos WHERE activo = FALSE;
SELECT categoria, COUNT(*) AS cantidad FROM productos GROUP BY categoria;
