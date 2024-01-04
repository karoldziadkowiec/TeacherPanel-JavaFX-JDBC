package com.example.teacherpaneljavafxjdbc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
public class ListOfGroupsController {
    @FXML
    private TableView<ClassTeacher> tableView;

    @FXML
    private TableColumn<ClassTeacher, String> groupNameColumn;

    @FXML
    private TableColumn<ClassTeacher, Integer> groupLimitColumn;
    @FXML
    private Label groupFillingCounter;

    private ClassContainer classContainer = new ClassContainer();

    public ListOfGroupsController() {

    }
    public ListOfGroupsController(ClassContainer classContainer) {
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
    void findEmptyGroup(ActionEvent event) {
        ObservableList<ClassTeacher> emptyGroupsObservableList = FXCollections.observableArrayList();

        String url = "jdbc:mysql://localhost:3306/teacherpanel";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT groups.name, groups.groupLimit FROM groups " +
                    "LEFT JOIN teachers ON groups.id = teachers.groupID " +
                    "WHERE teachers.id IS NULL";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String name = resultSet.getString("name");
                        int groupLimit = resultSet.getInt("groupLimit");

                        ClassTeacher emptyGroup = new ClassTeacher(name, groupLimit);
                        emptyGroupsObservableList.add(emptyGroup);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableView.setItems(emptyGroupsObservableList);
    }

    @FXML
    void removeGroup(ActionEvent event) {
        ClassTeacher selectedGroup = tableView.getSelectionModel().getSelectedItem();

        if (selectedGroup != null) {
            String groupName = selectedGroup.getGroupName();

            removeGroupFromDatabase(groupName);

            ObservableList<ClassTeacher> classTeachers = FXCollections.observableArrayList();
            loadDataFromDatabase();
        } else {
            System.err.println("Error: No group selected.");
        }
    }

    private void removeGroupFromDatabase(String groupName) {
        String url = "jdbc:mysql://localhost:3306/teacherpanel";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "DELETE FROM groups WHERE name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, groupName);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void calculateGroupFilling(ActionEvent event) {
        ClassTeacher selectedGroup = tableView.getSelectionModel().getSelectedItem();

        if (selectedGroup != null) {
            int groupId = getGroupId(selectedGroup.getGroupName());
            if (groupId != -1) {
                int currentSize = getTeacherCountForGroup(groupId);
                int limit = selectedGroup.getGroupLimit();

                double fillingPercentage = (double) currentSize / limit * 100;
                groupFillingCounter.setText("Filling: " + String.format("%.2f", fillingPercentage) + "%");
            } else {
                System.err.println("Error: Couldn't retrieve group ID.");
            }
        } else {
            System.err.println("Error: No group selected.");
        }
    }

    private int getGroupId(String groupName) {
        int groupId = -1;

        String url = "jdbc:mysql://localhost:3306/teacherpanel";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT id FROM groups WHERE name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, groupName);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        groupId = resultSet.getInt("id");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groupId;
    }

    private int getTeacherCountForGroup(int groupId) {
        int teacherCount = 0;

        String url = "jdbc:mysql://localhost:3306/teacherpanel";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT COUNT(*) AS teacherCount FROM teachers WHERE groupID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, groupId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        teacherCount = resultSet.getInt("teacherCount");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teacherCount;
    }
}