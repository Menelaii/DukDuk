package com.example.dripchip.searchCriterias;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnimalVisitedLocationSearchCriteria {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
