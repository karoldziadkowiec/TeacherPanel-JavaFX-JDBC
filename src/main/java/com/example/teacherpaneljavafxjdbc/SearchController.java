package com.example.teacherpaneljavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class SearchController {
    @FXML
    private TableView<Teacher> tableView;
    @FXML
    private TableColumn<Teacher, String> nameColumn;
    @FXML
    private TableColumn<Teacher, String> surnameColumn;
    @FXML
    private TableColumn<Teacher, TeacherCondition> conditionColumn;
    @FXML
    private TableColumn<Teacher, Integer> birthdayColumn;
    @FXML
    private TableColumn<Teacher, Double> salaryColumn;
    @FXML
    private TableColumn<Teacher, String> groupColumn;
    @FXML
    private TextField searchTextField;

    private ClassContainer classContainer = new ClassContainer();
    public SearchController() {

    }

    public SearchController(ClassContainer classContainer) {
        this.classContainer = classContainer;
    }

    public void setClassContainer(ClassContainer classContainer) {
        this.classContainer = classContainer;
    }

    @FXML
    public void initialize() {
        if (classContainer != null) {
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("teacherName"));
            surnameColumn.setCellValueFactory(new PropertyValueFactory<>("teacherSurname"));
            conditionColumn.setCellValueFactory(new PropertyValueFactory<>("teacherCondition"));
            birthdayColumn.setCellValueFactory(new PropertyValueFactory<>("teacherBirthday"));
            salaryColumn.setCellValueFactory(new PropertyValueFactory<>("teacherSalary"));
            groupColumn.setCellValueFactory(new PropertyValueFactory<>("teacherGroup"));

            ObservableList<Teacher> teachers = FXCollections.observableArrayList();

            for (ClassTeacher classTeacher : classContainer.teacherGroups.values()) {
                for (Teacher teacher : classTeacher.getTeacherList()) {
                    teacher.setTeacherGroup(classTeacher.getGroupName());
                    teachers.add(teacher);
                }
            }
            tableView.setItems(teachers);
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
    void searchBySurname(ActionEvent event) {
        String searchedSurname = searchTextField.getText();

        if (searchedSurname != null && !searchedSurname.isEmpty()) {
            ObservableList<Teacher> teachers = FXCollections.observableArrayList();

            for (ClassTeacher classTeacher : classContainer.teacherGroups.values()) {
                Teacher foundTeacher = classTeacher.search(searchedSurname);

                if (foundTeacher != null) {
                    teachers.add(foundTeacher);
                }
            }
            tableView.setItems(teachers);
        } else {
            System.err.println("Error: Please enter a surname to search.");
        }
    }

    @FXML
    void searchByPartial(ActionEvent event) {
        String partialString = searchTextField.getText();

        if (partialString != null && !partialString.isEmpty()) {
            ObservableList<Teacher> teachers = FXCollections.observableArrayList();

            for (ClassTeacher classTeacher : classContainer.teacherGroups.values()) {
                ArrayList<Teacher> foundTeachers = classTeacher.searchPartial(partialString);

                if (!foundTeachers.isEmpty()) {
                    teachers.addAll(foundTeachers);
                }
            }
            tableView.setItems(teachers);
        } else {
            System.err.println("Error: Please enter a partial name to search.");
        }
    }
}