package pe.edu.vallegrande.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pe.edu.vallegrande.Backend.model.OrderDetail;

import java.util.List;

public class OrderCompleteRequestDTO {
    
    @NotNull(message = "El ID del empleado es obligatorio")
    private Integer employeesIdEmployee;
    
    @NotNull(message = "El ID del cliente es obligatorio")
    private Integer customersIdCustomer;
    
    @NotNull(message = "El ID de la mesa es obligatorio")
    private Integer tablesIdTables;
    
    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(max = 100, message = "El nombre del cliente no puede exceder 100 caracteres")
    private String costumerName;
    
    @NotBlank(message = "El nombre del empleado es obligatorio")
    @Size(max = 100, message = "El nombre del empleado no puede exceder 100 caracteres")
    private String employeeName;
    
    private String orderStatus = "Pending";
    
    @Valid
    @NotNull(message = "Los detalles de la orden son obligatorios")
    @Size(min = 1, message = "Debe haber al menos un detalle de orden")
    private List<OrderDetail> orderDetails;

    // Constructores
    public OrderCompleteRequestDTO() {
    }

    public OrderCompleteRequestDTO(Integer employeesIdEmployee, Integer customersIdCustomer, 
                                  Integer tablesIdTables, String costumerName, String employeeName, 
                                  List<OrderDetail> orderDetails) {
        this.employeesIdEmployee = employeesIdEmployee;
        this.customersIdCustomer = customersIdCustomer;
        this.tablesIdTables = tablesIdTables;
        this.costumerName = costumerName;
        this.employeeName = employeeName;
        this.orderDetails = orderDetails;
    }

    // Getters y Setters
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

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public String toString() {
        return "OrderCompleteRequestDTO{" +
                "employeesIdEmployee=" + employeesIdEmployee +
                ", customersIdCustomer=" + customersIdCustomer +
                ", tablesIdTables=" + tablesIdTables +
                ", costumerName='" + costumerName + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderDetails=" + orderDetails +
                '}';
    }
}