package com.art.ddjj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.art.ddjj.entity.DeclaracionJurada;
import java.time.LocalDate;
import java.util.List;

public interface DeclaracionJuradaRepository extends JpaRepository<DeclaracionJurada, Long> {

    // ============================================
    // 1. QUERY METHODS (Derived Query Methods)
    // ============================================
    
    List<DeclaracionJurada> findByContribuyenteId(Long contribuyenteId);
    
    List<DeclaracionJurada> findByMontoTotalGreaterThan(Double monto);
    
    List<DeclaracionJurada> findByFechaPresentacionBetween(LocalDate fechaInicio, LocalDate fechaFin);
    
    List<DeclaracionJurada> findByImpuestoPagado(String impuestoPagado);
    
    List<DeclaracionJurada> findByMontoTotalGreaterThanOrderByMontoTotalDesc(Double monto);
    
    long countByContribuyenteId(Long contribuyenteId);

    // ============================================
    // 2. JPQL QUERIES (con @Query)
    // ============================================
    
    @Query("SELECT d FROM DeclaracionJurada d WHERE d.contribuyente.cuit = :cuit")
    List<DeclaracionJurada> buscarPorCuitContribuyente(@Param("cuit") String cuit);
    
    @Query("SELECT d FROM DeclaracionJurada d WHERE d.montoTotal > :monto AND d.fechaPresentacion >= :fecha")
    List<DeclaracionJurada> buscarPorMontoYFecha(@Param("monto") Double monto, @Param("fecha") LocalDate fecha);
    
    @Query("SELECT SUM(d.montoTotal) FROM DeclaracionJurada d WHERE d.contribuyente.id = :contribuyenteId")
    Double calcularMontoTotalPorContribuyente(@Param("contribuyenteId") Long contribuyenteId);
    
    @Query("SELECT d FROM DeclaracionJurada d JOIN FETCH d.contribuyente WHERE d.montoTotal > :monto")
    List<DeclaracionJurada> buscarConContribuyenteCargado(@Param("monto") Double monto);
    
    @Query("SELECT AVG(d.montoTotal) FROM DeclaracionJurada d WHERE d.impuestoPagado = :impuesto")
    Double calcularPromedioMontosPorImpuesto(@Param("impuesto") String impuesto);

    // ============================================
    // 3. NATIVE QUERIES (Consultas Nativas SQL)
    // ============================================
    
    @Query(value = "SELECT * FROM declaraciones_juradas WHERE contribuyente_id = :id", nativeQuery = true)
    List<DeclaracionJurada> buscarPorContribuyenteIdNativo(@Param("id") Long contribuyenteId);
    
    @Query(value = "SELECT * FROM declaraciones_juradas WHERE monto_total > :monto ORDER BY monto_total DESC", nativeQuery = true)
    List<DeclaracionJurada> buscarPorMontoMayorANativo(@Param("monto") Double monto);
    
    @Query(value = "SELECT * FROM declaraciones_juradas " +
                   "WHERE fecha_presentacion BETWEEN :fechaInicio AND :fechaFin", nativeQuery = true)
    List<DeclaracionJurada> buscarPorRangoFechasNativo(@Param("fechaInicio") LocalDate fechaInicio, 
                                                        @Param("fechaFin") LocalDate fechaFin);
    
    @Query(value = "SELECT d.* FROM declaraciones_juradas d " +
                   "INNER JOIN contribuyentes c ON d.contribuyente_id = c.id " +
                   "WHERE c.cuit = :cuit", nativeQuery = true)
    List<DeclaracionJurada> buscarPorCuitContribuyenteNativo(@Param("cuit") String cuit);
    
    @Query(value = "SELECT impuesto_pagado, COUNT(*), SUM(monto_total) " +
                   "FROM declaraciones_juradas " +
                   "GROUP BY impuesto_pagado", nativeQuery = true)
    List<Object[]> obtenerEstadisticasPorImpuesto();
    
    @Query(value = "SELECT d.* FROM declaraciones_juradas d " +
                   "WHERE d.monto_total = (SELECT MAX(monto_total) FROM declaraciones_juradas)", nativeQuery = true)
    List<DeclaracionJurada> buscarDeclaracionConMontoMaximoNativo();

}
