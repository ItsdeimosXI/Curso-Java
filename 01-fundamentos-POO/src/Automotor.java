public class Automotor implements IRecaudable {
    
    private String patente;
    private String modelo;
    private double valorFiscal;
    private static final double ALICUOTA_AUTOMOTOR = 0.03; // 3%

    public Automotor(String patente, String modelo, double valorFiscal) {
        this.patente = patente;
        this.modelo = modelo;
        this.valorFiscal = valorFiscal;
    }

    public void verDetalles() {
        System.out.println("Automotor - Patente: " + patente + ", Modelo: " + modelo + ", Valor Fiscal: " + valorFiscal);
    }

    @Override
    public double calcularImpuestoAnual() {
        return this.valorFiscal * ALICUOTA_AUTOMOTOR;
    }

}
