package co.edu.uniquindio.poo.parcia3.model;
/** Cajero: no participa en la producción de pan. */
public class Cajero extends Empleado {
    private String cajaAsignada;
    public Cajero(String codigo,String nombre,String documento,int minutos,String caja){
        super(codigo,nombre,documento,minutos);
        setCajaAsignada(caja);
    }
    public String getCajaAsignada(){ return cajaAsignada; }
    public void setCajaAsignada(String caja){ if(caja==null||caja.isBlank()) throw new IllegalArgumentException("Caja inválida."); cajaAsignada=caja; }
}
