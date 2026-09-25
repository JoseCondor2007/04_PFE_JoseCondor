package pe.edu.vallegrande.Backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.vallegrande.Backend.model.Reservation;
import pe.edu.vallegrande.Backend.model.ReservationDetail;
import pe.edu.vallegrande.Backend.repository.ReservationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    // Listar todas las reservaciones
    public List<Reservation> listarTodos() {
        return reservationRepository.findAll();
    }

    // Listar reservaciones por estado
    public List<Reservation> listarPorEstado(String estado) {
        return reservationRepository.findByStatus(estado);
    }

    // Buscar reservación por ID
    public Optional<Reservation> buscarPorId(Long id) {
        return reservationRepository.findById(id);
    }

    // Buscar reservaciones por cliente
    public List<Reservation> buscarPorCliente(Integer customersIdCustomer) {
        return reservationRepository.findByCustomersIdCustomer(customersIdCustomer);
    }

    // Buscar reservaciones por mesa
    public List<Reservation> buscarPorMesa(Integer tableId) {
        return reservationRepository.findByTableId(tableId);
    }

    // Buscar reservaciones por fecha - CORREGIDO
    public List<Reservation> buscarPorFecha(LocalDate fecha) {
        // Convertir LocalDate a LocalDateTime (buscar todo el día)
        LocalDateTime fechaInicio = fecha.atStartOfDay();
        LocalDateTime fechaFin = fecha.plusDays(1).atStartOfDay();
        
        return reservationRepository.findByReservationDatetimeBetween(fechaInicio, fechaFin);
    }

    // Buscar reservaciones por estado de reservación
    public List<Reservation> buscarPorEstadoReservacion(String reservationStatus) {
        return reservationRepository.findByReservationStatus(reservationStatus);
    }

    // Crear nueva reservación
    public Reservation crear(Reservation reservacion) {
        try {
            System.out.println("🎯 === INICIANDO CREACIÓN DE RESERVACIÓN ===");
            System.out.println("📦 Reservación recibida en servicio: " + reservacion);
            
            // Validaciones básicas
            if (reservacion.getTableId() == null) {
                throw new IllegalArgumentException("El ID de mesa es obligatorio");
            }
            
            if (reservacion.getReservationDatetime() == null) {
                throw new IllegalArgumentException("La fecha y hora de reservación son obligatorias");
            }
            
            if (reservacion.getNumberOfGuests() == null || reservacion.getNumberOfGuests() <= 0) {
                throw new IllegalArgumentException("El número de invitados debe ser mayor a 0");
            }
            
            if (reservacion.getLocation() == null || reservacion.getLocation().trim().isEmpty()) {
                throw new IllegalArgumentException("La ubicación es obligatoria");
            }
            
            if (reservacion.getCustomersIdCustomer() == null) {
                throw new IllegalArgumentException("El ID del cliente es obligatorio");
            }
            
            // Verificar disponibilidad de mesa
            if (reservationRepository.existsByTableIdAndReservationDatetime(reservacion.getTableId(), reservacion.getReservationDatetime())) {
                throw new IllegalArgumentException("La mesa ya está reservada para esta fecha y hora");
            }
            
            // Asegurar valores por defecto
            if (reservacion.getPaymentMethod() == null) {
                reservacion.setPaymentMethod("Cash");
            }
            if (reservacion.getReservationStatus() == null) {
                reservacion.setReservationStatus("Pending");
            }
            if (reservacion.getTotalPrice() == null) {
                reservacion.setTotalPrice(BigDecimal.ZERO);
            }
            if (reservacion.getPaid() == null) {
                reservacion.setPaid(false);
            }
            if (reservacion.getStatus() == null) {
                reservacion.setStatus("A");
            }
            
            // ✅ AUDITORÍA: Establecer fecha de creación
            reservacion.setFechaCreacion(LocalDateTime.now());
            
            System.out.println("💾 Guardando reservación: " + reservacion);
            
            Reservation guardada = reservationRepository.save(reservacion);
            System.out.println("✅ === RESERVACIÓN CREADA EXITOSAMENTE ===");
            System.out.println("📋 Reservación guardada: " + guardada);
            
            return guardada;
            
        } catch (DataIntegrityViolationException e) {
            System.out.println("❌ === ERROR DE INTEGRIDAD DE DATOS ===");
            System.out.println("💥 Mensaje completo: " + e.getMessage());
            System.out.println("🔍 Causa raíz: " + e.getRootCause());
            e.printStackTrace();
            throw new IllegalArgumentException("Error de integridad de datos: " + e.getRootCause().getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error de validación: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("❌ === ERROR GENERAL ===");
            System.out.println("💥 Tipo de error: " + e.getClass().getName());
            System.out.println("📝 Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw new IllegalArgumentException("Error al crear reservación: " + e.getMessage());
        }
    }

    // Actualizar reservación existente
    public Reservation actualizar(Long id, Reservation reservacionActualizada) {
        Optional<Reservation> reservacionExistente = reservationRepository.findById(id);

        if (reservacionExistente.isPresent()) {
            Reservation reservacion = reservacionExistente.get();

            System.out.println("🔄 Actualizando reservación ID: " + id);
            System.out.println("📦 Datos actuales: " + reservacion);
            System.out.println("📦 Nuevos datos: " + reservacionActualizada);

            // Actualizar campos
            if (reservacionActualizada.getTableId() != null) {
                reservacion.setTableId(reservacionActualizada.getTableId());
            }
            if (reservacionActualizada.getReservationDatetime() != null) {
                reservacion.setReservationDatetime(reservacionActualizada.getReservationDatetime());
            }
            if (reservacionActualizada.getNumberOfGuests() != null) {
                reservacion.setNumberOfGuests(reservacionActualizada.getNumberOfGuests());
            }
            if (reservacionActualizada.getLocation() != null) {
                reservacion.setLocation(reservacionActualizada.getLocation());
            }
            if (reservacionActualizada.getPaymentMethod() != null) {
                reservacion.setPaymentMethod(reservacionActualizada.getPaymentMethod());
            }
            if (reservacionActualizada.getReservationStatus() != null) {
                reservacion.setReservationStatus(reservacionActualizada.getReservationStatus());
            }
            if (reservacionActualizada.getTotalPrice() != null) {
                reservacion.setTotalPrice(reservacionActualizada.getTotalPrice());
            }
            if (reservacionActualizada.getPaid() != null) {
                reservacion.setPaid(reservacionActualizada.getPaid());
            }
            if (reservacionActualizada.getCustomersIdCustomer() != null) {
                reservacion.setCustomersIdCustomer(reservacionActualizada.getCustomersIdCustomer());
            }

            // ✅ AUDITORÍA: Establecer fecha de edición
            reservacion.setFechaEdicion(LocalDateTime.now());

            Reservation actualizada = reservationRepository.save(reservacion);
            System.out.println("✅ Reservación actualizada: " + actualizada);
            return actualizada;
        } else {
            throw new IllegalArgumentException("Reservación no encontrada con ID: " + id);
        }
    }

    // Eliminación lógica
    @Transactional
    public void eliminarLogicamente(Long id) {
        Optional<Reservation> reservacion = reservationRepository.findById(id);

        if (reservacion.isPresent()) {
            System.out.println("🗑️ Eliminando lógicamente reservación ID: " + id);
            
            // ✅ AUDITORÍA: Establecer fecha de eliminación lógica
            Reservation reservacionEntity = reservacion.get();
            reservacionEntity.setFechaEliminacionLogica(LocalDateTime.now());
            reservationRepository.save(reservacionEntity);
            
            reservationRepository.desactivarReservation(id);
            System.out.println("✅ Reservación eliminada lógicamente - Fecha: " + LocalDateTime.now());
        } else {
            throw new IllegalArgumentException("Reservación no encontrada con ID: " + id);
        }
    }

    // Restauración lógica
    @Transactional
    public void restaurarLogicamente(Long id) {
        Optional<Reservation> reservacion = reservationRepository.findById(id);

        if (reservacion.isPresent()) {
            System.out.println("🔄 Restaurando reservación ID: " + id);
            
            // ✅ AUDITORÍA: Establecer fecha de restauración lógica
            Reservation reservacionEntity = reservacion.get();
            reservacionEntity.setFechaRestauracionLogica(LocalDateTime.now());
            reservationRepository.save(reservacionEntity);
            
            reservationRepository.activarReservation(id);
            System.out.println("✅ Reservación restaurada - Fecha: " + LocalDateTime.now());
        } else {
            throw new IllegalArgumentException("Reservación no encontrada con ID: " + id);
        }
    }

    // Actualizar estado de reservación
    @Transactional
    public void actualizarEstadoReservacion(Long id, String nuevoEstado) {
        Optional<Reservation> reservacion = reservationRepository.findById(id);

        if (reservacion.isPresent()) {
            System.out.println("🔄 Actualizando estado de reservación ID: " + id + " a: " + nuevoEstado);
            
            // ✅ AUDITORÍA: Establecer fecha de edición
            Reservation reservacionEntity = reservacion.get();
            reservacionEntity.setFechaEdicion(LocalDateTime.now());
            reservationRepository.save(reservacionEntity);
            
            reservationRepository.actualizarReservationStatus(id, nuevoEstado);
            System.out.println("✅ Estado de reservación actualizado");
        } else {
            throw new IllegalArgumentException("Reservación no encontrada con ID: " + id);
        }
    }

    // Actualizar estado de pago
    @Transactional
    public void actualizarEstadoPago(Long id, Boolean pagado) {
        Optional<Reservation> reservacion = reservationRepository.findById(id);

        if (reservacion.isPresent()) {
            System.out.println("💰 Actualizando estado de pago ID: " + id + " a: " + pagado);
            
            // ✅ AUDITORÍA: Establecer fecha de edición
            Reservation reservacionEntity = reservacion.get();
            reservacionEntity.setFechaEdicion(LocalDateTime.now());
            reservationRepository.save(reservacionEntity);
            
            reservationRepository.actualizarEstadoPago(id, pagado);
            System.out.println("✅ Estado de pago actualizado");
        } else {
            throw new IllegalArgumentException("Reservación no encontrada con ID: " + id);
        }
    }

    // Listar reservaciones activas
    public List<Reservation> listarActivas() {
        return reservationRepository.findByStatus("A");
    }

    // Listar reservaciones inactivas
    public List<Reservation> listarInactivas() {
        return reservationRepository.findByStatus("I");
    }

    // Obtener estadísticas
    public String obtenerEstadisticas() {
        long total = reservationRepository.count();
        long activas = reservationRepository.countByStatus("A");
        long inactivas = reservationRepository.countByStatus("I");
        long pendientes = reservationRepository.countByReservationStatus("Pending");
        long confirmadas = reservationRepository.countByReservationStatus("Confirmed");
        
        return String.format("Total: %d | Activas: %d | Inactivas: %d | Pendientes: %d | Confirmadas: %d", 
                           total, activas, inactivas, pendientes, confirmadas);
    }
}