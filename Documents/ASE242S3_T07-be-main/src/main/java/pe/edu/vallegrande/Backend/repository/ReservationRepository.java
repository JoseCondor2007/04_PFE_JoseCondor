package pe.edu.vallegrande.Backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.Backend.model.Reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    // Buscar reservaciones por estado
    List<Reservation> findByStatus(String status);
    
    // Buscar reservaciones por estado de reservación
    List<Reservation> findByReservationStatus(String reservationStatus);
    
    // Buscar reservaciones por cliente
    List<Reservation> findByCustomersIdCustomer(Integer customersIdCustomer);
    
    // Buscar reservaciones por mesa
    List<Reservation> findByTableId(Integer tableId);
    
    // Buscar reservaciones por fecha y hora exacta
    List<Reservation> findByReservationDatetime(LocalDateTime reservationDatetime);
    
    // Buscar reservaciones por rango de fechas
    @Query("SELECT r FROM Reservation r WHERE r.reservationDatetime BETWEEN :startDate AND :endDate AND r.status = :status")
    List<Reservation> findByReservationDatetimeBetweenAndStatus(@Param("startDate") LocalDateTime startDate, 
                                                               @Param("endDate") LocalDateTime endDate, 
                                                               @Param("status") String status);
    
    // Buscar reservaciones por estado de pago
    List<Reservation> findByPaid(Boolean paid);
    
    // Contar reservaciones por estado
    long countByStatus(String status);
    
    // Contar reservaciones por estado de reservación
    long countByReservationStatus(String reservationStatus);
    
    // Desactivar reservación (cambiar estado a 'I')
    @Modifying
    @Query("UPDATE Reservation r SET r.status = 'I' WHERE r.idReservation = :id")
    void desactivarReservation(@Param("id") Long id);
    
    // Activar reservación (cambiar estado a 'A')
    @Modifying
    @Query("UPDATE Reservation r SET r.status = 'A' WHERE r.idReservation = :id")
    void activarReservation(@Param("id") Long id);
    
    // Actualizar estado de reservación
    @Modifying
    @Query("UPDATE Reservation r SET r.reservationStatus = :reservationStatus WHERE r.idReservation = :id")
    void actualizarReservationStatus(@Param("id") Long id, @Param("reservationStatus") String reservationStatus);
    
    // Actualizar estado de pago
    @Modifying
    @Query("UPDATE Reservation r SET r.paid = :paid WHERE r.idReservation = :id")
    void actualizarEstadoPago(@Param("id") Long id, @Param("paid") Boolean paid);
    
    // Verificar si existe reservación para mesa en fecha y hora específica
    @Query("SELECT COUNT(r) > 0 FROM Reservation r WHERE r.tableId = :tableId AND r.reservationDatetime = :fechaHora AND r.status = 'A'")
    boolean existsByTableIdAndReservationDatetime(@Param("tableId") Integer tableId, @Param("fechaHora") LocalDateTime fechaHora);
    
    // Método adicional: Buscar por rango de fechas sin filtro de estado
    List<Reservation> findByReservationDatetimeBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Método adicional: Buscar reservaciones activas por cliente
    List<Reservation> findByCustomersIdCustomerAndStatus(Integer customersIdCustomer, String status);
    
    // Método adicional: Buscar reservaciones activas por mesa
    List<Reservation> findByTableIdAndStatus(Integer tableId, String status);
    
    // Método adicional: Buscar reservaciones por estado de reservación y estado activo
    List<Reservation> findByReservationStatusAndStatus(String reservationStatus, String status);
    
    // Método adicional: Buscar reservaciones futuras
    @Query("SELECT r FROM Reservation r WHERE r.reservationDatetime >= :now AND r.status = 'A' ORDER BY r.reservationDatetime ASC")
    List<Reservation> findUpcomingReservations(@Param("now") LocalDateTime now);
    
    // Método adicional: Buscar reservaciones pasadas
    @Query("SELECT r FROM Reservation r WHERE r.reservationDatetime < :now AND r.status = 'A' ORDER BY r.reservationDatetime DESC")
    List<Reservation> findPastReservations(@Param("now") LocalDateTime now);
    
    // Método adicional: Verificar disponibilidad en rango de tiempo
    @Query("SELECT COUNT(r) > 0 FROM Reservation r WHERE r.tableId = :tableId AND r.status = 'A' AND r.reservationDatetime BETWEEN :startTime AND :endTime")
    boolean existsByTableIdAndTimeRange(@Param("tableId") Integer tableId, 
                                       @Param("startTime") LocalDateTime startTime, 
                                       @Param("endTime") LocalDateTime endTime);
}