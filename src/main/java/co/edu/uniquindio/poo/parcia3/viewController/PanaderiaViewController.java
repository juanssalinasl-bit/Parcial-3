package co.edu.uniquindio.poo.parcia3.viewController;

import co.edu.uniquindio.poo.parcia3.controller.PanaderiaController;
import co.edu.uniquindio.poo.parcia3.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/** Controla los campos, botones y tablas de la pantalla principal. */
public class PanaderiaViewController {
    @FXML private TextField txtCodigo, txtCliente, txtCantidad, txtCodigoAccion;
    @FXML private TextField txtCodigoEmpleado, txtNombreEmpleado, txtDocumentoEmpleado;
    @FXML private TextField txtMinutosEmpleado, txtCaja;
    @FXML private ComboBox<TipoPan> cmbTipo;
    @FXML private ComboBox<String> cmbCargo;
    @FXML private TableView<Pedido> tablaPedidos;
    @FXML private TableColumn<Pedido, String> colCodigo, colCliente, colEmpleado, colNombrePanadero;
    @FXML private TableColumn<Pedido, TipoPan> colTipo;
    @FXML private TableColumn<Pedido, Integer> colCantidad, colMinutos;
    @FXML private TableColumn<Pedido, EstadoPedido> colEstado;
    @FXML private TableView<Empleado> tablaEmpleados;
    @FXML private TableColumn<Empleado, String> colNombreEmpleado, colCodigoEmpleado, colTipoEmpleado;
    @FXML private TableColumn<Empleado, Integer> colDisponibles;
    @FXML private Label lblEstado, lblCaja;

    private final PanaderiaController controller = new PanaderiaController();

    /** Configura listas desplegables, columnas y datos iniciales de las tablas. */
    @FXML
    public void initialize() {
        cmbTipo.setItems(FXCollections.observableArrayList(TipoPan.values()));
        cmbTipo.getSelectionModel().select(TipoPan.TRADICIONAL);
        cmbCargo.setItems(FXCollections.observableArrayList(
                "Panadero artesanal", "Panadero industrial", "Cajero"));
        cmbCargo.getSelectionModel().selectFirst();
        actualizarCampoCaja();
        cmbCargo.setOnAction(event -> actualizarCampoCaja());

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoPan"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estadoPedido"));
        colEmpleado.setCellValueFactory(new PropertyValueFactory<>("codigoEmpleado"));
        colNombrePanadero.setCellValueFactory(data -> {
            String codigoEmpleado = data.getValue().getCodigoEmpleado();
            if (codigoEmpleado == null) {
                return new SimpleStringProperty("");
            }
            Empleado empleado = PanaderiaController.getPanaderia().buscarEmpleado(codigoEmpleado);
            return new SimpleStringProperty(empleado.getNombre());
        });
        colMinutos.setCellValueFactory(new PropertyValueFactory<>("minutosReservados"));

        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombreEmpleado.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDisponibles.setCellValueFactory(new PropertyValueFactory<>("minutosDisponibles"));
        colTipoEmpleado.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue() instanceof PanaderoArtesanal ? "Artesanal" :
                data.getValue() instanceof PanaderoIndustrial ? "Industrial" : "Cajero"));
        actualizarTablas();
    }

    /** Registra un pedido con los datos escritos en el formulario. */
    @FXML
    private void registrarPedido() {
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            controller.registrarPedido(txtCodigo.getText().trim(), txtCliente.getText().trim(),
                    cmbTipo.getValue(), cantidad);
            limpiarPedido();
            actualizarTablas();
            mostrar("Pedido registrado en estado PENDIENTE.");
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    /** Registra un panadero artesanal, industrial o cajero. */
    @FXML
    private void registrarEmpleado() {
        try {
            int minutos = Integer.parseInt(txtMinutosEmpleado.getText().trim());
            String caja = txtCaja.getText().trim();
            controller.registrarEmpleado(txtCodigoEmpleado.getText().trim(),
                    txtNombreEmpleado.getText().trim(), txtDocumentoEmpleado.getText().trim(),
                    minutos, cmbCargo.getValue(), caja);
            limpiarEmpleado();
            actualizarTablas();
            mostrar("Empleado registrado correctamente.");
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    /** Muestra u oculta el campo de caja según el cargo seleccionado. */
    private void actualizarCampoCaja() {
        boolean esCajero = "Cajero".equals(cmbCargo.getValue());
        lblCaja.setVisible(esCajero);
        lblCaja.setManaged(esCajero);
        txtCaja.setDisable(!esCajero);
        txtCaja.setVisible(esCajero);
        txtCaja.setManaged(esCajero);
    }

    /** Asigna el pedido y muestra el panadero seleccionado. */
    @FXML
    private void asignarPedido() {
        try {
            Empleado empleado = controller.asignarPedido(txtCodigoAccion.getText().trim());
            actualizarTablas();
            mostrar("Pedido asignado a " + empleado.getNombre() + ".");
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    /** Cancela el pedido y actualiza los minutos del empleado si corresponde. */
    @FXML
    private void cancelarPedido() {
        try {
            controller.cancelarPedido(txtCodigoAccion.getText().trim());
            actualizarTablas();
            mostrar("Pedido cancelado. Si estaba asignado, se devolvieron sus minutos.");
        } catch (Exception e) {
            mostrarError(e);
        }
    }

    /** Actualiza las tablas con las listas observables del modelo. */
    @FXML
    private void actualizarTablas() {
        tablaPedidos.setItems(PanaderiaController.getPanaderia().getListaPedidos());
        tablaEmpleados.setItems(PanaderiaController.getPanaderia().getListaPersonas());
        tablaPedidos.refresh();
        tablaEmpleados.refresh();
    }

    private void limpiarPedido() {
        txtCodigo.clear();
        txtCliente.clear();
        txtCantidad.clear();
    }

    private void limpiarEmpleado() {
        txtCodigoEmpleado.clear();
        txtNombreEmpleado.clear();
        txtDocumentoEmpleado.clear();
        txtMinutosEmpleado.clear();
        txtCaja.clear();
    }

    private void mostrar(String mensaje) { lblEstado.setText(mensaje); }
    private void mostrarError(Exception e) { lblEstado.setText("Error: " + e.getMessage()); }
}
