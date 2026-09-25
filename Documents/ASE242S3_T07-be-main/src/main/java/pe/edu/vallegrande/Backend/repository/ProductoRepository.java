package pe.edu.vallegrande.Backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pe.edu.vallegrande.Backend.model.Producto;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    // Buscar todos los productos por estado
    List<Producto> findByStatus(String status);
    
    // Buscar producto por ID y estado
    Optional<Producto> findByIdAndStatus(Long id, String status);
    
    // Buscar productos por categoría y estado
    List<Producto> findByCategoryAndStatus(String category, String status);
    
    // Buscar producto por código de producto
    Optional<Producto> findByProductCode(String productCode);
    
    // Verificar si existe un producto con el mismo código
    boolean existsByProductCode(String productCode);
    
    // Buscar productos por nombre (búsqueda parcial)
    List<Producto> findByNameContainingIgnoreCaseAndStatus(String name, String status);
    
    // Buscar productos por rango de precio
    @Query("SELECT p FROM Producto p WHERE p.price BETWEEN :priceMin AND :priceMax AND p.status = :status")
    List<Producto> findByPriceBetweenAndStatus(@Param("priceMin") Double priceMin, 
                                               @Param("priceMax") Double priceMax, 
                                               @Param("status") String status);
    
    // Contar productos por estado
    long countByStatus(String status);
    
    // Contar productos por categoría y estado
    long countByCategoryAndStatus(String category, String status);
    
    // Eliminación lógica (cambiar estado a 'ELIMINADO')
    @Modifying
    @Query("UPDATE Producto p SET p.status = 'ELIMINADO' WHERE p.id = :id")
    void desactivarProducto(@Param("id") Long id);
    
    // Restauración lógica (cambiar estado a 'ACTIVO')
    @Modifying
    @Query("UPDATE Producto p SET p.status = 'ACTIVO' WHERE p.id = :id")
    void activarProducto(@Param("id") Long id);
    
    // Desactivación lógica (cambiar estado a 'INACTIVO')
    @Modifying
    @Query("UPDATE Producto p SET p.status = 'INACTIVO' WHERE p.id = :id")
    void inactivarProducto(@Param("id") Long id);
    
    // Verificar si existe un producto con el mismo nombre (excluyendo el actual)
    @Query("SELECT COUNT(p) > 0 FROM Producto p WHERE p.name = :name AND p.id <> :id")
    boolean existsByNameAndIdNot(@Param("name") String name, @Param("id") Long id);
    
    // Verificar si existe un producto con el mismo código (excluyendo el actual)
    @Query("SELECT COUNT(p) > 0 FROM Producto p WHERE p.productCode = :productCode AND p.id <> :id")
    boolean existsByProductCodeAndIdNot(@Param("productCode") String productCode, @Param("id") Long id);
    
    // Obtener productos ordenados por precio (ascendente)
    List<Producto> findByStatusOrderByPriceAsc(String status);
    
    // Obtener productos ordenados por precio (descendente)
    List<Producto> findByStatusOrderByPriceDesc(String status);
    
    // Obtener productos ordenados por nombre
    List<Producto> findByStatusOrderByNameAsc(String status);
    
    // Obtener categorías distintas de productos activos
    @Query("SELECT DISTINCT p.category FROM Producto p WHERE p.status = 'ACTIVO' ORDER BY p.category")
    List<String> findDistinctCategoriesByStatusActivo();
    
    // Buscar productos por múltiples categorías
    @Query("SELECT p FROM Producto p WHERE p.category IN :categories AND p.status = :status")
    List<Producto> findByCategoryInAndStatus(@Param("categories") List<String> categories, 
                                             @Param("status") String status);
}