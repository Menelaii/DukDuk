package com.example.dripchip.SearchCriterias;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LocationPointSearchCriteria {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Integer from;
    private Integer size;
}
