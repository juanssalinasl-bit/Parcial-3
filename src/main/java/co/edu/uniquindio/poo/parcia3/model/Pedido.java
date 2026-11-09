package co.edu.uniquindio.poo.parcia3.model;

/**
 * Representa un pedido de pan realizado por un cliente.
 * Conserva los datos de la solicitud y la información de su asignación.
 */
public class Pedido {
    private final String codigo;
    private final String cliente;
    private final TipoPan tipoPan;
    private final int cantidad;
    private EstadoPedido estadoPedido;
    private String codigoEmpleado;
    private int minutosReservados;

    /**
     * Crea un pedido nuevo en estado PENDIENTE.
     * @param codigo código único del pedido
     * @param cliente nombre del cliente
     * @param tipoPan tipo de pan solicitado
     * @param cantidad cantidad de panes; debe ser mayor que cero
     */
    public Pedido(String codigo, String cliente, TipoPan tipoPan, int cantidad) {
        if (codigo == null || codigo.isBlank() || cliente == null || cliente.isBlank() || tipoPan == null) {
            throw new IllegalArgumentException("Datos del pedido incompletos.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        this.codigo = codigo;
        this.cliente = cliente;
        this.tipoPan = tipoPan;
        this.cantidad = cantidad;
        this.estadoPedido = EstadoPedido.PENDIENTE;
        this.minutosReservados = 0;
    }

    public String getCodigo() { return codigo; }
    public String getCliente() { return cliente; }
    public TipoPan getTipoPan() { return tipoPan; }
    public int getCantidad() { return cantidad; }
    public EstadoPedido getEstadoPedido() { return estadoPedido; }
    public String getCodigoEmpleado() { return codigoEmpleado; }
    public int getMinutosReservados() { return minutosReservados; }

    /**
     * Registra el código del panadero seleccionado y el tiempo reservado para el pedido.
     * @param codigoEmpleado código del panadero
     * @param minutos tiempo reservado para producir el pedido
     */
    public void asignar(String codigoEmpleado, int minutos) {
        if (estadoPedido != EstadoPedido.PENDIENTE) {
            throw new IllegalStateException("El pedido no está pendiente.");
        }
        if (codigoEmpleado == null || codigoEmpleado.isBlank() || minutos <= 0) {
            throw new IllegalArgumentException("Asignación inválida.");
        }
        this.codigoEmpleado = codigoEmpleado;
        this.minutosReservados = minutos;
        this.estadoPedido = EstadoPedido.ASIGNADO;
    }

    /** Cambia el estado del pedido a CANCELADO. */
    public void cancelar() {
        if (estadoPedido == EstadoPedido.CANCELADO) {
            throw new IllegalStateException("El pedido ya está cancelado.");
        }
        estadoPedido = EstadoPedido.CANCELADO;
    }
}
