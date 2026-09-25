package pe.edu.vallegrande.Backend.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import pe.edu.vallegrande.dto.OrderCompleteRequestDTO;
import pe.edu.vallegrande.Backend.model.Order;
import pe.edu.vallegrande.Backend.model.OrderDetail;
import pe.edu.vallegrande.Backend.service.OrderService;
import pe.edu.vallegrande.Backend.service.OrderTransactionalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/orders")
@Tag(name = "Pedidos", description = "Operaciones de gestión de pedidos del restaurante La sazón de Panchita")
public class OrderRest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTransactionalService orderTransactionalService;

    // Listar todos los pedidos
    @GetMapping
    @Operation(summary = "Obtener todos los pedidos", description = "Recupera una lista completa de todos los pedidos en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos obtenida exitosamente.")
    public ResponseEntity<List<Order>> listarTodos() {
        List<Order> pedidos = orderService.listarTodos();
        return ResponseEntity.ok(pedidos);
    }

    // Listar pedidos por estado
    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener pedidos por estado", description = "Filtra y devuelve pedidos según su estado (ej: 'A', 'I').")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pedidos por estado obtenida exitosamente."),
        @ApiResponse(responseCode = "400", description = "Estado de pedido no válido.")
    })
    public ResponseEntity<List<Order>> listarPorEstado(@Parameter(description = "Estado del pedido a filtrar (A, I)") @PathVariable String estado) {
        List<Order> pedidos = orderService.listarPorEstado(estado);
        return ResponseEntity.ok(pedidos);
    }

    // Listar pedidos por cliente
    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Obtener pedidos por cliente", description = "Busca y devuelve una lista de pedidos de un cliente específico.")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos por cliente obtenida exitosamente.")
    public ResponseEntity<List<Order>> buscarPorCliente(@Parameter(description = "ID del cliente") @PathVariable Integer clienteId) {
        List<Order> pedidos = orderService.buscarPorCliente(clienteId);
        return ResponseEntity.ok(pedidos);
    }

    // Listar pedidos por fecha
    @GetMapping("/fecha/{fecha}")
    @Operation(summary = "Obtener pedidos por fecha", description = "Busca y devuelve una lista de pedidos para una fecha específica.")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos por fecha obtenida exitosamente.")
    public ResponseEntity<List<Order>> buscarPorFecha(@Parameter(description = "Fecha del pedido (YYYY-MM-DD)") @PathVariable String fecha) {
        try {
            LocalDate fechaPedido = LocalDate.parse(fecha);
            List<Order> pedidos = orderService.buscarPorFecha(fechaPedido);
            return ResponseEntity.ok(pedidos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Buscar pedido por ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un pedido por su ID", description = "Busca y devuelve los detalles de un pedido usando su identificador único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido encontrado exitosamente."),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado.")
    })
    public ResponseEntity<Order> buscarPorId(@Parameter(description = "ID del pedido a buscar") @PathVariable Long id) {
        Optional<Order> pedido = orderService.buscarPorId(id);

        if (pedido.isPresent()) {
            return ResponseEntity.ok(pedido.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear nuevo pedido (solo cabecera)
    @PostMapping
    @Operation(summary = "Crear un nuevo pedido", description = "Crea un nuevo pedido en el sistema con los datos proporcionados en el cuerpo de la solicitud.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pedido creado exitosamente.", content = @Content(schema = @Schema(implementation = Order.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida. Los datos son incorrectos."),
        @ApiResponse(responseCode = "500", description = "Ocurrió un error interno del servidor.")
    })
    public ResponseEntity<?> crear(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Objeto 'Order' a crear.") @RequestBody Order pedido) {
        try {
            System.out.println("=== REST CONTROLLER - CREAR PEDIDO ===");
            System.out.println("Request recibido: " + pedido);
            
            Order nuevoPedido = orderService.crear(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPedido);
            
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
                    .body("Ocurrió un error interno al crear el pedido: " + e.getMessage());
        }
    }

    // ✅ CREAR PEDIDO COMPLETO CON DETALLES (TRANSACCIONAL)
    @PostMapping("/completa")
    @Operation(summary = "Crear pedido completo con detalles", description = "Crea un pedido completo con cabecera y detalles en una sola transacción")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pedido completo creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<?> crearPedidoCompleto(@RequestBody OrderCompleteRequestDTO orderRequest) {
        try {
            System.out.println("🎯 === CREANDO PEDIDO COMPLETO TRANSACCIONAL ===");
            System.out.println("📦 Datos recibidos: " + orderRequest);
            
            // Crear objeto Order desde el DTO
            Order pedido = new Order();
            pedido.setEmployeesIdEmployee(orderRequest.getEmployeesIdEmployee());
            pedido.setCustomersIdCustomer(orderRequest.getCustomersIdCustomer());
            pedido.setTablesIdTables(orderRequest.getTablesIdTables());
            pedido.setCostumerName(orderRequest.getCostumerName());
            pedido.setEmployeeName(orderRequest.getEmployeeName());
            pedido.setOrderStatus(orderRequest.getOrderStatus());
            
            // Llamar al servicio transaccional
            Order pedidoCreado = orderTransactionalService.crearPedidoConDetalles(pedido, orderRequest.getOrderDetails());
            
            System.out.println("✅ Pedido completo creado exitosamente - ID: " + pedidoCreado.getIdOrder());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoCreado);
            
        } catch (Exception e) {
            System.out.println("❌ Error en creación transaccional: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error al crear pedido completo: " + e.getMessage());
        }
    }

    // Actualizar pedido existente
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un pedido", description = "Actualiza la información de un pedido existente usando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido actualizado exitosamente."),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida. El pedido no fue encontrado o los datos son incorrectos.")
    })
    public ResponseEntity<?> actualizar(@Parameter(description = "ID del pedido a actualizar") @PathVariable Long id, @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos del pedido") @RequestBody Order pedido) {
        try {
            Order pedidoActualizado = orderService.actualizar(id, pedido);
            return ResponseEntity.ok(pedidoActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Eliminación lógica
    @PatchMapping("/{id}/eliminar")
    @Operation(summary = "Eliminación lógica", description = "Cambia el estado de un pedido a 'I' sin borrarlo de la base de datos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido eliminado lógicamente."),
        @ApiResponse(responseCode = "400", description = "Pedido no encontrado o ya eliminado.")
    })
    public ResponseEntity<?> eliminarLogicamente(@Parameter(description = "ID del pedido a eliminar") @PathVariable Long id) {
        try {
            orderService.eliminarLogicamente(id);
            return ResponseEntity.ok().body("Pedido eliminado lógicamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Restauración lógica
    @PatchMapping("/{id}/restaurar")
    @Operation(summary = "Restauración lógica", description = "Restaura un pedido que ha sido eliminado lógicamente, cambiando su estado de nuevo a 'A'.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido restaurado exitosamente."),
        @ApiResponse(responseCode = "400", description = "Pedido no encontrado o ya está activo.")
    })
    public ResponseEntity<?> restaurarLogicamente(@Parameter(description = "ID del pedido a restaurar") @PathVariable Long id) {
        try {
            orderService.restaurarLogicamente(id);
            return ResponseEntity.ok().body("Pedido restaurado");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Actualizar estado de pedido
    @PatchMapping("/{id}/estado/{nuevoEstado}")
    @Operation(summary = "Actualizar estado de pedido", description = "Actualiza el estado de un pedido (Pending, Completed, Cancelled, etc.).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado de pedido actualizado exitosamente."),
        @ApiResponse(responseCode = "400", description = "Pedido no encontrado o estado inválido.")
    })
    public ResponseEntity<?> actualizarEstadoPedido(@Parameter(description = "ID del pedido") @PathVariable Long id, 
                                                   @Parameter(description = "Nuevo estado del pedido") @PathVariable String nuevoEstado) {
        try {
            orderService.actualizarEstadoPedido(id, nuevoEstado);
            return ResponseEntity.ok().body("Estado de pedido actualizado a: " + nuevoEstado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Obtener detalles de un pedido
    @GetMapping("/{id}/detalles")
    @Operation(summary = "Obtener detalles de un pedido", description = "Devuelve todos los detalles (productos) de un pedido específico.")
    @ApiResponse(responseCode = "200", description = "Detalles del pedido obtenidos exitosamente.")
    public ResponseEntity<List<OrderDetail>> obtenerDetalles(@Parameter(description = "ID del pedido") @PathVariable Long id) {
        List<OrderDetail> detalles = orderService.obtenerDetallesPorPedido(id);
        return ResponseEntity.ok(detalles);
    }

    // Obtener estadísticas
    @GetMapping("/estadisticas")
    @Operation(summary = "Obtener estadísticas de pedidos", description = "Devuelve estadísticas generales sobre los pedidos.")
    @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente.")
    public ResponseEntity<String> obtenerEstadisticas() {
        String estadisticas = orderService.obtenerEstadisticas();
        return ResponseEntity.ok(estadisticas);
    }
}