package com.example.dripchip.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "animel_type")
@NoArgsConstructor
public class AnimalType extends AbstractEntity<Long> {
    private String type;

    @ManyToMany
    @JoinTable(
            name = "animal_animal_type",
            joinColumns = @JoinColumn(name = "animal_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "animal_type_id", referencedColumnName = "id")
    )
    private List<Animal> animals;

    public AnimalType(Long id) {
        super(id);
    }
}