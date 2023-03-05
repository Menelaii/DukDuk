package com.example.dripchip.utils;

import com.example.dripchip.dto.AnimalVisitedLocationDTO;
import com.example.dripchip.entities.AnimalVisitedLocation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnimalVisitedLocationsMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public AnimalVisitedLocationsMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public AnimalVisitedLocationDTO toDto(AnimalVisitedLocation animalVisitedLocation) {
        AnimalVisitedLocationDTO visitedLocationDTO =
                modelMapper.map(animalVisitedLocation, AnimalVisitedLocationDTO.class);

        visitedLocationDTO
                .setLocationPointId(animalVisitedLocation.getLocationPoint().getId());

        return visitedLocationDTO;
    }
}
