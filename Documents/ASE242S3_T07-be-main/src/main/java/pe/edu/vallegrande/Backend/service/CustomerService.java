package pe.edu.vallegrande.Backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.vallegrande.Backend.model.Customer;
import pe.edu.vallegrande.Backend.repository.CustomerRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    // 🔹 Listar todos los clientes
    public List<Customer> listarTodos() {
        return customerRepository.findAll();
    }

    // 🔹 Listar clientes por estado (A, I, E)
    public List<Customer> listarPorEstado(String estado) {
        return customerRepository.findByStatus(estado);
    }

    // 🔹 Buscar cliente por ID
    public Optional<Customer> buscarPorId(Integer id) {
        return customerRepository.findById(id);
    }

    // 🔹 Crear nuevo cliente - CORREGIDO: No establecer fechaEdicion al crear
    @Transactional
    public Customer crear(Customer customer) {
        try {
            // Validaciones obligatorias
            if (isBlank(customer.getName()))                 throw new IllegalArgumentException("El nombre del cliente es obligatorio");
            if (isBlank(customer.getLastname()))             throw new IllegalArgumentException("El apellido del cliente es obligatorio");
            if (isBlank(customer.getDocumentoIdentificac())) throw new IllegalArgumentException("El tipo de documento es obligatorio");
            if (isBlank(customer.getDocumentNumber()))       throw new IllegalArgumentException("El número de documento es obligatorio");
            if (customerRepository.existsByDocumentNumber(customer.getDocumentNumber()))
                throw new IllegalArgumentException("Ya existe un cliente con ese número de documento");
            if (isBlank(customer.getEmail()))                throw new IllegalArgumentException("El correo electrónico es obligatorio");
            if (customerRepository.existsByEmail(customer.getEmail()))
                throw new IllegalArgumentException("Ya existe un cliente con ese correo electrónico");
            if (isBlank(customer.getPhone()))                throw new IllegalArgumentException("El teléfono es obligatorio");
            if (isBlank(customer.getLocation()))             throw new IllegalArgumentException("La ubicación es obligatoria");

            // Estado y fecha por defecto
            if (isBlank(customer.getStatus())) {
                customer.setStatus("A");
            }
            if (customer.getFechaCreacion() == null) {
                customer.setFechaCreacion(LocalDateTime.now()); // ✅ Cambiado a LocalDateTime
            }
            // NO establecer fechaEdicion aquí - solo se establece al editar

            Customer guardado = customerRepository.save(customer);
            System.out.println("✅ Cliente creado exitosamente: " + guardado);
            System.out.println("📅 Fecha de creación: " + guardado.getFechaCreacion());
            System.out.println("📝 Fecha de edición: " + guardado.getFechaEdicion() + " (debe ser null)");
            return guardado;

        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Error de integridad: " +
                    (e.getRootCause() != null ? e.getRootCause().getMessage() : e.getMessage()));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al crear cliente: " + e.getMessage());
        }
    }

    // 🔹 Actualizar cliente existente (MERGE PARCIAL + soporta status)
    @Transactional
    public Customer actualizar(Integer id, Customer payload) {
        try {
            Customer existente = customerRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));

            // Validar duplicados SOLO si el campo viene en el payload y cambia
            if (payload.getDocumentNumber() != null
                    && !payload.getDocumentNumber().equals(existente.getDocumentNumber())
                    && customerRepository.existsByDocumentNumber(payload.getDocumentNumber())) {
                throw new IllegalArgumentException("El número de documento ya está registrado en otro cliente");
            }

            if (payload.getEmail() != null
                    && !payload.getEmail().equals(existente.getEmail())
                    && customerRepository.existsByEmail(payload.getEmail())) {
                throw new IllegalArgumentException("El correo ya está registrado en otro cliente");
            }

            // MERGE: solo pisa campos no nulos
            if (payload.getName() != null)                 existente.setName(payload.getName());
            if (payload.getLastname() != null)             existente.setLastname(payload.getLastname());
            if (payload.getDocumentoIdentificac() != null) existente.setDocumentoIdentificac(payload.getDocumentoIdentificac());
            if (payload.getDocumentNumber() != null)       existente.setDocumentNumber(payload.getDocumentNumber());
            if (payload.getEmail() != null)                existente.setEmail(payload.getEmail());
            if (payload.getPhone() != null)                existente.setPhone(payload.getPhone());
            if (payload.getLocation() != null)             existente.setLocation(payload.getLocation());
            if (payload.getStatus() != null)               existente.setStatus(payload.getStatus()); // 👈 habilita PUT { "status": "I|A|E" }
            if (payload.getFechaCreacion() != null)        existente.setFechaCreacion(payload.getFechaCreacion());

            // fechaEdicion se actualiza automáticamente por @PreUpdate en la entidad

            Customer actualizado = customerRepository.save(existente);
            System.out.println("✅ Cliente actualizado correctamente: " + actualizado);
            System.out.println("📅 Fecha de última edición: " + actualizado.getFechaEdicion());
            return actualizado;

        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Error de integridad: " +
                    (e.getRootCause() != null ? e.getRootCause().getMessage() : e.getMessage()));
        }
    }

    // 🔹 Inactivar cliente (status = 'I')
    @Transactional
    public void desactivarCliente(Integer id) {
        Customer cliente = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));

        if ("I".equalsIgnoreCase(cliente.getStatus())) {
            throw new IllegalArgumentException("El cliente ya está inactivo");
        }

        cliente.marcarComoEliminado(); // Usa el nuevo método con auditoría
        customerRepository.save(cliente);
        System.out.println("✅ Cliente inactivado ID: " + id);
        System.out.println("📅 Fecha de eliminación lógica: " + cliente.getFechaEliminacionLogica());
        System.out.println("📝 Fecha de edición actualizada: " + cliente.getFechaEdicion());
    }

    // 🔹 Activar cliente (status = 'A')
    @Transactional
    public void activarCliente(Integer id) {
        Customer cliente = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));

        if ("A".equalsIgnoreCase(cliente.getStatus())) {
            throw new IllegalArgumentException("El cliente ya está activo");
        }

        cliente.marcarComoRestaurado(); // Usa el nuevo método con auditoría
        customerRepository.save(cliente);
        System.out.println("✅ Cliente activado ID: " + id);
        System.out.println("📅 Fecha de restauración: " + cliente.getFechaRestauracionLogica());
        System.out.println("📝 Fecha de edición actualizada: " + cliente.getFechaEdicion());
    }

    // (Opcional) 🔹 Eliminar lógico (status = 'E')
    @Transactional
    public void eliminarLogico(Integer id) {
        Customer cliente = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));
        cliente.marcarComoEliminado(); // Usa el nuevo método con auditoría
        customerRepository.save(cliente);
        System.out.println("✅ Cliente marcado como eliminado ID: " + id);
        System.out.println("📅 Fecha de eliminación lógica: " + cliente.getFechaEliminacionLogica());
        System.out.println("📝 Fecha de edición actualizada: " + cliente.getFechaEdicion());
    }

    // (Opcional) 🔹 Restaurar (E -> A)
    @Transactional
    public void restaurar(Integer id) {
        Customer cliente = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));
        cliente.marcarComoRestaurado(); // Usa el nuevo método con auditoría
        customerRepository.save(cliente);
        System.out.println("✅ Cliente restaurado ID: " + id);
        System.out.println("📅 Fecha de restauración: " + cliente.getFechaRestauracionLogica());
        System.out.println("📝 Fecha de edición actualizada: " + cliente.getFechaEdicion());
    }

    // 🔹 Estadísticas simples
    public String obtenerEstadisticas() {
        long total = customerRepository.count();
        long activos = customerRepository.countByStatus("A");
        long inactivos = customerRepository.countByStatus("I");
        return String.format("Total: %d | Activos: %d | Inactivos: %d", total, activos, inactivos);
    }

    // Helper
    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}