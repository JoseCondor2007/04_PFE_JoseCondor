package pe.edu.vallegrande.Backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.math.BigDecimal;

@Entity
@Table(name = "Order_Detail")
public class OrderDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order_detail")
    private Long idOrderDetail;
    
    @NotNull(message = "El ID del producto es obligatorio")
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    // ✅ RELACIÓN CORREGIDA CON ORDER
    @NotNull(message = "El pedido es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Order_Id_order", nullable = false)
    @JsonBackReference
    private Order order;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    
    @NotNull(message = "El subtotal es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El subtotal no puede ser negativo")
    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    // Constructores
    public OrderDetail() {
    }
    
    public OrderDetail(Long productId, Order order, Integer quantity, BigDecimal subtotal) {
        this.productId = productId;
        this.order = order;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }
    
    // Constructor alternativo sin Order (para casos específicos)
    public OrderDetail(Long productId, Integer quantity, BigDecimal subtotal) {
        this.productId = productId;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }
    
    // Getters y Setters
    public Long getIdOrderDetail() {
        return idOrderDetail;
    }
    
    public void setIdOrderDetail(Long idOrderDetail) {
        this.idOrderDetail = idOrderDetail;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    // ✅ Getter y Setter corregidos para Order
    public Order getOrder() {
        return order;
    }
    
    public void setOrder(Order order) {
        this.order = order;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    
    @Override
    public String toString() {
        return "OrderDetail{" +
                "idOrderDetail=" + idOrderDetail +
                ", productId=" + productId +
                ", order=" + (order != null ? order.getIdOrder() : "null") +
                ", quantity=" + quantity +
                ", subtotal=" + subtotal +
                '}';
    }
}