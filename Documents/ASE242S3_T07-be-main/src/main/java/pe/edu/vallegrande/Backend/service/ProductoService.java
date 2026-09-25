package pe.edu.vallegrande.Backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.vallegrande.Backend.model.Producto;
import pe.edu.vallegrande.Backend.repository.ProductoRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    // Listar todos los productos
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    // Listar productos por estado
    public List<Producto> listarPorEstado(String estado) {
        return productoRepository.findByStatus(estado);
    }

    // Listar productos por categoría
    public List<Producto> buscarPorCategoria(String categoria) {
        return productoRepository.findByCategoryAndStatus(categoria, "ACTIVO");
    }

    // Buscar producto por ID
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    // Buscar producto por código
    public Optional<Producto> buscarPorCodigo(String productCode) {
        return productoRepository.findByProductCode(productCode);
    }

    // Verificar si existe producto por código
    public boolean existePorCodigo(String productCode) {
        return productoRepository.existsByProductCode(productCode);
    }

    // Crear nuevo producto
    public Producto crear(Producto producto) {
        try {
            System.out.println("🎯 === INICIANDO CREACIÓN DE PRODUCTO ===");
            System.out.println("📦 Producto recibido en servicio: " + producto);
            
            // Generar código automático si no se proporciona
            if (producto.getProductCode() == null || producto.getProductCode().trim().isEmpty()) {
                System.out.println("🔧 Generando código automático...");
                String codigoAutomatico = generarCodigoAutomatico();
                producto.setProductCode(codigoAutomatico);
                System.out.println("✅ Código asignado: " + codigoAutomatico);
            } else {
                System.out.println("📝 Usando código proporcionado: " + producto.getProductCode());
                // Verificar si el código proporcionado ya existe
                if (productoRepository.existsByProductCode(producto.getProductCode())) {
                    System.out.println("❌ Código ya existe en BD: " + producto.getProductCode());
                    throw new IllegalArgumentException("El código de producto ya existe: " + producto.getProductCode());
                }
            }
            
            // Validaciones básicas
            if (producto.getName() == null || producto.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del producto es obligatorio");
            }
            
            if (producto.getPrice() == null || producto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("El precio debe ser mayor a 0");
            }
            
            if (producto.getCategory() == null || producto.getCategory().trim().isEmpty()) {
                throw new IllegalArgumentException("La categoría es obligatoria");
            }
            
            // Asegurar que el estado sea "ACTIVO"
            producto.setStatus("ACTIVO");
            
            System.out.println("💾 Guardando producto: " + producto);
            
            Producto guardado = productoRepository.save(producto);
            System.out.println("✅ === PRODUCTO CREADO EXITOSAMENTE ===");
            System.out.println("📋 Producto guardado: " + guardado);
            
            return guardado;
            
        } catch (DataIntegrityViolationException e) {
            System.out.println("❌ === ERROR DE INTEGRIDAD DE DATOS ===");
            System.out.println("💥 Mensaje completo: " + e.getMessage());
            System.out.println("🔍 Causa raíz: " + e.getRootCause());
            e.printStackTrace();
            throw new IllegalArgumentException("Error de integridad de datos: " + e.getRootCause().getMessage());
        } catch (IllegalArgumentException e) {
            // Re-lanzar las excepciones de validación
            System.out.println("❌ Error de validación: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("❌ === ERROR GENERAL ===");
            System.out.println("💥 Tipo de error: " + e.getClass().getName());
            System.out.println("📝 Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw new IllegalArgumentException("Error al crear producto: " + e.getMessage());
        }
    }

    // Método para generar código automático único - SIGUIENDO PATRÓN EXISTENTE PROD001, PROD002, etc.
    private String generarCodigoAutomatico() {
        System.out.println("🔧 Buscando último número en secuencia PROD...");
        
        int ultimoNumero = encontrarUltimoNumeroPROD();
        int nuevoNumero = ultimoNumero + 1;
        
        String codigo = String.format("PROD%03d", nuevoNumero);
        
        System.out.println("✅ Código generado: " + codigo + " (siguiente en secuencia después de PROD" + String.format("%03d", ultimoNumero) + ")");
        
        return codigo;
    }

    // Método para encontrar el último número PROD en la base de datos
    private int encontrarUltimoNumeroPROD() {
        try {
            List<Producto> productos = productoRepository.findAll();
            int maxNumero = 0;
            boolean encontradoPROD = false;
            
            System.out.println("🔍 Analizando " + productos.size() + " productos existentes...");
            
            for (Producto producto : productos) {
                String codigo = producto.getProductCode();
                if (codigo != null && codigo.startsWith("PROD")) {
                    try {
                        String numeroStr = codigo.substring(4); // Quitar "PROD"
                        int numero = Integer.parseInt(numeroStr);
                        if (numero > maxNumero) {
                            maxNumero = numero;
                            encontradoPROD = true;
                        }
                        System.out.println("   - " + codigo + " -> número: " + numero);
                    } catch (NumberFormatException e) {
                        System.out.println("⚠️  Código con formato no numérico: " + codigo);
                    }
                }
            }
            
            if (!encontradoPROD) {
                System.out.println("📊 No se encontraron códigos PROD, empezando desde 0");
                return 0;
            }
            
            System.out.println("📊 Último número PROD encontrado: " + maxNumero);
            return maxNumero;
            
        } catch (Exception e) {
            System.out.println("❌ Error buscando último número PROD: " + e.getMessage());
            // Si hay error, buscar manualmente el máximo conocido
            return encontrarMaximoNumeroManual();
        }
    }

    // Método de respaldo para encontrar el máximo número manualmente
    private int encontrarMaximoNumeroManual() {
        // Basado en tu información de que tienes hasta PROD018
        // Podemos buscar secuencialmente desde 18 hacia abajo
        for (int i = 18; i >= 0; i--) {
            String codigoPrueba = String.format("PROD%03d", i);
            if (!productoRepository.existsByProductCode(codigoPrueba)) {
                System.out.println("🔍 Número disponible encontrado: " + (i - 1));
                return i - 1;
            }
        }
        System.out.println("🔍 No se pudo determinar el último número, usando 18 como referencia");
        return 18;
    }

    // Actualizar producto existente - CORREGIDO
    public Producto actualizar(Long id, Producto productoActualizado) {
        Optional<Producto> productoExistente = productoRepository.findById(id);

        if (productoExistente.isPresent()) {
            Producto producto = productoExistente.get();

            System.out.println("🔄 Actualizando producto ID: " + id);
            System.out.println("📦 Datos actuales: " + producto);
            System.out.println("📦 Nuevos datos: " + productoActualizado);

            // Validar que el código no esté duplicado (si se cambió y no es nulo)
            if (productoActualizado.getProductCode() != null && 
                !productoActualizado.getProductCode().equals(producto.getProductCode()) &&
                productoRepository.existsByProductCode(productoActualizado.getProductCode())) {
                throw new IllegalArgumentException("El código de producto ya existe: " + productoActualizado.getProductCode());
            }

            // Actualizar campos - SOLO si no son nulos (MERGE PARCIAL)
            if (productoActualizado.getProductCode() != null) {
                producto.setProductCode(productoActualizado.getProductCode());
            }
            if (productoActualizado.getName() != null) {
                producto.setName(productoActualizado.getName());
            }
            if (productoActualizado.getDescription() != null) {
                producto.setDescription(productoActualizado.getDescription());
            }
            if (productoActualizado.getPrice() != null) {
                producto.setPrice(productoActualizado.getPrice());
            }
            if (productoActualizado.getCategory() != null) {
                producto.setCategory(productoActualizado.getCategory());
            }
            // El estado se mantiene como estaba
            // fechaEdicion se actualiza automáticamente por @PreUpdate

            Producto actualizado = productoRepository.save(producto);
            System.out.println("✅ Producto actualizado: " + actualizado);
            return actualizado;
        } else {
            throw new IllegalArgumentException("Producto no encontrado con ID: " + id);
        }
    }

    // Eliminación lógica
    @Transactional
    public void eliminarLogicamente(Long id) {
        Optional<Producto> producto = productoRepository.findById(id);

        if (producto.isPresent()) {
            System.out.println("🗑️ Eliminando lógicamente producto ID: " + id);
            Producto prod = producto.get();
            prod.marcarComoEliminado(); // Usa el nuevo método con auditoría
            productoRepository.save(prod);
            System.out.println("✅ Producto eliminado lógicamente con fecha: " + prod.getFechaEliminacionLogica());
        } else {
            throw new IllegalArgumentException("Producto no encontrado con ID: " + id);
        }
    }

    // Restauración lógica
    @Transactional
    public void restaurarLogicamente(Long id) {
        Optional<Producto> producto = productoRepository.findById(id);

        if (producto.isPresent()) {
            System.out.println("🔄 Restaurando producto ID: " + id);
            Producto prod = producto.get();
            prod.marcarComoRestaurado(); // Usa el nuevo método con auditoría
            productoRepository.save(prod);
            System.out.println("✅ Producto restaurado con fecha: " + prod.getFechaRestauracionLogica());
        } else {
            throw new IllegalArgumentException("Producto no encontrado con ID: " + id);
        }
    }

    // Desactivar producto
    @Transactional
    public void desactivarLogicamente(Long id) {
        Optional<Producto> producto = productoRepository.findById(id);

        if (producto.isPresent()) {
            System.out.println("⏸️ Desactivando producto ID: " + id);
            Producto prod = producto.get();
            prod.setStatus("INACTIVO");
            prod.setFechaEdicion(java.time.LocalDateTime.now()); // Actualizar fecha de edición
            productoRepository.save(prod);
            System.out.println("✅ Producto desactivado");
        } else {
            throw new IllegalArgumentException("Producto no encontrado con ID: " + id);
        }
    }

    // Listar productos activos
    public List<Producto> listarActivos() {
        return productoRepository.findByStatus("ACTIVO");
    }

    // Listar productos inactivos
    public List<Producto> listarInactivos() {
        return productoRepository.findByStatus("INACTIVO");
    }

    // Listar productos eliminados
    public List<Producto> listarEliminados() {
        return productoRepository.findByStatus("ELIMINADO");
    }

    // Buscar productos por nombre (búsqueda parcial)
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNameContainingIgnoreCaseAndStatus(nombre, "ACTIVO");
    }

    // Obtener estadísticas
    public String obtenerEstadisticas() {
        long total = productoRepository.count();
        long activos = productoRepository.countByStatus("ACTIVO");
        long inactivos = productoRepository.countByStatus("INACTIVO");
        long eliminados = productoRepository.countByStatus("ELIMINADO");
        
        return String.format("Total: %d | Activos: %d | Inactivos: %d | Eliminados: %d", 
                           total, activos, inactivos, eliminados);
    }
}