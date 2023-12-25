package com.example.teacherpaneljavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ListController {
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
    private ComboBox<TeacherCondition> conditionComboBox;
    @FXML
    private Label conditionCounter;

    private ClassContainer classContainer = new ClassContainer();
    public ListController() {

    }
    public ListController(ClassContainer classContainer) {
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

            conditionComboBox.setItems(FXCollections.observableArrayList(TeacherCondition.values()));
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
    void removeTeacher(ActionEvent event) {
        Teacher selectedTeacher = tableView.getSelectionModel().getSelectedItem();

        if (selectedTeacher != null) {
            for (ClassTeacher classTeacher : classContainer.teacherGroups.values()) {
                if (classTeacher.getTeacherList().contains(selectedTeacher)) {
                    classTeacher.removeTeacher(selectedTeacher);
                    tableView.getItems().remove(selectedTeacher);
                    return;
                }
            }
            System.err.println("Error: Teacher's group not found.");
        } else {
            System.err.println("Error: No teacher selected.");
        }
    }

    @FXML
    void sortByName(ActionEvent event) {
        ObservableList<Teacher> teachers = tableView.getItems();

        if (teachers != null) {
            List<Teacher> allTeachers = new ArrayList<>();

            for (ClassTeacher classTeacher : classContainer.teacherGroups.values()) {
                allTeachers.addAll(classTeacher.sortByName());
            }
            teachers.setAll(allTeachers);
        } else {
            System.err.println("Error: Teacher's group not found.");
        }
    }

    @FXML
    void sortBySalary(ActionEvent event) {
        ObservableList<Teacher> teachers = tableView.getItems();

        if (teachers != null) {
            List<Teacher> allTeachers = new ArrayList<>();

            for (ClassTeacher classTeacher : classContainer.teacherGroups.values()) {
                allTeachers.addAll(classTeacher.sortBySalary());
            }
            teachers.setAll(allTeachers);
        } else {
            System.err.println("Error: Teacher's group not found.");
        }
    }

    @FXML
    void maxSalary(ActionEvent event) {
        ObservableList<Teacher> teachers = FXCollections.observableArrayList();

        if (classContainer != null && !classContainer.teacherGroups.isEmpty()) {
            for (ClassTeacher classTeacher : classContainer.teacherGroups.values()) {
                Teacher teacherWithMaxSalary = classTeacher.max();

                if (teacherWithMaxSalary != null) {
                    teachers.add(teacherWithMaxSalary);
                }
            }
            tableView.setItems(teachers);
        } else {
            System.err.println("Error: No teacher groups found.");
        }
    }

    @FXML
    void countByCondition(ActionEvent event) {
        TeacherCondition selectedCondition = conditionComboBox.getValue();

        if (selectedCondition != null) {
            int conditionCount = 0;

            for (ClassTeacher classTeacher : classContainer.teacherGroups.values()) {
                conditionCount += classTeacher.countByCondition(selectedCondition);
            }
            conditionCounter.setText("Counter: " + conditionCount);
        } else {
            System.err.println("Error: No condition selected.");
        }
    }
}