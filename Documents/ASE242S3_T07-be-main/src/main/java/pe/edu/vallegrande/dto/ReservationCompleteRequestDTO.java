package pe.edu.vallegrande.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pe.edu.vallegrande.Backend.model.ReservationDetail;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ReservationCompleteRequestDTO {
    
    @NotNull(message = "El ID de mesa es obligatorio")
    private Integer tableId;
    
    @NotNull(message = "La fecha y hora de reservación son obligatorias")
    private LocalDateTime reservationDatetime;
    
    @NotNull(message = "El número de invitados es obligatorio")
    private Integer numberOfGuests;
    
    @NotBlank(message = "La ubicación es obligatoria")
    @Size(max = 255, message = "La ubicación no puede exceder 255 caracteres")
    private String location;
    
    @NotNull(message = "El ID del cliente es obligatorio")
    private Integer customersIdCustomer;
    
    private String paymentMethod = "Cash";
    private String reservationStatus = "Pending";
    private BigDecimal totalPrice = BigDecimal.ZERO;
    private Boolean paid = false;
    
    @Valid
    @NotNull(message = "Los detalles de la reservación son obligatorios")
    @Size(min = 1, message = "Debe haber al menos un detalle de reservación")
    private List<ReservationDetail> reservationDetails;

    // Constructores
    public ReservationCompleteRequestDTO() {
    }

    public ReservationCompleteRequestDTO(Integer tableId, LocalDateTime reservationDatetime, 
                                        Integer numberOfGuests, String location, 
                                        Integer customersIdCustomer, List<ReservationDetail> reservationDetails) {
        this.tableId = tableId;
        this.reservationDatetime = reservationDatetime;
        this.numberOfGuests = numberOfGuests;
        this.location = location;
        this.customersIdCustomer = customersIdCustomer;
        this.reservationDetails = reservationDetails;
    }

    // Getters y Setters
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

    public Integer getCustomersIdCustomer() {
        return customersIdCustomer;
    }

    public void setCustomersIdCustomer(Integer customersIdCustomer) {
        this.customersIdCustomer = customersIdCustomer;
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

    public List<ReservationDetail> getReservationDetails() {
        return reservationDetails;
    }

    public void setReservationDetails(List<ReservationDetail> reservationDetails) {
        this.reservationDetails = reservationDetails;
    }

    @Override
    public String toString() {
        return "ReservationCompleteRequestDTO{" +
                "tableId=" + tableId +
                ", reservationDatetime=" + reservationDatetime +
                ", numberOfGuests=" + numberOfGuests +
                ", location='" + location + '\'' +
                ", customersIdCustomer=" + customersIdCustomer +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", reservationStatus='" + reservationStatus + '\'' +
                ", totalPrice=" + totalPrice +
                ", paid=" + paid +
                ", reservationDetails=" + reservationDetails +
                '}';
    }
}