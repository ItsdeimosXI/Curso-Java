package com.art.ddjj.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "personas_juridicas_contribuyentes")
public class PersonaJuridicaContribuyente extends Contribuyente {

    @Column(name = "facturacion_anual", nullable = false)
    private double facturacionAnual;

    @Column(name = "costos_operativos", nullable = false)
    private double costosOperativos;


    @Transient
    private static final double ALICUOTA_EMPRESA = 0.25; // 25%

    public PersonaJuridicaContribuyente(String cuit, String nombre, String domicilio, double facturacion, double costos) {
        super(cuit, nombre, domicilio);

        if (facturacion < 0) 
            throw new IllegalArgumentException("La facturación anual no puede ser negativa.");
        
        if (costos < 0)  
            throw new IllegalArgumentException("Los costos operativos no pueden ser negativos.");
        
        this.facturacionAnual = facturacion;
        this.costosOperativos = costos;
    }

    public PersonaJuridicaContribuyente() {}

    private double getGananciaNeta() {
        double ganancia = facturacionAnual - costosOperativos;
        return Math.max(0, ganancia);
    }

    @Override
    public double calcularImpuestoAnual() {
        double ganancia = getGananciaNeta();
        return ganancia * ALICUOTA_EMPRESA;
    }
}
