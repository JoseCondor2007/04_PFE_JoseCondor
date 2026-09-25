package pe.edu.vallegrande.Backend.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pe.edu.vallegrande.Backend.model.Customer;
import pe.edu.vallegrande.Backend.service.CustomerService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Clientes", description = "Operaciones de gestión de clientes")
@RequiredArgsConstructor
public class CustomerRest {

    private final CustomerService customerService;

    @GetMapping
    @Operation(summary = "Listar todos los clientes")
    public ResponseEntity<List<Customer>> listarTodos() {
        return ResponseEntity.ok(customerService.listarTodos());
    }

    @GetMapping("/estado/{status}")
    @Operation(summary = "Listar clientes por estado", description = "Usar A (activo) o I (inactivo).")
    public ResponseEntity<List<Customer>> listarPorEstado(
            @Parameter(description = "Estado del cliente: A (activo) o I (inactivo)") @PathVariable String status) {
        return ResponseEntity.ok(customerService.listarPorEstado(status));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cliente por ID")
    public ResponseEntity<Customer> buscarPorId(
            @Parameter(description = "ID del cliente") @PathVariable Integer id) {
        Optional<Customer> customer = customerService.buscarPorId(id);
        return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Registrar nuevo cliente")
    public ResponseEntity<?> crear(@Valid @RequestBody Customer customer) {
        try {
            if (customer.getStatus() == null || customer.getStatus().isBlank()) {
                customer.setStatus("A");
            }
            Customer nuevo = customerService.crear(customer);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocurrió un error al crear el cliente: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cliente por ID")
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID del cliente") @PathVariable Integer id,
            @Valid @RequestBody Customer customer) {
        try {
            Customer actualizado = customerService.actualizar(id, customer);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/inactivar")
    @Operation(summary = "Inactivar cliente", description = "Marca el cliente como inactivo (status = 'I').")
    public ResponseEntity<?> inactivar(@Parameter(description = "ID del cliente") @PathVariable Integer id) {
        try {
            customerService.desactivarCliente(id);
            return ResponseEntity.ok("Cliente inactivado correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/activar")
    @Operation(summary = "Activar cliente", description = "Marca el cliente como activo (status = 'A').")
    public ResponseEntity<?> activar(@Parameter(description = "ID del cliente") @PathVariable Integer id) {
        try {
            customerService.activarCliente(id);
            return ResponseEntity.ok("Cliente activado correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 🔹 Eliminar lógico (status = 'E')
    @PatchMapping("/{id}/eliminar")
    @Operation(summary = "Eliminar lógico", description = "Marca el cliente como eliminado (status = 'E').")
    public ResponseEntity<?> eliminarLogico(@Parameter(description = "ID del cliente") @PathVariable Integer id) {
        try {
            customerService.eliminarLogico(id);
            return ResponseEntity.ok("Cliente eliminado correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar: " + e.getMessage());
        }
    }

    // 🔹 Restaurar lógico (E -> A)
    @PatchMapping("/{id}/restaurar")
    @Operation(summary = "Restaurar lógico", description = "Restaura un cliente eliminado a activo (status = 'A').")
    public ResponseEntity<?> restaurar(@Parameter(description = "ID del cliente") @PathVariable Integer id) {
        try {
            customerService.restaurar(id);
            return ResponseEntity.ok("Cliente restaurado correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al restaurar: " + e.getMessage());
        }
    }

    @GetMapping("/estadisticas")
    @Operation(summary = "Estadísticas de clientes", description = "Total, activos e inactivos.")
    public ResponseEntity<String> estadisticas() {
        return ResponseEntity.ok(customerService.obtenerEstadisticas());
    }
}
