package com.art.ddjj.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "contribuyentes")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Contribuyente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cuit", nullable = false, unique = true)
    private String cuit;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "domicilio_fiscal", nullable = false)
    private String domicilioFiscal;

    @OneToMany(mappedBy = "contribuyente", cascade = CascadeType.ALL)
    private List<DeclaracionJurada> declaracionesJuradas = new ArrayList<>();

    protected Contribuyente(String cuit, String nombre, String domicilioFiscal) {
        this.cuit = cuit;
        this.nombre = nombre;
        this.domicilioFiscal = domicilioFiscal;
    }

    protected Contribuyente() {}

    public abstract double calcularImpuestoAnual();

    public void mostrarResumen() {
        System.out.println("Mostrando resumen para: " + nombre + " (CUIT: " + cuit + ") con domicilio en " + domicilioFiscal);
    }

    public String getCuit() {
        return cuit;
    }
    
}
