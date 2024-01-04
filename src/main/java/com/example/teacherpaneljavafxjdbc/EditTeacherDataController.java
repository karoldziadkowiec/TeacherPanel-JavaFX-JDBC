package com.example.teacherpaneljavafxjdbc;

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
import java.sql.*;

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

        loadDataFromDatabase();

        conditionComboBox.setItems(FXCollections.observableArrayList(TeacherCondition.values()));
    }

    private void loadDataFromDatabase() {
        ObservableList<Teacher> teachers = FXCollections.observableArrayList();
        String url = "jdbc:mysql://localhost:3306/teacherpanel";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT name, surname, teacherCondition, birthday, salary, groupID FROM teachers";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String name = resultSet.getString("name");
                        String surname = resultSet.getString("surname");
                        String teacherConditionValue = resultSet.getString("teacherCondition");
                        TeacherCondition teacherCondition = parseTeacherCondition(teacherConditionValue);
                        int birthday = resultSet.getInt("birthday");
                        double salary = resultSet.getDouble("salary");
                        int groupID = resultSet.getInt("groupID");

                        String groupName = getGroupName(groupID);

                        Teacher teacher = new Teacher(name, surname, teacherCondition, birthday, salary, groupName);
                        teachers.add(teacher);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableView.setItems(teachers);
    }

    private TeacherCondition parseTeacherCondition(String conditionValue) {
        try {
            return TeacherCondition.valueOf(conditionValue);
        } catch (NumberFormatException e) {
            return TeacherCondition.ABSENT;
        }
    }

    private String getGroupName(int groupID) {
        String groupName = "";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/teacherpanel", "root", "")) {
            String query = "SELECT name FROM groups WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, groupID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        groupName = resultSet.getString("name");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groupName;
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

            String url = "jdbc:mysql://localhost:3306/teacherpanel";
            String username = "root";
            String password = "";

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String updateSalaryQuery = "UPDATE teachers SET salary = salary + ? WHERE name = ? AND surname = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateSalaryQuery)) {
                    preparedStatement.setDouble(1, amount);
                    preparedStatement.setString(2, selectedTeacher.getTeacherName());
                    preparedStatement.setString(3, selectedTeacher.getTeacherSurname());
                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Salary updated successfully.");
                    } else {
                        System.err.println("Error: Teacher not found.");
                        return;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            tableView.refresh();

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

            String url = "jdbc:mysql://localhost:3306/teacherpanel";
            String username = "root";
            String password = "";

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String updateConditionQuery = "UPDATE teachers SET teacherCondition = ? WHERE name = ? AND surname = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateConditionQuery)) {
                    preparedStatement.setString(1, newCondition.toString());
                    preparedStatement.setString(2, selectedTeacher.getTeacherName());
                    preparedStatement.setString(3, selectedTeacher.getTeacherSurname());
                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Condition updated successfully.");
                    } else {
                        System.err.println("Error: Teacher not found.");
                        return;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            tableView.refresh();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void saveChanges(ActionEvent event) {

    }
}