package ec.edu.uees.algoritmos;

import modelo.CargarTxt;
import modelo.Estadisticas;
import modelo.RoundRobin;
import modelo.ShortTermPredict;
import modelo.ShortestJobFirst;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import modelo.FirstComeFirstServed;
import modelo.Proceso;

public class Controlador {

    @FXML
    private Button btnCargarArchivo;
    @FXML
    private Button btnEjecutar;
    @FXML
    private HBox hboxGanttFCFS;
    @FXML
    private HBox hboxGanttSJF;
    @FXML
    private HBox hboxGanttSTR;
    @FXML
    private GridPane gridTiemposFCFS;
    @FXML
    private GridPane gridTiemposSJF;
    @FXML
    private GridPane gridTiemposSTR;

    @FXML
    private HBox hboxTiemposFCFS;

    @FXML
    private HBox hboxTiemposSJF;

    @FXML
    private HBox hboxTiemposSTR;
    @FXML
    private Label lblEstadisticasFCFS;
    @FXML
    private Label lblEstadisticasSJF;
    @FXML
    private Label lblEstadisticasSTR;

    private List<Proceso> procesos;

    @FXML
    private void cargarArchivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos TXT", "*.txt"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                procesos = CargarTxt.cargarProcesos(file);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Cargar Archivo");
                alert.setHeaderText("Archivo cargado correctamente");
                alert.setContentText("Cierra para continuar");
                alert.showAndWait();

            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("No se pudo cargar el archivo");
                errorAlert.setContentText(e.getMessage());
                errorAlert.showAndWait();
            }
        }
    }

    @FXML
    private void ejecutarPlanificacion() {

        if (procesos == null || procesos.isEmpty()) {
            return;
        }
        String[] colors = new String[] {"pink", "aqua", "orange", "lightblue", "green"};
        
        
        /*List<Proceso> sjf = ShortestJobFirst.planificar(procesos);
        mostrarGantt(hboxGanttFCFS, hboxTiemposFCFS, sjf, colors[0]);
        lblEstadisticasFCFS.setText(Estadisticas.calcular(sjf));*/
        
        List<Proceso> fcfs = FirstComeFirstServed.planificar(procesos);
        mostrarGantt(hboxGanttFCFS, hboxTiemposFCFS, fcfs, colors[0]);
        lblEstadisticasFCFS.setText(Estadisticas.calcularFCFS(fcfs));

        List<Proceso> rr = RoundRobin.planificar(procesos, 4);
        mostrarGantt(hboxGanttSJF, hboxTiemposSJF, rr, colors[1]);
        lblEstadisticasSJF.setText(Estadisticas.calcular(procesos, rr));

        
        List<Proceso> str = ShortTermPredict.planificar(procesos);
        mostrarGantt(hboxGanttSTR, hboxTiemposSTR, str, colors[2]);
        lblEstadisticasSTR.setText(Estadisticas.calcularSRTF(str));

    }

    private void mostrarGantt(HBox hbox, HBox hboxTiempos, List<Proceso> procesos, String color) {
        hbox.getChildren().clear(); // Limpiar el contenedor de los procesos
        hboxTiempos.getChildren().clear(); // Limpiar el contenedor de los tiempos

        int tiempoActual = 0;

        for (Proceso p : procesos) {
            // Crear el rectángulo del proceso
            Label labelProceso = new Label(p.getId());
            labelProceso.setStyle("-fx-background-color: " +  color + "; -fx-padding: 10 1; -fx-font-weight: bold");
            labelProceso.setMinWidth(p.getDuracion() * 10); // Ajustar tamaño proporcionalmente
            labelProceso.setFont(Font.font("Courier New", 14));
            labelProceso.setAlignment(Pos.CENTER);

            // Crear el número debajo del proceso
            Label labelTiempo = new Label(String.valueOf(" " + tiempoActual));
            labelTiempo.setMinWidth(p.getDuracion() * 11);
            labelTiempo.setFont(Font.font("Courier New", 13));
            labelTiempo.setAlignment(Pos.CENTER_LEFT);

            hbox.getChildren().add(labelProceso);
            hboxTiempos.getChildren().add(labelTiempo);

            tiempoActual += p.getDuracion(); // Avanzar el tiempo
        }
        // Agregar el tiempo final alineado correctamente
        Label labelTiempoFinal = new Label(String.valueOf(tiempoActual));
        labelTiempoFinal.setFont(Font.font("Courier New", 13));

        hboxTiempos.getChildren().add(labelTiempoFinal);

    }

}
