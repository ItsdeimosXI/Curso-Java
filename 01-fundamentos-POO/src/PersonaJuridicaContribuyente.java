public class PersonaJuridicaContribuyente extends Contribuyente {
    
    private double facturacionAnual;
    private double costosOperativos;
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
