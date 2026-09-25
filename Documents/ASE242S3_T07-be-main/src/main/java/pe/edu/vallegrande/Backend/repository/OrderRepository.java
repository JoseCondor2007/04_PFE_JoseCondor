package pe.edu.vallegrande.Backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.Backend.model.Order;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // Buscar pedidos por estado
    List<Order> findByStatus(String status);
    
    // Buscar pedidos por estado de pedido
    List<Order> findByOrderStatus(String orderStatus);
    
    // Buscar pedidos por cliente
    List<Order> findByCustomersIdCustomer(Integer customersIdCustomer);
    
    // Buscar pedidos por empleado
    List<Order> findByEmployeesIdEmployee(Integer employeesIdEmployee);
    
    // Buscar pedidos por mesa
    List<Order> findByTablesIdTables(Integer tablesIdTables);
    
    // Buscar pedidos por fecha
    List<Order> findByOrderDatetime(LocalDate orderDatetime);
    
    // Contar pedidos por estado
    long countByStatus(String status);
    
    // Contar pedidos por estado de pedido
    long countByOrderStatus(String orderStatus);
    
    // Desactivar pedido (cambiar estado a 'I')
    @Modifying
    @Query("UPDATE Order o SET o.status = 'I' WHERE o.idOrder = :id")
    void desactivarOrder(@Param("id") Long id);
    
    // Activar pedido (cambiar estado a 'A')
    @Modifying
    @Query("UPDATE Order o SET o.status = 'A' WHERE o.idOrder = :id")
    void activarOrder(@Param("id") Long id);
    
    // Actualizar estado de pedido
    @Modifying
    @Query("UPDATE Order o SET o.orderStatus = :orderStatus WHERE o.idOrder = :id")
    void actualizarOrderStatus(@Param("id") Long id, @Param("orderStatus") String orderStatus);
}