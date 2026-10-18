package cr.ac.proyecto.controller;

import cr.ac.proyecto.App;
import cr.ac.proyecto.model.Arbitro;
import cr.ac.proyecto.model.Encuentro;
import cr.ac.proyecto.model.Equipo;
import cr.ac.proyecto.model.EstadoEncuentro;
import cr.ac.proyecto.model.Torneo;
import cr.ac.proyecto.service.GestorTorneos;
import java.io.IOException;
import java.time.LocalDate;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class ConsultaController {

    @FXML
    private ComboBox<Torneo> cbTorneo;

    @FXML
    private ComboBox<EstadoEncuentro> cbEstado;

    @FXML
    private TableView<Encuentro> tblEncuentros;

    @FXML
    private TableColumn<Encuentro, LocalDate> colFecha;

    @FXML
    private TableColumn<Encuentro, Equipo> colLocal;

    @FXML
    private TableColumn<Encuentro, String> colMarcador;

    @FXML
    private TableColumn<Encuentro, Equipo> colVisitante;

    @FXML
    private TableColumn<Encuentro, Arbitro> colArbitro;

    @FXML
    private TableColumn<Encuentro, EstadoEncuentro> colEstado;

    @FXML
    private Label lblTotal;

    private final GestorTorneos gestor = GestorTorneos.getInstancia();

    @FXML
    private void initialize() {
        
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colLocal.setCellValueFactory(new PropertyValueFactory<>("equipoLocal"));
        colVisitante.setCellValueFactory(new PropertyValueFactory<>("equipoVisitante"));
        colArbitro.setCellValueFactory(new PropertyValueFactory<>("arbitro"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        // El marcador combina dos getters, por eso se arma a mano.
        colMarcador.setCellValueFactory(fila -> new SimpleStringProperty(
                fila.getValue().getMarcadorLocal() + " - " + fila.getValue().getMarcadorVisitante()));

        tblEncuentros.setPlaceholder(new Label("No hay encuentros para mostrar."));

        cbEstado.getItems().setAll(EstadoEncuentro.values());
        cbTorneo.getItems().setAll(gestor.listarTorneos());
        if (!cbTorneo.getItems().isEmpty()) {
            cbTorneo.setValue(cbTorneo.getItems().get(0));
        }
        filtrar();
    }

    @FXML
    private void filtrar() {
        tblEncuentros.getItems().clear();
        Torneo torneo = cbTorneo.getValue();
        if (torneo == null) {
            lblTotal.setText("Seleccione un torneo.");
            return;
        }
        EstadoEncuentro estado = cbEstado.getValue();
        for (Encuentro e : torneo.getEncuentros()) {
            if (estado == null || e.getEstado() == estado) {
                tblEncuentros.getItems().add(e);
            }
        }
        lblTotal.setText(tblEncuentros.getItems().size() + " encuentro(s) en " + torneo.getNombre()
                + (estado == null ? "" : " con estado " + estado));
    }

    @FXML
    private void quitarFiltro() {
        cbEstado.setValue(null);
        filtrar();
    }

    @FXML
    private void volver() {
        try {
            App.cambiarVista("principal");
        } catch (IOException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setContentText("No se pudo volver al menú: " + e.getMessage());
            alerta.showAndWait();
        }
    }
}
