package com.art.ddjj.entity;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "declaraciones_juradas")
public class DeclaracionJurada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "impuesto_pagado", nullable = false)
    private String impuestoPagado;

    @Column(name= "monto_total", nullable = false)
    private Double montoTotal;

    @Column(name = "fecha_presentacion", nullable = false)
    private LocalDate fechaPresentacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contribuyente_id", nullable = false)
    private Contribuyente contribuyente;

    public DeclaracionJurada() {
    }

    public DeclaracionJurada(String impuestoPagado, Double montoTotal, Contribuyente contribuyente) {
        this.impuestoPagado = impuestoPagado;
        this.montoTotal = montoTotal;
        this.contribuyente = contribuyente;
        this.fechaPresentacion = LocalDate.now();
    }

    public void mostrarResumen() {
        System.out.println("Esta DDJJ fue emitida el " + fechaPresentacion + " con un importe de " + montoTotal + " por el contribuyente con CUIT: " + contribuyente.getCuit());
    }

}
