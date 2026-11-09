package co.edu.uniquindio.poo.parcia3.controller;

import co.edu.uniquindio.poo.parcia3.model.*;

/**
 * Coordina las operaciones solicitadas por la interfaz y las delega a Panaderia.
 */
public class PanaderiaController {
    private static Panaderia panaderia;

    /** Crea la panadería y sus empleados iniciales una sola vez. */
    public static void inicializarDatos() {
        if (panaderia == null) {
            panaderia = new Panaderia("Panadería El Trigal", "900123456-1");
            panaderia.registrarPersona(new PanaderoArtesanal("PA01", "Ana", "1001", 100));
            panaderia.registrarPersona(new PanaderoIndustrial("PI01", "Carlos", "1002", 120));
            panaderia.registrarPersona(new Cajero("CJ01", "Lucía", "1003", 120, "Caja 1"));
        }
    }

    /** @return la instancia que administra la información de la panadería */
    public static Panaderia getPanaderia() {
        if (panaderia == null) {
            inicializarDatos();
        }
        return panaderia;
    }

    /**
     * Construye y registra un empleado del tipo seleccionado.
     * @param codigo código único del empleado
     * @param nombre nombre completo
     * @param documento documento de identidad
     * @param minutos minutos disponibles al inicio del turno
     * @param tipo tipo de empleado: Artesanal, Industrial o Cajero
     * @param caja caja asignada; solo se utiliza para cajeros
     */
    public void registrarEmpleado(String codigo, String nombre, String documento,
                                  int minutos, String tipo, String caja) {
        Empleado empleado;
        if (tipo == null) {
            throw new IllegalArgumentException("Seleccione el tipo de empleado.");
        }
        switch (tipo) {
            case "Panadero artesanal" -> empleado = new PanaderoArtesanal(codigo, nombre, documento, minutos);
            case "Panadero industrial" -> empleado = new PanaderoIndustrial(codigo, nombre, documento, minutos);
            case "Cajero" -> empleado = new Cajero(codigo, nombre, documento, minutos, caja);
            default -> throw new IllegalArgumentException("Tipo de empleado inválido.");
        }
        getPanaderia().registrarPersona(empleado);
    }

    /** Registra un pedido pendiente sin reservar minutos. */
    public void registrarPedido(String codigo, String cliente, TipoPan tipo, int cantidad) {
        getPanaderia().registrarPedido(new Pedido(codigo, cliente, tipo, cantidad));
    }

    /** Asigna el pedido al panadero disponible que tarda menos. */
    public Empleado asignarPedido(String codigo) {
        return getPanaderia().asignarPedido(codigo);
    }

    /** Cancela un pedido y recupera los minutos si estaba asignado. */
    public void cancelarPedido(String codigo) {
        getPanaderia().cancelarPedido(codigo);
    }
}
