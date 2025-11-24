package com.inventario.controller;

import com.inventario.model.Producto;
import com.inventario.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService service;

    // GET - Listar todos (Vista HTML) con búsqueda y filtros
    @GetMapping
    public String listar(
            @RequestParam(value = "buscar", required = false) String buscar,
            @RequestParam(value = "categoria", required = false) String categoria,
            @RequestParam(value = "ordenar", required = false) String ordenar,
            Model model) {
        
        List<Producto> productos = service.buscarYFiltrar(buscar, categoria, ordenar);
        model.addAttribute("productos", productos);
        model.addAttribute("buscar", buscar);
        model.addAttribute("categoria", categoria);
        model.addAttribute("ordenar", ordenar);
        
        return "productos/listado";
    }

    // GET - Formulario nuevo producto (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        model.addAttribute("producto", new Producto());
        return "productos/formulario";
    }

    // POST - Crear producto (solo ADMIN) con validaciones
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public String crear(@Valid @ModelAttribute Producto producto,
                       BindingResult result,
                       RedirectAttributes redirect,
                       Model model) {
        if (result.hasErrors()) {
            return "productos/formulario";
        }
        
        try {
            service.crear(producto);
            redirect.addFlashAttribute("mensaje", "Producto creado exitosamente");
            redirect.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirect.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirect.addFlashAttribute("tipo", "error");
        }
        return "redirect:/productos";
    }

    // GET - Formulario editar (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable Integer id, Model model,
                            RedirectAttributes redirect) {
        Optional<Producto> p = service.obtenerPorId(id);
        if (p.isPresent()) {
            model.addAttribute("producto", p.get());
            return "productos/formulario";
        }
        redirect.addFlashAttribute("mensaje", "Producto no encontrado");
        redirect.addFlashAttribute("tipo", "error");
        return "redirect:/productos";
    }

    // PUT - Actualizar producto (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}")
    public String actualizar(@PathVariable Integer id,
                            @ModelAttribute Producto producto,
                            BindingResult result,
                            RedirectAttributes redirect,
                            Model model) {
        // Validación manual (sin @Valid para evitar validar el código)
        if (producto.getNombre() == null || producto.getNombre().trim().length() < 5) {
            result.rejectValue("nombre", "error.producto", "El nombre debe tener al menos 5 caracteres");
        }
        if (producto.getCategoria() == null || producto.getCategoria().trim().isEmpty()) {
            result.rejectValue("categoria", "error.producto", "La categoría es obligatoria");
        }
        if (producto.getPrecio() == null || producto.getPrecio() < 1) {
            result.rejectValue("precio", "error.producto", "El precio debe ser mayor a 0");
        }
        if (producto.getPrecio() != null && producto.getPrecio() > 999999999) {
            result.rejectValue("precio", "error.producto", "El precio no puede exceder $999.999.999");
        }
        if (producto.getStock() == null || producto.getStock() < 0) {
            result.rejectValue("stock", "error.producto", "El stock no puede ser negativo");
        }
        
        if (result.hasErrors()) {
            producto.setId(id);
            model.addAttribute("producto", producto);
            return "productos/formulario";
        }
        
        try {
            service.actualizar(id, producto);
            redirect.addFlashAttribute("mensaje", "Producto actualizado exitosamente");
            redirect.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirect.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirect.addFlashAttribute("tipo", "error");
        }
        return "redirect:/productos";
    }

    // DELETE - Eliminar producto (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirect) {
        try {
            service.eliminar(id);
            redirect.addFlashAttribute("mensaje", "Producto eliminado exitosamente");
            redirect.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirect.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirect.addFlashAttribute("tipo", "error");
        }
        return "redirect:/productos";
    }

    // GET - Home
    @GetMapping("/")
    public String home() {
        return "redirect:/productos";
    }
}
