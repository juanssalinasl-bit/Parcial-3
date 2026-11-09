package co.edu.uniquindio.poo.parcia3.model;
/** Clase base para todos los empleados. */
public abstract class Empleado extends Persona {
    private int minutosDisponibles;
    public Empleado(String codigo,String nombre,String documento,int minutosDisponibles){
        super(codigo,nombre,documento);
        if(minutosDisponibles<0) throw new IllegalArgumentException("Los minutos no pueden ser negativos.");
        this.minutosDisponibles=minutosDisponibles;
    }
    public int getMinutosDisponibles(){ return minutosDisponibles; }
    public void descontarMinutos(int minutos){
        if(minutos<0||minutos>minutosDisponibles) throw new IllegalArgumentException("No hay suficientes minutos.");
        minutosDisponibles-=minutos;
    }
    public void devolverMinutos(int minutos){ if(minutos<0) throw new IllegalArgumentException("Minutos inválidos."); minutosDisponibles+=minutos; }
}
