package com.example.teacherpaneljavafxjdbc;
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
import java.sql.*;

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
        groupNameColumn.setCellValueFactory(new PropertyValueFactory<>("groupName"));
        groupLimitColumn.setCellValueFactory(new PropertyValueFactory<>("groupLimit"));

        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        ObservableList<ClassTeacher> classTeachers = FXCollections.observableArrayList(classContainer.teacherGroups.values());
        String url = "jdbc:mysql://localhost:3306/teacherpanel";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT name, groupLimit FROM groups";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String name = resultSet.getString("name");
                        int groupLimit = resultSet.getInt("groupLimit");

                        ClassTeacher group = new ClassTeacher(name, groupLimit);
                        classTeachers.add(group);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableView.setItems(classTeachers);
    }

    @FXML
    void addNewGroup(ActionEvent event) {
        String groupName = groupNameTextField.getText();
        String groupLimitText = groupLimitTextField.getText();

        if (!groupName.isEmpty() && !groupLimitText.isEmpty()) {
            try {
                int groupLimit = Integer.parseInt(groupLimitText);
                if (groupLimit < 0) {
                    System.err.println("Error: Group limit cannot be a negative number.");
                    return;
                }

                addToDatabase(groupName, groupLimit);
                loadDataFromDatabase();

                groupNameTextField.clear();
                groupLimitTextField.clear();
            } catch (NumberFormatException e) {
                System.err.println("Error: Invalid group limit format. Please enter a valid integer.");
            }
        } else {
            System.err.println("Error: Textfields cannot be empty.");
        }
    }

    private void addToDatabase(String groupName, int groupLimit) {
        String url = "jdbc:mysql://localhost:3306/teacherpanel";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "INSERT INTO groups (name, groupLimit) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, groupName);
                preparedStatement.setInt(2, groupLimit);

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Group added to the database.");
                } else {
                    System.err.println("Error: Failed to add group to the database.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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