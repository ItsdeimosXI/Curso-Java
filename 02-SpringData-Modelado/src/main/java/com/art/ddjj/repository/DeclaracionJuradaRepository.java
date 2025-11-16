package com.art.ddjj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.art.ddjj.entity.DeclaracionJurada;

public interface DeclaracionJuradaRepository extends JpaRepository<DeclaracionJurada, Long> {

    @Query(
    value ="Select dj.monto_total, dj.impuesto_pagado, dj.fecha_presentacion, c.cuit, c.nombre from declaraciones_juradas dj where monto_total > 1000000 and inner join contribuyentes c on dj.contribuyente_id = c.id",
    nativeQuery = true
    )
    List<DeclaracionJurada> BusquedaXmonto();

}
