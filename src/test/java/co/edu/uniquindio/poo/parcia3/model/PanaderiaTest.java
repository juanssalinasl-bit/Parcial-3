package co.edu.uniquindio.poo.parcia3.model;

import org.junit.jupiter.api.Test;
import java.util.logging.Logger;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de las funcionalidades de registro, asignación y cancelación
 * de pedidos de la Panadería El Trigal.
 * Cada prueba prepara sus propios datos y verifica resultados y cambios de estado.
 * @author Área de Programación
 * @since 2026-11
 */
class PanaderiaTest {

    /** Instancia para registrar el inicio y final de cada prueba. */
    private static final Logger LOG = Logger.getLogger(PanaderiaTest.class.getName());

    /**
     * Verifica que registrar un pedido de 25 panes lo deje PENDIENTE,
     * conserve la cantidad y no descuente minutos a ningún empleado.
     */
    @Test
    void registrarPedidoPendienteNoDescuentaMinutos() {
        LOG.info("Inicio de prueba registrarPedidoPendienteNoDescuentaMinutos...");
        Panaderia panaderia = new Panaderia("El Trigal", "900");
        PanaderoArtesanal ana = new PanaderoArtesanal("A1", "Ana", "1", 100);
        PanaderoIndustrial carlos = new PanaderoIndustrial("I1", "Carlos", "2", 120);
        panaderia.registrarPersona(ana);
        panaderia.registrarPersona(carlos);

        Pedido pedido = new Pedido("P01", "Cafetería", TipoPan.TRADICIONAL, 25);
        panaderia.registrarPedido(pedido);

        assertEquals(EstadoPedido.PENDIENTE, pedido.getEstadoPedido());
        assertEquals(25, pedido.getCantidad());
        assertEquals(1, panaderia.getListaPedidos().size());
        assertEquals(100, ana.getMinutosDisponibles());
        assertEquals(120, carlos.getMinutosDisponibles());
        LOG.info("Fin de prueba registrarPedidoPendienteNoDescuentaMinutos.");
    }

    /**
     * Comprueba que el código de un pedido cancelado no pueda reutilizarse
     * y que el historial conserve únicamente el pedido original.
     */
    @Test
    void codigoNoSePuedeReutilizarDespuesDeCancelar() {
        LOG.info("Inicio de prueba codigoNoSePuedeReutilizarDespuesDeCancelar...");
        Panaderia panaderia = new Panaderia("El Trigal", "900");
        Pedido original = new Pedido("P01", "Restaurante", TipoPan.INTEGRAL, 10);
        panaderia.registrarPedido(original);
        panaderia.cancelarPedido("P01");

        assertThrows(IllegalArgumentException.class, () ->
                panaderia.registrarPedido(
                        new Pedido("P01", "Otro cliente", TipoPan.TRADICIONAL, 5)));
        assertEquals(1, panaderia.getListaPedidos().size());
        assertSame(original, panaderia.getListaPedidos().get(0));
        assertEquals(EstadoPedido.CANCELADO, original.getEstadoPedido());
        LOG.info("Fin de prueba codigoNoSePuedeReutilizarDespuesDeCancelar.");
    }

    /**
     * Verifica que no se pueda registrar un pedido de cero panes
     * y que el historial permanezca vacío.
     */
    @Test
    void noRegistraCantidadCero() {
        LOG.info("Inicio de prueba noRegistraCantidadCero...");
        Panaderia panaderia = new Panaderia("El Trigal", "900");

        assertThrows(IllegalArgumentException.class, () ->
                panaderia.registrarPedido(
                        new Pedido("P01", "Cliente", TipoPan.TRADICIONAL, 0)));
        assertTrue(panaderia.getListaPedidos().isEmpty());
        LOG.info("Fin de prueba noRegistraCantidadCero.");
    }

