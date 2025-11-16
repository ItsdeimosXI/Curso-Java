public abstract class Contribuyente implements IRecaudable {
    
    private String cuit;
    private String nombre;
    private String domicilioFiscal;

    public Contribuyente(String cuit, String nombre, String domicilioFiscal) {
        this.cuit = cuit;
        this.nombre = nombre;
        this.domicilioFiscal = domicilioFiscal;
    }

    public String getCuit() {
        return cuit;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDomicilioFiscal() {
        return domicilioFiscal;
    }

    @Override
    public abstract double calcularImpuestoAnual();

    public void mostrarResumen() {
        System.out.println("Mostrando resumen para: " + nombre + " (CUIT: " + cuit + ")");
    }
}
