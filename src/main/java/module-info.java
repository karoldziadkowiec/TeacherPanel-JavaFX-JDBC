module com.example.teacherpaneljavafxjdbc {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.teacherpaneljavafxjdbc to javafx.fxml;
    exports com.example.teacherpaneljavafxjdbc;
}