    /**
     * Verifica que para 25 panes tradicionales se seleccione a Carlos,
     * quien tarda 45 minutos, y que Ana conserve sus 100 minutos.
     */
    @Test
    void asignaAlPanaderoQueTardaMenos() {
        LOG.info("Inicio de prueba asignaAlPanaderoQueTardaMenos...");
        Panaderia panaderia = new Panaderia("El Trigal", "900");
        PanaderoArtesanal ana = new PanaderoArtesanal("A1", "Ana", "1", 100);
        PanaderoIndustrial carlos = new PanaderoIndustrial("I1", "Carlos", "2", 45);
        panaderia.registrarPersona(ana);
        panaderia.registrarPersona(carlos);
        panaderia.registrarPedido(new Pedido("P01", "Café", TipoPan.TRADICIONAL, 25));

        Empleado asignado = panaderia.asignarPedido("P01");
        Pedido pedido = panaderia.buscarPedido("P01");

        assertSame(carlos, asignado);
        assertEquals(EstadoPedido.ASIGNADO, pedido.getEstadoPedido());
        assertEquals("I1", pedido.getCodigoEmpleado());
        assertEquals("Carlos", panaderia.buscarEmpleado(pedido.getCodigoEmpleado()).getNombre());
        assertEquals(45, pedido.getMinutosReservados());
        assertEquals(100, ana.getMinutosDisponibles());
        assertEquals(0, carlos.getMinutosDisponibles());
        LOG.info("Fin de prueba asignaAlPanaderoQueTardaMenos.");
    }

    /**
     * Verifica que si dos panaderos tienen el mismo tiempo requerido,
     * se seleccione el primero registrado y se descarte a quien no tiene minutos suficientes.
     */
    @Test
    void empateSeResuelvePorOrdenDeRegistro() {
        LOG.info("Inicio de prueba empateSeResuelvePorOrdenDeRegistro...");
        Panaderia panaderia = new Panaderia("El Trigal", "900");
        PanaderoArtesanal ana = new PanaderoArtesanal("A1", "Ana", "1", 80);
        PanaderoArtesanal diana = new PanaderoArtesanal("A2", "Diana", "2", 80);
        PanaderoIndustrial carlos = new PanaderoIndustrial("I1", "Carlos", "3", 54);
        panaderia.registrarPersona(ana);
        panaderia.registrarPersona(diana);
        panaderia.registrarPersona(carlos);
        panaderia.registrarPedido(new Pedido("P02", "Restaurante", TipoPan.INTEGRAL, 13));

        Empleado asignado = panaderia.asignarPedido("P02");
        Pedido pedido = panaderia.buscarPedido("P02");

        assertSame(ana, asignado);
        assertEquals("Ana", panaderia.buscarEmpleado(pedido.getCodigoEmpleado()).getNombre());
        assertEquals(EstadoPedido.ASIGNADO, pedido.getEstadoPedido());
        assertEquals(0, ana.getMinutosDisponibles());
        assertEquals(80, diana.getMinutosDisponibles());
        assertEquals(54, carlos.getMinutosDisponibles());
        LOG.info("Fin de prueba empateSeResuelvePorOrdenDeRegistro.");
    }

    /**
     * Comprueba que un cajero no sea seleccionado y que, si ningún panadero
     * tiene tiempo suficiente, el pedido siga PENDIENTE sin modificar minutos.
     */
    @Test
    void cajeroNoPuedeRecibirPedidoYNoHayPanaderoConTiempo() {
        LOG.info("Inicio de prueba cajeroNoPuedeRecibirPedidoYNoHayPanaderoConTiempo...");
        Panaderia panaderia = new Panaderia("El Trigal", "900");
        PanaderoIndustrial carlos = new PanaderoIndustrial("I1", "Carlos", "1", 44);
        Cajero lucia = new Cajero("C1", "Lucía", "2", 120, "Caja 1");
        panaderia.registrarPersona(carlos);
        panaderia.registrarPersona(lucia);
        panaderia.registrarPedido(new Pedido("P03", "Café", TipoPan.TRADICIONAL, 25));

        assertThrows(IllegalStateException.class, () -> panaderia.asignarPedido("P03"));
        assertEquals(EstadoPedido.PENDIENTE, panaderia.buscarPedido("P03").getEstadoPedido());
        assertNull(panaderia.buscarPedido("P03").getCodigoEmpleado());
        assertEquals(44, carlos.getMinutosDisponibles());
        assertEquals(120, lucia.getMinutosDisponibles());
        LOG.info("Fin de prueba cajeroNoPuedeRecibirPedidoYNoHayPanaderoConTiempo.");
    }

