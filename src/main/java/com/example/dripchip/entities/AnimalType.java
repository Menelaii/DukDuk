package com.example.dripchip.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "animel_type")
public class AnimalType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @ManyToMany
    @JoinTable(
            name = "animal_animal_type",
            joinColumns = @JoinColumn(name = "animal_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "animal_type_id", referencedColumnName = "id"))
    private List<Animal> animals;
}