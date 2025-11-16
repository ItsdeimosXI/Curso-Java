package com.art.ddjj.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "personas_fisicas_contribuyentes")
public class PersonaFisicaContribuyente extends Contribuyente {

    @Column(name = "ingresos_anuales", nullable = false)
    private double ingresosAnuales;


    @Transient
    private static final double ALICUOTA_PERSONA = 0.15; // 15%

    public PersonaFisicaContribuyente(String cuit, String nombre, String domicilio, double ingresosAnuales) {
        super(cuit, nombre, domicilio);
        
        if (ingresosAnuales < 0)
            throw new IllegalArgumentException("Los ingresos anuales no pueden ser negativos.");
        
        this.ingresosAnuales = ingresosAnuales;
    }

    public PersonaFisicaContribuyente() {    }

    @Override
    public double calcularImpuestoAnual() {
        return this.ingresosAnuales * ALICUOTA_PERSONA;
    }
}
