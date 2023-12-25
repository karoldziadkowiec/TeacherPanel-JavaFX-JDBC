package com.example.teacherpaneljavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class ListOfGroupsController {
    @FXML
    private TableView<ClassTeacher> tableView;

    @FXML
    private TableColumn<ClassTeacher, String> groupNameColumn;

    @FXML
    private TableColumn<ClassTeacher, Integer> groupLimitColumn;
    @FXML
    private Label groupFillingCounter;

    private ClassContainer classContainer = new ClassContainer();
    public ListOfGroupsController() {

    }
    public ListOfGroupsController(ClassContainer classContainer) {
        this.classContainer = classContainer;
    }

    public void setClassContainer(ClassContainer classContainer) {
        this.classContainer = classContainer;
    }

    @FXML
    public void initialize() {
        if (classContainer != null) {
            groupNameColumn.setCellValueFactory(new PropertyValueFactory<>("groupName"));
            groupLimitColumn.setCellValueFactory(new PropertyValueFactory<>("groupLimit"));

            ObservableList<ClassTeacher> classTeachers = FXCollections.observableArrayList(classContainer.teacherGroups.values());
            tableView.setItems(classTeachers);
        } else {
            System.err.println("Error: classContainer is null.");
        }
    }
    @FXML
    void backToHelloWindow(ActionEvent event) {
        try {
            HelloApplication helloApplication = new HelloApplication(classContainer);
            helloApplication.setClassContainer(classContainer);
            helloApplication.start(new Stage());

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void findEmptyGroup(ActionEvent event) {
        ArrayList<ClassTeacher> emptyGroups = classContainer.findEmpty();
        ObservableList<ClassTeacher> emptyGroupsObservableList = FXCollections.observableArrayList(emptyGroups);
        tableView.setItems(emptyGroupsObservableList);
    }

    @FXML
    void removeGroup(ActionEvent event) {
        ClassTeacher selectedGroup = tableView.getSelectionModel().getSelectedItem();

        if (selectedGroup != null) {
            String groupName = selectedGroup.getGroupName();

            classContainer.removeClass(groupName);

            ObservableList<ClassTeacher> classTeachers = FXCollections.observableArrayList(classContainer.teacherGroups.values());
            tableView.setItems(classTeachers);
        } else {
            System.err.println("Error: No group selected.");
        }
    }

    @FXML
    void calculateGroupFilling(ActionEvent event) {
        ClassTeacher selectedGroup = tableView.getSelectionModel().getSelectedItem();

        if (selectedGroup != null) {
            int currentSize = selectedGroup.getTeacherList().size();
            int limit = selectedGroup.getGroupLimit();

            double fillingPercentage = (double) currentSize / limit * 100;
            groupFillingCounter.setText("Filling: " + String.format("%.2f", fillingPercentage) + "%");
        } else {
            System.err.println("Error: No group selected.");
        }
    }
}