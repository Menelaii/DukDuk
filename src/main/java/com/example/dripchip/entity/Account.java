package com.example.dripchip.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "first_name", nullable = false, length = 255)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 255)
    private String lastName;
    @Column(name = "email", nullable = false, length = 255)
    private String email;
    @Column(name = "email", nullable = false, length = 255)
    private String password;
    @OneToMany(mappedBy = "chipper")
    private List<Animal> animals;
}
