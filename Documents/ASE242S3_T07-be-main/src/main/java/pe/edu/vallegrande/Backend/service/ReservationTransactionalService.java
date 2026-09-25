package pe.edu.vallegrande.Backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.vallegrande.Backend.model.Reservation;
import pe.edu.vallegrande.Backend.model.ReservationDetail;
import pe.edu.vallegrande.Backend.repository.ReservationRepository;
import pe.edu.vallegrande.Backend.repository.ReservationDetailRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationTransactionalService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationDetailRepository reservationDetailRepository;

    /**
     * ✅ TRANSACCIONAL: Crea una reservación con sus detalles en una sola transacción
     */
    @Transactional
    public Reservation crearReservacionConDetalles(Reservation reservacion, List<ReservationDetail> detalles) {
        try {
            System.out.println("🎯 === INICIANDO TRANSACCIÓN RESERVACIÓN CON DETALLES ===");
            
            // 1. Configurar auditoría de la reservación
            reservacion.setFechaCreacion(LocalDateTime.now());
            reservacion.setCreatedAt(LocalDateTime.now());
            reservacion.setStatus("A");
            
            // 2. Validar disponibilidad de mesa
            if (reservationRepository.existsByTableIdAndReservationDatetime(
                reservacion.getTableId(), reservacion.getReservationDatetime())) {
                throw new IllegalArgumentException("La mesa ya está reservada para esta fecha y hora");
            }
            
            // 3. Guardar la reservación (cabecera)
            Reservation reservacionGuardada = reservationRepository.save(reservacion);
            System.out.println("✅ Reservación guardada - ID: " + reservacionGuardada.getIdReservation());
            
            // 4. Calcular y actualizar el precio total
            BigDecimal totalPrice = BigDecimal.ZERO;
            
            // 5. Guardar los detalles
            for (ReservationDetail detalle : detalles) {
                // Asignar el ID de la reservación guardada
                detalle.setIdReservation(reservacionGuardada.getIdReservation());
                detalle.setStatus("A");
                
                // Guardar el detalle
                ReservationDetail detalleGuardado = reservationDetailRepository.save(detalle);
                System.out.println("✅ Detalle de reservación guardado - ID: " + detalleGuardado.getIdReservationDetail() + 
                                 ", Mesa: " + detalleGuardado.getMesaNum());
            }
            
            // 6. Si hay lógica de precio por mesas, calcular total aquí
            // Por ejemplo: totalPrice = calcularPrecioPorMesas(detalles);
            
            System.out.println("🎉 === TRANSACCIÓN RESERVACIÓN COMPLETADA EXITOSAMENTE ===");
            
            return reservacionGuardada;
            
        } catch (Exception e) {
            System.out.println("❌ === ERROR EN TRANSACCIÓN RESERVACIÓN ===");
            System.out.println("💥 Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error transaccional al crear reservación con detalles: " + e.getMessage(), e);
        }
    }

    /**
     * ✅ TRANSACCIONAL: Actualiza reservación y sus detalles en una sola operación
     */
    @Transactional
    public Reservation actualizarReservacionConDetalles(Long idReservacion, Reservation reservacionActualizada, List<ReservationDetail> nuevosDetalles) {
        try {
            System.out.println("🎯 === INICIANDO ACTUALIZACIÓN TRANSACCIONAL RESERVACIÓN ===");
            
            // 1. Verificar que la reservación existe
            Reservation reservacionExistente = reservationRepository.findById(idReservacion)
                    .orElseThrow(() -> new IllegalArgumentException("Reservación no encontrada con ID: " + idReservacion));
            
            // 2. Validar disponibilidad si se cambia la mesa o fecha
            if ((reservacionActualizada.getTableId() != null && 
                 !reservacionActualizada.getTableId().equals(reservacionExistente.getTableId())) ||
                (reservacionActualizada.getReservationDatetime() != null && 
                 !reservacionActualizada.getReservationDatetime().equals(reservacionExistente.getReservationDatetime()))) {
                     
                if (reservationRepository.existsByTableIdAndReservationDatetime(
                    reservacionActualizada.getTableId(), reservacionActualizada.getReservationDatetime())) {
                    throw new IllegalArgumentException("La mesa ya está reservada para esta fecha y hora");
                }
            }
            
            // 3. Actualizar campos de la reservación
            if (reservacionActualizada.getTableId() != null) {
                reservacionExistente.setTableId(reservacionActualizada.getTableId());
            }
            if (reservacionActualizada.getReservationDatetime() != null) {
                reservacionExistente.setReservationDatetime(reservacionActualizada.getReservationDatetime());
            }
            if (reservacionActualizada.getNumberOfGuests() != null) {
                reservacionExistente.setNumberOfGuests(reservacionActualizada.getNumberOfGuests());
            }
            if (reservacionActualizada.getLocation() != null) {
                reservacionExistente.setLocation(reservacionActualizada.getLocation());
            }
            if (reservacionActualizada.getPaymentMethod() != null) {
                reservacionExistente.setPaymentMethod(reservacionActualizada.getPaymentMethod());
            }
            if (reservacionActualizada.getReservationStatus() != null) {
                reservacionExistente.setReservationStatus(reservacionActualizada.getReservationStatus());
            }
            if (reservacionActualizada.getTotalPrice() != null) {
                reservacionExistente.setTotalPrice(reservacionActualizada.getTotalPrice());
            }
            if (reservacionActualizada.getCustomersIdCustomer() != null) {
                reservacionExistente.setCustomersIdCustomer(reservacionActualizada.getCustomersIdCustomer());
            }
            
            // 4. Actualizar auditoría de edición
            reservacionExistente.setFechaEdicion(LocalDateTime.now());
            
            // 5. Eliminar detalles existentes
            List<ReservationDetail> detallesExistentes = reservationDetailRepository.findByIdReservation(idReservacion);
            reservationDetailRepository.deleteAll(detallesExistentes);
            System.out.println("🗑️ Detalles de reservación existentes eliminados: " + detallesExistentes.size());
            
            // 6. Guardar nuevos detalles
            for (ReservationDetail detalle : nuevosDetalles) {
                detalle.setIdReservation(idReservacion);
                detalle.setStatus("A");
                reservationDetailRepository.save(detalle);
            }
            
            // 7. Guardar reservación actualizada
            Reservation reservacionFinal = reservationRepository.save(reservacionExistente);
            
            System.out.println("🎉 === ACTUALIZACIÓN TRANSACCIONAL RESERVACIÓN COMPLETADA ===");
            
            return reservacionFinal;
            
        } catch (Exception e) {
            System.out.println("❌ === ERROR EN ACTUALIZACIÓN TRANSACCIONAL RESERVACIÓN ===");
            throw new RuntimeException("Error al actualizar reservación con detalles: " + e.getMessage(), e);
        }
    }
}