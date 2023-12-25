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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class AddNewTeacherController {
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
    private TextField nameTextField;
    @FXML
    private TextField surnameTextField;
    @FXML
    private ComboBox<TeacherCondition> conditionComboBox;
    @FXML
    private TextField birthdayTextField;
    @FXML
    private TextField salaryTextField;
    @FXML
    private ComboBox<String> groupComboBox;

    private ClassContainer classContainer = new ClassContainer();
    public AddNewTeacherController() {

    }
    public AddNewTeacherController(ClassContainer classContainer) {
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

            ObservableList<String> groupNames = FXCollections.observableArrayList();
            for (ClassTeacher classTeacher : classContainer.teacherGroups.values()) {
                groupNames.add(classTeacher.getGroupName());
            }
            groupComboBox.setItems(groupNames);
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
    void addNewTeacher(ActionEvent event) {
        try {
            String name = nameTextField.getText();
            String surname = surnameTextField.getText();
            TeacherCondition condition = conditionComboBox.getValue();
            String birthdayString = birthdayTextField.getText();
            double salary = Double.parseDouble(salaryTextField.getText());
            String selectedGroupName = groupComboBox.getValue();

            if (name.isEmpty() || surname.isEmpty() || condition == null || birthdayString.isEmpty() || selectedGroupName == null) {
                System.err.println("Error: Please fill in all the fields.");
                return;
            }
            int birthday = Integer.parseInt(birthdayString);

            ClassTeacher selectedGroup = classContainer.teacherGroups.get(selectedGroupName);

            if (selectedGroup != null) {
                Teacher newTeacher = new Teacher(name, surname, condition, birthday, salary);
                selectedGroup.addTeacher(newTeacher);
                tableView.refresh();
            } else {
                System.err.println("Error: Selected group not found in the classContainer.");
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid salary or birthday format.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}