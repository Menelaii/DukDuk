package com.example.dripchip.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "Animals")
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
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
    @OneToMany(mappedBy = "animal")
    private List<AnimalVisitedLocation> visitedLocations;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Animal that = (Animal) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}