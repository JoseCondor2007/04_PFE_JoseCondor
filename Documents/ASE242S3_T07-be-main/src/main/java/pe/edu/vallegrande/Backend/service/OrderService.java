package pe.edu.vallegrande.Backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.vallegrande.Backend.model.Order;
import pe.edu.vallegrande.Backend.model.OrderDetail;
import pe.edu.vallegrande.Backend.repository.OrderRepository;
import pe.edu.vallegrande.Backend.repository.OrderDetailRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    // Listar todos los pedidos
    public List<Order> listarTodos() {
        return orderRepository.findAll();
    }

    // Listar pedidos por estado
    public List<Order> listarPorEstado(String estado) {
        return orderRepository.findByStatus(estado);
    }

    // Buscar pedido por ID
    public Optional<Order> buscarPorId(Long id) {
        return orderRepository.findById(id);
    }

    // Buscar pedidos por cliente
    public List<Order> buscarPorCliente(Integer customersIdCustomer) {
        return orderRepository.findByCustomersIdCustomer(customersIdCustomer);
    }

    // Buscar pedidos por empleado
    public List<Order> buscarPorEmpleado(Integer employeesIdEmployee) {
        return orderRepository.findByEmployeesIdEmployee(employeesIdEmployee);
    }

    // Buscar pedidos por mesa
    public List<Order> buscarPorMesa(Integer tablesIdTables) {
        return orderRepository.findByTablesIdTables(tablesIdTables);
    }

    // Buscar pedidos por fecha
    public List<Order> buscarPorFecha(LocalDate fecha) {
        return orderRepository.findByOrderDatetime(fecha);
    }

    // Buscar pedidos por estado de pedido
    public List<Order> buscarPorEstadoPedido(String orderStatus) {
        return orderRepository.findByOrderStatus(orderStatus);
    }

    // Crear nuevo pedido
    public Order crear(Order pedido) {
        try {
            System.out.println("🎯 === INICIANDO CREACIÓN DE PEDIDO ===");
            System.out.println("📦 Pedido recibido en servicio: " + pedido);
            
            // Validaciones básicas
            if (pedido.getEmployeesIdEmployee() == null) {
                throw new IllegalArgumentException("El ID del empleado es obligatorio");
            }
            
            if (pedido.getCustomersIdCustomer() == null) {
                throw new IllegalArgumentException("El ID del cliente es obligatorio");
            }
            
            if (pedido.getTablesIdTables() == null) {
                throw new IllegalArgumentException("El ID de la mesa es obligatorio");
            }
            
            if (pedido.getCostumerName() == null || pedido.getCostumerName().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del cliente es obligatorio");
            }
            
            if (pedido.getEmployeeName() == null || pedido.getEmployeeName().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del empleado es obligatorio");
            }
            
            // Asegurar valores por defecto
            if (pedido.getOrderStatus() == null) {
                pedido.setOrderStatus("Pending");
            }
            if (pedido.getTotalAmount() == null) {
                pedido.setTotalAmount(BigDecimal.ZERO);
            }
            if (pedido.getStatus() == null) {
                pedido.setStatus("A");
            }
            
            // ✅ AUDITORÍA: Establecer fecha de creación
            pedido.setFechaCreacion(LocalDateTime.now());
            
            System.out.println("💾 Guardando pedido: " + pedido);
            
            Order guardado = orderRepository.save(pedido);
            System.out.println("✅ === PEDIDO CREADO EXITOSAMENTE ===");
            System.out.println("📋 Pedido guardado: " + guardado);
            System.out.println("🆔 ID generado: " + guardado.getIdOrder());
            
            return guardado;
            
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
            throw new IllegalArgumentException("Error al crear pedido: " + e.getMessage());
        }
    }

    // Actualizar pedido existente
    public Order actualizar(Long id, Order pedidoActualizado) {
        Optional<Order> pedidoExistente = orderRepository.findById(id);

        if (pedidoExistente.isPresent()) {
            Order pedido = pedidoExistente.get();

            System.out.println("🔄 Actualizando pedido ID: " + id);
            System.out.println("📦 Datos actuales: " + pedido);
            System.out.println("📦 Nuevos datos: " + pedidoActualizado);

            // Actualizar campos
            if (pedidoActualizado.getEmployeesIdEmployee() != null) {
                pedido.setEmployeesIdEmployee(pedidoActualizado.getEmployeesIdEmployee());
            }
            if (pedidoActualizado.getCustomersIdCustomer() != null) {
                pedido.setCustomersIdCustomer(pedidoActualizado.getCustomersIdCustomer());
            }
            if (pedidoActualizado.getTablesIdTables() != null) {
                pedido.setTablesIdTables(pedidoActualizado.getTablesIdTables());
            }
            if (pedidoActualizado.getOrderDatetime() != null) {
                pedido.setOrderDatetime(pedidoActualizado.getOrderDatetime());
            }
            if (pedidoActualizado.getOrderStatus() != null) {
                pedido.setOrderStatus(pedidoActualizado.getOrderStatus());
            }
            if (pedidoActualizado.getTotalAmount() != null) {
                pedido.setTotalAmount(pedidoActualizado.getTotalAmount());
            }
            if (pedidoActualizado.getCostumerName() != null) {
                pedido.setCostumerName(pedidoActualizado.getCostumerName());
            }
            if (pedidoActualizado.getEmployeeName() != null) {
                pedido.setEmployeeName(pedidoActualizado.getEmployeeName());
            }

            // ✅ AUDITORÍA: Establecer fecha de edición
            pedido.setFechaEdicion(LocalDateTime.now());

            Order actualizado = orderRepository.save(pedido);
            System.out.println("✅ Pedido actualizado: " + actualizado);
            return actualizado;
        } else {
            throw new IllegalArgumentException("Pedido no encontrado con ID: " + id);
        }
    }

    // Eliminación lógica
    @Transactional
    public void eliminarLogicamente(Long id) {
        Optional<Order> pedido = orderRepository.findById(id);

        if (pedido.isPresent()) {
            System.out.println("🗑️ Eliminando lógicamente pedido ID: " + id);
            
            // ✅ AUDITORÍA: Establecer fecha de eliminación lógica
            Order pedidoEntity = pedido.get();
            pedidoEntity.setFechaEliminacionLogica(LocalDateTime.now());
            orderRepository.save(pedidoEntity);
            
            orderRepository.desactivarOrder(id);
            System.out.println("✅ Pedido eliminado lógicamente - Fecha: " + LocalDateTime.now());
        } else {
            throw new IllegalArgumentException("Pedido no encontrado con ID: " + id);
        }
    }

    // Restauración lógica
    @Transactional
    public void restaurarLogicamente(Long id) {
        Optional<Order> pedido = orderRepository.findById(id);

        if (pedido.isPresent()) {
            System.out.println("🔄 Restaurando pedido ID: " + id);
            
            // ✅ AUDITORÍA: Establecer fecha de restauración lógica
            Order pedidoEntity = pedido.get();
            pedidoEntity.setFechaRestauracionLogica(LocalDateTime.now());
            orderRepository.save(pedidoEntity);
            
            orderRepository.activarOrder(id);
            System.out.println("✅ Pedido restaurado - Fecha: " + LocalDateTime.now());
        } else {
            throw new IllegalArgumentException("Pedido no encontrado con ID: " + id);
        }
    }

    // Actualizar estado de pedido
    @Transactional
    public void actualizarEstadoPedido(Long id, String nuevoEstado) {
        Optional<Order> pedido = orderRepository.findById(id);

        if (pedido.isPresent()) {
            System.out.println("🔄 Actualizando estado de pedido ID: " + id + " a: " + nuevoEstado);
            
            // ✅ AUDITORÍA: Establecer fecha de edición
            Order pedidoEntity = pedido.get();
            pedidoEntity.setFechaEdicion(LocalDateTime.now());
            orderRepository.save(pedidoEntity);
            
            orderRepository.actualizarOrderStatus(id, nuevoEstado);
            System.out.println("✅ Estado de pedido actualizado");
        } else {
            throw new IllegalArgumentException("Pedido no encontrado con ID: " + id);
        }
    }

    // Obtener detalles de un pedido
    public List<OrderDetail> obtenerDetallesPorPedido(Long orderId) {
        return orderDetailRepository.findByOrderIdOrder(orderId);
    }

    // Listar pedidos activos
    public List<Order> listarActivos() {
        return orderRepository.findByStatus("A");
    }

    // Listar pedidos inactivos
    public List<Order> listarInactivos() {
        return orderRepository.findByStatus("I");
    }

    // Obtener estadísticas
    public String obtenerEstadisticas() {
        long total = orderRepository.count();
        long activos = orderRepository.countByStatus("A");
        long inactivos = orderRepository.countByStatus("I");
        long pendientes = orderRepository.countByOrderStatus("Pending");
        long completados = orderRepository.countByOrderStatus("Completed");
        
        return String.format("Total: %d | Activos: %d | Inactivos: %d | Pendientes: %d | Completados: %d", 
                           total, activos, inactivos, pendientes, completados);
    }
}