package pe.edu.vallegrande.Backend.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import pe.edu.vallegrande.dto.ReservationCompleteRequestDTO;
import pe.edu.vallegrande.Backend.model.Reservation;
import pe.edu.vallegrande.Backend.service.ReservationService;
import pe.edu.vallegrande.Backend.service.ReservationTransactionalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/reservations")
@Tag(name = "Reservaciones", description = "Operaciones de gestión de reservaciones del restaurante La sazón de Panchita")
public class ReservationRest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTransactionalService reservationTransactionalService;

    // Listar todas las reservaciones
    @GetMapping
    @Operation(summary = "Obtener todas las reservaciones", description = "Recupera una lista completa de todas las reservaciones en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de reservaciones obtenida exitosamente.")
    public ResponseEntity<List<Reservation>> listarTodos() {
        List<Reservation> reservaciones = reservationService.listarTodos();
        return ResponseEntity.ok(reservaciones);
    }

    // Listar reservaciones por estado
    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener reservaciones por estado", description = "Filtra y devuelve reservaciones según su estado (ej: 'A', 'I').")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de reservaciones por estado obtenida exitosamente."),
        @ApiResponse(responseCode = "400", description = "Estado de reservación no válido.")
    })
    public ResponseEntity<List<Reservation>> listarPorEstado(@Parameter(description = "Estado de la reservación a filtrar (A, I)") @PathVariable String estado) {
        List<Reservation> reservaciones = reservationService.listarPorEstado(estado);
        return ResponseEntity.ok(reservaciones);
    }

    // Listar reservaciones por cliente
    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Obtener reservaciones por cliente", description = "Busca y devuelve una lista de reservaciones de un cliente específico.")
    @ApiResponse(responseCode = "200", description = "Lista de reservaciones por cliente obtenida exitosamente.")
    public ResponseEntity<List<Reservation>> buscarPorCliente(@Parameter(description = "ID del cliente") @PathVariable Integer clienteId) {
        List<Reservation> reservaciones = reservationService.buscarPorCliente(clienteId);
        return ResponseEntity.ok(reservaciones);
    }

    // Listar reservaciones por fecha
    @GetMapping("/fecha/{fecha}")
    @Operation(summary = "Obtener reservaciones por fecha", description = "Busca y devuelve una lista de reservaciones para una fecha específica.")
    @ApiResponse(responseCode = "200", description = "Lista de reservaciones por fecha obtenida exitosamente.")
    public ResponseEntity<List<Reservation>> buscarPorFecha(@Parameter(description = "Fecha de reservación (YYYY-MM-DD)") @PathVariable String fecha) {
        try {
            LocalDate fechaReservacion = LocalDate.parse(fecha);
            List<Reservation> reservaciones = reservationService.buscarPorFecha(fechaReservacion);
            return ResponseEntity.ok(reservaciones);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Buscar reservación por ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener una reservación por su ID", description = "Busca y devuelve los detalles de una reservación usando su identificador único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reservación encontrada exitosamente."),
        @ApiResponse(responseCode = "404", description = "Reservación no encontrada.")
    })
    public ResponseEntity<Reservation> buscarPorId(@Parameter(description = "ID de la reservación a buscar") @PathVariable Long id) {
        Optional<Reservation> reservacion = reservationService.buscarPorId(id);

        if (reservacion.isPresent()) {
            return ResponseEntity.ok(reservacion.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear nueva reservación (solo cabecera)
    @PostMapping
    @Operation(summary = "Crear una nueva reservación", description = "Crea una nueva reservación en el sistema con los datos proporcionados en el cuerpo de la solicitud.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reservación creada exitosamente.", content = @Content(schema = @Schema(implementation = Reservation.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida. Los datos son incorrectos."),
        @ApiResponse(responseCode = "500", description = "Ocurrió un error interno del servidor.")
    })
    public ResponseEntity<?> crear(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Objeto 'Reservation' a crear.") @RequestBody Reservation reservacion) {
        try {
            System.out.println("=== REST CONTROLLER - CREAR RESERVACIÓN ===");
            System.out.println("Request recibido: " + reservacion);
            
            Reservation nuevaReservacion = reservationService.crear(reservacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReservacion);
            
        } catch (IllegalArgumentException e) {
            System.out.println("=== ERROR DE VALIDACIÓN EN CONTROLLER ===");
            System.out.println("Mensaje de error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("=== ERROR INTERNO EN CONTROLLER ===");
            System.out.println("Tipo de error: " + e.getClass().getName());
            System.out.println("Mensaje: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocurrió un error interno al crear la reservación: " + e.getMessage());
        }
    }

    // ✅ CREAR RESERVACIÓN COMPLETA CON DETALLES (TRANSACCIONAL)
    @PostMapping("/completa")
    @Operation(summary = "Crear reservación completa con detalles", description = "Crea una reservación completa con cabecera y detalles de mesas en una sola transacción")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reservación completa creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida o mesa no disponible")
    })
    public ResponseEntity<?> crearReservacionCompleta(@RequestBody ReservationCompleteRequestDTO reservationRequest) {
        try {
            System.out.println("🎯 === CREANDO RESERVACIÓN COMPLETA TRANSACCIONAL ===");
            System.out.println("📦 Datos recibidos: " + reservationRequest);
            
            // Crear objeto Reservation desde el DTO
            Reservation reservacion = new Reservation();
            reservacion.setTableId(reservationRequest.getTableId());
            reservacion.setReservationDatetime(reservationRequest.getReservationDatetime());
            reservacion.setNumberOfGuests(reservationRequest.getNumberOfGuests());
            reservacion.setLocation(reservationRequest.getLocation());
            reservacion.setCustomersIdCustomer(reservationRequest.getCustomersIdCustomer());
            reservacion.setPaymentMethod(reservationRequest.getPaymentMethod());
            reservacion.setReservationStatus(reservationRequest.getReservationStatus());
            reservacion.setTotalPrice(reservationRequest.getTotalPrice());
            reservacion.setPaid(reservationRequest.getPaid());
            
            // Llamar al servicio transaccional
            Reservation reservacionCreada = reservationTransactionalService.crearReservacionConDetalles(
                reservacion, reservationRequest.getReservationDetails());
            
            System.out.println("✅ Reservación completa creada exitosamente - ID: " + reservacionCreada.getIdReservation());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(reservacionCreada);
            
        } catch (Exception e) {
            System.out.println("❌ Error en creación transaccional de reservación: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error al crear reservación completa: " + e.getMessage());
        }
    }

    // Actualizar reservación existente
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una reservación", description = "Actualiza la información de una reservación existente usando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reservación actualizada exitosamente."),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida. La reservación no fue encontrada o los datos son incorrectos.")
    })
    public ResponseEntity<?> actualizar(@Parameter(description = "ID de la reservación a actualizar") @PathVariable Long id, @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos de la reservación") @RequestBody Reservation reservacion) {
        try {
            Reservation reservacionActualizada = reservationService.actualizar(id, reservacion);
            return ResponseEntity.ok(reservacionActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Eliminación lógica
    @PatchMapping("/{id}/eliminar")
    @Operation(summary = "Eliminación lógica", description = "Cambia el estado de una reservación a 'I' sin borrarla de la base de datos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reservación eliminada lógicamente."),
        @ApiResponse(responseCode = "400", description = "Reservación no encontrada o ya eliminada.")
    })
    public ResponseEntity<?> eliminarLogicamente(@Parameter(description = "ID de la reservación a eliminar") @PathVariable Long id) {
        try {
            reservationService.eliminarLogicamente(id);
            return ResponseEntity.ok().body("Reservación eliminada lógicamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Restauración lógica
    @PatchMapping("/{id}/restaurar")
    @Operation(summary = "Restauración lógica", description = "Restaura una reservación que ha sido eliminada lógicamente, cambiando su estado de nuevo a 'A'.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reservación restaurada exitosamente."),
        @ApiResponse(responseCode = "400", description = "Reservación no encontrada o ya está activa.")
    })
    public ResponseEntity<?> restaurarLogicamente(@Parameter(description = "ID de la reservación a restaurar") @PathVariable Long id) {
        try {
            reservationService.restaurarLogicamente(id);
            return ResponseEntity.ok().body("Reservación restaurada");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Actualizar estado de reservación
    @PatchMapping("/{id}/estado/{nuevoEstado}")
    @Operation(summary = "Actualizar estado de reservación", description = "Actualiza el estado de una reservación (Pending, Confirmed, Cancelled, etc.).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado de reservación actualizado exitosamente."),
        @ApiResponse(responseCode = "400", description = "Reservación no encontrada o estado inválido.")
    })
    public ResponseEntity<?> actualizarEstadoReservacion(@Parameter(description = "ID de la reservación") @PathVariable Long id, 
                                                        @Parameter(description = "Nuevo estado de la reservación") @PathVariable String nuevoEstado) {
        try {
            reservationService.actualizarEstadoReservacion(id, nuevoEstado);
            return ResponseEntity.ok().body("Estado de reservación actualizado a: " + nuevoEstado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Actualizar estado de pago
    @PatchMapping("/{id}/pago/{pagado}")
    @Operation(summary = "Actualizar estado de pago", description = "Actualiza el estado de pago de una reservación.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado de pago actualizado exitosamente."),
        @ApiResponse(responseCode = "400", description = "Reservación no encontrada.")
    })
    public ResponseEntity<?> actualizarEstadoPago(@Parameter(description = "ID de la reservación") @PathVariable Long id, 
                                                 @Parameter(description = "Estado de pago (true/false)") @PathVariable Boolean pagado) {
        try {
            reservationService.actualizarEstadoPago(id, pagado);
            return ResponseEntity.ok().body("Estado de pago actualizado a: " + pagado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Obtener estadísticas
    @GetMapping("/estadisticas")
    @Operation(summary = "Obtener estadísticas de reservaciones", description = "Devuelve estadísticas generales sobre las reservaciones.")
    @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente.")
    public ResponseEntity<String> obtenerEstadisticas() {
        String estadisticas = reservationService.obtenerEstadisticas();
        return ResponseEntity.ok(estadisticas);
    }
}