package com.example.dripchip.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "Location_points")
@NoArgsConstructor
public class LocationPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private Double latitude;
    private Double longitude;
    @OneToMany(mappedBy = "chippingLocation")
    private List<Animal> animals;
    @OneToMany(mappedBy = "locationPoint")
    private List<AnimalVisitedLocation> animalVisitedLocation;

    public LocationPoint(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LocationPoint that = (LocationPoint) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}