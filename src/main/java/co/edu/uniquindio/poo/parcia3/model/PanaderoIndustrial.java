package co.edu.uniquindio.poo.parcia3.model;
/** Panadero que trabaja con lotes de máximo 30 panes. */
public class PanaderoIndustrial extends Empleado implements IPanadero {
    public PanaderoIndustrial(String codigo,String nombre,String documento,int minutos){ super(codigo,nombre,documento,minutos); }
    @Override public int calcularTiempoProduccion(TipoPan tipoPan,int cantidad){
        if(tipoPan==null||cantidad<=0) throw new IllegalArgumentException("Tipo o cantidad inválida.");
        int lotes=(int)Math.ceil(cantidad/30.0);
        int tiempoLote=tipoPan==TipoPan.TRADICIONAL?25:35;
        return 20+lotes*tiempoLote;
    }
}
