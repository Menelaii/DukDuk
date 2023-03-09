package com.example.dripchip.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "AnimalVisitedLocation")
@NoArgsConstructor
public class AnimalVisitedLocation extends AbstractEntity<Long> {
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
        super(id);
    }
}
