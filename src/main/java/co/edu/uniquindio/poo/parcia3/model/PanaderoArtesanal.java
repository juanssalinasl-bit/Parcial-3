package co.edu.uniquindio.poo.parcia3.model;
/** Panadero que trabaja con bandejas de máximo 12 panes. */
public class PanaderoArtesanal extends Empleado implements IPanadero {
    public PanaderoArtesanal(String codigo,String nombre,String documento,int minutos){ super(codigo,nombre,documento,minutos); }
    @Override public int calcularTiempoProduccion(TipoPan tipoPan,int cantidad){
        validar(tipoPan,cantidad);
        int bandejas=(int)Math.ceil(cantidad/12.0);
        return bandejas*(tipoPan==TipoPan.TRADICIONAL?30:40);
    }
    private void validar(TipoPan tipo,int cantidad){ if(tipo==null||cantidad<=0) throw new IllegalArgumentException("Tipo o cantidad inválida."); }
}
