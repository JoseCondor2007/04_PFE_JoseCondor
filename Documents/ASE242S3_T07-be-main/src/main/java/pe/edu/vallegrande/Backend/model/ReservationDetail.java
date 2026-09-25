package pe.edu.vallegrande.Backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "Reservation_Detail")
public class ReservationDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ✅ CORREGIDO: GeneratedValue
    @Column(name = "id_reservation_detail")
    private Long idReservationDetail; // ✅ Usar Long en lugar de long
    
    @NotNull(message = "El ID de reservación es obligatorio")
    @Column(name = "id_reservation", nullable = false)
    private Long idReservation; // ✅ Usar Long en lugar de long
    
    @NotNull(message = "El número de mesa es obligatorio")
    @Min(value = 1, message = "El número de mesa debe ser mayor a 0")
    @Column(name = "mesa_num", nullable = false)
    private Integer mesaNum;
    
    @Column(name = "status", length = 1)
    private String status = "A";
    
    @Column(name = "created_at")
    private LocalDate createdAt;
    
    // Constructores
    public ReservationDetail() {
    }
    
    public ReservationDetail(Long idReservation, Integer mesaNum) {
        this.idReservation = idReservation;
        this.mesaNum = mesaNum;
    }
    
    // Getters y Setters
    public Long getIdReservationDetail() {
        return idReservationDetail;
    }
    
    public void setIdReservationDetail(Long idReservationDetail) {
        this.idReservationDetail = idReservationDetail;
    }
    
    public Long getIdReservation() {
        return idReservation;
    }
    
    public void setIdReservation(Long idReservation) {
        this.idReservation = idReservation;
    }
    
    public Integer getMesaNum() {
        return mesaNum;
    }
    
    public void setMesaNum(Integer mesaNum) {
        this.mesaNum = mesaNum;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDate getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDate.now();
        }
    }
    
    @Override
    public String toString() {
        return "ReservationDetail{" +
                "idReservationDetail=" + idReservationDetail +
                ", idReservation=" + idReservation +
                ", mesaNum=" + mesaNum +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}