package com.example.dripchip.entities;


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
    private Double latitude;
    private Double longitude;
    @OneToMany(mappedBy = "chippingLocation")
    private List<Animal> animals;
    @OneToMany(mappedBy = "locationPoint")
    private List<AnimalVisitedLocation> animalVisitedLocation;
}