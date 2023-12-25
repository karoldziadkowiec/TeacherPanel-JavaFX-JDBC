package com.example.teacherpaneljavafx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class HelloController {
    ClassContainer classContainer = new ClassContainer();
    public HelloController() {
        this.classContainer = new ClassContainer();
    }

    public HelloController(ClassContainer classContainer) {
        this.classContainer = classContainer;
    }

    public void setClassContainer(ClassContainer classContainer) {
        this.classContainer = classContainer;
    }

    @FXML
    void goToAddNewGroupWindow(ActionEvent event) {
        try {
            AddNewGroupWindow addNewGroupWindow = new AddNewGroupWindow(classContainer);
            addNewGroupWindow.start(new Stage());

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goToListOfGroupsWindow(ActionEvent event) {
        try {
            ListOfGroupsWindow listOfGroupsWindow = new ListOfGroupsWindow(classContainer);
            listOfGroupsWindow.start(new Stage());

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goToAddNewTeacherWindow(ActionEvent event) {
        try {
            AddNewTeacherWindow addNewTeacherWindow = new AddNewTeacherWindow(classContainer);
            addNewTeacherWindow.start(new Stage());

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goToEditTeacherData(ActionEvent event) {
        try {
            EditTeacherDataWindow editTeacherDataWindow = new EditTeacherDataWindow(classContainer);
            editTeacherDataWindow.start(new Stage());

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goToListWindow(ActionEvent event) {
        try {
            ListWindow listWindow = new ListWindow(classContainer);
            listWindow.start(new Stage());

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goToSearchWindow(ActionEvent event) {
        try {
            SearchWindow searchWindow = new SearchWindow(classContainer);
            searchWindow.start(new Stage());

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void exitApplication(ActionEvent event) {
        Platform.exit();
    }
}