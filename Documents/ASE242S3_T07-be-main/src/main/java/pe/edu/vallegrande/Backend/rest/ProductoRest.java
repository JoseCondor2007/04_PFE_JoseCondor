package pe.edu.vallegrande.Backend.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import pe.edu.vallegrande.Backend.model.Producto;
import pe.edu.vallegrande.Backend.service.ProductoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/productos")
@Tag(name = "Productos", description = "Operaciones de gestión de productos del restaurante La sazón de Panchita")
public class ProductoRest {

    @Autowired
    private ProductoService productoService;

    // Listar todos los productos
    @GetMapping
    @Operation(summary = "Obtener todos los productos", description = "Recupera una lista completa de todos los productos en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente.")
    public ResponseEntity<List<Producto>> listarTodos() {
        List<Producto> productos = productoService.listarTodos();
        return ResponseEntity.ok(productos);
    }

    // Listar productos por estado
    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener productos por estado", description = "Filtra y devuelve productos según su estado (ej: 'ACTIVO', 'INACTIVO').")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de productos por estado obtenida exitosamente."),
        @ApiResponse(responseCode = "400", description = "Estado de producto no válido.")
    })
    public ResponseEntity<List<Producto>> listarPorEstado(@Parameter(description = "Estado del producto a filtrar (ACTIVO, INACTIVO, ELIMINADO)") @PathVariable String estado) {
        List<Producto> productos = productoService.listarPorEstado(estado);
        return ResponseEntity.ok(productos);
    }

    // Listar productos por categoría
    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Obtener productos por categoría", description = "Busca y devuelve una lista de productos activos que pertenecen a una categoría específica.")
    @ApiResponse(responseCode = "200", description = "Lista de productos por categoría obtenida exitosamente.")
    public ResponseEntity<List<Producto>> buscarPorCategoria(@Parameter(description = "Nombre de la categoría del producto") @PathVariable String categoria) {
        List<Producto> productos = productoService.buscarPorCategoria(categoria);
        return ResponseEntity.ok(productos);
    }

    // Buscar producto por ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un producto por su ID", description = "Busca y devuelve los detalles de un producto usando su identificador único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado exitosamente."),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado.")
    })
    public ResponseEntity<Producto> buscarPorId(@Parameter(description = "ID del producto a buscar") @PathVariable Long id) {
        Optional<Producto> producto = productoService.buscarPorId(id);

        if (producto.isPresent()) {
            return ResponseEntity.ok(producto.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear nuevo producto
    @PostMapping
    @Operation(summary = "Crear un nuevo producto", description = "Crea un nuevo producto en el sistema con los datos proporcionados en el cuerpo de la solicitud.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente.", content = @Content(schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida. Los datos son incorrectos."),
        @ApiResponse(responseCode = "500", description = "Ocurrió un error interno del servidor.")
    })
    public ResponseEntity<?> crear(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Objeto 'Producto' a crear.") @RequestBody Producto producto) {
        try {
            System.out.println("=== REST CONTROLLER - CREAR PRODUCTO ===");
            System.out.println("Request recibido: " + producto);
            
            // Remover el estado si fue enviado desde el frontend
            if (producto.getStatus() != null) {
                System.out.println("Removiendo estado enviado por cliente: " + producto.getStatus());
                producto.setStatus(null);
            }
            
            Producto nuevoProducto = productoService.crear(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
            
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
                    .body("Ocurrió un error interno al crear el producto: " + e.getMessage());
        }
    }

    // Actualizar producto existente
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un producto", description = "Actualiza la información de un producto existente usando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente."),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida. El producto no fue encontrado o los datos son incorrectos.")
    })
    public ResponseEntity<?> actualizar(@Parameter(description = "ID del producto a actualizar") @PathVariable Long id, @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos del producto") @RequestBody Producto producto) {
        try {
            Producto productoActualizado = productoService.actualizar(id, producto);
            return ResponseEntity.ok(productoActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Eliminación lógica
    @PatchMapping("/{id}/eliminar")
    @Operation(summary = "Eliminación lógica", description = "Cambia el estado de un producto a 'ELIMINADO' sin borrarlo de la base de datos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto eliminado lógicamente."),
        @ApiResponse(responseCode = "400", description = "Producto no encontrado o ya eliminado.")
    })
    public ResponseEntity<?> eliminarLogicamente(@Parameter(description = "ID del producto a eliminar") @PathVariable Long id) {
        try {
            productoService.eliminarLogicamente(id);
            return ResponseEntity.ok().body("Producto eliminado lógicamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Restauración lógica
    @PatchMapping("/{id}/restaurar")
    @Operation(summary = "Restauración lógica", description = "Restaura un producto que ha sido eliminado lógicamente, cambiando su estado de nuevo a 'ACTIVO'.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto restaurado exitosamente."),
        @ApiResponse(responseCode = "400", description = "Producto no encontrado o ya está activo.")
    })
    public ResponseEntity<?> restaurarLogicamente(@Parameter(description = "ID del producto a restaurar") @PathVariable Long id) {
        try {
            productoService.restaurarLogicamente(id);
            return ResponseEntity.ok().body("Producto restaurado");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}