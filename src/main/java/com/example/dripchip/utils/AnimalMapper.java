package com.example.dripchip.utils;

import com.example.dripchip.dto.AnimalDTO;
import com.example.dripchip.entities.Animal;
import com.example.dripchip.entities.AnimalType;
import com.example.dripchip.entities.AnimalVisitedLocation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}
