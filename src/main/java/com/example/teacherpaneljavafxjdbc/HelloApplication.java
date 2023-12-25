package com.example.teacherpaneljavafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class HelloApplication extends Application {
    ClassContainer classContainer;

    public HelloApplication() {

    }

    public HelloApplication(ClassContainer classContainer) {
        this.classContainer = classContainer;
    }

    public void setClassContainer(ClassContainer classContainer) {
        this.classContainer = classContainer;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Parent root = fxmlLoader.load();

        if (classContainer == null) {
            classContainer = new ClassContainer();
            preparedData();
        }

        HelloController controller = fxmlLoader.getController();
        controller.setClassContainer(classContainer);

        Scene scene = new Scene(root, 600, 520);
        stage.setTitle("TeacherPanel");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void preparedData() {
        ArrayList<Teacher> teacherGroup1 = new ArrayList<>();
        teacherGroup1.add(new Teacher("Karol", "Dziadkowiec", TeacherCondition.PRESENT, 2002, 4500));
        teacherGroup1.add(new Teacher("Cristiano", "Ronaldo", TeacherCondition.ABSENT, 1985, 4200));
        teacherGroup1.add(new Teacher("Leo", "Messi", TeacherCondition.DELEGATION, 1987, 3900));
        teacherGroup1.add(new Teacher("Robert", "Lewandowski", TeacherCondition.PRESENT, 1988, 4150));
        ClassTeacher wf = new ClassTeacher("Physical Education", teacherGroup1, 7);
        classContainer.addClass(wf);

        ArrayList<Teacher> teacherGroup2 = new ArrayList<>();
        teacherGroup2.add(new Teacher("Wojciech", "Szczęsny", TeacherCondition.PRESENT, 1990, 4300));
        teacherGroup2.add(new Teacher("Łukasz", "Fabiański", TeacherCondition.DELEGATION, 1985, 4200));
        teacherGroup2.add(new Teacher("Marcin", "Bułka", TeacherCondition.PRESENT, 1999, 3600));
        teacherGroup2.add(new Teacher("Łukasz", "Skorupski", TeacherCondition.PRESENT, 1991, 3900));
        ClassTeacher english = new ClassTeacher("English", teacherGroup2, 8);
        classContainer.addClass(english);

        ArrayList<Teacher> teacherGroup3 = new ArrayList<>();
        teacherGroup3.add(new Teacher("Piotr", "Zieliński", TeacherCondition.PRESENT, 1994, 4300));
        teacherGroup3.add(new Teacher("Mateusz", "Klich", TeacherCondition.ABSENT, 1990, 3700));
        teacherGroup3.add(new Teacher("Karol", "Linetty", TeacherCondition.ABSENT, 1995, 3800));
        teacherGroup3.add(new Teacher("Sebastian", "Szymański", TeacherCondition.PRESENT, 1999, 4000));
        ClassTeacher maths = new ClassTeacher("Maths", teacherGroup3, 5);
        classContainer.addClass(maths);

        ArrayList<Teacher> teacherGroup4 = new ArrayList<>();
        teacherGroup4.add(new Teacher("Thomas", "Muller", TeacherCondition.PRESENT, 1994, 4300));
        teacherGroup4.add(new Teacher("Manuel", "Neuer", TeacherCondition.ABSENT, 1990, 3700));
        teacherGroup4.add(new Teacher("Lucas", "Podolski", TeacherCondition.ABSENT, 1995, 3800));
        teacherGroup4.add(new Teacher("Joshua", "Kimmich", TeacherCondition.PRESENT, 1999, 4000));
        ClassTeacher german = new ClassTeacher("German", teacherGroup4, 5);
        classContainer.addClass(german);
    }
}