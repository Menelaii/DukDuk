package com.example.dripchip.controllers;

import com.example.dripchip.searchCriterias.AnimalSearchCriteria;
import com.example.dripchip.searchCriterias.AnimalVisitedLocationSearchCriteria;
import com.example.dripchip.searchCriterias.XPage;
import com.example.dripchip.dto.AnimalDTO;
import com.example.dripchip.dto.AnimalVisitedLocationDTO;
import com.example.dripchip.entities.Animal;
import com.example.dripchip.entities.AnimalVisitedLocation;
import com.example.dripchip.services.AnimalService;
import com.example.dripchip.services.AnimalVisitedLocationsService;
import com.example.dripchip.utils.AnimalMapper;
import com.example.dripchip.utils.AnimalValidator;
import com.example.dripchip.utils.AnimalVisitedLocationsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/animals")
public class AnimalsController {
    private final AnimalService animalService;
    private final AnimalMapper animalMapper;
    private final AnimalVisitedLocationsMapper visitedLocationsMapper;
    private final AnimalVisitedLocationsService animalVisitedLocationsService;

    @Autowired
    public AnimalsController(AnimalService animalService, AnimalMapper animalMapper,
                             AnimalVisitedLocationsMapper animalVisitedLocationsMapper,
                             AnimalVisitedLocationsService animalVisitedLocationService) {
        this.animalService = animalService;
        this.animalMapper = animalMapper;
        this.visitedLocationsMapper = animalVisitedLocationsMapper;
        this.animalVisitedLocationsService = animalVisitedLocationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalDTO> findOne(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            return new ResponseEntity<>(null, HttpStatusCode.valueOf(400));
        }

        return ResponseEntity.ok(animalMapper.toDto(animalService.findOne(id)));
    }

    @GetMapping("/search")
    public ResponseEntity<List<AnimalDTO>> search(AnimalSearchCriteria searchCriteria, XPage page) {
        //todo check dates format iso-8601?
        if (page.getFrom() == null || page.getFrom() < 0
                || page.getSize() == null || page.getSize() <= 0
                || !AnimalValidator.isValidGender(searchCriteria.getGender())
                || !AnimalValidator.isValidLifeStatus(searchCriteria.getLifeStatus())) {
            return new ResponseEntity<>(null, HttpStatusCode.valueOf(400));
        }

        List<Animal> animals = animalService.findWithFilters(page, searchCriteria);
        List<AnimalDTO> animalDTOS = animals
                .stream()
                .map(animalMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(animalDTOS);
    }


    @GetMapping("/{id}/locations")
    public ResponseEntity<List<AnimalVisitedLocationDTO>> findOne(@PathVariable("id") Long id,
                                                                  AnimalVisitedLocationSearchCriteria searchCriteria,
                                                                  XPage page) {
        //TODO проверка дат на соответствию стандарту
        if (id == null || id <= 0 || page.getFrom() < 0 || page.getSize() <= 0) {
            return new ResponseEntity<>(null, HttpStatusCode.valueOf(400));
        }

        page.setSortBy("dateTimeOfVisitLocationPoint");

        List<AnimalVisitedLocation> visitedLocations =
                animalVisitedLocationsService.findWithFilters(page, searchCriteria, id);
        List<AnimalVisitedLocationDTO> dtos = visitedLocations
                .stream()
                .map(visitedLocationsMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}
