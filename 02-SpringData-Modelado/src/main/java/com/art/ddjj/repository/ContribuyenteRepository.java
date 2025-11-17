package com.art.ddjj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.art.ddjj.entity.Contribuyente;
import java.util.List;
import java.util.Optional;

public interface ContribuyenteRepository extends JpaRepository<Contribuyente, Long> {

    // ============================================
    // 1. QUERY METHODS (Derived Query Methods)
    // ============================================
    // Spring genera automáticamente la consulta basándose en el nombre del método
    
    Optional<Contribuyente> findByCuit(String cuit);
    
    List<Contribuyente> findByNombreContaining(String nombre);
    
    List<Contribuyente> findByDomicilioFiscal(String domicilioFiscal);
    
    boolean existsByCuit(String cuit);
    
    long countByDomicilioFiscalContaining(String ciudad);

    // ============================================
    // 2. JPQL QUERIES (con @Query)
    // ============================================
    // Consultas usando JPQL (Java Persistence Query Language)
    // Trabajan con entidades y sus propiedades
    
    @Query("SELECT c FROM Contribuyente c WHERE c.nombre LIKE %:nombre%")
    List<Contribuyente> buscarPorNombreSimilar(@Param("nombre") String nombre);
    
    @Query("SELECT c FROM Contribuyente c WHERE c.cuit = :cuit")
    Optional<Contribuyente> buscarPorCuitJPQL(@Param("cuit") String cuit);
    
    @Query("SELECT c FROM Contribuyente c JOIN c.declaracionesJuradas d WHERE d.montoTotal > :monto")
    List<Contribuyente> buscarContribuyentesConDeclaracionesMayoresA(@Param("monto") Double monto);
    
    @Query("SELECT c FROM Contribuyente c WHERE SIZE(c.declaracionesJuradas) > :cantidad")
    List<Contribuyente> buscarContribuyentesConMasDe(@Param("cantidad") int cantidad);

    // ============================================
    // 3. NATIVE QUERIES (Consultas Nativas SQL)
    // ============================================
    // Consultas usando SQL nativo de la base de datos
    // Trabajan directamente con tablas y columnas
    
    @Query(value = "SELECT * FROM contribuyentes WHERE cuit = :cuit", nativeQuery = true)
    Optional<Contribuyente> buscarPorCuitNativo(@Param("cuit") String cuit);
    
    @Query(value = "SELECT * FROM contribuyentes WHERE nombre LIKE %:nombre%", nativeQuery = true)
    List<Contribuyente> buscarPorNombreNativo(@Param("nombre") String nombre);
    
    @Query(value = "SELECT c.* FROM contribuyentes c " +
                   "INNER JOIN declaraciones_juradas d ON c.id = d.contribuyente_id " +
                   "WHERE d.monto_total > :monto", nativeQuery = true)
    List<Contribuyente> buscarContribuyentesConDeclaracionesMayoresANativo(@Param("monto") Double monto);
    
    @Query(value = "SELECT COUNT(*) FROM contribuyentes WHERE domicilio_fiscal LIKE %:ciudad%", nativeQuery = true)
    long contarPorCiudadNativo(@Param("ciudad") String ciudad);
    
    @Query(value = "SELECT c.* FROM contribuyentes c " +
                   "LEFT JOIN declaraciones_juradas d ON c.id = d.contribuyente_id " +
                   "GROUP BY c.id " +
                   "HAVING COUNT(d.id) > :cantidad", nativeQuery = true)
    List<Contribuyente> buscarContribuyentesConMasDeclaracionesNativo(@Param("cantidad") int cantidad);
    
}
