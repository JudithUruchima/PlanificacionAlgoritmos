module ec.edu.uees.algoritmos {
    requires javafx.controls;
    requires javafx.fxml;

    opens ec.edu.uees.algoritmos to javafx.fxml;
    exports ec.edu.uees.algoritmos;
}
