package com.example.teacherpaneljavafxjdbc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AddNewTeacherWindow extends Application {

    private ClassContainer classContainer;

    public AddNewTeacherWindow(ClassContainer classContainer) {
        this.classContainer = classContainer;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-new-teacher-view.fxml"));
        Parent root = fxmlLoader.load();

        AddNewTeacherController controller = fxmlLoader.getController();
        controller.setClassContainer(classContainer);
        controller.initialize();

        Scene scene = new Scene(root, 600, 520);
        stage.setTitle("TeacherPanel");
        stage.setScene(scene);
        stage.show();
    }
}