package com.example.dripchip.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnimalVisitedLocationDTO {
    private Long id;
    private LocalDateTime dateTimeOfVisitLocationPoint;
    private Long locationPointId;
}
