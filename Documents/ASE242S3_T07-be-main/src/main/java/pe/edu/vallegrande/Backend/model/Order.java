package pe.edu.vallegrande.Backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_order")
    private Long idOrder;
    
    @NotNull(message = "El ID del empleado es obligatorio")
    @Column(name = "Employees_id_employee", nullable = false)
    private Integer employeesIdEmployee;
    
    @NotNull(message = "El ID del cliente es obligatorio")
    @Column(name = "Customers_id_customer", nullable = false)
    private Integer customersIdCustomer;
    
    @NotNull(message = "El ID de la mesa es obligatorio")
    @Column(name = "Tables_id_tables", nullable = false)
    private Integer tablesIdTables;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "order_datetime")
    private LocalDateTime orderDatetime;
    
    @Size(max = 20, message = "El estado del pedido no puede exceder 20 caracteres")
    @Column(name = "order_status", length = 20)
    private String orderStatus = "Pending";
    
    @DecimalMin(value = "0.0", inclusive = true, message = "El monto total no puede ser negativo")
    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;
    
    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(max = 100, message = "El nombre del cliente no puede exceder 100 caracteres")
    @Column(name = "costumer_name", nullable = false, length = 100)
    private String costumerName;
    
    @NotBlank(message = "El nombre del empleado es obligatorio")
    @Size(max = 100, message = "El nombre del empleado no puede exceder 100 caracteres")
    @Column(name = "Employee_name", nullable = false, length = 100)
    private String employeeName;
    
    @Column(name = "status", length = 1)
    private String status = "A";
    
    // ✅ CAMPOS DE AUDITORÍA IMPLEMENTADOS
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fecha_edicion")
    private LocalDateTime fechaEdicion;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fecha_eliminacion_logica")
    private LocalDateTime fechaEliminacionLogica;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fecha_restauracion_logica")
    private LocalDateTime fechaRestauracionLogica;
    
    // ✅ RELACIÓN CON ORDER_DETAIL AGREGADA
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<OrderDetail> orderDetails = new ArrayList<>();
    
    // Constructores
    public Order() {
    }
    
    public Order(Integer employeesIdEmployee, Integer customersIdCustomer, Integer tablesIdTables, 
                String costumerName, String employeeName) {
        this.employeesIdEmployee = employeesIdEmployee;
        this.customersIdCustomer = customersIdCustomer;
        this.tablesIdTables = tablesIdTables;
        this.costumerName = costumerName;
        this.employeeName = employeeName;
    }
    
    // Getters y Setters
    public Long getIdOrder() {
        return idOrder;
    }
    
    public void setIdOrder(Long idOrder) {
        this.idOrder = idOrder;
    }
    
    public Integer getEmployeesIdEmployee() {
        return employeesIdEmployee;
    }
    
    public void setEmployeesIdEmployee(Integer employeesIdEmployee) {
        this.employeesIdEmployee = employeesIdEmployee;
    }
    
    public Integer getCustomersIdCustomer() {
        return customersIdCustomer;
    }
    
    public void setCustomersIdCustomer(Integer customersIdCustomer) {
        this.customersIdCustomer = customersIdCustomer;
    }
    
    public Integer getTablesIdTables() {
        return tablesIdTables;
    }
    
    public void setTablesIdTables(Integer tablesIdTables) {
        this.tablesIdTables = tablesIdTables;
    }
    
    public LocalDateTime getOrderDatetime() {
        return orderDatetime;
    }
    
    public void setOrderDatetime(LocalDateTime orderDatetime) {
        this.orderDatetime = orderDatetime;
    }
    
    public String getOrderStatus() {
        return orderStatus;
    }
    
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String getCostumerName() {
        return costumerName;
    }
    
    public void setCostumerName(String costumerName) {
        this.costumerName = costumerName;
    }
    
    public String getEmployeeName() {
        return employeeName;
    }
    
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    // ✅ Getters y Setters para campos de auditoría
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getFechaEdicion() {
        return fechaEdicion;
    }
    
    public void setFechaEdicion(LocalDateTime fechaEdicion) {
        this.fechaEdicion = fechaEdicion;
    }
    
    public LocalDateTime getFechaEliminacionLogica() {
        return fechaEliminacionLogica;
    }
    
    public void setFechaEliminacionLogica(LocalDateTime fechaEliminacionLogica) {
        this.fechaEliminacionLogica = fechaEliminacionLogica;
    }
    
    public LocalDateTime getFechaRestauracionLogica() {
        return fechaRestauracionLogica;
    }
    
    public void setFechaRestauracionLogica(LocalDateTime fechaRestauracionLogica) {
        this.fechaRestauracionLogica = fechaRestauracionLogica;
    }
    
    // ✅ Getter y Setter para orderDetails
    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }
    
    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
    
    // ✅ Método helper para agregar orderDetail
    public void addOrderDetail(OrderDetail orderDetail) {
        orderDetails.add(orderDetail);
        orderDetail.setOrder(this);
    }
    
    // ✅ Método helper para remover orderDetail
    public void removeOrderDetail(OrderDetail orderDetail) {
        orderDetails.remove(orderDetail);
        orderDetail.setOrder(null);
    }
    
    @PrePersist
    protected void onCreate() {
        if (orderDatetime == null) {
            orderDatetime = LocalDateTime.now();
        }
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaEdicion = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "Order{" +
                "idOrder=" + idOrder +
                ", employeesIdEmployee=" + employeesIdEmployee +
                ", customersIdCustomer=" + customersIdCustomer +
                ", tablesIdTables=" + tablesIdTables +
                ", orderDatetime=" + orderDatetime +
                ", orderStatus='" + orderStatus + '\'' +
                ", totalAmount=" + totalAmount +
                ", costumerName='" + costumerName + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", status='" + status + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaEdicion=" + fechaEdicion +
                ", fechaEliminacionLogica=" + fechaEliminacionLogica +
                ", fechaRestauracionLogica=" + fechaRestauracionLogica +
                ", orderDetails=" + orderDetails +
                '}';
    }
}