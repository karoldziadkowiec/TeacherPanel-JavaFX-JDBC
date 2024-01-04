package com.example.teacherpaneljavafxjdbc;

import javax.persistence.*;

@Entity
@Table(name = "teachers")
public class TeacherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "teacherCondition")
    private String teacherCondition;

    @Column(name = "birthday")
    private int birthday;

    @Column(name = "salary")
    private double salary;

    @ManyToOne
    @JoinColumn(name = "groupID")
    private GroupEntity group;

    // Getters and setters
}