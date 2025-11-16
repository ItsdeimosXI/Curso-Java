import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("=== Simulador de Impuestos de la Agencia ===");

        List<Contribuyente> padron = new ArrayList<>();

        try {
            PersonaFisicaContribuyente juan = new PersonaFisicaContribuyente(
                "20-12345678-9",
                "Juan Perez",
                "Calle Falsa 123",
                5000000.00
            );
            padron.add(juan);

            PersonaJuridicaContribuyente techSRL = new PersonaJuridicaContribuyente(
                "30-98765432-1",
                "Tecnología del Sur SRL",
                "Av. Siempre Viva 742",
                25000000.00,
                18000000.00
            );
            padron.add(techSRL);

            System.out.println("\n--- Calculando Impuestos del Padrón ---");

            for (Contribuyente c : padron) {
                c.mostrarResumen();
                double impuesto = c.calcularImpuestoAnual();
                
                String montoFormateado = String.format("$%,.2f", impuesto);
                
                System.out.println("Impuesto Anual a Pagar: " + montoFormateado);
                System.out.println("----------------------------------------");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("\nError al procesar un contribuyente: " + e.getMessage());
            System.err.println("La simulación no puede continuar debido a datos inválidos.");
        }
    }
}
