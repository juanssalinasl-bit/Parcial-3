package co.edu.uniquindio.poo.parcia3.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Administra empleados y pedidos de la panadería El Trigal. */
public class Panaderia {
    private final String nombre;
    private final String nit;
    private final ObservableList<Empleado> listaPersonas=FXCollections.observableArrayList();
    private final ObservableList<Pedido> listaPedidos=FXCollections.observableArrayList();

    public Panaderia(String nombre,String nit){
        if(nombre==null||nombre.isBlank()||nit==null||nit.isBlank()) throw new IllegalArgumentException("Nombre y NIT son obligatorios.");
        this.nombre=nombre; this.nit=nit;
    }
    public String getNombre(){return nombre;} public String getNit(){return nit;}
    public ObservableList<Empleado> getListaPersonas(){return FXCollections.unmodifiableObservableList(listaPersonas);}
    public ObservableList<Pedido> getListaPedidos(){return FXCollections.unmodifiableObservableList(listaPedidos);}

    public void registrarPersona(Empleado empleado){
        if(empleado==null) throw new IllegalArgumentException("El empleado no puede ser nulo.");
        for(Empleado actual:listaPersonas){
            if(actual.getCodigo().equalsIgnoreCase(empleado.getCodigo()) || actual.getDocumento().equalsIgnoreCase(empleado.getDocumento()))
                throw new IllegalArgumentException("Ya existe un empleado con ese código o documento.");
        }
        listaPersonas.add(empleado);
    }

    /** Registra el pedido sin reservar tiempo. El código no se reutiliza ni tras cancelar. */
    public void registrarPedido(Pedido pedido){
        if(pedido==null) throw new IllegalArgumentException("El pedido no puede ser nulo.");
        if(buscarPedidoOpcional(pedido.getCodigo())!=null) throw new IllegalArgumentException("El código del pedido ya existe en el historial.");
        listaPedidos.add(pedido);
    }

    public Pedido buscarPedido(String codigo){
        Pedido pedido=buscarPedidoOpcional(codigo);
        if(pedido==null) throw new IllegalArgumentException("No existe un pedido con ese código.");
        return pedido;
    }
    private Pedido buscarPedidoOpcional(String codigo){
        if(codigo==null) return null;
        for(Pedido pedido:listaPedidos) if(pedido.getCodigo().equalsIgnoreCase(codigo)) return pedido;
        return null;
    }

    /** Escoge el panadero factible de menor tiempo; conserva el orden ante empates. */
    public Empleado asignarPedido(String codigo){
        Pedido pedido=buscarPedido(codigo);
        if(pedido.getEstadoPedido()!=EstadoPedido.PENDIENTE) throw new IllegalStateException("El pedido no está pendiente.");
        Empleado seleccionado=null;
        int menorTiempo=Integer.MAX_VALUE;
        for(Empleado empleado:listaPersonas){
            if(empleado instanceof IPanadero){
                IPanadero panadero=(IPanadero)empleado;
                int tiempo=panadero.calcularTiempoProduccion(pedido.getTipoPan(),pedido.getCantidad());
                if(empleado.getMinutosDisponibles()>=tiempo && tiempo<menorTiempo){
                    seleccionado=empleado; menorTiempo=tiempo;
                }
            }
        }
        if(seleccionado==null) throw new IllegalStateException("Ningún panadero tiene tiempo suficiente.");
        seleccionado.descontarMinutos(menorTiempo);
        pedido.asignar(seleccionado.getCodigo(), menorTiempo);
        return seleccionado;
    }

    /** Cancela y, si estaba asignado, devuelve exactamente una vez los minutos reservados. */
    public void cancelarPedido(String codigo){
        Pedido pedido=buscarPedido(codigo);
        if(pedido.getEstadoPedido()==EstadoPedido.CANCELADO) throw new IllegalStateException("El pedido ya está cancelado.");
        if(pedido.getEstadoPedido()==EstadoPedido.ASIGNADO){
            Empleado empleado=buscarEmpleado(pedido.getCodigoEmpleado());
            empleado.devolverMinutos(pedido.getMinutosReservados());
        }
        pedido.cancelar();
    }

    public Empleado buscarEmpleado(String codigo){
        for(Empleado empleado:listaPersonas) if(empleado.getCodigo().equalsIgnoreCase(codigo)) return empleado;
        throw new IllegalArgumentException("No existe el empleado asignado.");
    }
}
