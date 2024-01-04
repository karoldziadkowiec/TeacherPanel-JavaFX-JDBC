package com.example.teacherpaneljavafxjdbc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class HelloApplication extends Application {
    ClassContainer classContainer;

    public HelloApplication() {

    }

    public HelloApplication(ClassContainer classContainer) {
        this.classContainer = classContainer;
    }

    public void setClassContainer(ClassContainer classContainer) {
        this.classContainer = classContainer;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Parent root = fxmlLoader.load();

        if (classContainer == null) {
            classContainer = new ClassContainer();
        }

        HelloController controller = fxmlLoader.getController();
        controller.setClassContainer(classContainer);

        Scene scene = new Scene(root, 600, 520);
        stage.setTitle("TeacherPanel");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}