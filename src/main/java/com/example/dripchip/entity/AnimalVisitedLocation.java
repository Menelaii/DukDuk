package com.example.dripchip.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "AnimalVisitedLocation")
public class AnimalVisitedLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "date_time_of_visit_location_point")
    private LocalDateTime dateTimeOfVisitLocationPoint;

    @OneToOne
    @JoinColumn(name = "location_point_id", referencedColumnName = "id")
    private LocationPoint locationPoint;
}
