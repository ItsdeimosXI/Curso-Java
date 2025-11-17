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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

            // Crear más personas físicas
            PersonaFisicaContribuyente juan = new PersonaFisicaContribuyente(
                    "20-12345678-9",
                    "Juan Perez",
                    "Calle Falsa 123, Buenos Aires",
                    5000000.00
            );

            PersonaFisicaContribuyente maria = new PersonaFisicaContribuyente(
                    "27-23456789-0",
                    "Maria Garcia",
                    "Av. Corrientes 1500, Buenos Aires",
                    7500000.00
            );

            PersonaFisicaContribuyente carlos = new PersonaFisicaContribuyente(
                    "20-34567890-1",
                    "Carlos Rodriguez",
                    "San Martin 890, Córdoba",
                    3200000.00
            );

            // Crear más personas jurídicas
            PersonaJuridicaContribuyente techSRL = new PersonaJuridicaContribuyente(
                    "30-98765432-1",
                    "Tecnología del Sur SRL",
                    "Av. Siempre Viva 742, Buenos Aires",
                    25000000.00,
                    18000000.00
            );

            PersonaJuridicaContribuyente consultoraSA = new PersonaJuridicaContribuyente(
                    "30-87654321-2",
                    "Consultora Empresarial SA",
                    "Pellegrini 450, Rosario",
                    45000000.00,
                    32000000.00
            );

            PersonaJuridicaContribuyente innovaSRL = new PersonaJuridicaContribuyente(
                    "30-76543210-3",
                    "Innova Tech SRL",
                    "Libertad 234, Córdoba",
                    18000000.00,
                    12000000.00
            );

            contribuyenteRepository.save(juan);
            contribuyenteRepository.save(maria);
            contribuyenteRepository.save(carlos);
            contribuyenteRepository.save(techSRL);
            contribuyenteRepository.save(consultoraSA);
            contribuyenteRepository.save(innovaSRL);

            System.out.println("\n--- Calculando Impuestos del Padrón (desde la BBDD) ---");

            List<Contribuyente> padron = contribuyenteRepository.findAll();

            for (Contribuyente contribuyente : padron) {
                contribuyente.mostrarResumen();

                double monto = contribuyente.calcularImpuestoAnual();
                System.out.println("Impuesto Anual a Pagar: $" + monto);

                // Crear declaraciones con diferentes impuestos
                String tipoImpuesto = monto > 1000000 ? "Ganancias" : "Ingresos Brutos";
                DeclaracionJurada ddjj = new DeclaracionJurada(tipoImpuesto, monto, contribuyente);
                declaracionJuradaRepository.save(ddjj);

                // Crear una segunda declaración para algunos contribuyentes
                if (monto > 2000000) {
                    DeclaracionJurada ddjj2 = new DeclaracionJurada("IVA", monto * 0.21, contribuyente);
                    declaracionJuradaRepository.save(ddjj2);
                }

                System.out.println("----------------------------------------"); 
            }

            System.out.println("\n--- Mostrando Declaraciones Juradas creadas ---");
            List<DeclaracionJurada> declaraciones = declaracionJuradaRepository.findAll();
            for (DeclaracionJurada ddjj : declaraciones) {
                ddjj.mostrarResumen();
            }

            // ========================================
            // PROBANDO QUERY METHODS (Derived Queries)
            // ========================================
            System.out.println("\n\n=== PROBANDO QUERY METHODS ===");
            
            System.out.println("\n--- Buscar contribuyente por CUIT (findByCuit) ---");
            Optional<Contribuyente> contribByCuit = contribuyenteRepository.findByCuit("20-12345678-9");
            contribByCuit.ifPresent(c -> System.out.println("Encontrado: " + c.getCuit()));

            System.out.println("\n--- Buscar contribuyentes por nombre (findByNombreContaining) ---");
            List<Contribuyente> contribPorNombre = contribuyenteRepository.findByNombreContaining("Tech");
            System.out.println("Contribuyentes con 'Tech' en el nombre: " + contribPorNombre.size());
            contribPorNombre.forEach(c -> c.mostrarResumen());

            System.out.println("\n--- Verificar si existe CUIT (existsByCuit) ---");
            boolean existe = contribuyenteRepository.existsByCuit("30-98765432-1");
            System.out.println("¿Existe el CUIT 30-98765432-1?: " + existe);

            System.out.println("\n--- Contar contribuyentes de Buenos Aires (countByDomicilioFiscalContaining) ---");
            long cantidadBA = contribuyenteRepository.countByDomicilioFiscalContaining("Buenos Aires");
            System.out.println("Contribuyentes en Buenos Aires: " + cantidadBA);

            System.out.println("\n--- Declaraciones con monto mayor a 1.000.000 (findByMontoTotalGreaterThan) ---");
            List<DeclaracionJurada> declaracionesAltas = declaracionJuradaRepository.findByMontoTotalGreaterThan(1000000.0);
            System.out.println("Declaraciones con monto > 1.000.000: " + declaracionesAltas.size());

            System.out.println("\n--- Declaraciones ordenadas por monto (findByMontoTotalGreaterThanOrderByMontoTotalDesc) ---");
            List<DeclaracionJurada> declaracionesOrdenadas = declaracionJuradaRepository
                    .findByMontoTotalGreaterThanOrderByMontoTotalDesc(0.0);
            System.out.println("Top 3 declaraciones por monto:");
            declaracionesOrdenadas.stream().limit(3).forEach(DeclaracionJurada::mostrarResumen);

            // ========================================
            // PROBANDO JPQL QUERIES
            // ========================================
            System.out.println("\n\n=== PROBANDO JPQL QUERIES ===");

            System.out.println("\n--- Buscar por nombre similar JPQL (buscarPorNombreSimilar) ---");
            List<Contribuyente> similares = contribuyenteRepository.buscarPorNombreSimilar("Rodriguez");
            System.out.println("Contribuyentes con nombre similar a 'Rodriguez': " + similares.size());
            similares.forEach(c -> c.mostrarResumen());

            System.out.println("\n--- Contribuyentes con declaraciones > 2.000.000 (buscarContribuyentesConDeclaracionesMayoresA) ---");
            List<Contribuyente> contribAltas = contribuyenteRepository.buscarContribuyentesConDeclaracionesMayoresA(2000000.0);
            System.out.println("Contribuyentes con declaraciones > 2.000.000: " + contribAltas.size());
            contribAltas.forEach(c -> c.mostrarResumen());

            System.out.println("\n--- Contribuyentes con más de 1 declaración (buscarContribuyentesConMasDe) ---");
            List<Contribuyente> contribMultiples = contribuyenteRepository.buscarContribuyentesConMasDe(1);
            System.out.println("Contribuyentes con más de 1 declaración: " + contribMultiples.size());
            contribMultiples.forEach(c -> c.mostrarResumen());

            System.out.println("\n--- Declaraciones por CUIT (buscarPorCuitContribuyente) ---");
            List<DeclaracionJurada> declPorCuit = declaracionJuradaRepository.buscarPorCuitContribuyente("30-98765432-1");
            System.out.println("Declaraciones del CUIT 30-98765432-1: " + declPorCuit.size());

            System.out.println("\n--- Calcular monto total por contribuyente (calcularMontoTotalPorContribuyente) ---");
            Long juanId = juan.getId();
            if (juanId != null) {
                Double montoTotal = declaracionJuradaRepository.calcularMontoTotalPorContribuyente(juanId);
                System.out.println("Monto total de declaraciones de Juan: $" + montoTotal);
            }

            System.out.println("\n--- Promedio de montos por impuesto (calcularPromedioMontosPorImpuesto) ---");
            Double promedioGanancias = declaracionJuradaRepository.calcularPromedioMontosPorImpuesto("Ganancias");
            System.out.println("Promedio de declaraciones de Ganancias: $" + promedioGanancias);

            // ========================================
            // PROBANDO NATIVE QUERIES
            // ========================================
            System.out.println("\n\n=== PROBANDO NATIVE QUERIES ===");

            System.out.println("\n--- Buscar por CUIT nativo (buscarPorCuitNativo) ---");
            Optional<Contribuyente> contribNativo = contribuyenteRepository.buscarPorCuitNativo("27-23456789-0");
            contribNativo.ifPresent(c -> System.out.println("Encontrado con query nativa: " + c.getCuit()));

            System.out.println("\n--- Buscar por nombre nativo (buscarPorNombreNativo) ---");
            List<Contribuyente> contribNombreNativo = contribuyenteRepository.buscarPorNombreNativo("SRL");
            System.out.println("Contribuyentes con 'SRL' (query nativa): " + contribNombreNativo.size());

            System.out.println("\n--- Contar por ciudad nativo (contarPorCiudadNativo) ---");
            long contarCordoba = contribuyenteRepository.contarPorCiudadNativo("Córdoba");
            System.out.println("Contribuyentes en Córdoba (query nativa): " + contarCordoba);

            System.out.println("\n--- Declaraciones con monto mayor nativo (buscarPorMontoMayorANativo) ---");
            List<DeclaracionJurada> declNativas = declaracionJuradaRepository.buscarPorMontoMayorANativo(1500000.0);
            System.out.println("Declaraciones > 1.500.000 (query nativa): " + declNativas.size());

            System.out.println("\n--- Declaración con monto máximo (buscarDeclaracionConMontoMaximoNativo) ---");
            List<DeclaracionJurada> maxDecl = declaracionJuradaRepository.buscarDeclaracionConMontoMaximoNativo();
            if (!maxDecl.isEmpty()) {
                System.out.println("Declaración con monto máximo:");
                maxDecl.get(0).mostrarResumen();
            }

            System.out.println("\n--- Estadísticas por impuesto (obtenerEstadisticasPorImpuesto) ---");
            List<Object[]> estadisticas = declaracionJuradaRepository.obtenerEstadisticasPorImpuesto();
            System.out.println("Estadísticas por tipo de impuesto:");
            for (Object[] stat : estadisticas) {
                System.out.println("  Impuesto: " + stat[0] + ", Cantidad: " + stat[1] + ", Total: $" + stat[2]);
            }

            System.out.println("\n\n=== FIN DE LAS PRUEBAS ===");

        } catch (IllegalArgumentException e) {
            System.err.println("\nError al procesar un contribuyente: " + e.getMessage());

        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            System.err.println("\nError de BBDD: Probablemente un CUIT duplicado. " + e.getMessage());
        }
    }
}
