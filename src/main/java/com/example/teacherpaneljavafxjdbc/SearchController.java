package com.example.teacherpaneljavafxjdbc;

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
import java.sql.*;
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
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("teacherName"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("teacherSurname"));
        conditionColumn.setCellValueFactory(new PropertyValueFactory<>("teacherCondition"));
        birthdayColumn.setCellValueFactory(new PropertyValueFactory<>("teacherBirthday"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("teacherSalary"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("teacherGroup"));

        loadDataFromDatabase();
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

                        // Pobierz nazwę grupy na podstawie groupID
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
    void searchBySurname(ActionEvent event) {
        String searchedSurname = searchTextField.getText();

        if (searchedSurname != null && !searchedSurname.isEmpty()) {
            ObservableList<Teacher> teachers = FXCollections.observableArrayList();

            String url = "jdbc:mysql://localhost:3306/teacherpanel";
            String username = "root";
            String password = "";

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String query = "SELECT * FROM teachers WHERE surname = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, searchedSurname);

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                            int id = resultSet.getInt("id");
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
        } else {
            System.err.println("Error: Please enter a surname to search.");
        }
    }


    @FXML
    void searchByPartial(ActionEvent event) {
        String partialString = searchTextField.getText();

        if (partialString != null && !partialString.isEmpty()) {
            ObservableList<Teacher> teachers = FXCollections.observableArrayList();

            String url = "jdbc:mysql://localhost:3306/teacherpanel";
            String username = "root";
            String password = "";

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String query = "SELECT * FROM teachers WHERE surname LIKE ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, "%" + partialString + "%");

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                            int id = resultSet.getInt("id");
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
        } else {
            System.err.println("Error: Please enter a surname to search.");
        }
    }
}