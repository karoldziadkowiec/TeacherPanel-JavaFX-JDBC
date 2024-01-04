package com.example.teacherpaneljavafxjdbc;

import javax.persistence.*;

@Entity
@Table(name = "groups")
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "groupLimit")
    private int groupLimit;

    // Getters and setters
}

