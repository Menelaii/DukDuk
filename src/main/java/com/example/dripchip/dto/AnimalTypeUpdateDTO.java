package com.example.dripchip.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimalTypeUpdateDTO {
    private Long oldTypeId;
    private Long newTypeId;
}
