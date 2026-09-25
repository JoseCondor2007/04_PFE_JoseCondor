package pe.edu.vallegrande.Backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Reservations")
public class Reservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservation")
    private Long idReservation;
    
    @NotNull(message = "El ID de mesa es obligatorio")
    @Column(name = "table_id", nullable = false)
    private Integer tableId;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "La fecha y hora de reservación son obligatorias")
    @Column(name = "reservation_datetime", nullable = false)
    private LocalDateTime reservationDatetime;
    
    @NotNull(message = "El número de invitados es obligatorio")
    @Min(value = 1, message = "El número de invitados debe ser mayor a 0")
    @Column(name = "number_of_guests", nullable = false)
    private Integer numberOfGuests;
    
    @NotBlank(message = "La ubicación es obligatoria")
    @Size(max = 255, message = "La ubicación no puede exceder 255 caracteres")
    @Column(name = "location", nullable = false, length = 255)
    private String location;
    
    @Size(max = 20, message = "El método de pago no puede exceder 20 caracteres")
    @Column(name = "payment_method", length = 20)
    private String paymentMethod = "Cash";
    
    @Size(max = 20, message = "El estado de reservación no puede exceder 20 caracteres")
    @Column(name = "reservation_status", length = 20)
    private String reservationStatus = "Pending";
    
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio total no puede ser negativo")
    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice = BigDecimal.ZERO;
    
    @Column(name = "paid")
    private Boolean paid = false;
    
    @Column(name = "status", length = 1)
    private String status = "A";
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @NotNull(message = "El ID del cliente es obligatorio")
    @Column(name = "Customers_id_customer", nullable = false)
    private Integer customersIdCustomer;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reservation")
    private List<ReservationDetail> reservationDetails;
    
    // ✅ CAMPOS DE AUDITORÍA IMPLEMENTADOS
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fecha_edicion")
    private LocalDateTime fechaEdicion;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fecha_eliminacion_logica")
    private LocalDateTime fechaEliminacionLogica;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fecha_restauracion_logica")
    private LocalDateTime fechaRestauracionLogica;
    
    // Constructores
    public Reservation() {
    }
    
    public Reservation(Integer tableId, LocalDateTime reservationDatetime, Integer numberOfGuests, 
                      String location, Integer customersIdCustomer) {
        this.tableId = tableId;
        this.reservationDatetime = reservationDatetime;
        this.numberOfGuests = numberOfGuests;
        this.location = location;
        this.customersIdCustomer = customersIdCustomer;
    }
    
    // Getters y Setters existentes...
    public Long getIdReservation() {
        return idReservation;
    }
    
    public void setIdReservation(Long idReservation) {
        this.idReservation = idReservation;
    }
    
    public Integer getTableId() {
        return tableId;
    }
    
    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }
    
    public LocalDateTime getReservationDatetime() {
        return reservationDatetime;
    }
    
    public void setReservationDatetime(LocalDateTime reservationDatetime) {
        this.reservationDatetime = reservationDatetime;
    }
    
    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
    
    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getReservationStatus() {
        return reservationStatus;
    }
    
    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }
    
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public Boolean getPaid() {
        return paid;
    }
    
    public void setPaid(Boolean paid) {
        this.paid = paid;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public Integer getCustomersIdCustomer() {
        return customersIdCustomer;
    }
    
    public void setCustomersIdCustomer(Integer customersIdCustomer) {
        this.customersIdCustomer = customersIdCustomer;
    }
    
    public List<ReservationDetail> getReservationDetails() {
        return reservationDetails;
    }
    
    public void setReservationDetails(List<ReservationDetail> reservationDetails) {
        this.reservationDetails = reservationDetails;
    }
    
    // ✅ Getters y Setters para campos de auditoría
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
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaEdicion = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "Reservation{" +
                "idReservation=" + idReservation +
                ", tableId=" + tableId +
                ", reservationDatetime=" + reservationDatetime +
                ", numberOfGuests=" + numberOfGuests +
                ", location='" + location + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", reservationStatus='" + reservationStatus + '\'' +
                ", totalPrice=" + totalPrice +
                ", paid=" + paid +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", customersIdCustomer=" + customersIdCustomer +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaEdicion=" + fechaEdicion +
                ", fechaEliminacionLogica=" + fechaEliminacionLogica +
                ", fechaRestauracionLogica=" + fechaRestauracionLogica +
                '}';
    }
}