package com.example.teacherpaneljavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;

public class EditTeacherDataController {
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
    private TextField salaryTextField;
    @FXML
    private ComboBox<TeacherCondition> conditionComboBox;

    private ClassContainer classContainer = new ClassContainer();
    public EditTeacherDataController() {

    }
    public EditTeacherDataController(ClassContainer classContainer) {
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

            nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            surnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            conditionColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(TeacherCondition.values())));
            birthdayColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            salaryColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
            groupColumn.setCellFactory(TextFieldTableCell.forTableColumn());

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
    void addToSalary(ActionEvent event) {
        try {
            Teacher selectedTeacher = tableView.getSelectionModel().getSelectedItem();
            if (selectedTeacher == null) {
                System.err.println("Error: Please select a teacher from the table.");
                return;
            }

            String amountString = salaryTextField.getText();
            if (amountString.isEmpty()) {
                System.err.println("Error: Please enter a salary change amount.");
                return;
            }

            double amount = Double.parseDouble(amountString);
            ClassTeacher selectedGroup = null;

            for (ClassTeacher classTeacher : classContainer.teacherGroups.values()) {
                if (classTeacher.getTeacherList().contains(selectedTeacher)) {
                    selectedGroup = classTeacher;
                    break;
                }
            }

            if (selectedGroup != null) {
                selectedGroup.addSalary(selectedTeacher, amount);
                tableView.refresh();
            } else {
                System.err.println("Error: Selected teacher not found in any group.");
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid salary change amount format.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void changeCondition(ActionEvent event) {
        try {
            Teacher selectedTeacher = tableView.getSelectionModel().getSelectedItem();
            if (selectedTeacher == null) {
                System.err.println("Error: Please select a teacher from the table.");
                return;
            }

            TeacherCondition newCondition = conditionComboBox.getValue();
            if (newCondition == null) {
                System.err.println("Error: Please select a new condition for the teacher.");
                return;
            }

            ClassTeacher selectedGroup = null;
            for (ClassTeacher classTeacher : classContainer.teacherGroups.values()) {
                if (classTeacher.getTeacherList().contains(selectedTeacher)) {
                    selectedGroup = classTeacher;
                    break;
                }
            }

            if (selectedGroup != null) {
                selectedGroup.changeCondition(selectedTeacher, newCondition);
                tableView.refresh();
            } else {
                System.err.println("Error: Selected teacher not found in any group.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void saveChanges(ActionEvent event) {

    }
}