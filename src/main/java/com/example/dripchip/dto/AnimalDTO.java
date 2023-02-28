package com.example.dripchip.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnimalDTO {
    private long id;
    private float weight;
    private float length;
    private float height;
    private String gender;
    private String lifeStatus;
    private LocalDateTime chippingDateTime;
    private LocalDateTime deathDateTime;
    private Long[] animalTypes;
    private int chipperId;
    private long chippingLocationId;
    private Long[] visitedLocations;
}
