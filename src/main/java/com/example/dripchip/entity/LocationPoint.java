package com.example.dripchip.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "location_point")
public class LocationPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double latitude;
    private double longitude;
    @OneToMany(mappedBy = "chippingLocation")
    private List<Animal> animals;
    @OneToOne(mappedBy = "locationPoint")
    private AnimalVisitedLocation animalVisitedLocation;
}