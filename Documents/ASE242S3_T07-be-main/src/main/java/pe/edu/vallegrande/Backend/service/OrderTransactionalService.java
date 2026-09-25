package pe.edu.vallegrande.Backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.vallegrande.Backend.model.Order;
import pe.edu.vallegrande.Backend.model.OrderDetail;
import pe.edu.vallegrande.Backend.repository.OrderRepository;
import pe.edu.vallegrande.Backend.repository.OrderDetailRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderTransactionalService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    /**
     * ✅ TRANSACCIONAL: Crea un pedido con sus detalles en una sola transacción
     * Si falla algún detalle, se revierte toda la operación
     */
    @Transactional
    public Order crearPedidoConDetalles(Order pedido, List<OrderDetail> detalles) {
        try {
            System.out.println("🎯 === INICIANDO TRANSACCIÓN PEDIDO CON DETALLES ===");
            
            // 1. Configurar auditoría del pedido
            pedido.setFechaCreacion(LocalDateTime.now());
            pedido.setStatus("A");
            
            // 2. Guardar el pedido (cabecera)
            Order pedidoGuardado = orderRepository.save(pedido);
            System.out.println("✅ Pedido guardado - ID: " + pedidoGuardado.getIdOrder());
            
            // 3. Calcular y actualizar el monto total
            BigDecimal totalAmount = BigDecimal.ZERO;
            
            // 4. Guardar los detalles y calcular total
            for (OrderDetail detalle : detalles) {
                // Asignar el ID del pedido guardado
                detalle.setOrderIdOrder(pedidoGuardado.getIdOrder());
                
                // Validar que el subtotal esté presente
                if (detalle.getSubtotal() == null) {
                    throw new IllegalArgumentException("El subtotal del detalle es obligatorio");
                }
                
                // Sumar al total
                totalAmount = totalAmount.add(detalle.getSubtotal());
                
                // Guardar el detalle
                OrderDetail detalleGuardado = orderDetailRepository.save(detalle);
                System.out.println("✅ Detalle guardado - ID: " + detalleGuardado.getIdOrderDetail() + 
                                 ", Subtotal: " + detalleGuardado.getSubtotal());
            }
            
            // 5. Actualizar el monto total en el pedido
            pedidoGuardado.setTotalAmount(totalAmount);
            Order pedidoActualizado = orderRepository.save(pedidoGuardado);
            
            System.out.println("💰 Monto total calculado: " + totalAmount);
            System.out.println("🎉 === TRANSACCIÓN COMPLETADA EXITOSAMENTE ===");
            
            return pedidoActualizado;
            
        } catch (Exception e) {
            System.out.println("❌ === ERROR EN TRANSACCIÓN ===");
            System.out.println("💥 Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error transaccional al crear pedido con detalles: " + e.getMessage(), e);
        }
    }

    /**
     * ✅ TRANSACCIONAL: Actualiza pedido y sus detalles en una sola operación
     */
    @Transactional
    public Order actualizarPedidoConDetalles(Long idPedido, Order pedidoActualizado, List<OrderDetail> nuevosDetalles) {
        try {
            System.out.println("🎯 === INICIANDO ACTUALIZACIÓN TRANSACCIONAL ===");
            
            // 1. Verificar que el pedido existe
            Order pedidoExistente = orderRepository.findById(idPedido)
                    .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con ID: " + idPedido));
            
            // 2. Actualizar campos del pedido
            if (pedidoActualizado.getEmployeesIdEmployee() != null) {
                pedidoExistente.setEmployeesIdEmployee(pedidoActualizado.getEmployeesIdEmployee());
            }
            if (pedidoActualizado.getCustomersIdCustomer() != null) {
                pedidoExistente.setCustomersIdCustomer(pedidoActualizado.getCustomersIdCustomer());
            }
            if (pedidoActualizado.getTablesIdTables() != null) {
                pedidoExistente.setTablesIdTables(pedidoActualizado.getTablesIdTables());
            }
            if (pedidoActualizado.getOrderStatus() != null) {
                pedidoExistente.setOrderStatus(pedidoActualizado.getOrderStatus());
            }
            if (pedidoActualizado.getCostumerName() != null) {
                pedidoExistente.setCostumerName(pedidoActualizado.getCostumerName());
            }
            if (pedidoActualizado.getEmployeeName() != null) {
                pedidoExistente.setEmployeeName(pedidoActualizado.getEmployeeName());
            }
            
            // 3. Actualizar auditoría de edición
            pedidoExistente.setFechaEdicion(LocalDateTime.now());
            
            // 4. Eliminar detalles existentes
            List<OrderDetail> detallesExistentes = orderDetailRepository.findByOrderIdOrder(idPedido);
            orderDetailRepository.deleteAll(detallesExistentes);
            System.out.println("🗑️ Detalles existentes eliminados: " + detallesExistentes.size());
            
            // 5. Guardar nuevos detalles y calcular total
            BigDecimal totalAmount = BigDecimal.ZERO;
            
            for (OrderDetail detalle : nuevosDetalles) {
                detalle.setOrderIdOrder(idPedido);
                
                if (detalle.getSubtotal() == null) {
                    throw new IllegalArgumentException("El subtotal del detalle es obligatorio");
                }
                
                totalAmount = totalAmount.add(detalle.getSubtotal());
                orderDetailRepository.save(detalle);
            }
            
            // 6. Actualizar monto total
            pedidoExistente.setTotalAmount(totalAmount);
            Order pedidoFinal = orderRepository.save(pedidoExistente);
            
            System.out.println("💰 Nuevo monto total: " + totalAmount);
            System.out.println("🎉 === ACTUALIZACIÓN TRANSACCIONAL COMPLETADA ===");
            
            return pedidoFinal;
            
        } catch (Exception e) {
            System.out.println("❌ === ERROR EN ACTUALIZACIÓN TRANSACCIONAL ===");
            throw new RuntimeException("Error al actualizar pedido con detalles: " + e.getMessage(), e);
        }
    }
}