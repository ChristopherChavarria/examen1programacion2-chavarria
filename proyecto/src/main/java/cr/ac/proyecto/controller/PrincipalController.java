package cr.ac.proyecto.controller;

import cr.ac.proyecto.App;
import cr.ac.proyecto.model.Torneo;
import cr.ac.proyecto.service.GestorTorneos;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class PrincipalController {

    @FXML
    private Label lblResumen;

    @FXML
    private Label lblEstado;

    private final GestorTorneos gestor = GestorTorneos.getInstancia();

    @FXML
    private void initialize() {
        int encuentros = 0;
        for (Torneo t : gestor.listarTorneos()) {
            encuentros += t.getEncuentros().size();
        }
        lblResumen.setText(gestor.listarTorneos().size() + " torneos, "
                + gestor.listarEquipos().size() + " equipos y "
                + encuentros + " encuentros registrados");
        lblEstado.setText("Seleccione una opción del menú.");
    }

    @FXML
    private void irARegistro() {
        navegar("registro");
    }

    @FXML
    private void irAConsulta() {
        navegar("consulta");
    }

    @FXML
    private void irAGestion() {
        navegar("gestion");
    }

    private void navegar(String vista) {
        try {
            App.cambiarVista(vista);
        } catch (IOException | NullPointerException | IllegalStateException e) {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setHeaderText("Vista pendiente");
            alerta.setContentText("La vista \"" + vista + ".fxml\" aún no existe o tiene errores.");
            alerta.showAndWait();
        }
    }
}