    /**
     * Verifica que al cancelar uno de dos pedidos asignados se devuelvan
     * únicamente sus 45 minutos y el segundo pedido permanezca ASIGNADO.
     */
    @Test
    void cancelarPedidoAsignadoDevuelveSoloSusMinutos() {
        LOG.info("Inicio de prueba cancelarPedidoAsignadoDevuelveSoloSusMinutos...");
        Panaderia panaderia = new Panaderia("El Trigal", "900");
        PanaderoIndustrial carlos = new PanaderoIndustrial("I1", "Carlos", "1", 120);
        panaderia.registrarPersona(carlos);
        panaderia.registrarPedido(new Pedido("P01", "Café", TipoPan.TRADICIONAL, 25));
        panaderia.registrarPedido(new Pedido("P02", "Café", TipoPan.TRADICIONAL, 25));
        panaderia.asignarPedido("P01");
        panaderia.asignarPedido("P02");

        panaderia.cancelarPedido("P01");

        assertEquals(75, carlos.getMinutosDisponibles());
        assertEquals(EstadoPedido.CANCELADO, panaderia.buscarPedido("P01").getEstadoPedido());
        assertEquals(EstadoPedido.ASIGNADO, panaderia.buscarPedido("P02").getEstadoPedido());
        assertEquals(45, panaderia.buscarPedido("P02").getMinutosReservados());
        LOG.info("Fin de prueba cancelarPedidoAsignadoDevuelveSoloSusMinutos.");
    }

    /**
     * Comprueba que cancelar un pedido PENDIENTE lo conserve en el historial
     * y no modifique los minutos disponibles del panadero.
     */
    @Test
    void cancelarPedidoPendienteNoDescuentaMinutos() {
        LOG.info("Inicio de prueba cancelarPedidoPendienteNoDescuentaMinutos...");
        Panaderia panaderia = new Panaderia("El Trigal", "900");
        PanaderoArtesanal ana = new PanaderoArtesanal("A1", "Ana", "1", 100);
        panaderia.registrarPersona(ana);
        panaderia.registrarPedido(new Pedido("P01", "Cliente", TipoPan.TRADICIONAL, 12));

        panaderia.cancelarPedido("P01");

        assertEquals(EstadoPedido.CANCELADO, panaderia.buscarPedido("P01").getEstadoPedido());
        assertEquals(1, panaderia.getListaPedidos().size());
        assertEquals(100, ana.getMinutosDisponibles());
        LOG.info("Fin de prueba cancelarPedidoPendienteNoDescuentaMinutos.");
    }

    /**
     * Verifica que cancelar dos veces un pedido lance IllegalStateException
     * y que los minutos reservados se devuelvan una sola vez.
     */
    @Test
    void noDevuelveMinutosDosVeces() {
        LOG.info("Inicio de prueba noDevuelveMinutosDosVeces...");
        Panaderia panaderia = new Panaderia("El Trigal", "900");
        PanaderoIndustrial carlos = new PanaderoIndustrial("I1", "Carlos", "1", 120);
        panaderia.registrarPersona(carlos);
        panaderia.registrarPedido(new Pedido("P01", "Cliente", TipoPan.TRADICIONAL, 25));
        panaderia.asignarPedido("P01");

        panaderia.cancelarPedido("P01");

        assertEquals(120, carlos.getMinutosDisponibles());
        assertEquals(EstadoPedido.CANCELADO, panaderia.buscarPedido("P01").getEstadoPedido());
        assertThrows(IllegalStateException.class, () -> panaderia.cancelarPedido("P01"));
        assertEquals(120, carlos.getMinutosDisponibles());
        LOG.info("Fin de prueba noDevuelveMinutosDosVeces.");
    }
}
