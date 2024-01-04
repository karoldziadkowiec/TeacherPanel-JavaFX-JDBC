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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

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
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("teacherName"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("teacherSurname"));
        conditionColumn.setCellValueFactory(new PropertyValueFactory<>("teacherCondition"));
        birthdayColumn.setCellValueFactory(new PropertyValueFactory<>("teacherBirthday"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("teacherSalary"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("teacherGroup"));

        loadDataFromDatabase();

        conditionComboBox.setItems(FXCollections.observableArrayList(TeacherCondition.values()));
        loadGroupNamesFromDatabase();
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

    private void loadGroupNamesFromDatabase() {
        ObservableList<String> groupNames = FXCollections.observableArrayList();
        String url = "jdbc:mysql://localhost:3306/teacherpanel";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT DISTINCT name FROM groups";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String groupName = resultSet.getString("name");
                        groupNames.add(groupName);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        groupComboBox.setItems(groupNames);
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

            addToDatabase(name, surname, condition, birthday, salary, selectedGroupName);
            loadDataFromDatabase();

        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid salary or birthday format.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addToDatabase(String name, String surname, TeacherCondition condition, int birthday, double salary, String groupName) {
        String url = "jdbc:mysql://localhost:3306/teacherpanel";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String insertQuery = "INSERT INTO teachers (name, surname, teacherCondition, birthday, salary, groupID) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, surname);
                preparedStatement.setString(3, condition.toString());
                preparedStatement.setInt(4, birthday);
                preparedStatement.setDouble(5, salary);

                int groupID = getGroupID(groupName);
                preparedStatement.setInt(6, groupID);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getGroupID(String groupName) {
        int groupID = -1;

        String url = "jdbc:mysql://localhost:3306/teacherpanel";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT id FROM groups WHERE name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, groupName);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        groupID = resultSet.getInt("id");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groupID;
    }
}