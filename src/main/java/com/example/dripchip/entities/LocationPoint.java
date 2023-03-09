package com.example.dripchip.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "location_point")
@NoArgsConstructor
public class LocationPoint extends AbstractEntity<Long> {
    private Double latitude;
    private Double longitude;
    @OneToMany(mappedBy = "chippingLocation")
    private List<Animal> animals;
    @OneToMany(mappedBy = "locationPoint")
    private List<AnimalVisitedLocation> animalVisitedLocation;

    public LocationPoint(Long id) {
        super(id);
    }
}