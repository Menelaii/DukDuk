package com.example.dripchip.dto;

import com.example.dripchip.entity.LocationPoint;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnimalVisitedLocationDTO {

    private Long id;
    private LocalDateTime dateTimeOfVisitLocationPoint;
    private LocationPoint locationPoint;
}
