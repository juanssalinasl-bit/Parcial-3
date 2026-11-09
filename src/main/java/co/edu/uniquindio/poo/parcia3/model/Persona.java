package co.edu.uniquindio.poo.parcia3.model;
/** Información común de las personas. */
public abstract class Persona {
    private final String codigo;
    private String nombre;
    private String documento;
    public Persona(String codigo, String nombre, String documento) {
        if (codigo == null || codigo.isBlank() || nombre == null || nombre.isBlank() || documento == null || documento.isBlank())
            throw new IllegalArgumentException("Los datos de la persona son obligatorios.");
        this.codigo=codigo; this.nombre=nombre; this.documento=documento;
    }
    public String getCodigo(){ return codigo; }
    public String getNombre(){ return nombre; }
    public String getDocumento(){ return documento; }
    public void setNombre(String nombre){ if(nombre==null||nombre.isBlank()) throw new IllegalArgumentException("Nombre inválido."); this.nombre=nombre; }
    public void setDocumento(String documento){ if(documento==null||documento.isBlank()) throw new IllegalArgumentException("Documento inválido."); this.documento=documento; }
}
