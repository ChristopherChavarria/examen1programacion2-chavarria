package cr.ac.proyecto.controller;

import cr.ac.proyecto.App;
import cr.ac.proyecto.exception.EncuentroInvalidoException;
import cr.ac.proyecto.model.Arbitro;
import cr.ac.proyecto.model.Equipo;
import cr.ac.proyecto.model.Torneo;
import cr.ac.proyecto.service.GestorTorneos;
import java.io.IOException;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;


public class RegistroController {

    @FXML
    private ComboBox<Torneo> cbTorneo;

    @FXML
    private ComboBox<Equipo> cbLocal;

    @FXML
    private ComboBox<Equipo> cbVisitante;

    @FXML
    private DatePicker dpFecha;

    @FXML
    private ComboBox<Arbitro> cbArbitro;

    @FXML
    private Label lblMensaje;

    private final GestorTorneos gestor = GestorTorneos.getInstancia();

    @FXML
    private void initialize() {
        cbTorneo.getItems().setAll(gestor.listarTorneos());
        cbArbitro.getItems().setAll(gestor.listarArbitros());
    }

  
    @FXML
    private void seleccionarTorneo() {
        Torneo torneo = cbTorneo.getValue();
        cbLocal.getItems().clear();
        cbVisitante.getItems().clear();
        if (torneo != null) {
            cbLocal.getItems().setAll(torneo.getEquipos());
            cbVisitante.getItems().setAll(torneo.getEquipos());
            if (torneo.getEquipos().isEmpty()) {
                mostrarError("El torneo " + torneo.getNombre() + " no tiene equipos inscritos.");
            } else {
                lblMensaje.setText("");
            }
        }
    }

    @FXML
    private void programar() {
        Torneo torneo = cbTorneo.getValue();
        Equipo local = cbLocal.getValue();
        Equipo visitante = cbVisitante.getValue();
        LocalDate fecha = dpFecha.getValue();
        Arbitro arbitro = cbArbitro.getValue();

        // Validaciones de la interfaz: que no falte ningún dato.
        if (torneo == null) {
            mostrarError("Debe seleccionar un torneo.");
            return;
        }
        if (local == null || visitante == null) {
            mostrarError("Debe seleccionar el equipo local y el visitante.");
            return;
        }
        if (fecha == null) {
            mostrarError("Debe seleccionar una fecha.");
            return;
        }
        if (arbitro == null) {
            mostrarError("Debe seleccionar un árbitro.");
            return;
        }

     
        try {
            torneo.programarEncuentro(local, visitante, fecha, arbitro);
            lblMensaje.getStyleClass().setAll("exito");
            lblMensaje.setText("Encuentro programado: " + local.getNombre() + " vs "
                    + visitante.getNombre() + " el " + fecha + ".");
            limpiarCampos();
        } catch (EncuentroInvalidoException e) {
            mostrarError(e.getMessage());
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("No se pudo programar");
            alerta.setHeaderText("Encuentro inválido");
            alerta.setContentText(e.getMessage());
            alerta.showAndWait();
        }
    }

    @FXML
    private void limpiar() {
        cbTorneo.setValue(null);
        cbLocal.getItems().clear();
        cbVisitante.getItems().clear();
        limpiarCampos();
        lblMensaje.setText("");
    }

    @FXML
    private void volver() {
        try {
            App.cambiarVista("principal");
        } catch (IOException e) {
            mostrarError("No se pudo volver al menú: " + e.getMessage());
        }
    }

    private void limpiarCampos() {
        cbLocal.setValue(null);
        cbVisitante.setValue(null);
        dpFecha.setValue(null);
        cbArbitro.setValue(null);
    }

    private void mostrarError(String mensaje) {
        lblMensaje.getStyleClass().setAll("error");
        lblMensaje.setText(mensaje);
    }
}
