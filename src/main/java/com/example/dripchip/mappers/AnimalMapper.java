package com.example.dripchip.mappers;

import com.example.dripchip.dto.AnimalDTO;
import com.example.dripchip.dto.AnimalShortDTO;
import com.example.dripchip.entities.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class AnimalMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public AnimalMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public AnimalDTO toDto(Animal animal) {
        AnimalDTO animalDTO = modelMapper.map(animal, AnimalDTO.class);
        Long[] typesIds = animal.getAnimalTypes()
                .stream()
                .map(AnimalType::getId)
                .toArray(Long[]::new);

        animalDTO.setAnimalTypes(typesIds);
        animalDTO.setChipperId(animal.getChipper().getId());
        animalDTO.setChippingLocationId(animalDTO.getChippingLocationId());

        Long[] locationsIds = animal.getVisitedLocations()
                .stream()
                .map(AnimalVisitedLocation::getId)
                .toArray(Long[]::new);

        animalDTO.setVisitedLocations(locationsIds);

        return animalDTO;
    }

    public Animal toEntity(AnimalShortDTO shortDTO) {
        Animal entity = modelMapper.map(shortDTO, Animal.class);

        entity.setAnimalTypes(
                Arrays.stream(shortDTO.getAnimalTypes())
                        .map(AnimalType::new)
                        .collect(Collectors.toList())
        );

        entity.setChipper(
                new Account(shortDTO.getChipperId())
        );

        entity.setChippingLocation(
                new LocationPoint(shortDTO.getChippingLocationId())
        );

        entity.setVisitedLocations(
                Arrays.stream(shortDTO.getVisitedLocations())
                        .map(AnimalVisitedLocation::new)
                        .collect(Collectors.toList())
        );

        return entity;
    }
}
