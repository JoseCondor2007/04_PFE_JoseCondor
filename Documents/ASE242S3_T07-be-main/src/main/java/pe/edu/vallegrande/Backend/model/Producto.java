package pe.edu.vallegrande.Backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El código de producto es obligatorio")
    @Size(max = 20, message = "El código no puede exceder 20 caracteres")
    @Column(name = "product_code", nullable = false, length = 20, unique = true)
    private String productCode;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    @Column(name = "description", length = 500)
    private String description;
    
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @NotBlank(message = "La categoría es obligatoria")
    @Size(max = 50, message = "La categoría no puede exceder 50 caracteres")
    @Column(name = "category", nullable = false, length = 50)
    private String category;
    
    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVO";
    
    // CAMPOS DE AUDITORÍA (coinciden con BD) - AGREGAR @JsonFormat
    @Column(name = "fecha_creacion", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_edicion")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaEdicion;
    
    @Column(name = "fecha_eliminacion_logica")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaEliminacionLogica;
    
    @Column(name = "fecha_restauracion_logica")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaRestauracionLogica;
    
    // Constructores
    public Producto() {
    }
    
    public Producto(String productCode, String name, String description, BigDecimal price, String category) {
        this.productCode = productCode;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.status = "ACTIVO";
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getProductCode() {
        return productCode;
    }
    
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getFechaEdicion() {
        return fechaEdicion;
    }
    
    public void setFechaEdicion(LocalDateTime fechaEdicion) {
        this.fechaEdicion = fechaEdicion;
    }
    
    public LocalDateTime getFechaEliminacionLogica() {
        return fechaEliminacionLogica;
    }
    
    public void setFechaEliminacionLogica(LocalDateTime fechaEliminacionLogica) {
        this.fechaEliminacionLogica = fechaEliminacionLogica;
    }
    
    public LocalDateTime getFechaRestauracionLogica() {
        return fechaRestauracionLogica;
    }
    
    public void setFechaRestauracionLogica(LocalDateTime fechaRestauracionLogica) {
        this.fechaRestauracionLogica = fechaRestauracionLogica;
    }
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        // NO establecer fechaEdicion aquí - solo se establece al editar
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaEdicion = LocalDateTime.now(); // Solo se actualiza cuando hay una edición real
    }
    
    // Método para marcar eliminación lógica
    public void marcarComoEliminado() {
        this.status = "ELIMINADO";
        this.fechaEliminacionLogica = LocalDateTime.now();
        this.fechaEdicion = LocalDateTime.now(); // También actualiza fecha de edición
    }
    
    // Método para marcar restauración
    public void marcarComoRestaurado() {
        this.status = "ACTIVO";
        this.fechaRestauracionLogica = LocalDateTime.now();
        this.fechaEdicion = LocalDateTime.now(); // También actualiza fecha de edición
    }
    
    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", productCode='" + productCode + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", status='" + status + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaEdicion=" + fechaEdicion +
                ", fechaEliminacionLogica=" + fechaEliminacionLogica +
                ", fechaRestauracionLogica=" + fechaRestauracionLogica +
                '}';
    }
}