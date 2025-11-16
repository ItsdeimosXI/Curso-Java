package com.art.ddjj;

import com.art.ddjj.entity.Contribuyente;
import com.art.ddjj.entity.PersonaFisicaContribuyente;
import com.art.ddjj.entity.PersonaJuridicaContribuyente;
import com.art.ddjj.entity.DeclaracionJurada;
import com.art.ddjj.repository.ContribuyenteRepository;
import com.art.ddjj.repository.DeclaracionJuradaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class SimulatorRunner implements CommandLineRunner {

    @Autowired
    private ContribuyenteRepository contribuyenteRepository;

    @Autowired
    private DeclaracionJuradaRepository declaracionJuradaRepository;


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("=== Simulador de Impuestos de la Agencia, con Spring ===");

        try {

            System.out.println("\n--- Creando datos de contribuyentes. ---");

            PersonaFisicaContribuyente juan = new PersonaFisicaContribuyente(
                    "20-12345678-9",
                    "Juan Perez",
                    "Calle Falsa 123",
                    5000000.00
            );

            PersonaJuridicaContribuyente techSRL = new PersonaJuridicaContribuyente(
                    "30-98765432-1",
                    "Tecnología del Sur SRL",
                    "Av. Siempre Viva 742",
                    25000000.00,
                    18000000.00
            );

            contribuyenteRepository.save(juan);
            contribuyenteRepository.save(techSRL);

            List<Contribuyente> getByCuit = contribuyenteRepository.findByCuit("20-12345678-9");
            
            System.out.println("\n--- Calculando Impuestos del Padrón (desde la BBDD) ---");

            for (Contribuyente contribuyente : getByCuit) {
                contribuyente.mostrarResumen();

                double monto = contribuyente.calcularImpuestoAnual();
                System.out.println("Impuesto Anual a Pagar: $" + monto);
                DeclaracionJurada ddjj = new DeclaracionJurada("Ingresos Brutos", monto, contribuyente);

                declaracionJuradaRepository.save(ddjj);
                System.out.println("----------------------------------------"); 
            }


            // System.out.println("\n--- Calculando Impuestos del Padrón (desde la BBDD) ---");

            // List<Contribuyente> padron = contribuyenteRepository.findAll();

            // for (Contribuyente contribuyente : padron) {
            //     contribuyente.mostrarResumen();

            //     double monto = contribuyente.calcularImpuestoAnual();
            //     System.out.println("Impuesto Anual a Pagar: $" + monto);

            //     DeclaracionJurada ddjj = new DeclaracionJurada("Ingresos Brutos", monto, contribuyente);
                
            //     declaracionJuradaRepository.save(ddjj);

            //     System.out.println("----------------------------------------"); 
            // }

            System.out.println("\n--- Mostrando Declaraciones Juradas creadas ---");

            //List<DeclaracionJurada> busquedapormonto = declaracionJuradaRepository.BusquedaXmonto();

            //System.out.println(declaracionJuradaRepository.BusquedaXmonto());


            List<DeclaracionJurada> declaraciones = declaracionJuradaRepository.findAll();

            for (DeclaracionJurada ddjj : declaraciones) {
                ddjj.mostrarResumen();
            }

        } catch (IllegalArgumentException e) {
            System.err.println("\nError al procesar un contribuyente: " + e.getMessage());

        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            System.err.println("\nError de BBDD: Probablemente un CUIT duplicado. " + e.getMessage());
        }
    }
}
