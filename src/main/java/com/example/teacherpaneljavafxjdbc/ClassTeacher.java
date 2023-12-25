package com.example.teacherpaneljavafx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ClassTeacher {
    String groupName;
    ArrayList<Teacher>teacherList = new ArrayList<Teacher>();
    int groupLimit;

    public ClassTeacher(String groupName, ArrayList<Teacher>teacherList, int groupLimit) {
        this.groupName = groupName;
        this.groupLimit = groupLimit;
        this.teacherList = teacherList;
    }

    public ClassTeacher(String groupName, int groupLimit) {
        this.groupName = groupName;
        this.groupLimit = groupLimit;
    }

    public void addTeacher(Teacher newTeacher) {
        if (teacherList.contains(newTeacher)) {
            System.out.println("Teacher " + newTeacher.getTeacherName() + " " + newTeacher.getTeacherSurname() + " is already present in the group.");
            return;
        }

        if (teacherList.size() < groupLimit) {
            teacherList.add(newTeacher);
            System.out.println("Teacher " + newTeacher.getTeacherName() + " " + newTeacher.getTeacherSurname() + " has been added to the group.");
        }
        else {
            System.out.println("Error. Cannot add teacher, maximum number of teachers in group reached.");
        }
    }

    public void addSalary(Teacher teacher, double amount) {
        if (teacherList.contains(teacher)) {
            double teacherSalary = teacher.getTeacherSalary();
            double newSalary = teacherSalary + amount;
            teacher.setTeacherSalary(newSalary);
            System.out.println("The salary has been increased by " + amount + " zł. for teacher: " + teacher.getTeacherName() + " " + teacher.getTeacherSurname());
        }
        else {
            System.out.println("Error. Teacher " + teacher.getTeacherName() + " " + teacher.getTeacherSurname() + " does not exist.");
        }
    }

    public void removeTeacher(Teacher teacher) {
        if (teacherList.contains(teacher)) {
            teacherList.remove(teacher);
            System.out.println("Teacher " + teacher.getTeacherName() + " " + teacher.getTeacherSurname() + " has been removed.");
        }
        else {
            System.out.println("Error. Teacher " + teacher.getTeacherName() + " " + teacher.getTeacherSurname() + " does not exist.");
        }
    }

    public void changeCondition(Teacher teacher, TeacherCondition newCondition) {
        if (teacherList.contains(teacher)) {
            teacher.setTeacherCondition(newCondition);
            System.out.println("Teacher condition of " + teacher.getTeacherName() + " " + teacher.getTeacherSurname() + " has been set to: " + teacher.getTeacherCondition());
        }
        else {
            System.out.println("Error. Teacher " + teacher.getTeacherName() + " " + teacher.getTeacherSurname() + " does not exist.");
        }
    }

    public Teacher search(String surname) {
        for(Teacher teacher : teacherList) {
            String teacherSurname = teacher.getTeacherSurname();
            if(teacherSurname.compareTo(surname) == 0 ) {
                return teacher;
            }
        }
        return null;
    }

    public ArrayList<Teacher> searchPartial(String partialString) {
        ArrayList<Teacher> matchingTeachers = new ArrayList<>();

        for (Teacher teacher : teacherList) {
            String teacherName = teacher.getTeacherName();
            String teacherSurname = teacher.getTeacherSurname();

            if (teacherName.contains(partialString) || teacherSurname.contains(partialString)) {
                matchingTeachers.add(teacher);
            }
        }
        return matchingTeachers;
    }

    public int countByCondition(TeacherCondition searchedCondition) {
        int counter = 0;
        for(Teacher teacher : teacherList) {
            TeacherCondition teacherCondition = teacher.getTeacherCondition();
            if(teacherCondition.compareTo(searchedCondition) == 0 ) {
                counter++;
            }
        }
        return counter;
    }

    public void summary() {
        System.out.println("Summary of all teachers:");
        for(Teacher teacher : teacherList) {
            teacher.printing();
        }
    }

    public ArrayList<Teacher> sortByName() {
        ArrayList<Teacher> sortedList = teacherList;
        sortedList.sort(Teacher::compareTo);
        return sortedList;
    }

    public ArrayList<Teacher> sortBySalary() {
        ArrayList<Teacher> sortedList = teacherList;
        Comparator<Teacher> myComparator = new Comparator<Teacher>() {
            @Override
            public int compare(Teacher t1, Teacher t2) {
                return Double.compare(t1.getTeacherSalary(), t2.getTeacherSalary());
            }
        };
        sortedList.sort(Collections.reverseOrder(myComparator));
        return sortedList;
    }

    public Teacher max() {
        Teacher teacher = Collections.max(teacherList, new Comparator<Teacher>() {
            @Override
            public int compare(Teacher t1, Teacher t2) {
                return Double.compare(t1.getTeacherSalary(), t2.getTeacherSalary());
            }
        });
        return teacher;
    }

    public String getGroupName() {
        return groupName;
    }

    public int getGroupLimit() {
        return groupLimit;
    }

    public void setGroupName(String newName) {
        this.groupName = newName;
    }

    public void setGroupLimit(int newGroupLimit) {
        this.groupLimit = newGroupLimit;
    }
    public ArrayList<Teacher> getTeacherList() {
        return teacherList;
    }
}