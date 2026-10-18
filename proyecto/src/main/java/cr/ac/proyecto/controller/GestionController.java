package cr.ac.proyecto.controller;

import cr.ac.proyecto.App;
import cr.ac.proyecto.exception.EncuentroInvalidoException;
import cr.ac.proyecto.model.Cancelable;
import cr.ac.proyecto.model.Encuentro;
import cr.ac.proyecto.model.Equipo;
import cr.ac.proyecto.model.EstadoEncuentro;
import cr.ac.proyecto.model.Torneo;
import cr.ac.proyecto.service.GestorTorneos;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class GestionController {

    @FXML
    private ComboBox<Torneo> cbTorneo;

    @FXML
    private TableView<Encuentro> tblProgramados;

    @FXML
    private TableColumn<Encuentro, LocalDate> colFecha;

    @FXML
    private TableColumn<Encuentro, Equipo> colLocal;

    @FXML
    private TableColumn<Encuentro, Equipo> colVisitante;

    @FXML
    private TextField txtLocal;

    @FXML
    private TextField txtVisitante;

    @FXML
    private TableView<Equipo> tblPosiciones;

    @FXML
    private TableColumn<Equipo, Number> colPosicion;

    @FXML
    private TableColumn<Equipo, String> colEquipo;

    @FXML
    private TableColumn<Equipo, Number> colPuntos;

    @FXML
    private Label lblMensaje;

    private final GestorTorneos gestor = GestorTorneos.getInstancia();

    @FXML
    private void initialize() {
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colLocal.setCellValueFactory(new PropertyValueFactory<>("equipoLocal"));
        colVisitante.setCellValueFactory(new PropertyValueFactory<>("equipoVisitante"));

        colEquipo.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPosicion.setCellValueFactory(fila ->
                new SimpleIntegerProperty(tblPosiciones.getItems().indexOf(fila.getValue()) + 1));
        colPuntos.setCellValueFactory(fila ->
                new SimpleIntegerProperty(cbTorneo.getValue().calcularPuntos(fila.getValue())));

        tblProgramados.setPlaceholder(new Label("No hay encuentros programados."));
        tblPosiciones.setPlaceholder(new Label("El torneo no tiene equipos."));

        cbTorneo.getItems().setAll(gestor.listarTorneos());
        if (!cbTorneo.getItems().isEmpty()) {
            cbTorneo.setValue(cbTorneo.getItems().get(0));
        }
        seleccionarTorneo();
    }

    @FXML
    private void seleccionarTorneo() {
        lblMensaje.setText("");
        actualizarTablas();
    }

    @FXML
    private void registrarResultado() {
        Encuentro encuentro = tblProgramados.getSelectionModel().getSelectedItem();
        if (encuentro == null) {
            mostrarError("Seleccione un encuentro de la tabla.");
            return;
        }
        if (txtLocal.getText().isBlank() || txtVisitante.getText().isBlank()) {
            mostrarError("Debe escribir los goles de ambos equipos.");
            return;
        }
        int golesLocal;
        int golesVisitante;
        try {
            golesLocal = Integer.parseInt(txtLocal.getText().trim());
            golesVisitante = Integer.parseInt(txtVisitante.getText().trim());
        } catch (NumberFormatException e) {
            mostrarError("Los goles deben ser números enteros.");
            return;
        }

        try {
            encuentro.registrarResultado(golesLocal, golesVisitante);
            mostrarExito("Resultado registrado: " + encuentro.getEquipoLocal() + " " + golesLocal
                    + " - " + golesVisitante + " " + encuentro.getEquipoVisitante() + ".");
            txtLocal.clear();
            txtVisitante.clear();
            actualizarTablas();
        } catch (EncuentroInvalidoException e) {
            mostrarError(e.getMessage());
            mostrarAlerta("No se pudo registrar el resultado", e.getMessage());
        }
    }

    @FXML
    private void cancelarEncuentro() {
        Encuentro encuentro = tblProgramados.getSelectionModel().getSelectedItem();
        if (encuentro == null) {
            mostrarError("Seleccione un encuentro de la tabla.");
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Cancelar encuentro");
        confirmacion.setHeaderText("¿Cancelar " + encuentro.getEquipoLocal() + " vs "
                + encuentro.getEquipoVisitante() + "?");
        confirmacion.setContentText("El encuentro pasará a estado CANCELADO.");
        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        Cancelable cancelable = encuentro;
        try {
            cancelable.cancelar();
            mostrarExito("Encuentro cancelado.");
            actualizarTablas();
        } catch (IllegalStateException e) {
            mostrarError(e.getMessage());
            mostrarAlerta("No se pudo cancelar", e.getMessage());
        }
    }

    @FXML
    private void volver() {
        try {
            App.cambiarVista("principal");
        } catch (IOException e) {
            mostrarError("No se pudo volver al menú: " + e.getMessage());
        }
    }

    private void actualizarTablas() {
        tblProgramados.getItems().clear();
        tblPosiciones.getItems().clear();
        Torneo torneo = cbTorneo.getValue();
        if (torneo == null) {
            return;
        }
        for (Encuentro e : torneo.getEncuentros()) {
            if (e.getEstado() == EstadoEncuentro.PROGRAMADO) {
                tblProgramados.getItems().add(e);
            }
        }
        List<Equipo> tabla = gestor.tablaDePosiciones(torneo);
        tblPosiciones.getItems().setAll(tabla);
    }

    private void mostrarError(String mensaje) {
        lblMensaje.getStyleClass().setAll("error");
        lblMensaje.setText(mensaje);
    }

    private void mostrarExito(String mensaje) {
        lblMensaje.getStyleClass().setAll("exito");
        lblMensaje.setText(mensaje);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
