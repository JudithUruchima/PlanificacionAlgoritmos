package modelo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import modelo.Proceso;

public class FirstComeFirstServed {

    public static List<Proceso> planificar(List<Proceso> procesos) {
        procesos.sort(Comparator.comparingInt(p -> p.llegada));
        return new ArrayList<>(procesos);
    }
}
