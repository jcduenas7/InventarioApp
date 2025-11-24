package com.inventario.service;

import com.inventario.model.Producto;
import com.inventario.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EstadisticasService {

    @Autowired
    private ProductoRepository repository;

    public Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> stats = new HashMap<>();
        List<Producto> productos = repository.findAll();

        // Total de productos
        stats.put("totalProductos", productos.size());

        // Valor total del inventario
        double valorTotal = productos.stream()
                .mapToDouble(p -> p.getPrecio() * p.getStock())
                .sum();
        stats.put("valorTotal", valorTotal);

        // Productos activos
        long productosActivos = productos.stream()
                .filter(Producto::getActivo)
                .count();
        stats.put("productosActivos", productosActivos);

        // Productos con stock bajo (menos de 10)
        long stockBajo = productos.stream()
                .filter(p -> p.getStock() < 10)
                .count();
        stats.put("stockBajo", stockBajo);

        // Productos por categoría
        Map<String, Long> productosPorCategoria = productos.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getCategoria() != null ? p.getCategoria() : "Sin categoría",
                        Collectors.counting()
                ));
        stats.put("productosPorCategoria", productosPorCategoria);

        // Stock por categoría
        Map<String, Integer> stockPorCategoria = productos.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getCategoria() != null ? p.getCategoria() : "Sin categoría",
                        Collectors.summingInt(Producto::getStock)
                ));
        stats.put("stockPorCategoria", stockPorCategoria);

        // Productos más caros (top 5)
        List<Producto> productosMasCaros = productos.stream()
                .sorted(Comparator.comparing(Producto::getPrecio).reversed())
                .limit(5)
                .collect(Collectors.toList());
        stats.put("productosMasCaros", productosMasCaros);

        // Productos con más stock (top 5)
        List<Producto> productosMasStock = productos.stream()
                .sorted(Comparator.comparing(Producto::getStock).reversed())
                .limit(5)
                .collect(Collectors.toList());
        stats.put("productosMasStock", productosMasStock);

        // Productos con stock crítico (menos de 5)
        List<Producto> productosStockCritico = productos.stream()
                .filter(p -> p.getStock() < 5)
                .sorted(Comparator.comparing(Producto::getStock))
                .collect(Collectors.toList());
        stats.put("productosStockCritico", productosStockCritico);

        return stats;
    }
}
