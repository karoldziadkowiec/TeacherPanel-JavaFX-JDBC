module com.example.teacherpaneljavafxjdbc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.sql;
    requires java.persistence;


    opens com.example.teacherpaneljavafxjdbc to javafx.fxml;
    exports com.example.teacherpaneljavafxjdbc;
}