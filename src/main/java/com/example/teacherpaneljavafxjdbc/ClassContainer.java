package com.example.teacherpaneljavafxjdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClassContainer {
    Map<String, ClassTeacher> teacherGroups = new HashMap<>();

    public void addClass(String groupName, int groupLimit) {
        ClassTeacher group = new ClassTeacher(groupName, groupLimit);
        teacherGroups.put(groupName, group);
        System.out.println("The group has been created.");
    }

    public void addClass(ClassTeacher group) {
        teacherGroups.put(group.getGroupName(), group);
        System.out.println("The group has been added to container.");
    }

    public void removeClass(String groupName) {
        if (teacherGroups.containsKey(groupName)) {
            teacherGroups.remove(groupName);
            System.out.println("Group " + groupName + " has been removed");
        }
        else {
            System.out.println("Error: Group " + groupName + " does not exist.");
        }
    }

    public ArrayList<ClassTeacher> findEmpty() {
        ArrayList<ClassTeacher> emptyGroups = new ArrayList<>();
        for (ClassTeacher group : teacherGroups.values()) {
            if (group.teacherList.isEmpty()) {
                emptyGroups.add(group);
            }
        }
        return emptyGroups;
    }

    public void summary() {
        for (ClassTeacher group : teacherGroups.values()) {
            int currentSize = group.teacherList.size();
            int limit = group.getGroupLimit();

            double percent = (double) currentSize / limit * 100;

            System.out.println("Group: " + group.getGroupName() + ", Filling the group: " + percent + " %");
        }
    }
}