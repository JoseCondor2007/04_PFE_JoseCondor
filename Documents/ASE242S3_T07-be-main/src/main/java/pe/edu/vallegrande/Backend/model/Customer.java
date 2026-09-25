package pe.edu.vallegrande.Backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "Customers",
    uniqueConstraints = {
        @UniqueConstraint(name = "UQ_Customers_document", columnNames = "document_number"),
        @UniqueConstraint(name = "UQ_Customers_email", columnNames = "email")
    }
)
public class Customer {

    // =================== CAMPOS ===================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_customer")
    private Integer idCustomer;

    @NotBlank
    @Size(max = 100)
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotBlank
    @Size(max = 100)
    @Column(name = "lastname", nullable = false, length = 100)
    private String lastname;

    @NotBlank
    @Size(max = 10)
    @Column(name = "documento_identificac", nullable = false, length = 10)
    private String documentoIdentificac;

    @NotBlank
    @Size(max = 15)
    @Column(name = "document_number", nullable = false, length = 15)
    private String documentNumber;

    @Email
    @NotBlank
    @Size(max = 255)
    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @NotBlank
    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener 9 dígitos")
    @Column(name = "phone", nullable = false, length = 9)
    private String phone;

    @NotBlank
    @Pattern(regexp = "[AI]", message = "El estado debe ser A o I")
    @Column(name = "status", nullable = false, length = 1)
    private String status = "A";

    @NotBlank
    @Size(max = 255)
    @Column(name = "location", nullable = false, length = 255)
    private String location;

    @Transient
    private Boolean active;

    // === CAMPOS DE AUDITORÍA (todos LocalDateTime) ===
    @Column(name = "fecha_creacion", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaCreacion;                    // ← Cambiado a LocalDateTime

    @Column(name = "fecha_edicion")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaEdicion;

    @Column(name = "fecha_eliminacion_logica")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaEliminacionLogica;

    @Column(name = "fecha_restauracion_logica")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaRestauracionLogica;

    // =================== CONSTRUCTOR PERSONALIZADO ===================
    public Customer(String name, String lastname, String documentoIdentificac, String documentNumber,
                    String email, String phone, String status, String location) {
        this.name = name;
        this.lastname = lastname;
        this.documentoIdentificac = documentoIdentificac;
        this.documentNumber = documentNumber;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.location = location;
        this.fechaCreacion = LocalDateTime.now();           // ← Cambiado a LocalDateTime.now()
    }

    // =================== MÉTODOS ===================
    @PrePersist
    public void prePersist() {
        if (documentoIdentificac == null || documentoIdentificac.isBlank()) {
            documentoIdentificac = "DNI";
        }
        if (status == null || status.isBlank()) {
            status = "A";
        }
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();            // ← Cambiado a LocalDateTime.now()
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaEdicion = LocalDateTime.now();
    }

    public Boolean getActive() {
        return "A".equals(status);
    }

    public void setActive(Boolean active) {
        this.active = active;
        if (active != null) {
            this.status = active ? "A" : "I";
        }
    }

    // Método para marcar eliminación lógica
    public void marcarComoEliminado() {
        this.status = "I";
        this.fechaEliminacionLogica = LocalDateTime.now();
        this.fechaEdicion = LocalDateTime.now();
    }

    // Método para marcar restauración
    public void marcarComoRestaurado() {
        this.status = "A";
        this.fechaRestauracionLogica = LocalDateTime.now();
        this.fechaEdicion = LocalDateTime.now();
    }

    // =================== TO STRING ===================
    @Override
    public String toString() {
        return "Customer{" +
                "idCustomer=" + idCustomer +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", documentoIdentificac='" + documentoIdentificac + '\'' +
                ", documentNumber='" + documentNumber + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", status='" + status + '\'' +
                ", location='" + location + '\'' +
                ", active=" + active +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaEdicion=" + fechaEdicion +
                ", fechaEliminacionLogica=" + fechaEliminacionLogica +
                ", fechaRestauracionLogica=" + fechaRestauracionLogica +
                '}';
    }
}