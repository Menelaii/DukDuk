package com.example.dripchip.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnimalShortDTO {
    private Float weight;
    private Float length;
    private Float height;
    private String gender;
    private String lifeStatus;
    private LocalDateTime chippingDateTime;
    private LocalDateTime deathDateTime;
    private Long[] animalTypes;
    private Integer chipperId;
    private Long chippingLocationId;
    private Long[] visitedLocations;
}
