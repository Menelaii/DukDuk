package com.example.dripchip.searchCriterias;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnimalSearchCriteria {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Integer chipperId;
    private Long chippingLocationId;
    private String lifeStatus;
    private String gender;
}
