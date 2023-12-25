package com.example.teacherpaneljavafx;

public class Teacher implements Comparable<Teacher>{
    String name;
    String surname;
    TeacherCondition condition;
    Integer birthday;
    double salary;
    String teacherGroup = "";
    public Teacher(String name, String surname, TeacherCondition condition, Integer birthday, double salary) {
       this.name = name;
       this.surname = surname;
       this.condition = condition;
       this.birthday = birthday;
       this.salary = salary;
    }

    public void printing() {
        System.out.print("Teacher: " + name + " " + surname);
        System.out.print(", Condition: " + condition);
        System.out.print(", Birthday: " + birthday);
        System.out.println(", Salary: " + salary + " zł.");
    }

    @Override
    public int compareTo(Teacher otherTeacher) {
        return this.surname.compareTo(otherTeacher.surname);
    }

    public String getTeacherName() {
        return name;
    }

    public String getTeacherSurname() {
        return surname;
    }

    public TeacherCondition getTeacherCondition() {
        return condition;
    }

    public Integer getTeacherBirthday() {
        return birthday;
    }

    public double getTeacherSalary() {
        return salary;
    }

    public void setTeacherName(String newName) {
        this.name = newName;
    }

    public void setTeacherSurname(String newSurname) {
        this.surname = newSurname;
    }

    public void setTeacherCondition(TeacherCondition newCondition) {
        this.condition = newCondition;
    }

    public void setTeacherBirthday(Integer newBirthday) {
        this.birthday = newBirthday;
    }

    public void setTeacherSalary(double newSalary) {
        this.salary = newSalary;
    }
    public String getTeacherGroup() {
        return teacherGroup;
    }

    public void setTeacherGroup(String teacherGroup) {
        this.teacherGroup = teacherGroup;
    }
}