package pe.edu.vallegrande.Backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pe.edu.vallegrande.Backend.model.Customer;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    // 🔹 Buscar todos los clientes por estado (A o I)
    List<Customer> findByStatus(String status);

    // 🔹 Buscar cliente por ID y estado
    Optional<Customer> findByIdCustomerAndStatus(Integer idCustomer, String status);

    // 🔹 Buscar clientes por nombre o apellido (búsqueda parcial, insensible a mayúsculas)
    List<Customer> findByNameContainingIgnoreCaseOrLastnameContainingIgnoreCase(String name, String lastname);

    // 🔹 Buscar clientes por tipo de documento
    List<Customer> findByDocumentoIdentificac(String documentoIdentificac);

    // 🔹 Buscar cliente por número de documento
    Optional<Customer> findByDocumentNumber(String documentNumber);

    // 🔹 Verificar si existe un cliente con el mismo número de documento
    boolean existsByDocumentNumber(String documentNumber);

    // 🔹 Verificar si existe un cliente con el mismo correo electrónico
    boolean existsByEmail(String email);

    // 🔹 Contar clientes por estado
    long countByStatus(String status);

    // 🔹 Eliminación lógica (cambiar estado a 'I')
    @Modifying
    @Query("UPDATE Customer c SET c.status = 'I' WHERE c.idCustomer = :id")
    void desactivarCliente(@Param("id") Integer id);

    // 🔹 Activar cliente (cambiar estado a 'A')
    @Modifying
    @Query("UPDATE Customer c SET c.status = 'A' WHERE c.idCustomer = :id")
    void activarCliente(@Param("id") Integer id);

    // 🔹 Buscar clientes activos y ordenarlos por nombre ascendente
    List<Customer> findByStatusOrderByNameAsc(String status);

    // 🔹 Buscar clientes activos y ordenarlos por apellido ascendente
    List<Customer> findByStatusOrderByLastnameAsc(String status);

    // 🔹 Buscar clientes por ubicación (búsqueda parcial)
    List<Customer> findByLocationContainingIgnoreCase(String location);

    // 🔹 Buscar clientes creados después de cierta fecha (CORREGIDO: usar fechaCreacion en lugar de createdAt)
    @Query("SELECT c FROM Customer c WHERE c.fechaCreacion >= :fechaInicio AND c.status = :status")
    List<Customer> findByFechaCreacionAfterAndStatus(@Param("fechaInicio") LocalDate fechaInicio,
                                                     @Param("status") String status);
}