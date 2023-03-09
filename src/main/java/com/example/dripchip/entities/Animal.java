package com.example.dripchip.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "animal")
public class Animal extends AbstractEntity<Long> {
    private float weight;
    private float length;
    private float height;
    private String gender;
    @Column(name = "life_status")
    private String lifeStatus;
    @Column(name = "chipping_date_time")
    private LocalDateTime chippingDateTime;
    @Column(name = "death_date_time")
    private LocalDateTime deathDateTime;
    @ManyToMany(mappedBy = "animals")
    private List<AnimalType> animalTypes;
    @ManyToOne
    @JoinColumn(name = "chipper_id", referencedColumnName = "id")
    private Account chipper;
    @ManyToOne
    @JoinColumn(name = "chipping_location_id", referencedColumnName = "id")
    private LocationPoint chippingLocation;
    @OneToMany(mappedBy = "locationPoint")
    private List<AnimalVisitedLocation> visitedLocations;
}