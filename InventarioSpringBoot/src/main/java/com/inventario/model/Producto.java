package com.inventario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "productos")
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank(message = "El código es obligatorio")
    @Size(min = 3, max = 50, message = "El código debe tener entre 3 y 50 caracteres")
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "El código solo puede contener letras mayúsculas, números y guiones")
    @Column(nullable = false, length = 50, unique = true)
    private String codigo;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 5, max = 120, message = "El nombre debe tener entre 5 y 120 caracteres")
    @Column(nullable = false, length = 120)
    private String nombre;
    
    @NotBlank(message = "La categoría es obligatoria")
    @Column(length = 50)
    private String categoria;
    
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "1", message = "El precio debe ser mayor a 0")
    @DecimalMax(value = "999999999", message = "El precio no puede exceder $999.999.999")
    @Column(nullable = false)
    private Double precio;
    
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Max(value = 999999, message = "El stock no puede exceder 999,999")
    @Column(nullable = false)
    private Integer stock;
    
    @Column(nullable = false)
    private Boolean activo = true;
    
    // Constructores
    public Producto() {}
    
    public Producto(Integer id, String codigo, String nombre, String categoria, 
                   Double precio, Integer stock, Boolean activo) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
        this.activo = activo;
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public Double getPrecio() {
        return precio;
    }
    
    public void setPrecio(Double precio) {
        this.precio = precio;
    }
    
    public Integer getStock() {
        return stock;
    }
    
    public void setStock(Integer stock) {
        this.stock = stock;
    }
    
    public Boolean getActivo() {
        return activo;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", categoria='" + categoria + '\'' +
                ", precio=" + precio +
                ", stock=" + stock +
                ", activo=" + activo +
                '}';
    }
}
