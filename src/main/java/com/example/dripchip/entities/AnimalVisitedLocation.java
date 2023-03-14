package com.example.dripchip.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "Animal_visited_locations")
@NoArgsConstructor
public class AnimalVisitedLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "date_time_of_visit_location_point")
    private LocalDateTime dateTimeOfVisitLocationPoint;
    @ManyToOne
    @JoinColumn(name = "animal_id", referencedColumnName = "id")
    private Animal animal;
    @ManyToOne
    @JoinColumn(name = "location_point_id", referencedColumnName = "id")
    private LocationPoint locationPoint;

    public AnimalVisitedLocation(LocalDateTime dateTimeOfVisitLocationPoint,
                                 Animal animal, LocationPoint locationPoint) {
        this.dateTimeOfVisitLocationPoint = dateTimeOfVisitLocationPoint;
        this.animal = animal;
        this.locationPoint = locationPoint;
    }

    public AnimalVisitedLocation(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AnimalVisitedLocation that = (AnimalVisitedLocation) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
