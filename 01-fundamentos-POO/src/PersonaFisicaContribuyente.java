public class PersonaFisicaContribuyente extends Contribuyente {
    
    private double ingresosAnuales;
    private static final double ALICUOTA_PERSONA = 0.15; // 15%

    public PersonaFisicaContribuyente(String cuit, String nombre, String domicilio, double ingresosAnuales) {
        super(cuit, nombre, domicilio);
        
        if (ingresosAnuales < 0)
            throw new IllegalArgumentException("Los ingresos anuales no pueden ser negativos.");
        
        this.ingresosAnuales = ingresosAnuales;
    }

    public double getIngresosAnuales() {
        return ingresosAnuales;
    }

    @Override
    public double calcularImpuestoAnual() {
        return this.ingresosAnuales * ALICUOTA_PERSONA;
    }
}
