package com.inventario.config;

import com.inventario.model.Producto;
import com.inventario.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public void run(String... args) throws Exception {
        // Solo inicializar si no hay productos
        if (productoRepository.count() == 0) {
            // Productos de ejemplo con precios en COP
            productoRepository.save(new Producto(null, "LAPTOP-001", "Laptop Dell Inspiron 15", "Electronicos", 3500000.0, 15, true));
            productoRepository.save(new Producto(null, "MOUSE-001", "Mouse Logitech MX Master", "Accesorios", 320000.0, 45, true));
            productoRepository.save(new Producto(null, "TECLADO-001", "Teclado Mecánico RGB", "Accesorios", 450000.0, 30, true));
            productoRepository.save(new Producto(null, "MONITOR-001", "Monitor LG 27 pulgadas", "Electronicos", 1200000.0, 8, true));
            productoRepository.save(new Producto(null, "SILLA-001", "Silla Ergonómica Oficina", "Muebles", 850000.0, 12, true));
            productoRepository.save(new Producto(null, "ESCRITORIO-001", "Escritorio Ejecutivo", "Muebles", 1500000.0, 5, true));
            productoRepository.save(new Producto(null, "CAMISA-001", "Camisa Formal Blanca", "Ropa", 120000.0, 50, true));
            productoRepository.save(new Producto(null, "PANTALON-001", "Pantalón de Vestir Negro", "Ropa", 180000.0, 35, true));
            productoRepository.save(new Producto(null, "AURICULAR-001", "Auriculares Bluetooth", "Accesorios", 280000.0, 3, true));
            productoRepository.save(new Producto(null, "TABLET-001", "Tablet Samsung Galaxy", "Electronicos", 1800000.0, 2, true));
            productoRepository.save(new Producto(null, "IMPRESORA-001", "Impresora HP LaserJet", "Electronicos", 950000.0, 20, true));
            productoRepository.save(new Producto(null, "LAMPARA-001", "Lámpara LED Escritorio", "Muebles", 85000.0, 25, true));
            
            System.out.println("✅ Datos de ejemplo inicializados correctamente");
        }
    }
}
