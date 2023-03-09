package com.example.dripchip.mappers;

import com.example.dripchip.dto.AnimalVisitedLocationDTO;
import com.example.dripchip.dto.AnimalVisitedLocationShortDTO;
import com.example.dripchip.entities.AnimalVisitedLocation;
import com.example.dripchip.entities.LocationPoint;
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

    public AnimalVisitedLocation toEntity(AnimalVisitedLocationShortDTO shortDTO) {
        AnimalVisitedLocation entity = new AnimalVisitedLocation();
        entity.setId(shortDTO.getId());
        entity.setLocationPoint(new LocationPoint(shortDTO.getLocationPointId()));

        return entity;
    }
}
