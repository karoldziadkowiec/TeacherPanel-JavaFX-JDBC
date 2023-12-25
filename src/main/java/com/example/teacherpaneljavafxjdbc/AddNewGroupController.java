package com.example.teacherpaneljavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class AddNewGroupController {
    @FXML
    private TableView<ClassTeacher> tableView;
    @FXML
    private TableColumn<ClassTeacher, String> groupNameColumn;
    @FXML
    private TableColumn<ClassTeacher, Integer> groupLimitColumn;
    @FXML
    private TextField groupNameTextField;
    @FXML
    private TextField groupLimitTextField;

    private ClassContainer classContainer = new ClassContainer();

    public AddNewGroupController() {

    }
    public AddNewGroupController(ClassContainer classContainer) {
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
    void addNewGroup(ActionEvent event) {
        String groupName = groupNameTextField.getText();
        String groupLimitText = groupLimitTextField.getText();

        if (!groupName.isEmpty() && !groupLimitText.isEmpty()) {
            try {
                int groupLimit = Integer.parseInt(groupLimitText);
                if(groupLimit < 0){
                    System.err.println("Error: Group limit cannot negative number.");
                    return;
                }

                classContainer.addClass(groupName, groupLimit);

                ObservableList<ClassTeacher> classTeachers = FXCollections.observableArrayList(classContainer.teacherGroups.values());
                tableView.setItems(classTeachers);

                groupNameTextField.clear();
                groupLimitTextField.clear();
            } catch (NumberFormatException e) {
                System.err.println("Error: Invalid group limit format. Please enter a valid integer.");
            }
        } else {
            System.err.println("Error: Textfields cannot be empty.");
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
}