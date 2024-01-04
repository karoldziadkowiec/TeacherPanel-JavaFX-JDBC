package com.example.teacherpaneljavafxjdbc;

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
import java.sql.*;
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
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("teacherName"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("teacherSurname"));
        conditionColumn.setCellValueFactory(new PropertyValueFactory<>("teacherCondition"));
        birthdayColumn.setCellValueFactory(new PropertyValueFactory<>("teacherBirthday"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("teacherSalary"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("teacherGroup"));

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
    void removeTeacher(ActionEvent event) {
        Teacher selectedTeacher = tableView.getSelectionModel().getSelectedItem();

        if (selectedTeacher != null) {
            removeFromDatabase(selectedTeacher);
            tableView.getItems().remove(selectedTeacher);
        } else {
            System.err.println("Error: No teacher selected.");
        }
    }

    private void removeFromDatabase(Teacher teacher) {
        String url = "jdbc:mysql://localhost:3306/teacherpanel";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String deleteQuery = "DELETE FROM teachers WHERE name = ? AND surname = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setString(1, teacher.getTeacherName());
                preparedStatement.setString(2, teacher.getTeacherSurname());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void sortByName(ActionEvent event) {
        String url = "jdbc:mysql://localhost:3306/teacherpanel";
        String username = "root";
        String password = "";

        ObservableList<Teacher> teachers = FXCollections.observableArrayList();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT * FROM teachers ORDER BY name";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
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
    }


    @FXML
    void sortBySalary(ActionEvent event) {
        String url = "jdbc:mysql://localhost:3306/teacherpanel";
        String username = "root";
        String password = "";

        ObservableList<Teacher> teachers = FXCollections.observableArrayList();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT * FROM teachers ORDER BY salary DESC";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
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
    }

    @FXML
    void maxSalary(ActionEvent event) {
        ObservableList<Teacher> teachers = FXCollections.observableArrayList();

        String url = "jdbc:mysql://localhost:3306/teacherpanel";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT id, name, surname, teacherCondition, birthday, salary, groupID FROM ( " +
                    "SELECT id, name, surname, teacherCondition, birthday, salary, groupID, " +
                    "ROW_NUMBER() OVER (PARTITION BY groupID ORDER BY salary DESC) as row_num " +
                    "FROM teachers " +
                    ") AS ranked " +
                    "WHERE row_num = 1";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
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
    }

    @FXML
    void countByCondition(ActionEvent event) {
        TeacherCondition selectedCondition = conditionComboBox.getValue();

        if (selectedCondition != null) {
            int conditionCount = 0;

            String url = "jdbc:mysql://localhost:3306/teacherpanel";
            String username = "root";
            String password = "";

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String query = "SELECT COUNT(*) AS count FROM teachers WHERE teacherCondition = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, selectedCondition.toString());

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            conditionCount = resultSet.getInt("count");
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            conditionCounter.setText("Counter: " + conditionCount);
        } else {
            System.err.println("Error: No condition selected.");
        }
    }
}