package com.art.ddjj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.art.ddjj.entity.Contribuyente;

public interface ContribuyenteRepository extends JpaRepository<Contribuyente, Long> {

    @Query("SELECT c FROM Contribuyente c where c.cuit = :cuit")
    List<Contribuyente> findByCuit(
    @Param("cuit") String cuit
    );

}
